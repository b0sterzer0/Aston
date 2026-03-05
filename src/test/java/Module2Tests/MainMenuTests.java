package Module2Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Module2.dto.UserDTO;
import Module2.services.UserService;
import Module2.app.MainMenu;

public class MainMenuTests {

    private UserService userService;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    private UserDTO createUser(long id) {
        return new UserDTO(
                id,
                "Ivan",
                "ivan@test.com",
                25,
                LocalDateTime.now()
        );
    }

    @Test
    void getUsers_shouldPrintUsers() {

        List<UserDTO> users = List.of(createUser(1), createUser(2));
        when(userService.getUsers()).thenReturn(users);

        Scanner scanner = new Scanner(new StringReader(""));
        MainMenu menu = new MainMenu(userService, scanner);

        menu.getUsers();

        verify(userService).getUsers();
        assertTrue(output.toString().contains("Ivan"));
    }

    @Test
    void getUser_shouldPrintUser() {

        when(userService.getUser(1)).thenReturn(createUser(1));

        Scanner scanner = new Scanner(new StringReader("1\n"));
        MainMenu menu = new MainMenu(userService, scanner);

        menu.getUser();

        verify(userService).getUser(1);
        assertTrue(output.toString().contains("Ivan"));
    }

    @Test
    void deleteUser_shouldCallService() {

        when(userService.deleteUser(1)).thenReturn(createUser(1));

        Scanner scanner = new Scanner(new StringReader("1\n"));
        MainMenu menu = new MainMenu(userService, scanner);

        menu.deleteUser();

        verify(userService).deleteUser(1);
    }

    @Test
    void createUser_shouldCreateUser() {

        when(userService.createUser(any())).thenReturn(createUser(1));

        String input =
                "Ivan\n" +
                        "ivan@test.com\n" +
                        "25\n";

        Scanner scanner = new Scanner(new StringReader(input));
        MainMenu menu = new MainMenu(userService, scanner);

        menu.createUser();

        verify(userService).createUser(any());
        assertTrue(output.toString().contains("Ivan"));
    }

    @Test
    void updateUser_shouldUpdateUser() {

        UserDTO user = createUser(1);

        when(userService.getUser(1)).thenReturn(user);
        when(userService.updateUser(eq(1L), any())).thenReturn(user);

        String input =
                "1\n" +   // id
                        "4\n";    // send update

        Scanner scanner = new Scanner(new StringReader(input));
        MainMenu menu = new MainMenu(userService, scanner);

        menu.updateUser();

        verify(userService).updateUser(eq(1L), any());
    }
}
