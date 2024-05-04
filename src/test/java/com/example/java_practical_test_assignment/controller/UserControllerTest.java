package com.example.java_practical_test_assignment.controller;

import com.example.java_practical_test_assignment.model.User;
import com.example.java_practical_test_assignment.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    void testUpdateAllUserFields_Success() throws Exception {

        String email = "nazar@gmail.com";

        User user = createUser();

        when(userService.updateAllUserFields(eq(email), any(User.class))).thenReturn(user);

        String requestBody = new String(objectMapper.writeValueAsBytes(user), StandardCharsets.UTF_8);
        mockMvc.perform(put("/api/users/update/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateAllUserFields(email, user);
    }

    @Test
    public void testUpdateUserInvalidRequest() throws Exception {

        String userEmail = "test@example.com";
        String invalidRequestBody = "{}";
        String endpoint = String.format("/api/users/update/%s", userEmail);

        mockMvc.perform(put(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest());
    }


    @Test
    void createUsers_Success() throws Exception {

        User user = createUser();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());

        verify(userService, times(1)).createUser(user);
    }


    @Test
    void createUsers_InvalidRequest() throws Exception {

        User user = createUser();
        user.setEmail(null);
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateSomeUserFields_Success() throws Exception {

        User updatedUser = createUser();

        when(userService.updateSomeUserFields("oleg@gmail.com", updatedUser)).thenReturn(updatedUser);

        mockMvc.perform(patch("/api/users/oleg@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"oleg@gmail.com\",\"firstName\":\"Oleg\",\"lastName\":\"Sakki\",\"birthDate\":\"1990-01-01\",\"address\":\"11 Okean St\",\"phoneNumber\":\"0937622166\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("oleg@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Oleg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Sakki"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("11 Okean St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("0937622166"));
    }

    @Test
    void updateSomeFields_InvalidRequest() throws Exception {

        User user = createUser();
        String email = "test@example.com";
        user.setEmail(null);
        String updatedField = "updatedName";

        user.setFirstName(updatedField);
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(patch("/api/users/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUser_InvalidEmail() throws Exception {
        mockMvc.perform(patch("/api/users/invalid_email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalid_email\",\"firstName\":\"Oleg\",\"lastName\":\"Chup\",\"birthDate\":\"1990-01-01\",\"address\":\"11 Shevchenka St\",\"phoneNumber\":\"3627283822\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testSearchUserByBirthDate() throws Exception {

        LocalDate fromDate = LocalDate.of(1990, 1, 1);
        LocalDate toDate = LocalDate.of(1990, 12, 31);
        List<User> users = Arrays.asList(
                new User("Oleg@example.com", "Oleg", "Puk", fromDate, "11 Shevchenka St", "7363522811"),
                new User("Nazar@example.com", "Nazar", "Tak", toDate, "12 Shevchenka St", "8372616388")
        );

        when(userService.searchUserByBirthDate("1990-01-01", "1990-12-31")).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/search")
                        .param("fromDate", "1990-01-01")
                        .param("toDate", "1990-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("Oleg@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Oleg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Puk"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("Nazar@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Nazar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value("Tak"));
    }

    @Test
    void searchUserByBirthDate_MissingParams() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users/search"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testDeleteUser() throws Exception {

        User userToDelete = createUser();

        when(userService.deleteUser(userToDelete)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"oleg@gmail.com\",\"firstName\":\"Oleg\",\"lastName\":\"Sakki\",\"birthDate\":\"1990-01-01\",\"address\":\"11 Okean St\",\"phoneNumber\":\"0937622166\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void deleteUser_InvalidRequest_ReturnsBadRequest() throws Exception {

        String emptyContent = "";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    private User createUser() {
        return new User("oleg@gmail.com", "Oleg", "Sakki", LocalDate.of(1990, 1, 1), "11 Okean St", "0937622166");
    }
}



