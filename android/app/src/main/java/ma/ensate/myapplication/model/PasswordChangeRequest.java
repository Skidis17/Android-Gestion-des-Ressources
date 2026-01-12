package ma.ensate.myapplication.model;

public class PasswordChangeRequest {
    public String oldPassword;
    public String newPassword;

    public PasswordChangeRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
