package com.user.controller;

import com.user.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            produces = MediaType.TEXT_HTML_VALUE,method = RequestMethod.GET)
    public String PageConfirmationEmail(@PathVariable UUID uuid,Model model) {
        log.warn(uuid.toString());
        String email = userService.getEmailByUUid(uuid);
        Map <String,String> map = new HashMap<>();
        map.put("email",email);
        model.addAllAttributes(map);
        return "index";
    }

    @Operation(summary = "confirmation email",
            description = "Подтверждение емейла при регистрации", tags = {"Account service"})
    @RequestMapping(value = "/api/v1/approve/{uuid}",
            produces = MediaType.TEXT_HTML_VALUE,method = RequestMethod.POST)
    public String confirmationEmail(@PathVariable UUID uuid,Model model) {
        log.warn(uuid.toString());
        String email = userService.compareUUid(uuid);
        Map <String,String> map = new HashMap<>();
        map.put("email",email);
        model.addAllAttributes(map);
        return "index";
    }


}
