package Module2.mappers;

import Module2.dto.UserDtoInterface;
import Module2.dto.UserDTO;
import Module2.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO (User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }

    public User toUser (UserDtoInterface userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        return user;
    }
}
