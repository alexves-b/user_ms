package com.user.service;
import com.user.model.User;
import org.springframework.data.jpa.domain.Specification;
public class UserSpecification {
    public static Specification<User> findByFirstName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), String.format("%%%s%%", name).toLowerCase());
    }
    public static Specification<User> findByLastName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), String.format("%%%s%%", name).toLowerCase());
    }
}