package com.user.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.dto.account.AccountDto;
import com.user.dto.response.AccountResponseDto;
import com.user.dto.secure.AccountSecureDto;
import com.user.exception.EmailIsBlank;
import com.user.exception.EmailNotUnique;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public AccountResponseDto getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("user with email: " + email + " not found"));
        log.info("user with email: " + email + " not found");
        return new AccountResponseDto(new AccountSecureDto(user.getId(),
                user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPassword(), user.getRoles()), true);
    }

    @Override
    public AccountResponseDto createUser(AccountSecureDto accountSecureDto) {
        if (accountSecureDto.getEmail().isEmpty() || accountSecureDto.getEmail().isBlank()) {
            throw new EmailIsBlank("email is blank");
        }
        if (userRepository.existsByEmail(accountSecureDto.getEmail())) {
            log.warn("email: " + accountSecureDto.getEmail() + " not unique!");
           throw new EmailNotUnique("email: " + accountSecureDto.getEmail() + " not unique!");
        }
        User user = User.builder().email(accountSecureDto.getEmail())
                .firstName(accountSecureDto.getFirstName())
                .lastName(accountSecureDto.getLastName())
                .password(accountSecureDto.getPassword())
                .roles("ROLE_USER").build();
        userRepository.save(user);
        log.info(user.toString());
        return new AccountResponseDto(new AccountSecureDto(user.getId(), user.getFirstName(),
                user.getLastName(), user.getEmail(), user.getPassword(), user.getRoles()), true);
    }

    @Override
    @Transactional
    public User editUser(AccountDto accountDto) {
        User oldUser = userRepository.findUserByEmail(accountDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException
                        ("user with email: " + accountDto.getEmail() + " not found"));
        if (oldUser.getEmail().equals(accountDto.getEmail())) {
            oldUser = (User) objectMapper(accountDto);
            userRepository.save(oldUser);
        }
        return oldUser;
    }

    @Override
    public ResponseEntity<List<User>> searchUser(String username) {
        if (username == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String[] fullName = username.split(" ");
        String firstName = null;
        String lastName = null;

        if (fullName.length < 2) {
            firstName = fullName[0];
            lastName = fullName[0];
        } else {
            firstName = fullName[0];
            lastName = fullName[1];
        }
        Specification<User> specification = Specification.where(null);
        String finalFirstName = firstName;
        String finalLastName = lastName;
        specification.or(((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("firstName"), String.format("%%%s%%", finalFirstName))));
        specification.or(((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("lastName"), String.format("%%%s%%", finalLastName))));
        return new ResponseEntity<>(userRepository.findAll(specification), HttpStatus.OK);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("user with id: " + id + " not found"));
    }
    @Override
    public List <User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public Long deleteUserById(Long id) {
        return userRepository.deleteUserById(id).orElseThrow(() ->
                new UsernameNotFoundException("user with id: " + id + " not found")).getId();
    }
    @Override
    public void blockUser(Long id) {
//        Optional<User> user = userRepository.findById(id);
//        user.ifPresent(() -> {
//            user.get().setBlocked(!user.get().isBlocked());
//            userRepository.save(user.get());
//        });
    }
    public static Object objectMapper(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.convertValue(object, User.class);
    }
}
