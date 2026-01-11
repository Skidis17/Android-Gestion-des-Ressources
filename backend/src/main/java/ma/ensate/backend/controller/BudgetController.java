package ma.ensate.backend.controller;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.dto.BudgetSummaryDto;
import ma.ensate.backend.service.BudgetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping("/summary")
    public BudgetSummaryDto summary() {
        return budgetService.getBudgetSummary();
    }
}
