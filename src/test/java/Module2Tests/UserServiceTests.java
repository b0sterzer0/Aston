package Module2Tests;

import Module2.exceptions.UserDeletionException;
import Module2.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Module2.dao.UserDAO;
import Module2.dto.UserDTO;
import Module2.models.User;
import Module2.services.UserService;

class UserServiceTests {

    private UserDAO userDAO;
    private UserService userService;

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

    @Test
    void getUsers_shouldReturnListOfDTO() {

        List<User> users = List.of(createUser(1), createUser(2));
        when(userDAO.getAll()).thenReturn(users);

        List<UserDTO> result = userService.getUsers();

        assertEquals(2, result.size());
        verify(userDAO).getAll();
    }

    @Test
    void getUser_shouldReturnDTO() {

        User user = createUser(1);
        when(userDAO.get(1)).thenReturn(user);

        UserDTO result = userService.getUser(1);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getUser_shouldThrowExceptionIfNotFound() {

        when(userDAO.get(1)).thenReturn(null);

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUser(1)
        );
    }

    @Test
    void createUser_shouldCreateUser() {

        UserDTO dto = new UserDTO(0, "Ivan", "ivan@test.com", 25, null);

        User savedUser = createUser(1);

        when(userDAO.create(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.createUser(dto);

        assertEquals(savedUser.getId(), result.getId());
        verify(userDAO).create(any(User.class));
    }

    @Test
    void updateUser_shouldUpdateFields() {

        User existingUser = createUser(1);

        UserDTO updateDTO = new UserDTO(0, "NewName", "new@mail.com", 30, null);

        when(userDAO.get(1)).thenReturn(existingUser);
        when(userDAO.update(any(User.class))).thenReturn(existingUser);

        UserDTO result = userService.updateUser(1, updateDTO);

        assertEquals("NewName", result.getName());
        assertEquals("new@mail.com", result.getEmail());

        verify(userDAO).update(existingUser);
    }

    @Test
    void updateUser_shouldThrowIfUserNotFound() {

        when(userDAO.get(1)).thenReturn(null);

        UserDTO dto = new UserDTO(0, "Name", "mail@test.com", 20, null);

        assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(1, dto)
        );
    }

    @Test
    void deleteUser_shouldDeleteUser() {

        User user = createUser(1);

        when(userDAO.delete(1)).thenReturn(user);

        UserDTO result = userService.deleteUser(1);

        assertEquals(1, result.getId());
        verify(userDAO).delete(1);
    }

    @Test
    void deleteUser_shouldThrowIfUserNotFound() {

        when(userDAO.delete(1)).thenReturn(null);

        assertThrows(
                UserDeletionException.class,
                () -> userService.deleteUser(1)
        );
    }
}
