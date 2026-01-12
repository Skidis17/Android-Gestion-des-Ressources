package ma.ensate.backend.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Recrutement;
import ma.ensate.backend.domain.CandidatureRecrutement;
import ma.ensate.backend.domain.Entretien;
import ma.ensate.backend.dto.CandidatureRankingDto;
import ma.ensate.backend.dto.RecrutementPipelineDto;
import ma.ensate.backend.exception.ResourceNotFoundException;
import ma.ensate.backend.repository.CandidatureRecrutementRepository;
import ma.ensate.backend.repository.EntretienRepository;
import ma.ensate.backend.repository.RecrutementRepository;
import ma.ensate.backend.service.RecrutementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecrutementServiceImpl implements RecrutementService {

    private final RecrutementRepository recrutementRepository;
    private final CandidatureRecrutementRepository candidatureRecrutementRepository;
    private final EntretienRepository entretienRepository;

    @Override
    public List<Recrutement> findAll() {
        return recrutementRepository.findAll();
    }

    @Override
    public Recrutement findById(Long id) {
        return recrutementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recrutement not found: " + id));
    }

    @Override
    @Transactional
    public Recrutement create(Recrutement recrutement) {
        if (recrutement.getNombrePostes() == null) {
            recrutement.setNombrePostes(1);
        }
        if (recrutement.getStatut() == null) {
            recrutement.setStatut("OUVERT");
        }
        if (recrutement.getDateOuverture() == null) {
            recrutement.setDateOuverture(LocalDate.now());
        }
        if (recrutement.getCreatedAt() == null) {
            recrutement.setCreatedAt(LocalDateTime.now());
        }
        return recrutementRepository.save(recrutement);
    }

    @Override
    @Transactional
    public Recrutement update(Long id, Recrutement update) {
        Recrutement existing = findById(id);
        existing.setPoste(update.getPoste());
        existing.setTypeContrat(update.getTypeContrat());
        existing.setDepartement(update.getDepartement());
        existing.setNombrePostes(update.getNombrePostes());
        existing.setDescription(update.getDescription());
        existing.setDateOuverture(update.getDateOuverture());
        existing.setDateCloture(update.getDateCloture());
        existing.setCreatedBy(update.getCreatedBy());
        return recrutementRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Recrutement existing = findById(id);
        recrutementRepository.delete(existing);
    }

    @Override
    @Transactional
    public Recrutement changeStatus(Long id, String statut) {
        Recrutement existing = findById(id);
        existing.setStatut(statut);
        return recrutementRepository.save(existing);
    }

    @Override
    public RecrutementPipelineDto pipeline(Long id) {
        findById(id);
        List<CandidatureRecrutement> candidatures = candidatureRecrutementRepository.findByRecrutementId(id);
        Map<String, Long> counts = candidatures.stream()
                .collect(Collectors.groupingBy(c -> normalizeStatus(c.getStatut()), Collectors.counting()));

        return RecrutementPipelineDto.builder()
                .recrutementId(id)
                .total(candidatures.size())
                .enAttente(counts.getOrDefault("EN_ATTENTE", 0L).intValue())
                .preselection(counts.getOrDefault("PRESELECTION", 0L).intValue())
                .test(counts.getOrDefault("TEST", 0L).intValue())
                .entretien(counts.getOrDefault("ENTRETIEN", 0L).intValue())
                .retenu(counts.getOrDefault("RETENU", 0L).intValue())
                .refuse(counts.getOrDefault("REFUSE", 0L).intValue())
                .build();
    }

    @Override
    public List<CandidatureRankingDto> rankings(Long id) {
        findById(id);
        List<CandidatureRecrutement> candidatures = candidatureRecrutementRepository.findByRecrutementId(id);
        return candidatures.stream()
                .map(c -> {
                    BigDecimal interviewScore = averageInterviewScore(c.getId());
                    BigDecimal total = sumScores(c.getScoreEcrit(), c.getScoreOral(), interviewScore);
                    return CandidatureRankingDto.builder()
                            .candidatureId(c.getId())
                            .nom(c.getNom())
                            .prenom(c.getPrenom())
                            .email(c.getEmail())
                            .statut(c.getStatut())
                            .scoreEcrit(c.getScoreEcrit())
                            .scoreOral(c.getScoreOral())
                            .interviewScore(interviewScore)
                            .totalScore(total)
                            .build();
                })
                .sorted((a, b) -> compareScores(b.getTotalScore(), a.getTotalScore()))
                .collect(Collectors.toList());
    }

    private String normalizeStatus(String statut) {
        if (statut == null) return "EN_ATTENTE";
        String s = statut.trim().toUpperCase();
        return switch (s) {
            case "EN_ATTENTE", "PRESELECTION", "TEST", "ENTRETIEN", "RETENU", "REFUSE" -> s;
            default -> s;
        };
    }

    private BigDecimal averageInterviewScore(Long candidatureId) {
        List<Entretien> entretiens = entretienRepository.findByCandidatureId(candidatureId);
        if (entretiens == null || entretiens.isEmpty()) return null;
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        for (Entretien e : entretiens) {
            if (e.getScoreTotal() != null) {
                sum = sum.add(e.getScoreTotal());
                count++;
            }
        }
        if (count == 0) return null;
        return sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal sumScores(BigDecimal... scores) {
        BigDecimal sum = BigDecimal.ZERO;
        boolean has = false;
        for (BigDecimal s : scores) {
            if (s != null) {
                sum = sum.add(s);
                has = true;
            }
        }
        return has ? sum : null;
    }

    private int compareScores(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) return 0;
        if (a == null) return 1;
        if (b == null) return -1;
        return a.compareTo(b);
    }
}
