package com.example.java_practical_test_assignment.repository;

import com.example.java_practical_test_assignment.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Repository
public class InMemoryUserDAO {
    public final List<User> USERS = new ArrayList<>();

    @Value("${user.minAge}")
    public int minimumAge;
    public User createUser(User user) {
        int age = getAge(user);

        if (age > minimumAge) {
            USERS.add(user);
            return user;
        } else {
            throw new IllegalArgumentException("Invalid age exception");
        }
    }
    public User updateSomeUserFields(String email, User updatedUser) {

        Map<String, User> userMap = new HashMap<>();
        for (User user : USERS) {
            userMap.put(user.getEmail(), user);
        }
        int age = getAge(updatedUser);

        if (age <= minimumAge) {
            throw new IllegalArgumentException("User is too young to be updated");
        }
        User userToUpdate = userMap.get(email);
        if (userToUpdate != null) {
            userToUpdate.setEmail(updatedUser.getEmail());
            userToUpdate.setFirstName(updatedUser.getFirstName());
            userToUpdate.setLastName(updatedUser.getLastName());
            userToUpdate.setBirthDate(updatedUser.getBirthDate());
            userToUpdate.setAddress(updatedUser.getAddress());
            userToUpdate.setPhoneNumber(updatedUser.getPhoneNumber());
        } else {
            throw new IllegalArgumentException("User with the specified email address was not found " + email);
        }
        return userToUpdate;
    }
    public User updateAllUserFields(String email, User user) {
        int age = getAge(user);
        if (age <= minimumAge) {
            throw new IllegalArgumentException("User must be at least 18 years old");
        }

        User existingUser = USERS.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        if (!isAnyFieldUpdated(user, existingUser)) {
            throw new IllegalArgumentException("All user field must be update");
        }
        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setBirthDate(user.getBirthDate());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        return existingUser;
    }
    public boolean isUserDeleted(User user) {
        var removed = USERS.removeIf((e) -> e.getEmail().equals(user.getEmail()));
        if (!removed) {
            throw new NoSuchElementException("User not found: " + user.getEmail());
        }
        return true;
    }
    public List<User> searchUserByBirthDate(String fromDate, String toDate) {
        if (fromDate == null || toDate == null) {
            throw new IllegalArgumentException("From and To dates must be provided");
        }
        LocalDate fromDateObj = LocalDate.parse(fromDate);
        LocalDate toDateObj = LocalDate.parse(toDate);

        if (fromDateObj.isAfter(toDateObj)) {
            throw new IllegalArgumentException("From date must be less than To date");
        }
        List<User> usersInRange = new ArrayList<>();
        for (User user : USERS) {
            LocalDate userBirthDate = user.getBirthDate();
            if (!userBirthDate.isBefore(fromDateObj) && !userBirthDate.isAfter(toDateObj)) {
                usersInRange.add(user);
            }
        }
        return usersInRange;
    }
    public static int getAge(User user) {
        LocalDate birthDate = user.getBirthDate();
        LocalDate currentDate = LocalDate.now();

        return birthDate.isBefore(currentDate) ? Period.between(birthDate, currentDate).getYears() : 0;
    }
    private boolean isAnyFieldUpdated(User updatedUser, User existingUser) {
        return !Objects.equals(updatedUser.getEmail(), existingUser.getEmail()) &&
                !Objects.equals(updatedUser.getFirstName(), existingUser.getFirstName()) &&
                !Objects.equals(updatedUser.getLastName(), existingUser.getLastName()) &&
                !Objects.equals(updatedUser.getBirthDate(), existingUser.getBirthDate()) &&
                !Objects.equals(updatedUser.getAddress(), existingUser.getAddress()) &&
                !Objects.equals(updatedUser.getPhoneNumber(), existingUser.getPhoneNumber());
    }
}
