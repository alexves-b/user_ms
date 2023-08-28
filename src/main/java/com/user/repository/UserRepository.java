package com.user.repository;

import com.user.dto.response.AccountResponseDto;
import com.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> , JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);
    Optional <User> findUserByEmail(String email);

    Optional <User> findById(Long id);

    Optional <User> deleteUserById(Long id);
}
