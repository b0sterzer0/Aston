package Module2Tests.menu;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;

import Module2.dto.UserDTO;
import Module2.services.UserService;
import Module2.app.MainMenu;

public class MainMenuTests {

    @Test
    void start_shouldCallGetUsers() {

        String input = "1\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        UserService userService = mock(UserService.class);

        when(userService.getUsers()).thenReturn(List.of());

        MainMenu menu = new MainMenu(userService, scanner);

        menu.start();

        verify(userService).getUsers();
    }

    @Test
    void start_shouldCallGetUser() {

        String input = "2\n1\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        UserService userService = mock(UserService.class);

        UserDTO dto = new UserDTO(
                1,
                "John",
                "john@mail.com",
                25,
                LocalDateTime.now()
        );

        when(userService.getUser(1)).thenReturn(dto);

        MainMenu menu = new MainMenu(userService, scanner);

        menu.start();

        verify(userService).getUser(1);
    }

    @Test
    void start_shouldCallCreateUser() {

        String input = """
                3
                John
                john@mail.com
                25
                0
                """;

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        UserService userService = mock(UserService.class);

        when(userService.createUser(any())).thenReturn(
                new UserDTO(1,"John","john@mail.com",25,LocalDateTime.now())
        );

        MainMenu menu = new MainMenu(userService, scanner);

        menu.start();

        verify(userService).createUser(any());
    }

    @Test
    void start_shouldCallDeleteUser() {

        String input = """
                5
                1
                0
                """;

        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        UserService userService = mock(UserService.class);

        when(userService.deleteUser(1)).thenReturn(
                new UserDTO(1,"John","mail",25,LocalDateTime.now())
        );

        MainMenu menu = new MainMenu(userService, scanner);

        menu.start();

        verify(userService).deleteUser(1);
    }

}
