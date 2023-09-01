package com.user.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.dto.account.AccountDto;
import com.user.dto.account.AccountStatisticRequestDto;
import com.user.dto.secure.AccountSecureDto;
import com.user.exception.EmailIsBlank;
import com.user.exception.EmailNotUnique;
import com.user.exception.TokenDoesNotMatchEditUser;
import com.user.jwt_token.JwtTokenUtils;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import com.user.service.UserSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public AccountDto getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("user with email: " + email + " not found"));
        log.info("user from repository: \n" + user);
        return new AccountDto(user);
    }
    @Override
    public AccountSecureDto createUser(AccountSecureDto accountSecureDto) {
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
                .regDate(LocalDateTime.now())
                .roles("ROLE_USER").build();
        userRepository.save(user);
        log.info("User was created:  " + user);
        return new AccountSecureDto(user.getId(), user.getFirstName(),
                user.getLastName(), user.getEmail(), user.getPassword(), user.getRoles());
    }

    @Override
    @Transactional
    public User editUser(AccountDto accountDto) {
        User oldUser = userRepository.findUserByEmail(accountDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException
                        ("user with email: " + accountDto.getEmail() + " not found"));
        if (oldUser.getEmail().equals(accountDto.getEmail())) {
            oldUser = (User) objectMapper(accountDto);
            log.info("user was edited: " + oldUser);
            userRepository.save(oldUser);
        }
        return oldUser;
    }

    @Override
    public User editUser(AccountDto accountDto, String email) {
        if (accountDto.getEmail().equals(email)) {
            log.info("user was edited: " + accountDto);
           return editUser(accountDto);
        } else throw new TokenDoesNotMatchEditUser("Email from token not equals email in Dto");
    }


    @Override
    public List<AccountDto> searchUser(String userFullName, String offset, String limit) {
        if (userFullName.isBlank()) {
            throw new UsernameNotFoundException("пусто");
        }
        log.info("Fullname user: - " + userFullName);
        String[] fullName = userFullName.split(" ");
        String firstName;
        String lastName;

        if (fullName.length < 2) {
            firstName = fullName[0];
            lastName = fullName[0];
        } else {
            firstName = fullName[0];
            lastName = fullName[1];
        }
        Specification<User> specification = Specification
                .where(UserSpecification.findByFirstName(firstName))
                .or(UserSpecification.findByLastName(lastName));

        Page<User> userList = userRepository.findAll(specification,
                PageRequest.of(Integer.parseInt(offset), Integer.parseInt(limit)));

        ObjectMapper objectMapper = new ObjectMapper();
        return userList.stream()
                .map(user -> objectMapper.convertValue(user, AccountDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("user with id: " + id + " not found"));
        log.info("user with " + id + "was found" );
        return user;
    }
    @Override
    public List <User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    @Transactional
    public Long deleteUserById(Long id) {
        userRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("user with id: " + id + " not found"));
        log.info("user with id: " + id + " was deleted.");
        return userRepository.deleteUserById(id);
    }
    @Override
    public void markForDeleteUserAfterThirtyDaysByToken(String bearToken) {
        String email = getEmailFromBearerToken(bearToken);
        User user = (userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("user with email: " + email + " not found")));
        user.setIsDeleted(true);
        user.setDeletionDate(LocalDateTime.now().plusDays(30));
        userRepository.save(user);
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteAccountMarkedDeleteAndDelDateToday(){
        List <User> listForDeletion = userRepository.
                findUserByIsDeletedAndDeletionDateBeforeNow(LocalDate.now());
        log.info("time when was deleted: - " + LocalDateTime.now());
        log.info(listForDeletion.toString());
        userRepository.deleteAll(listForDeletion);
        log.info("users was deleted!");
    }
    @Transactional
    @Override
    public void unmarkForDeleteUserAfterThirtyDaysByToken(String bearToken) {
        String email = getEmailFromBearerToken(bearToken);
        User user = (userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("user with email: " + email + " not found")));
        user.setIsDeleted(false);
        user.setDeletionDate(null);
    }
    @Override
    public void blockUser(Long id) {
//        Optional<User> user = userRepository.findById(id);
//        user.ifPresent(() -> {
//            user.get().setBlocked(!user.get().isBlocked());
//            userRepository.save(user.get());
//        });
    }

    @Override
    public String getEmailFromBearerToken(String bearerToken) {
        log.info(bearerToken);
        final String[] parts = bearerToken.split("\\s");
        final String jwtToken = parts[1];
        log.info(jwtTokenUtils.decodeJWTToken(jwtToken));
        ObjectMapper mapper = new ObjectMapper();
        String email = "";
        try {
            Map<String,String> obj = mapper.readValue(jwtTokenUtils.decodeJWTToken(jwtToken), Map.class);
             email = obj.get("sub");
        } catch (Exception ex) {
            log.error("problem with parsing jwt token: - " +ex.getMessage());
        }
        return email;
    }

    @Override
    public Long getUserCount() {
       return userRepository.count();
    }

    @Override
    public AccountStatisticRequestDto getStatistic(AccountStatisticRequestDto accountStatisticRequestDto) {
        return new AccountStatisticRequestDto();

    }

    public static Object objectMapper(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.convertValue(object, User.class);
    }

}
