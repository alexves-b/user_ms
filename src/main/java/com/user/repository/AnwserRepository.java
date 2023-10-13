package com.user.repository;

import com.user.model.RecoveryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnwserRepository extends JpaRepository<RecoveryAnswer,Integer>, JpaSpecificationExecutor<RecoveryAnswer> {
    Optional<RecoveryAnswer> findAnswerByUserId(Long userId);

}
