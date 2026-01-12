package ma.ensate.backend.controller;

import ma.ensate.backend.dto.LoginRequest;
import ma.ensate.backend.dto.PasswordChangeRequest;
import ma.ensate.backend.service.AuthService;
import ma.ensate.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Email ou mot de passe incorrect"));
        }
    }




    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(java.util.Map.of("message", "Déconnecté"));
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request) {
        try {
            authService.changePassword(id, request.oldPassword, request.newPassword);
            return ResponseEntity.ok("Mot de passe modifié avec succès");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}