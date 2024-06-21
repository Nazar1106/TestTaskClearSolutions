package com.example.java_practical_test_assignment.controller;

import com.example.java_practical_test_assignment.dto.CreateUserDTO;
import com.example.java_practical_test_assignment.dto.UserAllUpdateDTO;
import com.example.java_practical_test_assignment.dto.UserPartialUpdateDTO;
import com.example.java_practical_test_assignment.exception.ApiRequestException;
import com.example.java_practical_test_assignment.exception.BusinessException;
import com.example.java_practical_test_assignment.model.User;
import com.example.java_practical_test_assignment.model.UserKey;
import com.example.java_practical_test_assignment.service.UserServiceImpl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CreateUserDTO> createUsers(@Valid @RequestBody CreateUserDTO user, UserKey key) throws ApiRequestException {
        userService.createUser(user, key);

        URI location = URI.create("/users/" + key.getId());

        return ResponseEntity.created(location).body(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public UserAllUpdateDTO updateAllUserFields(@PathVariable UUID id, @Valid @RequestBody
    UserAllUpdateDTO requestUserAllUpdateDTO) throws BusinessException, ApiRequestException {
        return userService.updateWholeUser(id, requestUserAllUpdateDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public UserPartialUpdateDTO partialUpdate(@PathVariable UUID id, @Valid @RequestBody UserPartialUpdateDTO user) throws BusinessException, ApiRequestException {
        return userService.updatePartialUser(id, user);
    }

    //todo: return Data
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Map<String, Object> searchUserByBirthDate(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) throws ApiRequestException, BusinessException {

        List<User> users = userService.searchUserByBirthDate(fromDate, toDate);

        Map<String, Object> response = new HashMap<>();
        response.put("data", users);

        return response;

    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") UUID id) throws ApiRequestException {
        userService.deleteUser(id);
    }
}