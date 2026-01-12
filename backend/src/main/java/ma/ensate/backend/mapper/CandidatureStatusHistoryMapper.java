package ma.ensate.backend.mapper;

import ma.ensate.backend.domain.CandidatureStatusHistory;
import ma.ensate.backend.dto.CandidatureStatusHistoryDto;

public class CandidatureStatusHistoryMapper {
    public static CandidatureStatusHistoryDto toDto(CandidatureStatusHistory h) {
        if (h == null) return null;
        return CandidatureStatusHistoryDto.builder()
                .id(h.getId())
                .candidatureId(h.getCandidatureId())
                .fromStatus(h.getFromStatus())
                .toStatus(h.getToStatus())
                .reason(h.getReason())
                .changedBy(h.getChangedBy())
                .changedAt(h.getChangedAt())
                .build();
    }
}
