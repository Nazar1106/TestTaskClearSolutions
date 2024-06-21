package com.example.java_practical_test_assignment.repository;

import com.example.java_practical_test_assignment.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserDAOTest {

    private InMemoryUserDAO userDAO;

    @BeforeEach
    public void setUp() {
        userDAO = new InMemoryUserDAO();
    }

    @Test
    public void testPutUser() {

        UUID id = UUID.randomUUID();
        User expectedUser = new User("Nazar", "Petro", "test@example.com", LocalDate.of(2000, 6, 1), "shevchenka", "12345678");

        User result = userDAO.putUser(id, expectedUser);
        User retrievedUser = userDAO.getUserToId(id);

        assertNotNull(expectedUser);
        assertNotNull(result);
        assertNotNull(retrievedUser);
        assertEquals(expectedUser, retrievedUser);
    }


    @Test
    public void testGetUserToId() {

        UUID id = UUID.randomUUID();
        User user = User.builder().birthDate(LocalDate.of(2000, 6, 1)).email("test@example.com").firstName("Nazar").lastName("Petro").address("shevchenka").phoneNumber("12345678").build();
        userDAO.putUser(id, user);

        User result = userDAO.getUserToId(id);

        assertSame(user, result);
    }

    @Test
    public void testReplaceUser() {

        UUID id = UUID.randomUUID();

        User user = User.builder().birthDate(LocalDate.of(2000, 6, 1)).email("test@example.com").firstName("Nazar").lastName("Petro").address("shevchenka").phoneNumber("12345678").build();
        userDAO.putUser(id, user);

        User newUser = User.builder().birthDate(LocalDate.of(2000, 6, 1)).email("TEST@example.com").firstName("IVAN").lastName("KALYG").address("dniprovska").phoneNumber("1111111111").build();

        User result = userDAO.replaceUser(id, newUser);

        assertSame(user, result);
        assertEquals("IVAN", userDAO.getUserToId(id).getFirstName());
        assertEquals("KALYG", userDAO.getUserToId(id).getLastName());
        assertEquals("TEST@example.com", userDAO.getUserToId(id).getEmail());
        assertEquals("dniprovska", userDAO.getUserToId(id).getAddress());
        assertEquals("1111111111", userDAO.getUserToId(id).getPhoneNumber());
    }

    @Test
    public void testContainsKey() {
        UUID id = UUID.randomUUID();
        User user = User.builder().birthDate(LocalDate.of(2000, 6, 1)).email("test@example.com").firstName("Nazar").lastName("Petro").address("shevchenka").phoneNumber("12345678").build();
        userDAO.putUser(id, user);

        assertTrue(userDAO.containsKey(id));
    }

    @Test
    public void testDelete() {
        UUID id = UUID.randomUUID();
        User user = User.builder().birthDate(LocalDate.of(2000, 6, 1)).email("test@example.com").firstName("Nazar").lastName("Petro").address("shevchenka").phoneNumber("12345678").build();
        userDAO.putUser(id, user);
        assertTrue(userDAO.containsKey(id));

        userDAO.delete(id);

        assertFalse(userDAO.containsKey(id));
        assertNull(userDAO.getUserToId(id));
    }

    @Test
    public void testValues() {
        UUID id1 = UUID.randomUUID();
        User user1 = User.builder().birthDate(LocalDate.of(2000, 6, 1)).email("test@example.com").firstName("Nazar").lastName("Petro").address("shevchenka").phoneNumber("12345678").build();

        UUID id2 = UUID.randomUUID();
        User user2 = User.builder().birthDate(LocalDate.of(2000, 6, 1)).email("TEST@example.com").firstName("IVAN").lastName("KALYG").address("dniprovska").phoneNumber("1111111111").build();

        userDAO.putUser(id1, user1);
        userDAO.putUser(id2, user2);

        Collection<User> values = userDAO.values();

        assertEquals(2, values.size());
        assertTrue(values.contains(user1));
        assertTrue(values.contains(user2));
    }
}