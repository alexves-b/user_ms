package com.user.dto.page;

import lombok.Data;

@Data
public class PegableObject {

    Sort sort;

    int pageNumber;

    boolean unpaged;

    boolean paged;

    int pageSize;

    long offset;


}
