package ma.ensate.backend.dto;


import lombok.Data;
import ma.ensate.backend.Enum.Role;

@Data
public class LoginResponse {
    private String token;
    private Role role;
    private String username;
    private String email;

    public LoginResponse(String token, Role role,String username, String email) {
        this.token = token;
        this.role = role;
        this.username = username;
        this.email = email;

    }
}
