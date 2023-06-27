package com.user.dto.page;

import lombok.Data;

@Data
public class Pageable {

    int page = 0;

    int size = 1;
    String sort;

}
