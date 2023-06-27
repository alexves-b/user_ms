package com.user.dto.page;

import com.user.dto.account.AccountDto;
import lombok.Data;

@Data
public class PageAccountDto {

    long totalElements;

    int totalPages;

    Sort sort;

    int numberOfElements;

    PegableObject pageable;

    boolean first;

    boolean last;

    int size;

    AccountDto content;

    int number;

    boolean empty;


}
