package Module2Tests.services;

import Module2.exceptions.UserDeletionException;
import Module2.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Module2.dao.UserDAO;
import Module2.dto.UserDTO;
import Module2.models.User;
import Module2.services.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    private UserDAO userDAO;
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        userService = new UserService(userDAO);
    }

    private User createUser(long id) {
        User user = new User();
        user.setId(id);
        user.setName("Ivan");
        user.setEmail("ivan@test.com");
        user.setAge(25);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    private UserDTO createUserDTO(long id) {
        return new UserDTO(id, "Ivan", "ivan@test.com", 25, LocalDateTime.now());
    }

    @Test
    void getUsers_shouldReturnListOfDTO() {
        List<User> users = List.of(createUser(1), createUser(2));
        when(userDAO.getAll()).thenReturn(users);

        List<UserDTO> result = userService.getUsers();

        assertEquals(2, result.size());
        verify(userDAO).getAll();
    }

    @Test
    void getUsers_shouldReturnEmptyListWhenNoUsers() {
        when(userDAO.getAll()).thenReturn(List.of());

        List<UserDTO> result = userService.getUsers();

        assertTrue(result.isEmpty());
        verify(userDAO).getAll();
    }

    @Test
    void getUser_shouldReturnDTO() {
        User user = createUser(1);
        when(userDAO.get(1)).thenReturn(user);

        UserDTO result = userService.getUser(1);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getAge(), result.getAge());
    }

    @Test
    void getUser_shouldThrowExceptionIfNotFound() {
        when(userDAO.get(1)).thenReturn(null);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUser(1)
        );

        assertTrue(exception.getMessage().contains("1"));
    }

    @Test
    void createUser_shouldCreateUser() {
        UserDTO dto = new UserDTO(0, "Ivan", "ivan@test.com", 25, null);
        User savedUser = createUser(1);

        when(userDAO.create(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.createUser(dto);

        assertEquals(savedUser.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getEmail(), result.getEmail());
        assertEquals(dto.getAge(), result.getAge());

        verify(userDAO).create(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertNotNull(capturedUser.getCreatedAt());
        assertEquals(dto.getName(), capturedUser.getName());
        assertEquals(dto.getEmail(), capturedUser.getEmail());
        assertEquals(dto.getAge(), capturedUser.getAge());
    }

    @Test
    void createUser_shouldHandleNullDTOFields() {
        UserDTO dto = new UserDTO(0, null, null, 0, null);
        User savedUser = createUser(1);
        savedUser.setName(null);
        savedUser.setEmail(null);
        savedUser.setAge(0);

        when(userDAO.create(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.createUser(dto);

        assertNotNull(result);
        verify(userDAO).create(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertNull(capturedUser.getName());
        assertNull(capturedUser.getEmail());
        assertEquals(0, capturedUser.getAge());
    }

    @Test
    void updateUser_shouldUpdateAllFields() {
        User existingUser = createUser(1);
        UserDTO updateDTO = new UserDTO(0, "NewName", "new@mail.com", 30, null);

        when(userDAO.get(1)).thenReturn(existingUser);
        when(userDAO.update(any(User.class))).thenReturn(existingUser);

        UserDTO result = userService.updateUser(1, updateDTO);

        assertEquals("NewName", result.getName());
        assertEquals("new@mail.com", result.getEmail());
        assertEquals(30, result.getAge());

        verify(userDAO).update(existingUser);
        assertEquals("NewName", existingUser.getName());
        assertEquals("new@mail.com", existingUser.getEmail());
        assertEquals(30, existingUser.getAge());
    }

    @Test
    void updateUser_shouldUpdateOnlyProvidedFields() {
        User existingUser = createUser(1);
        UserDTO updateDTO = new UserDTO(0, "NewName", null, -1, null);

        when(userDAO.get(1)).thenReturn(existingUser);
        when(userDAO.update(any(User.class))).thenReturn(existingUser);

        UserDTO result = userService.updateUser(1, updateDTO);

        assertEquals("NewName", result.getName());
        assertEquals("ivan@test.com", result.getEmail()); // Не изменился
        assertEquals(25, result.getAge()); // Не изменился

        verify(userDAO).update(existingUser);
    }

    @Test
    void updateUser_shouldThrowIfUserNotFound() {
        when(userDAO.get(1)).thenReturn(null);

        UserDTO dto = new UserDTO(0, "Name", "mail@test.com", 20, null);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(1, dto)
        );

        assertTrue(exception.getMessage().contains("1"));
        verify(userDAO, never()).update(any());
    }

    @Test
    void deleteUser_shouldDeleteUser() {
        User user = createUser(1);
        when(userDAO.delete(1)).thenReturn(user);

        UserDTO result = userService.deleteUser(1);

        assertEquals(1, result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());

        verify(userDAO).delete(1);
    }

    @Test
    void deleteUser_shouldThrowIfUserNotFound() {
        when(userDAO.delete(1)).thenReturn(null);

        UserDeletionException exception = assertThrows(
                UserDeletionException.class,
                () -> userService.deleteUser(1)
        );

        assertTrue(exception.getMessage().contains("1"));
    }

    @Test
    void deleteUser_shouldPropagateDaoException() {
        when(userDAO.delete(1)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> userService.deleteUser(1));
    }
}
