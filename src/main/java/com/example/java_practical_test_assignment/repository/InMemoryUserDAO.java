package com.example.java_practical_test_assignment.repository;

import com.example.java_practical_test_assignment.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryUserDAO {

    private final Map<UUID, User> userStorage = new HashMap<>();

    public User getUserToId(UUID id) {
        return userStorage.get(id);
    }

    public User putUser(UUID id, User user) {

        userStorage.put(id, user);

        return user;
    }

    public User replaceUser(UUID id, User user) {

        return userStorage.replace(id, user);

    }

    public boolean containsKey(UUID id) {

        return userStorage.containsKey(id);
    }

    public void delete(UUID id) {

        userStorage.remove(id);
    }

    public Collection<User> values() {
        return userStorage.values();
    }
}


