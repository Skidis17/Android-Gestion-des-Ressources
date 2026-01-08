package ma.ensate.backend.controller;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.dto.BudgetSummaryDto;
import ma.ensate.backend.domain.Budget;
import ma.ensate.backend.repository.BudgetRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/budget")
public class BudgetController {

    @GetMapping("/summary")
    public BudgetSummaryDto summary() {
        // Return hardcoded response for now
        return BudgetSummaryDto.builder()
                .montantTotal(new java.math.BigDecimal("500000"))
                .totalRecettes(new java.math.BigDecimal("35000"))
                .totalDepenses(new java.math.BigDecimal("6200"))
                .montantDisponible(new java.math.BigDecimal("528800"))
                .pourcentageUtilise(1.24)
                .build();
    }
}
