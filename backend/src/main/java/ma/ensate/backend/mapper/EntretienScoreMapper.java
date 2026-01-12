package ma.ensate.backend.mapper;

import ma.ensate.backend.domain.EntretienScore;
import ma.ensate.backend.dto.EntretienScoreDto;
import ma.ensate.backend.dto.EntretienScoreRequest;

public class EntretienScoreMapper {
    public static EntretienScoreDto toDto(EntretienScore s) {
        if (s == null) return null;
        return EntretienScoreDto.builder()
                .id(s.getId())
                .entretienId(s.getEntretienId())
                .criterion(s.getCriterion())
                .score(s.getScore())
                .weight(s.getWeight())
                .reviewer(s.getReviewer())
                .notes(s.getNotes())
                .createdAt(s.getCreatedAt())
                .build();
    }

    public static EntretienScore toEntity(Long entretienId, EntretienScoreRequest r) {
        if (r == null) return null;
        return EntretienScore.builder()
                .entretienId(entretienId)
                .criterion(r.getCriterion())
                .score(r.getScore())
                .weight(r.getWeight())
                .reviewer(r.getReviewer())
                .notes(r.getNotes())
                .build();
    }
}
