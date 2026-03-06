package Module2.services;

import java.time.LocalDateTime;
import java.util.List;

import Module2.exceptions.UserDeletionException;
import Module2.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Module2.dao.UserDaoInterface;
import Module2.dto.UserDTO;
import Module2.models.User;
import Module2.mappers.UserMapper;


public class UserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserDaoInterface userDAO;

    public UserService(UserDaoInterface userDAO) {
        this.userDAO = userDAO;
    }

    public List<UserDTO> getUsers() {
        LOGGER.debug("Request to get all users");
        List<User> usersFromDB = userDAO.getAll();
        if (usersFromDB.isEmpty()) LOGGER.debug("Returned User list is empty");
        return usersFromDB.stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public UserDTO getUser(long id) {
        LOGGER.debug("Request to get User with id {}", id);
        User user = userDAO.get(id);
        if (user == null) {
            LOGGER.warn("User with id {} not found", id);
            throw new UserNotFoundException(id);
        }
        return UserMapper.toDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        LOGGER.info("Request to create new User");
        User user = UserMapper.toUser(userDTO);
        user.setCreatedAt(LocalDateTime.now());
        User addedUser = userDAO.create(user);
        return UserMapper.toDTO(addedUser);
    }

    public UserDTO updateUser(long id, UserDTO userDTO) {
        LOGGER.info("Request to update User with id {}", id);
        User userForUpdate = userDAO.get(id);
        if  (userForUpdate == null) {
            LOGGER.warn("Updating failed. User with id {} not found", id);
            throw new UserNotFoundException(id);
        }
        if (userDTO.getName() != null) userForUpdate.setName(userDTO.getName());
        if (userDTO.getEmail() != null) userForUpdate.setEmail(userDTO.getEmail());
        if (userDTO.getAge() >= 0) userForUpdate.setAge(userDTO.getAge());
        User updatedUser = userDAO.update(userForUpdate);
        return UserMapper.toDTO(updatedUser);
    }

    public UserDTO deleteUser(long id) {
        LOGGER.info("Request to delete User with id {}", id);
        User user = userDAO.delete(id);
        if (user == null) throw new UserDeletionException(id);
        return UserMapper.toDTO(user);
    }
}
