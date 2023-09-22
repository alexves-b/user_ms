package com.user.controller;
import com.netflix.discovery.EurekaClient;
import com.user.dto.Email;
import com.user.dto.RequestDtoChangeEmail;
import com.user.dto.account.AccountDto;
import com.user.dto.secure.AccountSecureDto;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(locations = "/test-context.xml")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = {AccountController.class})
@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private User user;
    private AccountSecureDto accountSecureDto;

    private AccountDto accountDto;
    private AccountDto expectedAccountDto;
    private  String bearerToken = "Bearer token";
    @MockBean
    EurekaClient eurekaClient;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    UserServiceImpl userService;
    @MockBean
    private UserRepository userRepository;


    @BeforeEach
    public void init() {
        expectedAccountDto = new AccountDto();

        objectMapper = new ObjectMapper();
        accountSecureDto = AccountSecureDto.builder()
                .email("testt@google.com")
                .roles("ROLE_ADMIN")
                .firstName("Vasya")
                .lastName("Pupkin")
                .password("1234567890")
                .build();

         accountDto = AccountDto.builder().id(1L)
                 .email("testt2@google.com")
                 .city("Piter")
                 .firstName("Andrey")
                 .about("t42t4")
                 .password("12345678901234567890")
                 .build();
    }

    @Test
    public void createUserTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(accountSecureDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void getUserTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/account")
                        .param("email","alexves@bk.ru"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void editUserTest() throws Exception {
        given(userService.editUser(accountDto)).willReturn(new User());
            mockMvc.perform(put("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(accountDto)))
                    .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void getAccountWhenLoginTest() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/account/me")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .header("Authorization",bearerToken))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void addAvatarControllerTest() throws  Exception{
        MockMultipartFile multipartFile = new MockMultipartFile("file", "kot_mulr53tfilm_4689.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "TEST".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/account/me/addAvatar").file(multipartFile)
                        .characterEncoding("utf-8")
                        .header("Authorization",bearerToken))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void editAccountIfLoginTest() throws Exception {
        String bearerToken = "Bearer <your_token>";
        when(userService.getEmailFromBearerToken(bearerToken)).thenReturn("test@example.com");
        when(userService.editUser(accountDto, "test@example.com"))
                .thenReturn(User.builder().id(333L).build());
        mockMvc.perform(put("/api/v1/account/me")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void changeEmailTest() throws Exception {
        RequestDtoChangeEmail requestDtoChangeEmail = new RequestDtoChangeEmail(new Email("new-email@example.com"));
        when(userService.changeEmail(anyString(), anyString())).thenReturn(expectedAccountDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account/me/change-email")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(new ObjectMapper().writeValueAsString(requestDtoChangeEmail)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void changePasswordTest() throws Exception {
        when(userService.changeEmail(anyString(), anyString())).thenReturn(expectedAccountDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/account/me/change-password")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(new ObjectMapper().writeValueAsString(accountDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void markAccountForDeleteTest() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/account/me")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .header("Authorization",bearerToken))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void unmarkAccountForDeleteTest() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/account/me/unmark")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .header("Authorization",bearerToken))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getAccountByIdTest() throws  Exception{
        when(userService.getUserById(anyLong())).thenReturn(new User());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/{id}/account",1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void deleteAccountByIdTest() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/{id}/account",1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void getAllAccountsTest() throws  Exception{
        when(userService.getAllUsers()).thenReturn(new ArrayList<User>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/account/unsupported")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void blockAccountByIdTest() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/account/is-block")
                        .param("id","1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}


