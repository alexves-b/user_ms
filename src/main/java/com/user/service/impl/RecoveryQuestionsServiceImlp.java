package com.user.service.impl;

import com.user.model.RecoveryQuestion;
import com.user.repository.QuestionRepository;
import com.user.service.RecoveryQuestionsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class RecoveryQuestionsServiceImlp implements RecoveryQuestionsService {

    private final QuestionRepository questionRepository;

    @Override
    public List<RecoveryQuestion> getListRecoveryQuestions() {
        return questionRepository.findAll();
    }

}
