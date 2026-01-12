package ma.ensate.backend.service;

import ma.ensate.backend.domain.Recrutement;
import ma.ensate.backend.dto.CandidatureRankingDto;
import ma.ensate.backend.dto.RecrutementPipelineDto;
import ma.ensate.backend.dto.RecrutementStatsDto;

import java.util.List;

public interface RecrutementService {
    List<Recrutement> findAll();
    Recrutement findById(Long id);
    Recrutement create(Recrutement recrutement);
    Recrutement update(Long id, Recrutement recrutement);
    void delete(Long id);
    Recrutement changeStatus(Long id, String statut);
    RecrutementPipelineDto pipeline(Long id);
    List<CandidatureRankingDto> rankings(Long id);
    RecrutementStatsDto stats();
}
