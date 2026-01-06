package ma.ensate.backend.dto;


import lombok.Data;
import ma.ensate.backend.Enum.Role;

@Data
public class LoginResponse {
    private String token;
    private Role role;

    public LoginResponse(String token, Role role) {
        this.token = token;
        this.role = role;
    }
}
