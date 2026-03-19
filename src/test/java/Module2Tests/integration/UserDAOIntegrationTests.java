//package Module2Tests.integration;
//
//import Module2.dao.UserDAO;
//import Module2.models.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class UserDAOIntegrationTests extends BaseIntegrationTest {
//    private static final Logger log = LoggerFactory.getLogger(UserDAOIntegrationTests.class);
//    private UserDAO userDAO;
//
//    @BeforeEach
//    void setUp() {
//        userDAO = new UserDAO();
//        cleanDatabase(); // Очищаем БД перед каждым тестом
//    }
//
//    private User createTestUser(String name, String email, int age) {
//        User user = new User();
//        user.setName(name);
//        user.setEmail(email);
//        user.setAge(age);
//        user.setCreatedAt(LocalDateTime.now());
//        return user;
//    }
//
//    @Test
//    void create_ShouldPersistUserAndGenerateId() {
//        User user = createTestUser("Test User", "test@example.com", 25);
//
//        User created = userDAO.create(user);
//
//        assertThat(created).isNotNull();
//        assertThat(created.getId()).isPositive();
//        assertThat(created.getName()).isEqualTo("Test User");
//        assertThat(created.getEmail()).isEqualTo("test@example.com");
//        assertThat(created.getAge()).isEqualTo(25);
//        assertThat(created.getCreatedAt()).isNotNull();
//
//        log.info("Created user with ID: {}", created.getId());
//    }
//
//    @Test
//    void get_ShouldReturnUserById() {
//        User user = createTestUser("John Doe", "john@example.com", 30);
//        User saved = userDAO.create(user);
//
//        User found = userDAO.get(saved.getId());
//
//        assertThat(found).isNotNull();
//        assertThat(found.getId()).isEqualTo(saved.getId());
//        assertThat(found.getName()).isEqualTo("John Doe");
//        assertThat(found.getEmail()).isEqualTo("john@example.com");
//        assertThat(found.getAge()).isEqualTo(30);
//    }
//
//    @Test
//    void get_ShouldReturnNullForNonExistentId() {
//        User found = userDAO.get(99999L);
//
//        assertThat(found).isNull();
//    }
//
//    @Test
//    void getAll_ShouldReturnAllUsers() {
//        userDAO.create(createTestUser("User1", "user1@test.com", 25));
//        userDAO.create(createTestUser("User2", "user2@test.com", 30));
//        userDAO.create(createTestUser("User3", "user3@test.com", 35));
//
//        List<User> users = userDAO.getAll();
//
//        assertThat(users).hasSize(3);
//        assertThat(users)
//                .extracting(User::getName)
//                .containsExactlyInAnyOrder("User1", "User2", "User3");
//    }
//
//    @Test
//    void update_ShouldModifyExistingUser() {
//        User user = createTestUser("Original Name", "original@test.com", 25);
//        User saved = userDAO.create(user);
//
//        saved.setName("Updated Name");
//        saved.setEmail("updated@test.com");
//        saved.setAge(35);
//        User updated = userDAO.update(saved);
//
//        assertThat(updated).isNotNull();
//        assertThat(updated.getId()).isEqualTo(saved.getId());
//        assertThat(updated.getName()).isEqualTo("Updated Name");
//        assertThat(updated.getEmail()).isEqualTo("updated@test.com");
//        assertThat(updated.getAge()).isEqualTo(35);
//
//        User found = userDAO.get(saved.getId());
//        assertThat(found.getName()).isEqualTo("Updated Name");
//    }
//
//    @Test
//    void delete_ShouldRemoveUser() {
//        User user = createTestUser("To Delete", "delete@test.com", 40);
//        User saved = userDAO.create(user);
//        long id = saved.getId();
//
//        User deleted = userDAO.delete(id);
//
//        assertThat(deleted).isNotNull();
//        assertThat(deleted.getId()).isEqualTo(id);
//
//        User shouldBeNull = userDAO.get(id);
//        assertThat(shouldBeNull).isNull();
//    }
//
//    @Test
//    void delete_ShouldReturnNullForNonExistentUser() {
//        User deleted = userDAO.delete(99999L);
//
//        assertThat(deleted).isNull();
//    }
//
//    @Test
//    void create_ShouldHandleMultipleUsers() {
//        User user1 = createTestUser("Alice", "alice@test.com", 28);
//        User user2 = createTestUser("Bob", "bob@test.com", 32);
//        User user3 = createTestUser("Charlie", "charlie@test.com", 45);
//
//        User created1 = userDAO.create(user1);
//        User created2 = userDAO.create(user2);
//        User created3 = userDAO.create(user3);
//
//        assertThat(created1.getId()).isPositive();
//        assertThat(created2.getId()).isPositive();
//        assertThat(created3.getId()).isPositive();
//
//        List<User> allUsers = userDAO.getAll();
//        assertThat(allUsers).hasSize(3);
//    }
//}
