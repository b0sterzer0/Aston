package Module2Tests.controllers;

import Module2.controllers.UserController;
import Module2.dto.CreateUserDto;
import Module2.dto.UpdateUserDto;
import Module2.dto.UserDTO;
import Module2.services.UserServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    private MockMvc mockMvc;

    @Mock
    private UserServiceInterface userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void createUser_shouldReturn201() throws Exception {
        CreateUserDto request = new CreateUserDto("John", "john@test.com", 20);
        UserDTO response = new UserDTO(
                1L,
                "John",
                "john@test.com",
                20,
                LocalDateTime.of(2024, 1, 1, 10, 0)
        );

        when(userService.createEntity(any())).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@test.com"))
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    void getUser_shouldReturn200() throws Exception {
        long id = 1L;

        UserDTO response = new UserDTO(
                id,
                "John",
                "john@test.com",
                20,
                LocalDateTime.now()
        );

        when(userService.getEntity(id)).thenReturn(response);

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        List<UserDTO> list = List.of(
                new UserDTO(1L, "A", "a@mail.com", 20, LocalDateTime.now()),
                new UserDTO(2L, "B", "b@mail.com", 30, LocalDateTime.now())
        );

        when(userService.getAllEntities()).thenReturn(list);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateUser_shouldReturn200() throws Exception {
        long id = 1L;

        UpdateUserDto request = new UpdateUserDto();
        request.setName("New Name");

        UserDTO response = new UserDTO(
                id,
                "New Name",
                "old@mail.com",
                20,
                LocalDateTime.now()
        );

        when(userService.updateEntity(eq(id), any())).thenReturn(response);

        mockMvc.perform(patch("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    void deleteUser_shouldReturn204() throws Exception {
        long id = 1L;

        doNothing().when(userService).deleteEntity(id);

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isNoContent());

        verify(userService).deleteEntity(id);
    }
}
