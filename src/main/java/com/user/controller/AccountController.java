package com.user.controller;

import com.netflix.discovery.EurekaClient;
import com.user.dto.account.AccountDto;
import com.user.dto.response.AccountResponseDto;
import com.user.dto.secure.AccountSecureDto;
import com.user.dto.account.AccountStatisticRequestDto;
import com.user.dto.page.PageAccountDto;
import com.user.dto.search.AccountSearchDto;
import com.user.jwt_token.JwtTokenUtils;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = {"http://192.168.84.187:8101", "http://5.63.154.191:8098", "http://5.63.154.191:8084"}, maxAge = 3600)
public class AccountController {
    @Autowired
    private EurekaClient eurekaClient;
    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Operation(summary = "get AccountByEmail", description = "Получение данных аккаунта по email", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),})
    @GetMapping(value = "/api/v1/account", produces = {"application/json"})
    AccountResponseDto getAccount(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    //Возможность
    @Operation(summary = "Edit Account", description = "Обновление данных аккаунта", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account",
            method = RequestMethod.PUT)
    AccountResponseDto editAccount(@RequestBody AccountDto accountDto) {
        return userService.editUser(accountDto);
    }

    @Operation(summary = "create Account", description = "Создание аккаунта при регистрации", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @PostMapping(value = "/api/v1/account",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
   public AccountResponseDto createAccount(@RequestBody AccountSecureDto accountSecureDto) {
        return userService.createUser(accountSecureDto);
    }

    @Operation(summary = "get account when login",
            description = "Получение своих данных при входе на сайт", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/me",
            method = RequestMethod.GET)
    AccountResponseDto getAccountWhenLogin(@RequestHeader("Authorization") @NonNull String bearerToken) {
        log.info(" i am in 'AccountResponseDto getAccountWhenLogin(@NotNull Principal principal)'");
        final String[] parts = bearerToken.split(" ");
        final String jwtToken = parts[1];
        final Boolean result = jwtTokenUtils.isJwtTokenIsNotExpired(jwtToken);
        if (result) {
            log.info("claims from token: " + jwtTokenUtils.getAllClaimsFromToken(jwtToken).toString());
        }
        return userService.getUserByEmail(jwtTokenUtils.getAllClaimsFromToken(jwtToken).getSubject());
    }

    @Operation(summary = "edit account if login", description = "Обновление авторизованного аккаунта", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/me",
            method = RequestMethod.PUT)
    ResponseEntity<AccountDto> editAccountIfLogin(Principal principal) {
        return new ResponseEntity<AccountDto>(HttpStatus.OK);
    }

    @Operation(summary = "mark account for delete",
            description = "Помечает авторизованный аккаунт как удалённый" +
                    " и через заданное время стирает данные об аккаунте",
            tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/me",
            method = RequestMethod.DELETE)
    ResponseEntity<AccountSearchDto> markAccountForDelete() {
        return new ResponseEntity<AccountSearchDto>(HttpStatus.OK);
    }
    @Operation(summary = "Get account by id", description = "Получение данных по id", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/{id}",
            method = RequestMethod.GET)
    User getAccountById(Long id) {
        return userService.getUserById(id);
    }


    @Operation(summary = "Delete account by id", description = "Полность удаляет аккаунт из базы по id", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/{id}",
            method = RequestMethod.DELETE)
    Long deleteAccountById(Long id) {
        return userService.deleteUserById(id);
    }

    @Operation(summary = "Get all accounts, not work",
            description = "Позволяет получить все аккаунты, не реализован", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/unsupported",
            method = RequestMethod.GET)
    List<User> getAllAccounts() {
        return  userService.getAllUsers();
    }


    @Operation(summary = "Get Statistic",
            description = "Позволяет получить статистику по кол-ву" +
                    " регистраций по возрастам и по кол-ву регистраций" +
                    " по месяцам за указанный промежуток времени", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/statistic",
            method = RequestMethod.GET)
    ResponseEntity<AccountStatisticRequestDto> getListAllAccounts() {
        return new ResponseEntity<AccountStatisticRequestDto>(HttpStatus.OK);
    }

    @Operation(summary = "Get Account By statusCode",
            description = "Позволяет получать аккаунты относительно запрашиваемого статуса", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/search/statusCode",
            method = RequestMethod.GET)
    ResponseEntity<PageAccountDto> getAccountByStatusCode() {
        return new ResponseEntity<PageAccountDto>(HttpStatus.OK);
    }

    @Operation(summary = "Search users by name",
            description = "Поиск пользователей по имени", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/search{username}",
            method = RequestMethod.GET)
    ResponseEntity<List<User>> getUserByName(@PathVariable(value = "username") String username) {
        return userService.searchUser(username);
    }

    @Operation(summary = "Block/unblock user",
            description = "блокировка / разблокировка пользователя", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/is-block",
            method = RequestMethod.PUT)
    ResponseEntity<Void> blockUser(@RequestParam(value = "id") Long id) {
        userService.blockUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Operation(summary = "all users count",
            description = "всего пользователей", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/all-users",
            method = RequestMethod.GET)
    long getAllUsersCount() {
        return userRepository.count();
    }
}

