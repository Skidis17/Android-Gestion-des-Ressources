package ma.ensate.backend.dto;

import ma.ensate.backend.Enum.Role;

public class AddUserResponse {
    private Long id;
    private String email;
    private String username;
    private Role role;

    public AddUserResponse(Long id, String email, String username, Role role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public Role getRole() { return role; }
}
