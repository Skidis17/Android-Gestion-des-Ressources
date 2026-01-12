package ma.ensate.backend.mapper;

import ma.ensate.backend.domain.Entretien;
import ma.ensate.backend.dto.EntretienDto;
import ma.ensate.backend.dto.EntretienRequest;

public class EntretienMapper {
    public static EntretienDto toDto(Entretien e) {
        if (e == null) return null;
        return EntretienDto.builder()
                .id(e.getId())
                .candidatureId(e.getCandidatureId())
                .type(e.getType())
                .scheduledAt(e.getScheduledAt())
                .mode(e.getMode())
                .location(e.getLocation())
                .status(e.getStatus())
                .notes(e.getNotes())
                .scoreTotal(e.getScoreTotal())
                .createdBy(e.getCreatedBy())
                .createdAt(e.getCreatedAt())
                .build();
    }

    public static Entretien toEntity(Long candidatureId, EntretienRequest r) {
        if (r == null) return null;
        return Entretien.builder()
                .candidatureId(candidatureId)
                .type(r.getType())
                .scheduledAt(r.getScheduledAt())
                .mode(r.getMode())
                .location(r.getLocation())
                .status(r.getStatus())
                .notes(r.getNotes())
                .createdBy(r.getCreatedBy())
                .build();
    }
}
