package ma.ensate.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BesoinRequest {
    @NotNull
    private Long personnelId;
    @NotNull
    private String typeBesoin;
    @NotNull
    private String description;
    private Integer quantite;
    private BigDecimal montantEstime;
    private String priorite;
    private LocalDate dateLivraison;
}