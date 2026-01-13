package ma.ensate.backend.mapper;

import ma.ensate.backend.domain.Recrutement;
import ma.ensate.backend.dto.RecrutementDto;
import ma.ensate.backend.dto.RecrutementRequest;

public class RecrutementMapper {

    public static RecrutementDto toDto(Recrutement r) {
        if (r == null) return null;
        return RecrutementDto.builder()
                .id(r.getId())
                .poste(r.getPoste())
                .typeContrat(r.getTypeContrat())
                .departement(r.getDepartement())
                .nombrePostes(r.getNombrePostes())
                .description(r.getDescription())
                .dateOuverture(r.getDateOuverture())
                .dateCloture(r.getDateCloture())
                .statut(r.getStatut())
                .createdBy(r.getCreatedBy())
                .createdAt(r.getCreatedAt())
                .pdfUrl(r.getPdfUrl())
                .build();
    }

    public static Recrutement toEntity(RecrutementRequest r) {
        if (r == null) return null;
        return Recrutement.builder()
                .poste(r.getPoste())
                .typeContrat(r.getTypeContrat())
                .departement(r.getDepartement())
                .nombrePostes(r.getNombrePostes())
                .description(r.getDescription())
                .dateOuverture(r.getDateOuverture())
                .dateCloture(r.getDateCloture())
                .statut(r.getStatut())
                .createdBy(r.getCreatedBy())
                .build();
    }
}
