package Module2Tests.integration;

import Module2.config.AppConfig;
import Module2.config.WebConfig;
import Module2.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

@Testcontainers
@SpringJUnitConfig(classes = {AppConfig.class, WebConfig.class})
@WebAppConfiguration
@Transactional
public class UserControllerServiceIntegrationTests {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    static {
        postgres.start();
        System.setProperty("db.url", postgres.getJdbcUrl());
        System.setProperty("db.username", postgres.getUsername());
        System.setProperty("db.password", postgres.getPassword());
        System.setProperty("db.driver", "org.postgresql.Driver");
    }

    @Resource
    private WebApplicationContext context;

    @Resource
    private UserRepository userRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void createAndGetUser() throws Exception {
        String json = """
                {
                    "name": "John",
                    "email": "john@test.com",
                    "age": 20
                }
                """;

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value("john@test.com"));

        assertThat(userRepository.findAll()).hasSize(1);
    }

    @Test
    void updateUser() throws Exception {
        String createJson = """
                {
                    "name": "Old",
                    "email": "old@test.com",
                    "age": 10
                }
                """;

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = objectMapper.readTree(response).get("id").asLong();

        String updateJson = """
                {
                    "name": "New Name"
                }
                """;

        mockMvc.perform(patch("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.email").value("old@test.com"));

        assertThat(userRepository.findById(id).get().getName())
                .isEqualTo("New Name");
    }

    @Test
    void deleteUser() throws Exception {
        String json = """
                {
                    "name": "ToDelete",
                    "email": "del@test.com",
                    "age": 30
                }
                """;

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(id)).isEmpty();
    }

    @Test
    void createUser_shouldRollbackAfterTest() throws Exception {
        String json = """
                {
                    "name": "John",
                    "email": "john@test.com",
                    "age": 20
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        assert(userRepository.findAll().size() == 1);
    }

    @Test
    void db_shouldBeEmptyBecauseOfRollback() {
        assert(userRepository.findAll().isEmpty());
    }
}

