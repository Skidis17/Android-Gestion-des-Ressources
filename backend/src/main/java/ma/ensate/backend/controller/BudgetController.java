package ma.ensate.backend.controller;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.dto.BudgetSummaryDto;
import ma.ensate.backend.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping("/summary")
    public BudgetSummaryDto summary() {
        return budgetService.getBudgetSummary();
    }

    @PutMapping("/total")
    public ResponseEntity<BudgetSummaryDto> updateBudgetTotal(@RequestBody Map<String, Double> request) {
        Double newTotal = request.get("montantTotal");
        if (newTotal == null || newTotal < 0) {
            return ResponseEntity.badRequest().build();
        }
        BudgetSummaryDto updated = budgetService.updateBudgetTotal(BigDecimal.valueOf(newTotal));
        return ResponseEntity.ok(updated);
    }
}
