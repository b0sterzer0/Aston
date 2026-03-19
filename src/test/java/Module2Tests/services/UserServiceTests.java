package Module2Tests.services;

import Module2.dto.CreateUserDto;
import Module2.dto.UpdateUserDto;
import Module2.exceptions.UserDeletionException;
import Module2.exceptions.UserNotFoundException;
import Module2.mappers.UserMapper;
import Module2.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Module2.dto.UserDTO;
import Module2.models.User;
import Module2.services.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    @Test
    void createEntity_shouldReturnUserDTO() {
        CreateUserDto input = new CreateUserDto("John", "john@test.com", 20);

        User user = new User();
        user.setName("John");
        user.setEmail("john@test.com");
        user.setAge(20);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John");
        savedUser.setEmail("john@test.com");
        savedUser.setAge(20);
        savedUser.setCreatedAt(LocalDateTime.now());

        UserDTO expected = new UserDTO(
                1L,
                "John",
                "john@test.com",
                20,
                savedUser.getCreatedAt()
        );

        when(userMapper.toUser(input)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDTO(savedUser)).thenReturn(expected);

        UserDTO result = userService.createEntity(input);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getName());
        assertEquals("john@test.com", result.getEmail());
        assertEquals(20, result.getAge());
        assertNotNull(result.getCreatedAt());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void getEntity_shouldReturnUserDTO() {
        long id = 1L;

        User user = new User();
        user.setId(id);
        user.setName("John");
        user.setEmail("john@test.com");
        user.setAge(20);

        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        user.setCreatedAt(createdAt);

        UserDTO expected = new UserDTO(
                id,
                "John",
                "john@test.com",
                20,
                createdAt
        );

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(expected);

        UserDTO result = userService.getEntity(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("John", result.getName());
        assertEquals("john@test.com", result.getEmail());
        assertEquals(20, result.getAge());
        assertEquals(createdAt, result.getCreatedAt());

        verify(userRepository).findById(id);
        verify(userMapper).toDTO(user);
    }

    @Test
    void getEntity_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getEntity(1L));
    }

    @Test
    void updateEntity_shouldUpdateFields() {
        long id = 1L;

        User user = new User();
        user.setId(id);
        user.setName("Old Name");
        user.setEmail("old@test.com");
        user.setAge(10);

        UpdateUserDto dto = new UpdateUserDto();
        dto.setName("New Name");
        dto.setEmail(null);   // не должен измениться
        dto.setAge(25);

        UserDTO expected = new UserDTO(
                id,
                "New Name",
                "old@test.com",
                25,
                LocalDateTime.of(2024, 1, 1, 10, 0)
        );

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(expected);

        UserDTO result = userService.updateEntity(id, dto);

        assertEquals("New Name", user.getName());
        assertEquals("old@test.com", user.getEmail()); // не изменился
        assertEquals(25, user.getAge());

        assertNotNull(result);
        assertEquals(id, result.getId());

        verify(userRepository).findById(id);
        verify(userMapper).toDTO(user);
    }

    @Test
    void deleteEntity_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserDeletionException.class,
                () -> userService.deleteEntity(1L));
    }
}
