package com.example.java_practical_test_assignment.repository;

import com.example.java_practical_test_assignment.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserDAOTest {

    private InMemoryUserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new InMemoryUserDAO();
        userDAO.minimumAge = 18;
    }

    @Test
    void testCreateUser_Success() {

        User user = getUser();

        User createdUser = userDAO.createUser(user);

        assertEquals(user, createdUser);
        assertTrue(userDAO.USERS.contains(user));
    }

    @Test
    void testCreateUser_InvalidAge() {

        LocalDate invalidBirthDate = LocalDate.now().plusYears(1); // дата народження в майбутньому
        User user = new User("oleg@example.com", "Oleg", "Messi", invalidBirthDate, "11 Shevchenka St", "0937622166");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userDAO.createUser(user);
        });
        assertEquals("Invalid age exception", exception.getMessage());
        assertFalse(userDAO.USERS.contains(user));
    }

    @Test
    void testUpdateSomeUserFields_Success() {

        User existingUser = new User("oleg@gmail.com", "Oleg", "Messi", LocalDate.of(1995, 3, 1), "11 Shevchenka St", "0937622166");
        User updatedUser = new User("Ivan@gmail.com", "Ivan", "Barbara", LocalDate.of(1990, 6, 2), "45 Shevchenka St", "0987654321");

        userDAO.USERS.add(existingUser);
        String email = "oleg@gmail.com";

        User result = userDAO.updateSomeUserFields(email, updatedUser);

        assertEquals("Ivan@gmail.com", result.getEmail());
        assertEquals("Ivan", result.getFirstName());
        assertEquals("Barbara", result.getLastName());
        assertEquals(LocalDate.of(1990, 6, 2), result.getBirthDate());
        assertEquals("45 Shevchenka St", result.getAddress());
        assertEquals("0987654321", result.getPhoneNumber());
    }

    @Test
    void testUpdateSomeUserFields_InvalidAge() {

        User existingUser = new User("oleg@gmail.com", "Oleg", "Messi", LocalDate.of(1995, 3, 1), "11 Shevchenka St", "0937622166");
        User updatedUser = new User("Ivan@gmail.com", "Ivan", "Barbara", LocalDate.of(2006, 6, 2), "45 Shevchenka St", "0987654321");
        userDAO.USERS.add(existingUser);
        String email = "oleg@gmail.com";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userDAO.updateSomeUserFields(email, updatedUser);
        });
        assertEquals("User is too young to be updated", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForUserNotFound")
    void testUpdateSomeUserFields_UserNotFound(String email) {

        User updatedUser = getUser();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userDAO.updateSomeUserFields(email, updatedUser);
        });
        assertEquals("User with the specified email address was not found " + email, exception.getMessage());
    }

    private static Stream<String> provideArgumentsForUserNotFound() {
        return Stream.of("nonexistent@example.com", "unknown@example.com", "missing@example.com");
    }

    @Test
    void testUpdateAllUserFields_Success() {

        User existingUser = new User("oleg@gmail.com", "Oleg", "Messi", LocalDate.of(1995, 3, 1), "11 Shevchenka St", "0937622166");
        User updatedUser = new User("Ivan@gmail.com", "Ivan", "Barbara", LocalDate.of(1995, 5, 11), "45 Shevchenka St", "0987654321");

        userDAO.USERS.add(existingUser);
        String email = "oleg@gmail.com";

        User result = userDAO.updateAllUserFields(email, updatedUser);

        assertEquals("Ivan@gmail.com", result.getEmail());
        assertEquals("Ivan", result.getFirstName());
        assertEquals("Barbara", result.getLastName());
        assertEquals(LocalDate.of(1995, 5, 11), result.getBirthDate());
        assertEquals("45 Shevchenka St", result.getAddress());
        assertEquals("0987654321", result.getPhoneNumber());
    }

    @Test
    void testUpdateAllUserFields_TooYoung() {

        User existingUser = new User("oleg@gmail.com", "Oleg", "Messi", LocalDate.of(1995, 3, 1), "11 Shevchenka St", "0937622166");
        User updatedUser = new User("Ivan@gmail.com", "Ivan", "Barbara", LocalDate.of(2005, 5, 11), "45 Shevchenka St", "0987654321");

        userDAO.USERS.add(existingUser);
        String email = "oleg@gmail.com";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userDAO.updateAllUserFields(email, updatedUser);
        });
        assertEquals("User must be at least 18 years old", exception.getMessage());
    }

    @Test
    void testUpdateAllUserFields_NoFieldsUpdated() {

        User existingUser = new User("oleg@gmail.com", "Oleg", "Messi", LocalDate.of(1995, 3, 1), "11 Shevchenka St", "0937622166");
        User updatedUser = new User("oleg@gmail.com", "Oleg", "Messi", LocalDate.of(1995, 3, 1), "11 Shevchenka St", "0937622166");
        userDAO.USERS.add(existingUser);
        String email = "oleg@gmail.com";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userDAO.updateAllUserFields(email, updatedUser);
        });
        assertEquals("All user field must be update", exception.getMessage());
    }

    @Test
    void testIsUserDeleted_Success() {

        User existingUser = getUser();
        userDAO.USERS.add(existingUser);

        boolean result = userDAO.isUserDeleted(existingUser);

        assertTrue(result);
        assertFalse(userDAO.USERS.contains(existingUser));
    }

    @Test
    void testIsUserDeleted_UserNotFound() {

        User nonExistingUser = getUser();

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            userDAO.isUserDeleted(nonExistingUser);
        });
        assertEquals("User not found: leo@gmail.com", exception.getMessage());
    }

    @Test
    void testSearchUserByBirthDate_Success() {

        Users users = getUsers();

        userDAO.USERS.add(users.user1());
        userDAO.USERS.add(users.user2());
         userDAO.USERS.add(users.user3());
        String fromDate = "1990-01-01";
        String toDate = "2000-01-01";

        List<User> result = userDAO.searchUserByBirthDate(fromDate, toDate);

        assertEquals(2, result.size());
        assertTrue(result.contains(users.user1()));
        assertTrue(result.contains(users.user2()));
        assertFalse(result.contains(users.user3()));
    }

    @Test
    void testSearchUserByBirthDate_InvalidDateRange() {

        String fromDate = "2020-01-01";
        String toDate = "2010-01-01";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userDAO.searchUserByBirthDate(fromDate, toDate);
        });
        assertEquals("From date must be less than To date", exception.getMessage());
    }

    @Test
    void testSearchUserByBirthDate_NullDates() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userDAO.searchUserByBirthDate(null, null);
        });
        assertEquals("From and To dates must be provided", exception.getMessage());
    }

    private static User getUser() {
        return new User("leo@gmail.com", "Oleg", "Messi", LocalDate.of(1995, 3, 1), "11 Shevchenka St", "0937622166");
    }
    private static Users getUsers() {
        User user1 = new User("oleg@gmail.com", "Oleg", "Sakki", LocalDate.of(1990, 1, 1), "11 Okean St", "0937622166");
        User user2 = new User("Ivan@gmail.com", "Ivan", "Barbara", LocalDate.of(1995, 5, 5), "45 Shevchenka St", "0987654321");
        User user3 = new User("leo@gmail.com", "Leo", "Messi", LocalDate.of(2000, 10, 10), "11 Viking St", "0326222133");
        return new Users(user1, user2, user3);
    }
    private record Users(User user1, User user2, User user3) {
    }
}