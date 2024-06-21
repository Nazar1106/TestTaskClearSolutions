package com.example.java_practical_test_assignment.service.UserServiceImpl;

import com.example.java_practical_test_assignment.dto.CreateUserDTO;
import com.example.java_practical_test_assignment.dto.UserAllUpdateDTO;
import com.example.java_practical_test_assignment.dto.UserPartialUpdateDTO;
import com.example.java_practical_test_assignment.exception.ApiRequestException;
import com.example.java_practical_test_assignment.exception.BusinessException;
import com.example.java_practical_test_assignment.mapper.UserMapper;
import com.example.java_practical_test_assignment.model.User;
import com.example.java_practical_test_assignment.model.UserKey;
import com.example.java_practical_test_assignment.repository.InMemoryUserDAO;
import com.example.java_practical_test_assignment.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.*;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final InMemoryUserDAO repository;
    private final UserMapper userMapper;

    public UserServiceImpl(InMemoryUserDAO repository, UserMapper userMapper) {
        this.repository = repository;
        this.userMapper = userMapper;
    }

    @Value("${user.minAge}")
    int minimumAge;

    @Override
    public CreateUserDTO createUser(CreateUserDTO userDTO, UserKey userKey) throws ApiRequestException {

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        int age = getAge(user);
        if (age > minimumAge) {
            userKey.setId(UUID.randomUUID());
            repository.putUser(userKey.getId(), user);
            return userDTO;
        } else {
            throw new ApiRequestException("invalid user age " + userDTO.getBirthDate());
        }
    }

    @Override
    public User updateUser(UUID id, User user) {

        return repository.replaceUser(id, user);
    }

    public UserAllUpdateDTO updateWholeUser(UUID id, UserAllUpdateDTO userDto) throws ApiRequestException, BusinessException {

        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        int age = getAge(user);
        if (!repository.containsKey(id)) {
            throw new BusinessException("User with the specified id was not found " + id);
        } else if (minimumAge >= age) {
            throw new ApiRequestException("invalid user age " + user.getBirthDate());
        }

        User existingUser = repository.getUserToId(id);
        if (repository.containsKey(id) && isAnyFieldUpdated(user, existingUser)) {
            updateUser(id, user);
            return userDto;
        } else {
            throw new ApiRequestException("All user field must be update");
        }
    }

    public UserPartialUpdateDTO updatePartialUser(UUID id, UserPartialUpdateDTO user) throws BusinessException, ApiRequestException {

        User existingUser = repository.getUserToId(id);

        if (user.getBirthDate() != null) {
            int age = getAge(userMapper.toUserPartial(user));
            if (minimumAge >= age) {
                throw new ApiRequestException("invalid user age " + user.getBirthDate());
            }
        }
        if (existingUser != null) {
            String[] ignoreProperties = getNullPropertyNames(user);
            BeanUtils.copyProperties(user, existingUser, ignoreProperties);
            updateUser(id, existingUser);
            return userMapper.userToPartialUser(existingUser);
        } else {
            throw new BusinessException("User with the specified id was not found ");//+ user.getId());
        }
    }

    private String[] getNullPropertyNames(Object source) {
        final Field[] fields = source.getClass().getDeclaredFields();
        List<String> nullProperties = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(source) == null) {
                    nullProperties.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could not access field", e);
            }
        }
        return nullProperties.toArray(new String[0]);
    }

    @Override
    public void deleteUser(UUID id) throws ApiRequestException {
        {
            if (!repository.containsKey(id)) {
                throw new ApiRequestException("User not found with id: " + id);
            }
            repository.delete(id);
        }
    }

    @Override
    public List<User> searchUserByBirthDate(String fromDate, String toDate) throws ApiRequestException {
        if (fromDate == null || toDate == null) {
            throw new ApiRequestException("From and To dates must be provided");
        }
        LocalDate fromDateObj = LocalDate.parse(fromDate);
        LocalDate toDateObj = LocalDate.parse(toDate);

        if (fromDateObj.isAfter(toDateObj)) {
            throw new ApiRequestException("From date must be less than To date");
        }

        List<User> usersInRange = new ArrayList<>();
        for (User user : repository.values()) {
            LocalDate userBirthDate = user.getBirthDate();
            if (!userBirthDate.isBefore(fromDateObj) && !userBirthDate.isAfter(toDateObj)) {
                usersInRange.add(user);
            }
        }
        return usersInRange;
    }

    public static int getAge(User user) {
        if (user == null) {
            throw new ApiRequestException("User cannot be null");
        }

        LocalDate birthDate = user.getBirthDate();
        LocalDate currentDate = LocalDate.now();

        return birthDate.isBefore(currentDate) ? Period.between(birthDate, currentDate).getYears() : 0;
    }

    public boolean isAnyFieldUpdated(User updatedUser, User existingUser) {
        return !Objects.equals(updatedUser.getEmail(), existingUser.getEmail()) &&
                !Objects.equals(updatedUser.getFirstName(), existingUser.getFirstName()) &&
                !Objects.equals(updatedUser.getLastName(), existingUser.getLastName()) &&
                !Objects.equals(updatedUser.getBirthDate(), existingUser.getBirthDate()) &&
                !Objects.equals(updatedUser.getAddress(), existingUser.getAddress()) &&
                !Objects.equals(updatedUser.getPhoneNumber(), existingUser.getPhoneNumber());
    }
}

