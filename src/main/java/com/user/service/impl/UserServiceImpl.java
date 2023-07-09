package com.user.service.impl;

import com.user.dto.account.AccountSecureDto;
import com.user.dto.secure.Authority;
import com.user.dto.secure.Role;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    @Override
    public AccountSecureDto getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        System.out.println(user);
        Role role = new Role();
        role.setId("1");
        role.setRole(user.getRole());
        com.user.dto.secure.Authority authority = new Authority();
        authority.setId("1");
        authority.setAuthority(user.getAuthority());
        return new AccountSecureDto(user.getId().toString(),
                user.isDeleted(), user.getFirstName(), user.getEmail(),
                user.getPassword(), role,authority);
    }
}