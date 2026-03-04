package Module2.dto;

import java.time.LocalDateTime;

public class UserDTO {
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

    public int getAge() {
        return age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
