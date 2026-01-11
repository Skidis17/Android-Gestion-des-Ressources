package ma.ensate.backend.exception;



public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Mot de passe incorrect");
    }
}
