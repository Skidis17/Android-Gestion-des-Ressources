package ma.ensate.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BudgetSummaryDto {
    private BigDecimal montantTotal;
    private BigDecimal totalRecettes;
    private BigDecimal totalDepenses;
    private BigDecimal montantDisponible;
    private Double pourcentageUtilise;
}
