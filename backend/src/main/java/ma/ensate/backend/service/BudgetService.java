package ma.ensate.backend.service;

import ma.ensate.backend.dto.BudgetSummaryDto;
import java.math.BigDecimal;

public interface BudgetService {
    BudgetSummaryDto getBudgetSummary();

    BudgetSummaryDto updateBudgetTotal(BigDecimal newTotal);
}
