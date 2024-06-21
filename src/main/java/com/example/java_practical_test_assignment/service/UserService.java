package com.example.java_practical_test_assignment.service;

import com.example.java_practical_test_assignment.dto.CreateUserDTO;
import com.example.java_practical_test_assignment.exception.ApiRequestException;
import com.example.java_practical_test_assignment.exception.BusinessException;
import com.example.java_practical_test_assignment.model.User;
import com.example.java_practical_test_assignment.model.UserKey;

import java.util.*;

public interface UserService {
    CreateUserDTO createUser(CreateUserDTO userDataDTO, UserKey id) throws ApiRequestException;

    User updateUser(UUID id, User user);

    void deleteUser(UUID id) throws ApiRequestException, BusinessException;

    List<User> searchUserByBirthDate(String fromDate, String toDate) throws ApiRequestException, BusinessException;

}

