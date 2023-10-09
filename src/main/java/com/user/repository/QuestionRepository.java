package com.user.repository;

import com.user.model.RecoveryQuestion;
import com.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<RecoveryQuestion,Integer>, JpaSpecificationExecutor<RecoveryQuestion>  {

}
