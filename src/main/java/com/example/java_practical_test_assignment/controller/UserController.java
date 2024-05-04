package com.example.java_practical_test_assignment.controller;

import com.example.java_practical_test_assignment.exception.ApiRequestException;
import com.example.java_practical_test_assignment.model.User;
import com.example.java_practical_test_assignment.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/update/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @Valid @RequestBody User user) {
        try {
            var updateUser = userService.updateAllUserFields(email, user);
            return ResponseEntity.ok(updateUser);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<User> createUsers(@Valid @RequestBody User user) {
        try {
            var createUser = userService.createUser(user);
            return ResponseEntity.ok(createUser);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @PatchMapping("/{email}")
    public ResponseEntity<User> updateSomeFields(@PathVariable String email, @Valid @RequestBody User updatedUser) {
        try {
            var update = userService.updateSomeUserFields(email, updatedUser);
            return ResponseEntity.ok(update);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUserByBirthDate(
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate) throws ApiRequestException {
        try {
            var userSearch = userService.searchUserByBirthDate(fromDate, toDate);
            return ResponseEntity.ok(userSearch);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteUser(@RequestBody User user) {
        try {
            var isUserDeleted = userService.deleteUser(user);
            return ResponseEntity.ok(isUserDeleted);
        } catch (Exception e) {
            throw new ApiRequestException("Can't find user by this email.");
        }
    }
}