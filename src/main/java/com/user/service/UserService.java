package com.user.service;

import com.user.dto.account.AccountSecureDto;

public interface UserService {

    AccountSecureDto getUserByEmail(String email);

}
