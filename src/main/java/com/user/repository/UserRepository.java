package com.user.repository;

import com.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> , JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);
    Optional <User> findUserByEmail(String email);
    Optional <User> findById(Long id);
    Long deleteUserById(Long id);

    @Query(value = "SELECT * FROM public.users WHERE is_deleted = true AND deletion_date <= NOW()"
            ,nativeQuery = true)
   Optional <ArrayList<User>> findUserByIsDeletedAndDeletionDateBeforeNow();

}
