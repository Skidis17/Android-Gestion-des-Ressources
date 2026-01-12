package ma.ensate.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class RecetteDto {
    private Long id;
    private String source;
    private String categorie;
    private BigDecimal montant;
    private LocalDate date;
    private String description;
    private String reference;
    private Long enregistrePar;
    private LocalDateTime createdAt;
}
