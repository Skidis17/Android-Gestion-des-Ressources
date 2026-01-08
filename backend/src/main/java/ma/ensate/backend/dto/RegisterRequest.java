package ma.ensate.backend.dto;

import ma.ensate.backend.Enum.Role;

public class RegisterRequest {
    private Long personnelId;
    private String email;
    private String username;
    private String password;
    private Role role; // ex: HR

    public Long getPersonnelId() { return personnelId; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }

    public void setPersonnelId(Long personnelId) { this.personnelId = personnelId; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
}
