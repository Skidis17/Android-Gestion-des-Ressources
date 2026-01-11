package ma.ensate.backend.dto;

import ma.ensate.backend.Enum.Role;

public class AddUserRequest {
    private Long personnelId;
    private String username;
    private String email;
    private String password;
    private Role role;

    public Long getPersonnelId() { return personnelId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public void setPersonnelId(Long personnelId) { this.personnelId = personnelId; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
}
