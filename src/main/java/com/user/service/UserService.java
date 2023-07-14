package com.user.service;

import com.user.dto.account.AccountDto;
import com.user.dto.secure.AccountSecureDto;
import com.user.model.AuthorityEntity;
import com.user.model.RoleEntity;
import com.user.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    AccountSecureDto getUserByEmail(String email);

    AccountDto createUser(boolean isDeleted, String firstName, String lastName,
                          String email, String password, List <RoleEntity> role, List <AuthorityEntity> authority);

    ResponseEntity<List<User>> searchUser(String username);

    void blockUser(Long id);

}
