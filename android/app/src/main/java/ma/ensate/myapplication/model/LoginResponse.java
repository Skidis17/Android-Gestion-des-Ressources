package ma.ensate.myapplication.model;

public class LoginResponse {

    private Long id;
    private String token;
    private String role;
    private String username;
    private String email;

    public Long getId() { return id; }
    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}
