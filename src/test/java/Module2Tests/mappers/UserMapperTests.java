package Module2Tests.mappers;

import Module2.dto.CreateUserDto;
import Module2.dto.UserDTO;
import Module2.mappers.UserMapper;
import Module2.models.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTests {
    private final UserMapper userMapper = new UserMapper();

    @Test
    void toDTO_shouldMapUserToUserDTO() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);

        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@test.com");
        user.setAge(20);
        user.setCreatedAt(createdAt);

        UserDTO dto = userMapper.toDTO(user);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getName());
        assertEquals("john@test.com", dto.getEmail());
        assertEquals(20, dto.getAge());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void toUser_shouldMapDtoToUser() {
        CreateUserDto dto = new CreateUserDto(
                "Alice",
                "alice@test.com",
                25
        );

        User user = userMapper.toUser(dto);

        assertNotNull(user);
        assertEquals("Alice", user.getName());
        assertEquals("alice@test.com", user.getEmail());
        assertEquals(25, user.getAge());

        assertNull(user.getCreatedAt());
    }
}
