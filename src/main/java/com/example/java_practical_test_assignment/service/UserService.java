package com.example.java_practical_test_assignment.service;

import com.example.java_practical_test_assignment.model.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateSomeUserFields(String email, User updatedUser);
    User updateAllUserFields(String email, User updateFields);
    boolean deleteUser(User user);
    List<User> searchUserByBirthDate(String fromDate, String toDate);
}
