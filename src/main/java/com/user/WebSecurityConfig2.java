package com.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface WebSecurityConfig2 {
    void configure(HttpSecurity httpSecurity) throws Exception;
}
