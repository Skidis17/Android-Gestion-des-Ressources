package ma.ensate.backend.mapper;

import ma.ensate.backend.domain.Recette;
import ma.ensate.backend.dto.RecetteDto;
import ma.ensate.backend.dto.RecetteRequest;

public class RecetteMapper {
    public static RecetteDto toDto(Recette r) {
        if (r == null)
            return null;
        return RecetteDto.builder()
                .id(r.getId())
                .source(r.getSource())
                .categorie(r.getCategorie())
                .montant(r.getMontant())
                .date(r.getDateRecette())
                .description(r.getDescription())
                .reference(r.getReferenceDocument())
                .enregistrePar(r.getEnregistrePar())
                .createdAt(r.getCreatedAt())
                .build();
    }

    public static Recette toEntity(RecetteRequest req) {
        if (req == null)
            return null;

        java.time.LocalDate parsedDate = null;
        if (req.getDate() != null) {
            String s = req.getDate().trim();
            // try ISO first
            try {
                parsedDate = java.time.LocalDate.parse(s);
            } catch (Exception ignored) {
            }
            // try dd/MM/yyyy
            if (parsedDate == null) {
                try {
                    java.time.format.DateTimeFormatter f = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    parsedDate = java.time.LocalDate.parse(s, f);
                } catch (Exception ignored) {
                }
            }
        }

        return Recette.builder()
                .source(req.getSource())
                .categorie(req.getCategorie())
                .montant(req.getMontant())
                .dateRecette(parsedDate)
                .description(req.getDescription())
                .referenceDocument(req.getReference())
                .enregistrePar(req.getEnregistrePar())
                .build();
    }
}
