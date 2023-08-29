package com.user.service;

import com.user.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> findByFirstName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("firstName"), String.format("%%%s%%", name));
    }

    public static Specification<User> findByLastName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("last_name"), String.format("%%%s%%", name));
    }
}