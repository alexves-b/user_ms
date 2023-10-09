package com.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "user_recovery_answers")
@NoArgsConstructor
public class RecoveryAnswer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public RecoveryAnswer(int numberQuestion, String answerEncrypted, User user) {
        this.numberQuestion = numberQuestion;
        this.answerEncrypted = answerEncrypted;
        this.user = user;
    }

    private int numberQuestion;

    private String answerEncrypted;

    @OneToOne
    private User user;
}
