package com.user.service;

import com.user.dto.response.AccountResponseDto;
import com.user.dto.secure.AccountSecureDto;
import com.user.model.User;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface UserService {

    AccountResponseDto getUserByEmail(String email);

    AccountResponseDto createUser(AccountSecureDto accountSecureDto);


    AccountResponseDto editUser(AccountSecureDto accountSecureDto);

    ResponseEntity<List<User>> searchUser(String username);
    User getUserById(Long id);

    List <User> getAllUsers();

    Long deleteUserById(Long id);

    void blockUser(Long id);

}
