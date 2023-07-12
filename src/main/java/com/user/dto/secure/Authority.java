package com.user.dto.secure;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Table;

@Data
@AllArgsConstructor
@Table(name = "authorities")
public class Authority {
    String id;
    String authority;
}
