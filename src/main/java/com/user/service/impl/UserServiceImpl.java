package com.user.service.impl;

import com.user.dto.account.AccountDto;
import com.user.dto.secure.AccountSecureDto;
import com.user.model.AuthorityEntity;
import com.user.model.RoleEntity;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    @Override
    public AccountSecureDto getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            System.out.println(user);
            System.out.println(user.getAuthorities().toString());

            return new AccountSecureDto(user.getId().toString(),
                    user.isDeleted(), user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getPassword(), Arrays.toString(user.getRoles().toString().toCharArray()),"342");
        }
        else return new AccountSecureDto();

    }

    @Override
    public AccountDto createUser(boolean isDeleted, String firstName, String lastName,
                                 String email, String password, List<RoleEntity> role, List<AuthorityEntity> authority) {
        User user = new User(isDeleted,firstName,lastName,email,password,role,authority);
        System.out.println(user);
        userRepository.save(user);

        return new AccountDto(user.getId().toString(),user.isDeleted(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
    }
}