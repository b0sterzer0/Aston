package Module2.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserDTO implements UserDtoInterface {
    private final long id;
    private final String name;
    private final String email;
    private final int age;
    private final LocalDateTime createdAt;

    public UserDTO(long id, String name, String email, int age, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        return id == userDTO.id &&
                age == userDTO.age &&
                Objects.equals(name, userDTO.name) &&
                Objects.equals(email, userDTO.email) &&
                Objects.equals(createdAt, userDTO.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age, createdAt);
    }

    @Override
    public String toString() {
        return String.format("ID: %d Username: %s Email: %s Age: %d, CreatedAt: %s",
                id,
                name,
                email,
                age,
                createdAt
        );
    }
}
