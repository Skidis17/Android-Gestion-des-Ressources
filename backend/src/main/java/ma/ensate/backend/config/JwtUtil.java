package ma.ensate.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "secret_incendie_key_256_bits_minimum_secure_123456";

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Méthode pour générer le token avec durée personnalisée
    public String generateToken(String username, long expirationMillis) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Méthode pour générer un token avec durée par défaut (24h)
    public String generateToken(String username) {
        return generateToken(username, 24 * 60 * 60 * 1000); // 24 heures
    }

    // Extraire le username du token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraire la date d'expiration
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraire un claim spécifique
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extraire tous les claims
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("❌ JWT Parse Error: " + e.getMessage());
            throw e;
        }
    }

    // Vérifier si le token est expiré
    private Boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            boolean expired = expiration.before(new Date());
            System.out.println("🔍 Token expiration check:");
            System.out.println("   - Expiration date: " + expiration);
            System.out.println("   - Current date: " + new Date());
            System.out.println("   - Is expired: " + expired);
            return expired;
        } catch (Exception e) {
            System.out.println("❌ Error checking expiration: " + e.getMessage());
            return true;
        }
    }

    // ⚠️ MÉTHODE CRITIQUE: Valider le token
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            System.out.println("🔍 Validating token...");

            final String username = extractUsername(token);
            System.out.println("   - Token username: " + username);
            System.out.println("   - UserDetails username: " + userDetails.getUsername());

            boolean usernameMatches = username.equals(userDetails.getUsername());
            boolean isNotExpired = !isTokenExpired(token);

            System.out.println("   - Username matches: " + usernameMatches);
            System.out.println("   - Token not expired: " + isNotExpired);

            boolean isValid = usernameMatches && isNotExpired;
            System.out.println("   - Final validation result: " + (isValid ? "✅ VALID" : "❌ INVALID"));

            return isValid;
        } catch (Exception e) {
            System.out.println("❌ Token validation exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Valider le token (surcharge simple)
    public Boolean validateToken(String token, String username) {
        try {
            final String tokenUsername = extractUsername(token);
            return (tokenUsername.equals(username) && !isTokenExpired(token));
        } catch (Exception e) {
            System.out.println("❌ Token validation failed: " + e.getMessage());
            return false;
        }
    }
}