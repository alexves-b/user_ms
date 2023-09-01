package com.user.dto.search;

import com.user.dto.account.StatusCodeType;
import lombok.Data;

@Data
public class AccountSearchDto {

    String id;
    boolean isDeleted;
    String ids;
    String blockedByIds;
    String author;

    String firstName;

    String lastName;

    String city;

    String country;

    boolean isBlocked;

    StatusCodeType statusCode;

    Integer ageTo;

    Integer ageFrom;

}
