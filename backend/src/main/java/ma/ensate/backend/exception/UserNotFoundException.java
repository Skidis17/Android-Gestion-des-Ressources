package ma.ensate.backend.exception;



public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Utilisateur introuvable");
    }
}
