package ma.ensate.backend.dto;


import lombok.Data;
import ma.ensate.backend.Enum.Role;

@Data
public class LoginResponse {
    private Long id;
    private String token;
    private Role role;
    private String username;
    private String email;

    public LoginResponse(Long id,String token, Role role,String username, String email) {
        this.id = id;
        this.token = token;
        this.role = role;
        this.username = username;
        this.email = email;

    }
}
