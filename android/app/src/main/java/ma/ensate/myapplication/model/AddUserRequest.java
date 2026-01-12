package ma.ensate.myapplication.model;


public class AddUserRequest {

    private Long personnelId;
    private String username;
    private String password;
    private String role;

    public AddUserRequest() {}

    public AddUserRequest(Long personnelId, String username, String password, String role) {
        this.personnelId = personnelId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getPersonnelId() { return personnelId; }
    public void setPersonnelId(Long personnelId) { this.personnelId = personnelId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

