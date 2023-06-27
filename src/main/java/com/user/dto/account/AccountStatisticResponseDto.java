package com.user.dto.account;

import com.user.dto.statistic.StatisticPerDateDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountStatisticResponseDto {

    LocalDateTime date;

    int count;

    AccountCountPerAge countPerAge;

    StatisticPerDateDto countPerMonth;

}
