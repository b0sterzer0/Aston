package Module2.services;

import java.time.LocalDateTime;
import java.util.List;

import Module2.dto.UpdateUserDto;
import Module2.exceptions.UserDeletionException;
import Module2.exceptions.UserNotFoundException;
import Module2.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Module2.dto.UserDTO;
import Module2.dto.CreateUserDto;
import Module2.models.User;
import Module2.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserServiceInterface {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllEntities() {
        LOGGER.debug("Request to get all users");
        List<User> usersFromDB = userRepository.findAll();
        if (usersFromDB.isEmpty()) LOGGER.debug("Returned User list is empty");
        return usersFromDB.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDTO getEntity(long id) {
        LOGGER.debug("Request to get User with id {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            LOGGER.warn("User with id {} not found", id);
            return new UserNotFoundException(id);
        });
        return userMapper.toDTO(user);
    }

    @Transactional
    public UserDTO createEntity(CreateUserDto userDTO) {
        LOGGER.info("Request to create new User");
        User user = userMapper.toUser(userDTO);
        user.setCreatedAt(LocalDateTime.now());
        User addedUser = userRepository.save(user);
        return userMapper.toDTO(addedUser);
    }

    @Transactional
    public UserDTO updateEntity(long id, UpdateUserDto userDto) {
        LOGGER.info("Request to update User with id {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            LOGGER.warn("Updating User failed. User with id {} not found", id);
            return new UserNotFoundException(id);
        });
        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getAge() >= 0) user.setAge(userDto.getAge());
        return userMapper.toDTO(user);
    }

    @Transactional
    public void deleteEntity(long id) {
        LOGGER.info("Request to delete User with id {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            LOGGER.warn("Deletion User failed. User with id {} not found", id);
            return new UserDeletionException(id);
        });
        userRepository.delete(user);
    }
}
