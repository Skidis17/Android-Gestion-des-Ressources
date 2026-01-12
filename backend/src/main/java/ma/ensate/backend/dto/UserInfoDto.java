package ma.ensate.backend.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserInfoDto {

    // User
    private Long userId;
    private Long personnelId;
    private String username;
    private String email;
    private String role;
    private Boolean isActive;

    // Personnel
    private String cin;
    private String nom;
    private String prenom;
    private String telephone;
    private String typePersonnel;
    private String grade;
    private String echelon;
    private String personnelEmail;
    private String departement;
    private Boolean statut;
}
