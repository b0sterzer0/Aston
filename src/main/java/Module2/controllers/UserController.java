package Module2.controllers;

import Module2.dto.CreateUserDto;
import Module2.dto.UpdateUserDto;
import Module2.dto.UserDTO;
import Module2.services.UserServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceInterface userService;

    @Autowired
    public UserController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDto userForCreate) {
        UserDTO createdUser = userService.createEntity(userForCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDtoList = userService.getAllEntities();
        return ResponseEntity.ok(userDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") long id) {
        UserDTO userDTO = userService.getEntity(id);
        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") long id,
                                              @Valid @RequestBody UpdateUserDto userDtoForUpdate) {
        UserDTO updatedUser = userService.updateEntity(id,  userDtoForUpdate);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        userService.deleteEntity(id);
        return ResponseEntity.noContent().build();
    }
}
