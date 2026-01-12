package ma.ensate.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "personnel")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Personnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cin")
    private String cin;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "type_personnel")
    private String  type_personnel;

    @Column(name = "grade")
    private String grade;

    @Column(name = "echelon")
    private String echelon;

    @Column(name = "email")
    private String email;
    @Column(name = "departement")
    private String departement;

}
