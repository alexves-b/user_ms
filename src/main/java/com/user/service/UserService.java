package com.user.service;

import com.user.dto.account.AccountDto;
import com.user.dto.response.AccountResponseDto;
import com.user.dto.secure.AccountSecureDto;
import com.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

public interface UserService {

    AccountResponseDto getUserByEmail(String email);

    AccountResponseDto createUser(AccountSecureDto accountSecureDto);
    @Transactional
    User editUser(AccountDto accountDto);

    ResponseEntity<List<User>> searchUser(String username);
    User getUserById(Long id);

    List <User> getAllUsers();

    Long deleteUserById(Long id);

    void blockUser(Long id);

}
