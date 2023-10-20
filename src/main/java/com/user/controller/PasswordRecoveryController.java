package com.user.controller;

        import com.user.dto.RequestDtoChangeEmail;
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

@Controller
@Slf4j
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class PasswordRecoveryController {

    private final UserServiceImpl userService;

    @Operation(summary = "recovery password",
            description = "Обработка запроса на отправку нового пароля на емейл", tags = {"Auth service"})
    @RequestMapping(value = "/password/recovery/",
            produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String sendNewPasswordToUser(@RequestBody RequestDtoChangeEmail dto) {
        log.info("email for recovery: " + dto.getEmail().getEmail());
        userService.sendNewPasswordForUserEmail(dto.getEmail().getEmail());
        //страничку вернуть? шаблон?
        return "it works";
    }
    @Operation(summary = "view page for recovery password",
            description = "Отображение страницы для восстановления пароля по кодовому вопросу",
            tags = {"Auth service"})
    @RequestMapping(value = "/recovery_bq",
            produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.GET)
    public String validateEmailAndRecoveryQuestion(Model model) {
        return "recovery";
    }

    @Operation(summary = "validate answer on recovery question",
            description = "Валидация ответа на контрольный вопрос", tags = {"Auth service"})
    @RequestMapping(value = "/recovery_bq/approved",
            produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.GET)
    public String validateEmailAndRecoveryQuestion(Model model, @RequestParam String oldEmail,
                                                   @RequestParam String newEmail,
                                                   @RequestParam String answer,
                                                   @RequestParam Integer numberQuestion) {
        log.info(answer);
        log.info(numberQuestion.toString());
        log.info(oldEmail);
        log.info(newEmail);

        if (!userService.checkRecoveryQuestionAndAnswer(oldEmail,answer,numberQuestion)) {
            throw new ConfirmationCodeNotCorrect("Введен не верный ответ на контрольный вопрос" +
                    " или не правильно выбран контрольный вопрос!");
        } else {
            userService.sendNewPasswordForNewUserEmail(oldEmail,newEmail);
            Map<String,String> map = new HashMap<>();
            map.put("email",newEmail);
            model.addAllAttributes(map);
            return "recovery_approved";
        }
    }
}