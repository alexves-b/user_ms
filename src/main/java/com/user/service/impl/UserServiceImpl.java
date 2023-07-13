package com.user.service.impl;

import com.user.dto.account.AccountDto;
import com.user.dto.secure.AccountSecureDto;
import com.user.model.AuthorityEntity;
import com.user.model.RoleEntity;
import com.user.model.User;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    @Override
    public AccountSecureDto getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            System.out.println(user);
            System.out.println(user.getAuthorities().toString());

            return new AccountSecureDto(user.getId().toString(),
                    user.isDeleted(), user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getPassword(), Arrays.toString(user.getRoles().toString().toCharArray()),"342");
        }
        else return new AccountSecureDto();

    }

    @Override
    public AccountDto createUser(boolean isDeleted, String firstName, String lastName,
                                 String email, String password, List<RoleEntity> role, List<AuthorityEntity> authority) {
        User user = new User(isDeleted,firstName,lastName,email,password,role,authority);
        System.out.println(user);
        userRepository.save(user);

        return new AccountDto(user.getId().toString(),user.isDeleted(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
    }

    @Override
    public ResponseEntity<List<User>> searchUser(String username) {

        if (username == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        String[] fullName = username.split(" ");
        String firstName = null;
        String lastName = null;

        if (fullName.length < 2){
            firstName = fullName[0];
            lastName = fullName[0];
        } else {
            firstName = fullName[0];
            lastName = fullName[1];
        }
        Specification<User> specification = Specification.where(null);
        String finalFirstName = firstName;
        String finalLastName = lastName;
        specification.or(((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("firstName"), String.format("%%%s%%", finalFirstName))));
        specification.or(((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("lastName"), String.format("%%%s%%", finalLastName))));
        return new ResponseEntity<>(userRepository.findAll(specification), HttpStatus.OK);
    }

    @Override
    public void blockUser(Long id) {
//        Optional<User> user = userRepository.findById(id);
//        user.ifPresent(() -> {
//            user.get().setBlocked(!user.get().isBlocked());
//            userRepository.save(user.get());
//        });
    }
}