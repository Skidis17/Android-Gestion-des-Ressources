package ma.ensate.backend.mapper;

import ma.ensate.backend.domain.CandidatureRecrutement;
import ma.ensate.backend.dto.CandidatureRecrutementDto;
import ma.ensate.backend.dto.CandidatureRecrutementRequest;

public class CandidatureRecrutementMapper {

    public static CandidatureRecrutementDto toDto(CandidatureRecrutement c) {
        if (c == null) return null;
        return CandidatureRecrutementDto.builder()
                .id(c.getId())
                .recrutementId(c.getRecrutementId())
                .nom(c.getNom())
                .prenom(c.getPrenom())
                .cin(c.getCin())
                .email(c.getEmail())
                .telephone(c.getTelephone())
                .cvUrl(c.getCvUrl())
                .lettreMotivationUrl(c.getLettreMotivationUrl())
                .statut(c.getStatut())
                .scoreEcrit(c.getScoreEcrit())
                .scoreOral(c.getScoreOral())
                .commentaires(c.getCommentaires())
                .dateCandidature(c.getDateCandidature())
                .build();
    }

    public static CandidatureRecrutement toEntity(CandidatureRecrutementRequest r) {
        if (r == null) return null;
        return CandidatureRecrutement.builder()
                .recrutementId(r.getRecrutementId())
                .nom(r.getNom())
                .prenom(r.getPrenom())
                .cin(r.getCin())
                .email(r.getEmail())
                .telephone(r.getTelephone())
                .cvUrl(r.getCvUrl())
                .lettreMotivationUrl(r.getLettreMotivationUrl())
                .statut(r.getStatut())
                .scoreEcrit(r.getScoreEcrit())
                .scoreOral(r.getScoreOral())
                .commentaires(r.getCommentaires())
                .build();
    }
}
