package ma.ensate.backend.service;

import ma.ensate.backend.domain.User;
import ma.ensate.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        String email = auth.getName(); // Spring met souvent l'email ici
        return userRepository.findByEmail(email).orElse(null);
    }

    public void updateProfile(Long userId, String newUsername) {

        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Le username est obligatoire");
        }

        String username = newUsername.trim();

        if (username.length() < 3) {
            throw new IllegalArgumentException("Le username doit contenir au moins 3 caractères");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // (Optionnel) si tu veux empêcher les doublons
        // if (userRepository.existsByUsername(username)) {
        //     throw new IllegalArgumentException("Username déjà utilisé");
        // }

        user.setUsername(username);
        userRepository.save(user);
    }

}
