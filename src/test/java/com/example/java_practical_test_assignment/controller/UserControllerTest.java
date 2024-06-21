package com.example.java_practical_test_assignment.controller;

import com.example.java_practical_test_assignment.dto.CreateUserDTO;
import com.example.java_practical_test_assignment.dto.UserAllUpdateDTO;
import com.example.java_practical_test_assignment.dto.UserPartialUpdateDTO;
import com.example.java_practical_test_assignment.mapper.UserMapper;
import com.example.java_practical_test_assignment.model.User;
import com.example.java_practical_test_assignment.model.UserKey;
import com.example.java_practical_test_assignment.repository.InMemoryUserDAO;
import com.example.java_practical_test_assignment.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InMemoryUserDAO userRepository;

    @Autowired
    private UserService userService;

    private Map<UUID, User> userStorage;

    @BeforeEach
    public void setUp() {
        userStorage = new HashMap<>();
    }


    @Test
    public void createUser() throws Exception {

        UUID uuid = UUID.randomUUID();

        CreateUserDTO user = new CreateUserDTO();
        user.setFirstName("Nazar");
        user.setLastName("Petro");
        user.setEmail("test@example.com");
        user.setBirthDate(LocalDate.of(2000, 6, 1));
        user.setAddress("shevchenka");
        user.setPhoneNumber("11111");

        objectMapper.registerModule(new JavaTimeModule());
        String userJson = objectMapper.writeValueAsString(user);

        UserKey key = new UserKey();
        key.setId(uuid);

        when(userRepository.putUser(uuid, userMapper.createdDtoToUser(user))).thenReturn(userMapper.createdDtoToUser(user));
        CreateUserDTO result = userService.createUser(user, key);


        Assertions.assertNotNull(result);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Nazar"))
                .andExpect(jsonPath("$.lastName").value("Petro"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.birthDate").value("2000-06-01"))
                .andExpect(jsonPath("$.address").value("shevchenka"))
                .andExpect(jsonPath("$.phoneNumber").value(11111))
                .andExpect(MockMvcResultMatchers.header().exists("Location"));
    }

    @Test
    public void returnStatusCode201() throws Exception {
        CreateUserDTO user = new CreateUserDTO();
        user.setFirstName("Nazar");
        user.setLastName("Petro");
        user.setEmail("test@example.com");
        user.setBirthDate(LocalDate.of(2000, 6, 1));
        user.setAddress("shevchenka");
        user.setPhoneNumber("11111");

        objectMapper.registerModule(new JavaTimeModule());
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void updateAllFields() throws Exception {

        UUID id = UUID.randomUUID();

        UserAllUpdateDTO userDto = new UserAllUpdateDTO();
        userDto.setFirstName("Nazar");
        userDto.setLastName("Petro");
        userDto.setEmail("test@example.com");
        userDto.setBirthDate(LocalDate.of(2000, 6, 1));
        userDto.setAddress("shevchenka");
        userDto.setPhoneNumber("11111");

        User user = new User();
        user.setFirstName("Ivanka");
        user.setLastName("Bogdanka");
        user.setEmail("kinash@gmail.com");
        user.setBirthDate(LocalDate.of(20003, 6, 1));
        user.setAddress("Lushpunskogo");
        user.setPhoneNumber("222222");

        userStorage.put(id, user);

        when(userRepository.containsKey(id)).thenReturn(true);
        when(userRepository.getUserToId(id)).thenReturn(user);
        when(userService.updateUser(id, user)).thenAnswer(invocation -> {
            userStorage.put(id, user);
            return user;
        });

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));


        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(userDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(userDto.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(userDto.getEmail())))
                .andExpect(jsonPath("$.birthDate", CoreMatchers.is(userDto.getBirthDate().toString())))
                .andExpect(jsonPath("$.address", CoreMatchers.is(userDto.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", CoreMatchers.is(userDto.getPhoneNumber())));
    }


    @Test
    public void updatePartialFields() throws Exception {

        UUID id = UUID.randomUUID();

        UserPartialUpdateDTO userDto = new UserPartialUpdateDTO();
        userDto.setFirstName("Nazar");
        userDto.setLastName("Petro");
        userDto.setEmail("test@example.com");
        userDto.setBirthDate(LocalDate.of(2000, 6, 1));
        userDto.setAddress("shevchenka");
        userDto.setPhoneNumber("11111");

        User user = new User();
        user.setFirstName("Ivanka");
        user.setLastName("Bogdanka");
        user.setEmail("kinash@gmail.com");
        user.setBirthDate(LocalDate.of(20003, 6, 1));
        user.setAddress("Lushpunskogo");
        user.setPhoneNumber("222222");

        userStorage.put(id, user);

        when(userRepository.containsKey(id)).thenReturn(true);
        when(userRepository.getUserToId(id)).thenReturn(user);
        when(userService.updateUser(id, user)).thenAnswer(invocation -> {
            userStorage.put(id, user);
            return user;
        });

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(userDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(userDto.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(userDto.getEmail())))
                .andExpect(jsonPath("$.birthDate", CoreMatchers.is(userDto.getBirthDate().toString())))
                .andExpect(jsonPath("$.address", CoreMatchers.is(userDto.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", CoreMatchers.is(userDto.getPhoneNumber())));
    }


    @Test
    public void searchByBirthdayDateTest() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        User user1 = new User();
        user1.setFirstName("Nazar");
        user1.setLastName("Nikki");
        user1.setEmail("naz@gmail.com");
        user1.setBirthDate(LocalDate.of(2000, 1, 1));
        user1.setAddress("shevchencka");
        user1.setPhoneNumber("1111111");

        User user2 = new User();
        user2.setFirstName("Lina");
        user2.setLastName("Kochernik");
        user2.setEmail("lina@gmail.com");
        user2.setBirthDate(LocalDate.of(2005, 1, 1));
        user2.setAddress("santabarbara");
        user2.setPhoneNumber("222222");

        userStorage.put(id1, user1);
        userStorage.put(id2, user2);

        List<User> users = Arrays.asList(user1, user2);

        when(userService.searchUserByBirthDate("2000-01-01", "2005-12-31")).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .param("fromDate", "2000-01-01")
                        .param("toDate", "2005-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].firstName", CoreMatchers.is("Nazar")))
                .andExpect(jsonPath("$.data[0].lastName", CoreMatchers.is("Nikki")))
                .andExpect(jsonPath("$.data[0].email", CoreMatchers.is("naz@gmail.com")))
                .andExpect(jsonPath("$.data[1].firstName", CoreMatchers.is("Lina")))
                .andExpect(jsonPath("$.data[1].lastName", CoreMatchers.is("Kochernik")))
                .andExpect(jsonPath("$.data[1].email", CoreMatchers.is("lina@gmail.com")));
    }


    @Test
    public void deleteTest() throws Exception {

        UUID id = UUID.randomUUID();

        User user = new User();
        user.setFirstName("Ivanka");
        user.setLastName("Bogdanka");
        user.setEmail("kinash@gmail.com");
        user.setBirthDate(LocalDate.of(20003, 6, 1));
        user.setAddress("Lushpunskogo");
        user.setPhoneNumber("222222");

        userStorage.put(id, user);

        when(userRepository.containsKey(id)).thenReturn(true);
        when(userRepository.getUserToId(id)).thenReturn(user);

        doNothing().when(userRepository).delete(id);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());

    }
}







