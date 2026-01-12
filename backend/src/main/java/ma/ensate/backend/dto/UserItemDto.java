package ma.ensate.backend.dto;

import ma.ensate.backend.Enum.Role; // <-- adapte ici

public class UserItemDto {

    private Long id;
    private String username;
    private String email;
    private Role role; // ✅ Enum

    public UserItemDto(Long id, String username, String email, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
}
