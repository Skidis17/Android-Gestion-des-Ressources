package ma.ensate.backend.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Budget;
import ma.ensate.backend.domain.Depense;
import ma.ensate.backend.domain.Recette;
import ma.ensate.backend.dto.BudgetSummaryDto;
import ma.ensate.backend.repository.BudgetRepository;
import ma.ensate.backend.repository.DepenseRepository;
import ma.ensate.backend.repository.RecetteRepository;
import ma.ensate.backend.service.BudgetService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final RecetteRepository recetteRepository;
    private final DepenseRepository depenseRepository;

    @Override
    public BudgetSummaryDto getBudgetSummary() {
        // Get current year's budget or use default
        int currentYear = Year.now().getValue();
        Budget budget = budgetRepository.findByAnnee(currentYear)
                .orElse(Budget.builder()
                        .annee(currentYear)
                        .montantTotal(BigDecimal.ZERO)
                        .build());

        // Calculate total recettes
        List<Recette> recettes = recetteRepository.findAll();
        BigDecimal totalRecettes = recettes.stream()
                .map(Recette::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate total depenses
        List<Depense> depenses = depenseRepository.findAll();
        BigDecimal totalDepenses = depenses.stream()
                .map(Depense::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate montant disponible = montant_total + total_recettes -
        // total_depenses
        BigDecimal montantTotal = budget.getMontantTotal() != null ? budget.getMontantTotal() : BigDecimal.ZERO;
        BigDecimal montantDisponible = montantTotal.add(totalRecettes).subtract(totalDepenses);

        // Calculate percentage used = (total_depenses / (montant_total +
        // total_recettes)) * 100
        BigDecimal totalBudget = montantTotal.add(totalRecettes);
        double pourcentageUtilise = 0.0;
        if (totalBudget.compareTo(BigDecimal.ZERO) > 0) {
            pourcentageUtilise = totalDepenses
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalBudget, 2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        return BudgetSummaryDto.builder()
                .montantTotal(montantTotal)
                .totalRecettes(totalRecettes)
                .totalDepenses(totalDepenses)
                .montantDisponible(montantDisponible)
                .pourcentageUtilise(pourcentageUtilise)
                .build();
    }
}
