package Module2.mappers;

import Module2.dto.UserDTO;
import Module2.models.User;

import java.time.LocalDateTime;

public class UserMapper {
    public static UserDTO toDTO (User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }

    public static User toUser (UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
