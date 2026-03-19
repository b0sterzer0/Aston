package Module2.services;

import Module2.dto.CreateUserDto;
import Module2.dto.UpdateUserDto;
import Module2.dto.UserDTO;

import java.util.List;

public interface UserServiceInterface {
    List<UserDTO> getAllEntities();
    UserDTO getEntity(long id);
    UserDTO createEntity(CreateUserDto dto);
    UserDTO updateEntity(long id, UpdateUserDto dto);
    void deleteEntity(long id);
}
