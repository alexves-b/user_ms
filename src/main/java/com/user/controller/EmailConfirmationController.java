package com.user.controller;

import com.user.exception.ConfirmationCodeNotCorrect;
import com.user.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RequiredArgsConstructor
@Slf4j
@Controller
public class EmailConfirmationController {
    private UUID uuidFromController;
    private String email;
    private String futureEmail;
    private String presentEmail;
    private final UserServiceImpl userService;
    @Operation(summary = "return page for confirmation",
            description = "Подтверждение емейла при регистрации", tags = {"Account service"})
    @RequestMapping(value = "/api/v1/approve/{uuid}",
            produces = MediaType.TEXT_HTML_VALUE,method = RequestMethod.GET)
    public String PageConfirmationEmail(@PathVariable UUID uuid,Model model) {
        log.warn(uuid.toString());
        uuidFromController = uuid;
        email = userService.getEmailByUUid(uuid);
        Map <String,String> map = new HashMap<>();
        map.put("email",email);
        model.addAllAttributes(map);
        return "index";
    }

    @Operation(summary = "confirmation email",
            description = "Ваш емейл подтвержден, отправка страницы", tags = {"Account service"})
    @RequestMapping(value = "/api/v1/approve/my_email", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE,method = RequestMethod.POST)
    public String confirmationEmail(Model model,
                                    @RequestParam String answer,
                                    @RequestParam Integer numberQuestion ) {
        log.info(String.valueOf(uuidFromController));
        Map <String,String> map = new HashMap<>();
        map.put("email",email);
        model.addAllAttributes(map);
        userService.addRecoveryQuestionAndConfirmEmail(uuidFromController,numberQuestion,answer);
        return "approved";
    }

    @Operation(summary = "return page for confirmation",
            description = "Страница подтверждерния изменения емейла", tags = {"Account service"})
    @RequestMapping(value = "api/v1/approve/change",
            produces = MediaType.TEXT_HTML_VALUE,method = RequestMethod.GET)
    public String pageChangeEmail(Model model, @RequestParam UUID uuid,@RequestParam String presentEmail, @RequestParam String futureEmail) {
        Map <String,String> map = new HashMap<>();
        log.info("viev page edit email");
        this.futureEmail = futureEmail;
        this.presentEmail = presentEmail;
        map.put("email",presentEmail);
        map.put("future_email",futureEmail);
        model.addAllAttributes(map);
        return "editEmail";
    }


    @Operation(summary = "confirmation email",
            description = "Контроллер проверки кода", tags = {"Account service"})
    @RequestMapping(value = "/api/v1/code/approve/change_email_code", consumes = MediaType.ALL_VALUE,
            produces = MediaType.TEXT_HTML_VALUE,method = RequestMethod.GET)
    public String confirmationEditEmailByCode(Model model,
                                    @RequestParam String code) {
        int codeFromString;
        try {
            codeFromString = Integer.parseInt(code);
        } catch (Exception exception){
            exception.printStackTrace();
            throw new ConfirmationCodeNotCorrect("Код подтверждения содержит только цифры");
        }

        log.info("код из контроллера: " + codeFromString + " емейл куда отправлен код: " + presentEmail);
       if (!userService.checkConfirmationCode(codeFromString)) {
          throw new ConfirmationCodeNotCorrect("Введен не верный код подтверждения");
       } else {
           userService.setEmail(presentEmail,futureEmail);
           log.info("change email to: "+ futureEmail + " was confirmed by code from preveous email!" );
           Map <String,String> map = new HashMap<>();
           map.put("email",futureEmail);
           model.addAllAttributes(map);
           return "approved";
       }
    }




    @Operation(summary = "confirmation email",
            description = "Подтверждение емейла при регистрации", tags = {"Account service"})
    @RequestMapping(value = "/api/v1/code/approve/change_email", consumes = MediaType.ALL_VALUE,
            produces = MediaType.TEXT_HTML_VALUE,method = RequestMethod.GET)
    public String confirmationEditEmailByUUID(Model model,
                                        @RequestParam String answer,
                                        @RequestParam Integer numberQuestion ) {
        log.info(answer);
        log.info(numberQuestion.toString());
        //сравнение контрольныйх впросов

        if (!userService.checkRecoveryQuestionAndAnswer(presentEmail,answer,numberQuestion)) {
            throw new ConfirmationCodeNotCorrect("Введен не верный ответ на контрольный вопрос" +
                    " или не правильно выбран контрольный вопрос!");
        } else {
            userService.setEmail(presentEmail,futureEmail);
            log.info("Change email to: "+ futureEmail + " was confirmed by recovery question!" );
            Map <String,String> map = new HashMap<>();
            map.put("email",futureEmail);
            model.addAllAttributes(map);
            return "approved";
        }
    }


}
