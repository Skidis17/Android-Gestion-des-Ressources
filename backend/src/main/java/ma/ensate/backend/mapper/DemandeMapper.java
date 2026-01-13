package ma.ensate.backend.mapper;

import ma.ensate.backend.domain.Demande;
import ma.ensate.backend.domain.DemandeStatut;
import ma.ensate.backend.domain.DemandeType;
import ma.ensate.backend.dto.DemandeDto;
import ma.ensate.backend.dto.DemandeRequest;

public class DemandeMapper {
    public static Demande toEntity(DemandeRequest r) {
        if (r == null) return null;
        return Demande.builder()
                .type(DemandeType.valueOf(r.getType()))
                .dateDebut(r.getDateDebut())
                .dateFin(r.getDateFin())
                .motif(r.getMotif())
                .justificatifUrl(r.getJustificatifUrl())
                .createdBy(r.getCreatedBy())
                .build();
    }

    public static DemandeDto toDto(Demande d) {
        if (d == null) return null;
        
        String personnelName = null;
        if (d.getCreatedByUser() != null && d.getCreatedByUser().getPersonnel() != null) {
            var p = d.getCreatedByUser().getPersonnel();
            String nom = p.getNom() != null ? p.getNom() : "";
            String prenom = p.getPrenom() != null ? p.getPrenom() : "";
            personnelName = (nom + " " + prenom).trim();
            if (personnelName.isEmpty()) {
                personnelName = null;
            }
        }
        
        return DemandeDto.builder()
                .id(d.getId())
                .type(d.getType().name())
                .dateDebut(d.getDateDebut())
                .dateFin(d.getDateFin())
                .motif(d.getMotif())
                .justificatifUrl(d.getJustificatifUrl())
                .statut(d.getStatut().name())
                .createdBy(d.getCreatedBy())
                .createdByName(personnelName)
                .createdAt(d.getCreatedAt())
                .build();
    }
}
