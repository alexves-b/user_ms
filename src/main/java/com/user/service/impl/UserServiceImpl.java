package com.user.service.impl;

import com.user.dto.secure.AccountSecureDto;
import com.user.exception.EmailNotUnique;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    @Override
    public AccountSecureDto getUserByEmail(String email) {
        try {
            User user = userRepository.findUserByEmail(email);
            if (user != null) {
                return new AccountSecureDto(user.getFirstName(), user.getLastName(), user.getEmail(),
                        user.getPassword());
            } else {
                throw new UsernameNotFoundException("user with email: " + email + " not found");
            }
        } catch (Exception ex) {
            throw new UsernameNotFoundException("user with email: " + email + " not found");
        }
    }
    @Override
    public AccountSecureDto createUser(AccountSecureDto accountSecureDto) {
        try {
            User user = new User(accountSecureDto);
            user.setRoles("ROLE_USER");
            userRepository.save(user);
            user.setId(userRepository.save(user).getId());
            System.out.println(user);
            return (new AccountSecureDto(user.getFirstName(),
                    user.getLastName(), user.getEmail(), user.getPassword()));
        } catch (Exception exception) {
            throw new EmailNotUnique("email " + accountSecureDto.getEmail() + " not unique");
        }
    }

    @Override
    public ResponseEntity<List<User>> searchUser(String username) {
        if (username == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String[] fullName = username.split(" ");
        String firstName = null;
        String lastName = null;

        if (fullName.length < 2){
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
    public void blockUser(Long id) {
//        Optional<User> user = userRepository.findById(id);
//        user.ifPresent(() -> {
//            user.get().setBlocked(!user.get().isBlocked());
//            userRepository.save(user.get());
//        });
    }
}