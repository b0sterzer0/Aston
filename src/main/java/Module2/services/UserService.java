package Module2.services;

import Module2.dao.UserDAO;
import Module2.dto.UserDTO;
import Module2.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private UserDTO getUserDtoFromUser(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }

    private User getUserFromDTO(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    public List<UserDTO> getUsers() {
        List<User> usersFromDB = userDAO.getAll();
        return usersFromDB.stream()
                .map(this::getUserDtoFromUser)
                .toList();
    }

    public UserDTO getUser(long id) {
        User user = userDAO.get(id);
        if (user == null) throw new IllegalArgumentException("User with id " + id + " is not found");
        return getUserDtoFromUser(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = getUserFromDTO(userDTO);
        User addedUser = userDAO.create(user);
        return getUserDtoFromUser(addedUser);
    }

    public UserDTO updateUser(long id, UserDTO userDTO) {
        User userForUpdate = userDAO.get(id);
        if  (userForUpdate == null) throw new IllegalArgumentException("User with id " + id + " is not found");
        userForUpdate.setName(userDTO.getName());
        userForUpdate.setEmail(userDTO.getEmail());
        userForUpdate.setAge(userDTO.getAge());
        User updatedUser = userDAO.update(userForUpdate);
        return getUserDtoFromUser(updatedUser);
    }

    public UserDTO deleteUser(long id) {
        User user = userDAO.delete(id);
        if (user == null) throw new IllegalArgumentException("Can not delete User with id " + id);
        return getUserDtoFromUser(user);
    }
}
