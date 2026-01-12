package ma.ensate.backend.mapper;

import ma.ensate.backend.domain.CandidatureScore;
import ma.ensate.backend.dto.CandidatureScoreDto;
import ma.ensate.backend.dto.CandidatureScoreRequest;

public class CandidatureScoreMapper {
    public static CandidatureScoreDto toDto(CandidatureScore s) {
        if (s == null) return null;
        return CandidatureScoreDto.builder()
                .id(s.getId())
                .candidatureId(s.getCandidatureId())
                .stage(s.getStage())
                .criterion(s.getCriterion())
                .score(s.getScore())
                .weight(s.getWeight())
                .reviewer(s.getReviewer())
                .notes(s.getNotes())
                .createdAt(s.getCreatedAt())
                .build();
    }

    public static CandidatureScore toEntity(Long candidatureId, CandidatureScoreRequest r) {
        if (r == null) return null;
        return CandidatureScore.builder()
                .candidatureId(candidatureId)
                .stage(r.getStage())
                .criterion(r.getCriterion())
                .score(r.getScore())
                .weight(r.getWeight())
                .reviewer(r.getReviewer())
                .notes(r.getNotes())
                .build();
    }
}
