package ma.ensate.backend.controller;

import ma.ensate.backend.domain.User;
import ma.ensate.backend.dto.UpdateProfileRequest;
import ma.ensate.backend.dto.UserDto;
import ma.ensate.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        User user = userService.getCurrentUser();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("message", "Non authentifié"));
        }

        UserDto dto = new UserDto(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getUsername()
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<?> updateProfile(@PathVariable Long id,
                                           @RequestBody UpdateProfileRequest request) {
        try {
            userService.updateProfile(id, request.username);
            return ResponseEntity.ok("Profil mis à jour avec succès");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
