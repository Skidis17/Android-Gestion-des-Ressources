package ma.ensate.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensate.backend.Enum.Role;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private Role role;
    private String username;

    public UserDto(Long id, String email, Role role, String username) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.username = username;

    }


}
