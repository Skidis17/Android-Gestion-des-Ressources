package ma.ensate.backend.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ma.ensate.backend.Enum.Role;
import ma.ensate.backend.domain.Personnel;
import ma.ensate.backend.domain.User;
import ma.ensate.backend.dto.AddUserRequest;
import ma.ensate.backend.dto.AddUserResponse;
import ma.ensate.backend.dto.UserInfoDto;
import ma.ensate.backend.repository.PersonnelRepository;
import ma.ensate.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PersonnelRepository personnelRepository;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, PersonnelRepository personnelRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.personnelRepository = personnelRepository;
    }

    @Transactional
    public AddUserResponse AjouterUtilisateur(AddUserRequest request) {

        // 1) validations simples
        if (request.getPersonnelId() == null)
            throw new IllegalArgumentException("personnelId requis");

        if (request.getPassword() == null || request.getPassword().isBlank())
            throw new IllegalArgumentException("Mot de passe requis");

        if (request.getUsername() == null || request.getUsername().isBlank())
            throw new IllegalArgumentException("Username requis");

        Long id = request.getPersonnelId();
        boolean exists = personnelRepository.existsById(id);
        System.out.println(">>> existsById(" + id + ") = " + exists);

        // 2) récupérer Personnel + email
        Personnel personnel = personnelRepository.findById(request.getPersonnelId())
                .orElseThrow(() -> new IllegalArgumentException("Personnel introuvable"));

        System.out.println(">>> count personnel = " + personnelRepository.count());
        System.out.println(">>> personnelId demandé = " + request.getPersonnelId());

        if (personnel.getEmail() == null || personnel.getEmail().isBlank())
            throw new IllegalArgumentException("Email manquant dans la table personnel");

        String email = personnel.getEmail().trim();

        // 3) email unique
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email déjà utilisé");
        }

        // 4) role par défaut
        Role role = (request.getRole() != null) ? request.getRole() : Role.RH;

        // 5) créer user
        User user = new User();
        user.setPersonnelId(request.getPersonnelId());
        user.setEmail(email);
        user.setUsername(request.getUsername().trim());
        user.setRole(role);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setPersonnel(personnel);

        User saved = userRepository.save(user);

        // 6) mail invitation (si tu veux)
        if (emailService != null) {
            emailService.sendUserInvitationEmail(
                    saved.getEmail(),
                    saved.getUsername(),
                    saved.getEmail(),
                    request.getPassword()
            );
        }

        return new AddUserResponse(saved.getId(), saved.getEmail(), saved.getUsername(), saved.getRole());
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

        user.setUsername(username);
        userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public UserInfoDto getInfoUserById(Long userId) {

        User u = userRepository.findWithPersonnelById(userId)
                .orElseThrow(() -> new RuntimeException("User introuvable: " + userId));

        Personnel p = u.getPersonnel(); // déjà chargé grâce à JOIN FETCH

        return UserInfoDto.builder()
                // user
                .userId(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .role(u.getRole() != null ? u.getRole().name() : null)
                .isActive(u.getIs_active())

                // personnel
                .personnelId(p != null ? p.getId() : null)
                .cin(p != null ? p.getCin() : null)
                .nom(p != null ? p.getNom() : null)
                .prenom(p != null ? p.getPrenom() : null)
                .telephone(p != null ? p.getTelephone() : null)
                .typePersonnel(p != null ? p.getType_personnel() : null)
                .grade(p != null ? p.getGrade() : null)
                .echelon(p != null ? p.getEchelon() : null)
                .personnelEmail(p != null ? p.getEmail() : null)
                .departement(p != null ? p.getDepartement() : null)
                .statut(p != null ? p.getStatut() : null)
                .build();
    }
}
