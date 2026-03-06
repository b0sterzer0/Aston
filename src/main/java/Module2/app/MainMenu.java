package Module2.app;

import Module2.dao.UserDAO;
import Module2.dto.UserDTO;
import Module2.services.UserService;

import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private final UserService userService;
    private final Scanner scanner;

    public MainMenu(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            printMenu();
            switch (scanner.nextLine()) {
                case "0" : return;
                case "1":
                    getUsers();
                    break;
                case "2":
                    getUser();
                    break;
                case "3":
                    createUser();
                    break;
                case "4":
                    updateUser();
                    break;
                case "5":
                    deleteUser();
                    break;
                default:
                    System.out.println("Такого действия нет!");
            }
        }
    }

    private void getUsers() {
        List<UserDTO> userList = userService.getUsers();
        userList.forEach(System.out::println);
    }

    private void getUser() {
        System.out.println("Введите id пользователя:");
        long id = Long.parseLong(scanner.nextLine());
        UserDTO user = userService.getUser(id);
        System.out.println(user);
    }

    private void deleteUser() {
        System.out.println("Введите id пользователя:");
        long id = Long.parseLong(scanner.nextLine());
        System.out.println(userService.deleteUser(id));
    }

    private void createUser() {
        System.out.println("Введите имя пользователя:");
        String name = scanner.nextLine();
        System.out.println("Введите электронную почту:");
        String email = scanner.nextLine();
        System.out.println("Введите возраст пользователя:");
        int age = Integer.parseInt(scanner.nextLine());
        UserDTO userDTO = new UserDTO(0, name, email, age, null);
        UserDTO createdUser = userService.createUser(userDTO);
        System.out.println(createdUser);
    }

    private void updateUser() {
        System.out.println("Введите id пользователя:");
        long id = Long.parseLong(scanner.nextLine());

        UserDTO user = userService.getUser(id);

        String name = user.getName();
        String email = user.getEmail();
        int age = user.getAge();

        boolean running = true;

        while (running) {
            printUpdateMenu();

            switch (scanner.nextLine()) {
                case "1":
                    System.out.println("Введите новое имя:");
                    name = scanner.nextLine();
                    break;
                case "2":
                    System.out.println("Введите новый email:");
                    email = scanner.nextLine();
                    break;
                case "3":
                    System.out.println("Введите новый возраст:");
                    age = Integer.parseInt(scanner.nextLine());
                    break;
                case "4":
                    UserDTO userForUpdate = new UserDTO(id, name, email, age, user.getCreatedAt());
                    System.out.println(userService.updateUser(id, userForUpdate));
                    running = false;
                    break;
                case "0":
                    System.out.println("Обновление отменено");
                    running = false;
                    break;
                default:
                    System.out.println("Неверная команда");
            }
        }
    }

    private static void printUpdateMenu() {
        System.out.println();
        System.out.println("Выберите одно из возможный действий:");
        System.out.println("0: Отменить изменения и вернутся");
        System.out.println("1: Изменить имя пользователя");
        System.out.println("2: Изменить электронную почту");
        System.out.println("3: Изменить возраст");
        System.out.println("4: Отправить изменения");
        System.out.println();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("Выберите одно из возможный действий:");
        System.out.println("0: Выйти");
        System.out.println("1: Получить всех пользователей");
        System.out.println("2: Получить пользователя по id");
        System.out.println("3: Создать нового пользователя");
        System.out.println("4: Обновить пользователя");
        System.out.println("5: Удалить пользователя");
        System.out.println();
    }
}
