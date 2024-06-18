package com.example.java_practical_test_assignment.service.UserServiceImpl;

import com.example.java_practical_test_assignment.dto.CreateUserDTO;
import com.example.java_practical_test_assignment.dto.RequestUserAllUpdateDTO;
import com.example.java_practical_test_assignment.dto.RequestUserPartialUpdateDTO;
import com.example.java_practical_test_assignment.exception.ApiRequestException;
import com.example.java_practical_test_assignment.exception.BusinessException;
import com.example.java_practical_test_assignment.mapper.UserMapper;
import com.example.java_practical_test_assignment.model.User;
import com.example.java_practical_test_assignment.model.UserKey;
import com.example.java_practical_test_assignment.repository.InMemoryUserDAO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private InMemoryUserDAO userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;


    private User user;


    @Test
    void testCreateUser_Success() throws ApiRequestException {

        UserKey userKey = new UserKey();
        userKey.setId(UUID.randomUUID());

        User user = User.builder()
                .birthDate(LocalDate.of(2000, 6, 1))
                .email("test@example.com")
                .firstName("Nazar")
                .lastName("Petro")
                .address("shevchenka")
                .phoneNumber("12345678")
                .build();

        CreateUserDTO userDTO = CreateUserDTO.builder().
                birthDate(LocalDate.of(2000, 6, 1))
                .email("test@example.com")
                .firstName("Nazar")
                .lastName("Petro")
                .address("shevchenka")
                .phoneNumber("12345678")
                .build();

        when(userRepository.putUser(any(UUID.class), any(User.class))).thenReturn(user);
        CreateUserDTO createUserDTO = userService.createUser(userDTO, userKey);

        org.junit.jupiter.api.Assertions.assertNotNull(createUserDTO); //todo: fix this import
    }

    @Test
    void updateUser_allFieldsUpdated() throws ApiRequestException, BusinessException {

        UUID uuid = UUID.randomUUID();

        user = User.builder()
                .birthDate(LocalDate.of(2000, 6, 1))
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .address("shevchenka")
                .phoneNumber("12345678")
                .build();

        RequestUserAllUpdateDTO userDTO = RequestUserAllUpdateDTO.builder()
                .birthDate(LocalDate.of(2000, 6, 1))
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .address("shevchenka")
                .phoneNumber("12345678")
                .build();

        User existingUser = User.builder()
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("existing@example.com")
                .firstName("Existing")
                .lastName("User")
                .address("existing address")
                .phoneNumber("98765432")
                .build();

        when(userRepository.containsKey(uuid)).thenReturn(true);
        when(userRepository.getUserToId(uuid)).thenReturn(existingUser);
        when(userRepository.replaceUser(any(UUID.class), any(User.class))).thenReturn(user);

        RequestUserAllUpdateDTO savedUserDTO = userService.updateWholeUser(uuid, userDTO);

        Assertions.assertThat(savedUserDTO).isNotNull();
        Assertions.assertThat(savedUserDTO.getEmail()).isNotEqualTo(existingUser.getEmail());
        Assertions.assertThat(savedUserDTO.getFirstName()).isNotEqualTo(existingUser.getFirstName());
        Assertions.assertThat(savedUserDTO.getLastName()).isNotEqualTo(existingUser.getLastName());
        Assertions.assertThat(savedUserDTO.getAddress()).isNotEqualTo(existingUser.getAddress());
        Assertions.assertThat(savedUserDTO.getPhoneNumber()).isNotEqualTo(existingUser.getPhoneNumber());
        Assertions.assertThat(savedUserDTO.getBirthDate()).isNotEqualTo(existingUser.getBirthDate());

        verify(userRepository, times(1)).replaceUser(uuid, user);
    }

    @Test
    void updateUser_returnUserDto() throws ApiRequestException, BusinessException {
        UUID uuid = UUID.randomUUID();

        User user = User.builder()
                .birthDate(LocalDate.of(2000, 6, 1))
                .email("test@example.com")
                .firstName("Nazar")
                .lastName("Petro")
                .address("shevchenka")
                .phoneNumber("12345678")
                .build();

        RequestUserAllUpdateDTO userDTO = RequestUserAllUpdateDTO.builder()
                .birthDate(LocalDate.of(2000, 6, 1))
                .email("test@example.com")
                .firstName("Nazar")
                .lastName("Petro")
                .address("shevchenka")
                .phoneNumber("12345678")
                .build();

        User existingUser = User.builder()
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("existing@example.com")
                .firstName("Existing")
                .lastName("User")
                .address("existing address")
                .phoneNumber("98765432")
                .build();

        when(userRepository.containsKey(uuid)).thenReturn(true);
        when(userRepository.getUserToId(uuid)).thenReturn(existingUser);
        when(userRepository.replaceUser(any(UUID.class), any(User.class))).thenReturn(user);

        RequestUserAllUpdateDTO savedUserDTO = userService.updateWholeUser(uuid, userDTO);

        Assertions.assertThat(savedUserDTO).isNotNull();
        Assertions.assertThat(savedUserDTO.getEmail()).isEqualTo("test@example.com");
        Assertions.assertThat(savedUserDTO.getFirstName()).isEqualTo("Nazar");
        Assertions.assertThat(savedUserDTO.getLastName()).isEqualTo("Petro");

        verify(userRepository, times(1)).replaceUser(uuid, user);
    }

    @Test
    void updatePartialUser_success() throws BusinessException, ApiRequestException {

        UUID uuid = UUID.randomUUID();

        User existingUser = User.builder()
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("existing@example.com")
                .firstName("Existing")
                .lastName("User")
                .address("existing address")
                .phoneNumber("98765432")
                .build();

        RequestUserPartialUpdateDTO partialUpdateDTO = RequestUserPartialUpdateDTO.builder()
                .email("new@example.com")
                .build();

        when(userRepository.getUserToId(uuid)).thenReturn(existingUser);
        when(userMapper.userToPartialUser(existingUser)).thenReturn(partialUpdateDTO);

        RequestUserPartialUpdateDTO result = userService.updatePartialUser(uuid, partialUpdateDTO);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getEmail()).isEqualTo(partialUpdateDTO.getEmail());

        verify(userRepository, times(1)).getUserToId(uuid);
        verify(userMapper, times(1)).userToPartialUser(existingUser);
        verify(userRepository, times(1)).replaceUser(uuid, existingUser);
    }

    @Test
    void updatePartialUser_userNotFound() {
        UUID uuid = UUID.randomUUID();
        RequestUserPartialUpdateDTO partialUpdateDTO = RequestUserPartialUpdateDTO.builder()
                .email("new@example.com")
                .build();

        when(userRepository.getUserToId(uuid)).thenReturn(null);

        Assertions.assertThatThrownBy(() -> userService.updatePartialUser(uuid, partialUpdateDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("User with the specified id was not found");

        verify(userRepository, times(1)).getUserToId(uuid);
        verify(userMapper, never()).userToPartialUser(any());
        verify(userRepository, never()).replaceUser(any(), any());
    }

    @Test
    public void deleteUser_existingUser() throws ApiRequestException {
        // Arrange
        Map<UUID, User> userMap = new HashMap<>();
        UUID userId = UUID.randomUUID();

        user = User.builder()
                .birthDate(LocalDate.of(2000, 6, 1))
                .email("test@example.com")
                .firstName("Nazar")
                .lastName("Petro")
                .address("shevchenka")
                .phoneNumber("12345678")
                .build();

        userMap.put(userId, user);

        when(userRepository.containsKey(userId)).thenReturn(true);
        doAnswer(invocation -> {
            UUID id = invocation.getArgument(0);
            userMap.remove(id);
            return null;
        }).when(userRepository).delete(any(UUID.class));

        assertDoesNotThrow(() -> userService.deleteUser(userId));

        verify(userRepository, times(1)).containsKey(userId);
        verify(userRepository, times(1)).delete(userId);
        assertFalse(userMap.containsKey(userId));
    }

    @Test
    public void deleteUser_nonExistingUser() {

        UUID userId = UUID.randomUUID();

        when(userRepository.containsKey(userId)).thenReturn(false);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> userService.deleteUser(userId));
        assertEquals("User not found with id: " + userId, exception.getMessage());

        verify(userRepository, times(1)).containsKey(userId);
        verify(userRepository, never()).delete(any(UUID.class));
    }
}

