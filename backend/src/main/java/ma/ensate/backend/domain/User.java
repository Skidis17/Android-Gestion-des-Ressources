package ma.ensate.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensate.backend.Enum.Role;

import java.sql.Date;

@Entity
@Table(name = "utilisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "personnel_id")
    private Long personnelId;

    private String username;
    @Column(name = "password_hash")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;

    @Column(name = "derniere_connexion")
    private Date derniereConnexion;

    @Column(name = "is_active")
    private Boolean is_active =true;
    @Column(name = "created_at")
    private  Date createdAt;

}
