package com.user.service;

import com.user.dto.account.AccountDto;
import com.user.dto.account.AccountStatisticRequestDto;
import com.user.dto.secure.AccountSecureDto;
import com.user.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

public interface UserService {

    AccountDto getUserByEmail(String email);

    AccountSecureDto createUser(AccountSecureDto accountSecureDto);
    @Transactional
    User editUser(AccountDto accountDto);
    @Transactional
    User editUser(AccountDto accountDto, String email);
    List<AccountDto> searchUser(String username, String offset, String limit);
    User getUserById(Long id);
    List <User> getAllUsers();

    @Transactional
    Long deleteUserById(Long id);
    void markForDeleteUserAfterThirtyDaysByToken(String bearToken);
    void unmarkForDeleteUserAfterThirtyDaysByToken(String bearToken);
    void blockUser(Long id);
    String getEmailFromBearerToken(String bearerToken);
    Long getUserCount();
    AccountStatisticRequestDto getStatistic(AccountStatisticRequestDto accountStatisticRequestDto);
    void deleteAccountMarkedDeleteAndDelDateToday();

    String uploadAvatarToServer(byte[] file);
}
