package Module2.services;

import Module2.dao.UserDAO;
import Module2.dto.UserDTO;
import Module2.models.User;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
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
        LOGGER.debug("Request to get all users");
        List<User> usersFromDB = userDAO.getAll();
        if (usersFromDB.isEmpty()) LOGGER.debug("Returned User list is empty");
        return usersFromDB.stream()
                .map(this::getUserDtoFromUser)
                .toList();
    }

    public UserDTO getUser(long id) {
        LOGGER.debug("Request to get User with id {}", id);
        User user = userDAO.get(id);
        if (user == null) {
            LOGGER.warn("User with id {} not found", id);
            throw new IllegalArgumentException("User with id " + id + " is not found");
        }
        return getUserDtoFromUser(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        LOGGER.info("Request to create new User");
        User user = getUserFromDTO(userDTO);
        User addedUser = userDAO.create(user);
        return getUserDtoFromUser(addedUser);
    }

    public UserDTO updateUser(long id, UserDTO userDTO) {
        LOGGER.info("Request to update User with id {}", id);
        User userForUpdate = userDAO.get(id);
        if  (userForUpdate == null) {
            LOGGER.warn("Updating failed. User with id {} not found", id);
            throw new IllegalArgumentException("User with id " + id + " is not found");
        }
        userForUpdate.setName(userDTO.getName());
        userForUpdate.setEmail(userDTO.getEmail());
        userForUpdate.setAge(userDTO.getAge());
        User updatedUser = userDAO.update(userForUpdate);
        return getUserDtoFromUser(updatedUser);
    }

    public UserDTO deleteUser(long id) {
        LOGGER.info("Request to delete User with id {}", id);
        User user = userDAO.delete(id);
        if (user == null) throw new IllegalArgumentException("Can not delete User with id " + id);
        return getUserDtoFromUser(user);
    }
}
