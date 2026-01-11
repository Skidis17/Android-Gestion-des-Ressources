package ma.ensate.myapplication.model;

public class LoginResponse {
    private String token;
    private String role;
    private String username;
    private String email;

    public String getToken() { return token; }
    public String getRole() { return role; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
