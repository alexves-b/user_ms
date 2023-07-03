package com.user.dto.account;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountStatisticRequestDto {

    LocalDateTime date;

    LocalDateTime firstMonth;

    LocalDateTime lastMonth;

}
