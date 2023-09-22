package com.user.service;

import com.user.client.AdminClient;
import com.user.database.BuildingPostgresqlContainer;
import com.user.dto.account.AccountDto;
import com.user.dto.secure.AccountSecureDto;
import com.user.jwt_token.JwtTokenUtils;
import com.user.kafka.KafkaProducer;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@Testcontainers
public class UserRepositoryTest {
        @Autowired
        private UserServiceImpl userService;
        @Autowired
        private UserRepository userRepository;
        @MockBean
        private  JwtTokenUtils jwtTokenUtils;
        @MockBean
        private  KafkaProducer kafkaProducer;
        @MockBean
        private  AdminClient adminClient;
        @MockBean
        private  BCryptPasswordEncoder passwordEncoder;
        private AccountSecureDto accountSecureDto;
        private AccountDto accountDto;
        @ClassRule
        public static PostgreSQLContainer<BuildingPostgresqlContainer> postgreSQLContainer = BuildingPostgresqlContainer.getInstance();

        


       // @BeforeEach
        public void setUp(){
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

        
    //@Test
    public void animalsCountShouldBeCorrect() throws Exception {

        User user = User.builder().email(accountSecureDto.getEmail())
                .firstName(accountSecureDto.getFirstName())
                .lastName(accountSecureDto.getLastName())
                .password(accountSecureDto.getPassword())
                .regDate(LocalDateTime.now())
                .id(3234L)
                .roles("ROLE_USER").build();

        Mockito.when(userRepository.save(user))
                .thenReturn(user);

        userService.createUser(accountSecureDto);
        long count = userService.getUserCount();
        assertEquals(1, count);
    }

      //  @Test
        public void animalsShouldBeCorrect() throws Exception {
            List<User> users = userService.getAllUsers();
        }
    }

