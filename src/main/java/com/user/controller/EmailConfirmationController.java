package com.user.controller;

import com.user.exception.ConfirmationCodeNotCorrect;
import com.user.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RequiredArgsConstructor
@Slf4j
@Controller
public class EmailConfirmationController {

    private final UserServiceImpl userService;

    @Operation(summary = "return page for confirmation",
            description = "Подтверждение емейла при регистрации", tags = {"Account service"})
    @RequestMapping(value = "/api/v1/approve/{uuid}",
            produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.GET)
    public String PageConfirmationEmail(@PathVariable UUID uuid, Model model, HttpServletResponse response) {
        log.warn(uuid.toString());
        String email = userService.getEmailByUUid(uuid);
        Cookie cookie = new Cookie("user_uuid", uuid.toString());
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");
        cookie.setPath("/my_email/approve");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        model.addAllAttributes(map);
        return "index";
    }



    @Operation(summary = "confirmation email",
            description = "Ваш емейл подтвержден, отправка страницы", tags = {"Account service"})
    @RequestMapping(value = "/api/v1/my_email/approve", consumes = MediaType.ALL_VALUE,
            produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.POST)
    public String confirmationEmail(Model model,
                                    @RequestParam String answer,
                                    @RequestParam Integer numberQuestion,
                                    HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies !=null) {
            for(Cookie c: cookies) {
                System.out.println(c.getName() + " - " + c.getValue());
            }
        }
        String userUuid = "7e4f8d1c-917d-4804-b2e4-a5bfef0d2967";
        log.info(String.valueOf(userUuid));
        Map<String, String> map = new HashMap<>();
        String email = userService.getEmailByUUid(UUID.fromString(userUuid));
        map.put("email", email);
        model.addAllAttributes(map);
        userService.addRecoveryQuestionAndConfirmEmail(UUID.fromString(userUuid), numberQuestion, answer);
        return "approved";
    }




    @Operation(summary = "return page for confirmation",
            description = "Страница подтверждерния изменения емейла", tags = {"Account service"})
    @RequestMapping(value = "api/v1/approve/change",
            produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.GET)
    public String pageChangeEmail(Model model,
                                  @RequestParam UUID uuid,
                                  @RequestParam String presentEmail,
                                  @RequestParam String futureEmail,
                                  HttpServletResponse response) {

        Cookie cookie = new Cookie("presentEmail", presentEmail);
        cookie.setMaxAge(24 * 60 * 60); // expires in 1 day
        Cookie cookie2 = new Cookie("futureEmail", futureEmail);
        cookie2.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
        response.addCookie(cookie2);
        Map<String, String> map = new HashMap<>();
        log.info("viev page edit email");
        map.put("email", presentEmail);
        map.put("future_email", futureEmail);
        model.addAllAttributes(map);
        return "editEmail";
    }


    @Operation(summary = "confirmation email",
            description = "Контроллер проверки кода", tags = {"Account service"})
    @RequestMapping(value = "/api/v1/code/approve/change_email_code", consumes = MediaType.ALL_VALUE,
            produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.GET)
    public String confirmationEditEmailByCode(Model model,
                                              @RequestParam String code,
                                              @CookieValue(value = "presentEmail") String presentEmail,
                                              @CookieValue(value = "futureEmail") String futureEmail) {
        int codeFromString;
        try {
            codeFromString = Integer.parseInt(code);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new ConfirmationCodeNotCorrect("Код подтверждения содержит только цифры");
        }

        log.info("код из контроллера: " + codeFromString + " емейл куда отправлен код: " + presentEmail);

        if (!userService.checkConfirmationCode(codeFromString)) {
            throw new ConfirmationCodeNotCorrect("Введен не верный код подтверждения");
        } else {
            userService.setEmail(presentEmail, futureEmail);
            log.info("change email to: " + futureEmail + " was confirmed by code from preveous email!");
            Map<String, String> map = new HashMap<>();
            map.put("email", futureEmail);
            model.addAllAttributes(map);
            return "approved";
        }
    }


    @Operation(summary = "confirmation email",
            description = "Подтверждение емейла при регистрации", tags = {"Account service"})
    @RequestMapping(value = "/api/v1/code/approve/change_email", consumes = MediaType.ALL_VALUE,
            produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.GET)
    public String confirmationEditEmailByUUID(Model model,
                                              @RequestParam String answer,
                                              @RequestParam Integer numberQuestion,
                                              @CookieValue(value = "presentEmail") String presentEmail,
                                              @CookieValue(value = "futureEmail") String futureEmail) {
        log.info(answer);
        log.info(numberQuestion.toString());
        //сравнение контрольныйх впросов

        if (!userService.checkRecoveryQuestionAndAnswer(presentEmail, answer, numberQuestion)) {
            throw new ConfirmationCodeNotCorrect("Введен не верный ответ на контрольный вопрос" +
                    " или не правильно выбран контрольный вопрос!");
        } else {
            userService.setEmail(presentEmail, futureEmail);
            log.info("Change email to: " + futureEmail + " was confirmed by recovery question!");
            Map<String, String> map = new HashMap<>();
            map.put("email", futureEmail);
            model.addAllAttributes(map);
            return "approved";
        }
    }
}
