package Module2Tests.integration;

import Module2.dao.UserDAO;
import Module2.dto.UserDTO;
import Module2.exceptions.UserNotFoundException;
import Module2.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceIntegrationTests extends BaseIntegrationTest {
    private UserService userService;
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {

        cleanDatabase(); // из BaseIntegrationTest

        userDAO = new UserDAO();
        userService = new UserService(userDAO);
    }

    @Test
    void createAndGetUser_ShouldWork() {

        UserDTO createDTO = new UserDTO(0, "Integration User", "integration@test.com", 28, null);

        UserDTO created = userService.createUser(createDTO);

        assertTrue(created.getId() > 0);
        assertEquals(createDTO.getName(), created.getName());
        assertEquals(createDTO.getEmail(), created.getEmail());
        assertEquals(createDTO.getAge(), created.getAge());
        assertNotNull(created.getCreatedAt());

        UserDTO fetched = userService.getUser(created.getId());

        assertEquals(created.getId(), fetched.getId());
    }

    @Test
    void updateUser_ShouldModifyExistingUser() {

        UserDTO created = userService.createUser(
                new UserDTO(0, "Update User", "update@test.com", 30, null)
        );

        UserDTO updateDTO =
                new UserDTO(0, "Updated Name", "updated@test.com", 35, null);

        UserDTO updated = userService.updateUser(created.getId(), updateDTO);

        assertEquals(created.getId(), updated.getId());
        assertEquals("Updated Name", updated.getName());
        assertEquals("updated@test.com", updated.getEmail());
        assertEquals(35, updated.getAge());
    }

    @Test
    void deleteUser_ShouldRemoveUser() {

        UserDTO created = userService.createUser(
                new UserDTO(0, "Delete User", "delete@test.com", 22, null)
        );

        UserDTO deleted = userService.deleteUser(created.getId());

        assertEquals(created.getId(), deleted.getId());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUser(created.getId())
        );
    }

    @Test
    void getUsers_ShouldReturnAllUsers() {

        userService.createUser(
                new UserDTO(0, "User1", "user1@test.com", 25, null)
        );

        userService.createUser(
                new UserDTO(0, "User2", "user2@test.com", 26, null)
        );

        List<UserDTO> users = userService.getUsers();

        assertEquals(2, users.size());
    }

    @Test
    void getUser_WithNonExistentId_ShouldThrowException() {

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUser(99999L)
        );
    }
}
