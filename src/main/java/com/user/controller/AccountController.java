package com.user.controller;

import com.netflix.discovery.EurekaClient;
import com.user.dto.RequestDtoChangeEmail;
import com.user.dto.account.AccountDto;
import com.user.dto.account.AccountStatisticResponseDto;
import com.user.dto.secure.AccountSecureDto;
import com.user.dto.account.AccountStatisticRequestDto;
import com.user.model.User;
import com.user.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AccountController {
    private final EurekaClient eurekaClient;
    private final UserServiceImpl userService;
    @Operation(summary = "get AccountByEmail", description = "Получение данных аккаунта по email", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),})
    @GetMapping(value = "/api/v1/account", produces = {"application/json"})
    AccountDto getAccount(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    @Operation(summary = "Edit Account", description = "Обновление данных аккаунта", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @PutMapping(value = "/api/v1/account")
    AccountDto editAccount(@RequestBody AccountDto accountDto) {
        return new AccountDto(userService.editUser(accountDto));
    }
    @Operation(summary = "create Account", description = "Создание аккаунта при регистрации", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @PostMapping(value = "/api/v1/account",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
   public AccountSecureDto createAccount(@RequestBody AccountSecureDto accountSecureDto) {
        return userService.createUser(accountSecureDto);
    }
    @Operation(summary = "get account when login",
            description = "Получение своих данных при входе на сайт", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @GetMapping(value = "/api/v1/account/me", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    AccountDto getAccountWhenLogin(@RequestHeader("Authorization") @NonNull String bearerToken) {
       String email = userService.getEmailFromBearerToken(bearerToken);
        return userService.getUserByEmail(email);
    }
    @Operation(summary = "edit photo from profile settings", description = "Обновление аватара",
            tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/me/addAvatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    String editAccountIfLogin(@RequestHeader("Authorization") @NonNull String bearerToken,
                              @RequestParam("file") MultipartFile file ){
        return userService.uploadAvatarToServer(bearerToken,file);
    }
    @Operation(summary = "edit account if login", description = "Обновление авторизованного аккаунта",
            tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/me",
            consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT)
    AccountDto editAccountIfLogin(@RequestHeader("Authorization") @NonNull String bearerToken,
                            @RequestBody AccountDto accountDto ){
        String email = userService.getEmailFromBearerToken(bearerToken);
        log.info(accountDto.toString());
        return new AccountDto(userService.editUser(accountDto,email));
    }
    @Operation(summary = "edit Email", description = "Обновление авторизованного аккаунта",
            tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/me/change-email",
            consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    AccountDto editEmail(@RequestHeader("Authorization") @NonNull String bearerToken,
                                  @RequestBody RequestDtoChangeEmail requestDtoChangeEmail ){
        log.info(requestDtoChangeEmail.getEmail().getEmail());
        return userService.changeEmail(requestDtoChangeEmail.getEmail().getEmail(),bearerToken);
    }
    @Operation(summary = "edit Password", description = "Обновление авторизованного аккаунта",
            tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/me/change-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT)
    AccountDto editPassword(@RequestHeader("Authorization") @NonNull String bearerToken,
                                  @RequestBody AccountDto accountDto ){
        return userService.changePassword(accountDto.getPassword(),bearerToken);
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
    void markAccountForDelete(@RequestHeader("Authorization") @NonNull String bearerToken) {
        userService.markForDeleteUserAfterThirtyDaysByToken(bearerToken);
    }
    @Operation(summary = "unmark account for delete",
            description = "Снимает пометку авторизованного аккаунта как удалённый",
            tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/me/unmark",
            method = RequestMethod.POST)
    void unmarkAccountForDelete(@RequestHeader("Authorization") @NonNull String bearerToken) {
        userService.unmarkForDeleteUserAfterThirtyDaysByToken(bearerToken);
    }
    @Operation(summary = "Get account by id", description = "Получение данных по id", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/{id}/account",
            method = RequestMethod.GET)
    AccountDto getAccountById(@PathVariable Long id) {
        return new AccountDto(userService.getUserById(id));
    }
    @Operation(summary = "Delete account by id", description = "Полность удаляет аккаунт из базы по id", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/{id}/account",
            method = RequestMethod.DELETE)
    Long deleteAccountById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }
    @Operation(summary = "Get all accounts",
            description = "Позволяет получить все аккаунты", tags = {"Account service"})
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
    AccountStatisticResponseDto getListAllAccounts(@RequestBody AccountStatisticRequestDto accountStatisticRequestDto) {
        userService.getStatistic(accountStatisticRequestDto);
        return new AccountStatisticResponseDto();
    }

    @Operation(summary = "Get Account By statusCode",
            description = "Позволяет получать аккаунты относительно запрашиваемого статуса", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/search/statusCode",
            method = RequestMethod.GET)
    List <AccountDto> getAccountByStatusCode() {
        return new ArrayList<AccountDto>();
    }

    @Operation(summary = "Search users by name",
            description = "Поиск пользователей по имени", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/search",
            method = RequestMethod.GET)
    List<AccountDto> getUserByName(@RequestParam(name = "username") String userFullName,
                                   @RequestParam(name = "offset", defaultValue = "-1") String offset,
                                   @RequestParam(name = "limit", defaultValue = "3") String limit) {
        return userService.searchUser(userFullName, offset, limit);
    }
    @Operation(summary = "Block/unblock user",
            description = "блокировка / разблокировка пользователя", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @RequestMapping(value = "/api/v1/account/is-block",
            method = RequestMethod.PUT)
    void blockUser(@RequestParam(value = "id") Long id) {
        userService.blockUser(id);
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
        return userService.getUserCount();
    }
    @Operation(summary = "get country list when login",
            description = "Получение своих данных при входе на сайт", tags = {"Account service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @GetMapping(value = "/api/v1/geo/country", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Map getCountryWhenLogin(@RequestHeader("Authorization") @NonNull String bearerToken) {
        return userService.getCountryList();
    }

}
