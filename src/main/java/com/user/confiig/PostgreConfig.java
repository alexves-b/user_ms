package com.user.confiig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;



public class PostgreConfig {
    @Bean
    @Profile("default")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://5.63.154.191:5432/users");
        dataSource.setUsername("selectel");
        dataSource.setPassword("selectel");
        return dataSource;
    }
}
