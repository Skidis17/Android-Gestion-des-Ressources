package ma.ensate.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import ma.ensate.backend.domain.User;
import ma.ensate.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Optionnel : bloquer si inactive
        if (Boolean.FALSE.equals(u.getIs_active())) {
            throw new DisabledException("User is inactive");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername())
                .password(u.getPasswordHash())
                .roles(u.getRole().name()) // ✅ enum USER -> ROLE_USER
                .build();
    }
}

