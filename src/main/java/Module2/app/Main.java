package Module2.app;

import Module2.dao.UserDAO;
import Module2.dto.UserDTO;
import Module2.services.UserService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final UserDAO USER_DAO = new UserDAO();
    private static final UserService USER_SERVICE = new UserService(USER_DAO);
    private static final Scanner SCANNER = new Scanner(System.in);


    public static void main(String[] args) {
        while (true) {
            printMenu();
            switch (SCANNER.nextLine()) {
                case "0" : break;
                case "1": {
                    getUsers();
                    return;
                }
                case "2": {
                    getUser();
                    return;
                }
                case "3": return;
                case "4": return;
                case "5": {
                    deleteUser();
                    return;
                }
                default:
                    System.out.println("Такого действия нет!");;
            }
        }
    }

    private static void getUsers() {
        List<UserDTO> userList = USER_SERVICE.getUsers();
        userList.forEach(System.out::println);
    }

    private static void getUser() {
        System.out.println("Введите id пользователя:");
        UserDTO user = USER_SERVICE.getUser(SCANNER.nextInt());
        System.out.println(user);
    }

    private static void deleteUser() {
        System.out.println("Введите id пользователя:");
        UserDTO user = USER_SERVICE.deleteUser(SCANNER.nextInt());
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
