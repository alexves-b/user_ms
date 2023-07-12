package com.user.dto.secure;

import lombok.Data;

@Data
public class Role {
    public Role(String id, String role) {
        this.id = id;
        this.role = role;
    }
    String id;
    String role;


}
