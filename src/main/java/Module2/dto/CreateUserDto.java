package Module2.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class CreateUserDto implements UserDtoInterface {
    @NotBlank
    private final String name;
    @Email
    private final String email;
    @Min(0)
    private final int age;

    @JsonCreator
    public CreateUserDto(@JsonProperty("name") String name,
                         @JsonProperty("email") String email,
                         @JsonProperty("age") int age) {
        this.name = name;
        this.email = email;
        this.age = age;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CreateUserDto that = (CreateUserDto) o;
        return age == that.age && Objects.equals(name, that.name) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, age);
    }

    @Override
    public String toString() {
        return String.format("Username: %s Email: %s Age: %d",
                name,
                email,
                age
        );
    }
}
