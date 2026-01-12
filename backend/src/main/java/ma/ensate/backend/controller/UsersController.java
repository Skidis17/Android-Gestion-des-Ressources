package ma.ensate.backend.controller;

import ma.ensate.backend.domain.User;
import ma.ensate.backend.dto.*;
import ma.ensate.backend.repository.UserRepository;
import ma.ensate.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UsersController {

    private final UserService userService;
    private final UserRepository userRepository;
    public UsersController(UserService userService, UserRepository userRepository) {

        this.userService = userService;
        this.userRepository = userRepository;
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


    @PostMapping("/addUser")
    public ResponseEntity<?> register(@RequestBody AddUserRequest request) {
        System.out.println(">>> personnelId reçu = " + request.getPersonnelId());
        System.out.println(">>> username reçu = " + request.getUsername());
        try {
            AddUserResponse res = userService.AjouterUtilisateur(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur serveur"));
        }
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

    @GetMapping("/all")
    public List<UserItemDto> getAllUsers() {
        return userRepository.findAllUsers();
    }

}
