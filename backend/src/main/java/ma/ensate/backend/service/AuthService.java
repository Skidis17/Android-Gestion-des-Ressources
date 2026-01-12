package ma.ensate.backend.service;

import ma.ensate.backend.Enum.Role;
import ma.ensate.backend.config.JwtUtil;
import ma.ensate.backend.domain.User;
import ma.ensate.backend.dto.LoginRequest;
import ma.ensate.backend.dto.LoginResponse;
import ma.ensate.backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil,UserRepository userRepository,PasswordEncoder passwordEncoder,EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
        this.emailService=emailService;
    }




    public LoginResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtUtil.generateToken(request.getEmail());

        Role role = Role.valueOf(
                authentication.getAuthorities().stream()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No role found"))
                        .getAuthority()
                        .replace("ROLE_", "")
        );

        // récupérer l'utilisateur depuis la DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new LoginResponse(
                user.getId(),
                token,
                role,
                user.getUsername(), // ou getNom() selon ton modèle
                user.getEmail()
        );
    }




    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }

        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("Le nouveau mot de passe est trop court");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}