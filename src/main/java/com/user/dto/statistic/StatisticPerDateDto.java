package com.user.dto.statistic;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StatisticPerDateDto {
    LocalDateTime date;
    int count;
}
