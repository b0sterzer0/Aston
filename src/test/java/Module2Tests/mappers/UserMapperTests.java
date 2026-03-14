package Module2Tests.mappers;

import Module2.dto.UserDTO;
import Module2.mappers.UserMapper;
import Module2.models.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTests {
    @Test
    void toDTO_ShouldMapAllFields() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(25);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);

        UserDTO dto = UserMapper.toDTO(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getAge(), dto.getAge());
        assertEquals(user.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    void toUser_ShouldMapFieldsAndSetCreatedAt() {
        UserDTO dto = new UserDTO(0, "Test User", "test@example.com", 25, null);

        User user = UserMapper.toUser(dto);

        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getAge(), user.getAge());
        assertNotNull(user.getCreatedAt());
        assertEquals(0, user.getId()); // ID не должен маппиться
    }

    @Test
    void toUser_ShouldHandleNullDTO() {
        assertThrows(NullPointerException.class, () -> UserMapper.toUser(null));
    }

    @Test
    void toDTO_ShouldHandleNullUser() {
        assertThrows(NullPointerException.class, () -> UserMapper.toDTO(null));
    }
}
