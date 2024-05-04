package com.example.java_practical_test_assignment.service.UserServiceImpl;

import com.example.java_practical_test_assignment.model.User;
import com.example.java_practical_test_assignment.repository.InMemoryUserDAO;
import com.example.java_practical_test_assignment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryUserServiceImpl implements UserService {

    private final InMemoryUserDAO repository;
    @Override
    public User createUser(User user) {
        return repository.createUser(user);
    }
    @Override
    public User updateSomeUserFields(String email, User updatedUser) {
        return repository.updateSomeUserFields(email, updatedUser);
    }
    @Override
    public User updateAllUserFields(String email, User user) {
        return repository.updateAllUserFields(email, user);
    }
    @Override
    public boolean deleteUser(User user) {
        return repository.isUserDeleted(user);
    }
    @Override
    public List<User> searchUserByBirthDate(String fromDate, String toDate) {
        return repository.searchUserByBirthDate(fromDate, toDate);
    }
}


