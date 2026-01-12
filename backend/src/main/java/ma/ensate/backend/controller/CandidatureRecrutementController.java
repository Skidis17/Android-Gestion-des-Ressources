package ma.ensate.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.CandidatureRecrutement;
import ma.ensate.backend.domain.CandidatureScore;
import ma.ensate.backend.domain.Entretien;
import ma.ensate.backend.domain.EntretienScore;
import ma.ensate.backend.dto.CandidatureRecrutementDto;
import ma.ensate.backend.dto.CandidatureRecrutementRequest;
import ma.ensate.backend.dto.CandidatureScoreDto;
import ma.ensate.backend.dto.CandidatureScoreRequest;
import ma.ensate.backend.dto.CandidatureStatusHistoryDto;
import ma.ensate.backend.dto.EntretienDto;
import ma.ensate.backend.dto.EntretienRequest;
import ma.ensate.backend.dto.EntretienScoreDto;
import ma.ensate.backend.dto.EntretienScoreRequest;
import ma.ensate.backend.dto.StatusChangeRequest;
import ma.ensate.backend.mapper.CandidatureRecrutementMapper;
import ma.ensate.backend.mapper.CandidatureScoreMapper;
import ma.ensate.backend.mapper.CandidatureStatusHistoryMapper;
import ma.ensate.backend.mapper.EntretienMapper;
import ma.ensate.backend.mapper.EntretienScoreMapper;
import ma.ensate.backend.service.CandidatureRecrutementService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/candidatures-recrutement")
@RequiredArgsConstructor
@Validated
public class CandidatureRecrutementController {

    private final CandidatureRecrutementService candidatureRecrutementService;

    @GetMapping
    public List<CandidatureRecrutementDto> listAll() {
        return candidatureRecrutementService.findAll().stream()
                .map(CandidatureRecrutementMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidatureRecrutementDto> getById(@PathVariable Long id) {
        CandidatureRecrutement candidature = candidatureRecrutementService.findById(id);
        return ResponseEntity.ok(CandidatureRecrutementMapper.toDto(candidature));
    }

    @PostMapping
    public ResponseEntity<CandidatureRecrutementDto> create(@Valid @RequestBody CandidatureRecrutementRequest request) {
        CandidatureRecrutement created = candidatureRecrutementService.create(CandidatureRecrutementMapper.toEntity(request));
        return ResponseEntity.created(URI.create("/api/v1/candidatures-recrutement/" + created.getId()))
                .body(CandidatureRecrutementMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidatureRecrutementDto> update(@PathVariable Long id,
                                                            @Valid @RequestBody CandidatureRecrutementRequest request) {
        CandidatureRecrutement updated = candidatureRecrutementService.update(id, CandidatureRecrutementMapper.toEntity(request));
        return ResponseEntity.ok(CandidatureRecrutementMapper.toDto(updated));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<CandidatureRecrutementDto> changeStatus(@PathVariable Long id,
                                                                  @RequestParam String statut,
                                                                  @RequestParam(defaultValue = "false") boolean sendEmail) {
        CandidatureRecrutement updated = candidatureRecrutementService.updateStatus(id, statut, sendEmail);
        return ResponseEntity.ok(CandidatureRecrutementMapper.toDto(updated));
    }

    @PostMapping("/{id}/status-detail")
    public ResponseEntity<CandidatureRecrutementDto> changeStatusDetailed(@PathVariable Long id,
                                                                          @Valid @RequestBody StatusChangeRequest request) {
        boolean sendEmail = request.getSendEmail() != null && request.getSendEmail();
        CandidatureRecrutement updated = candidatureRecrutementService.updateStatus(
                id,
                request.getStatut(),
                sendEmail,
                request.getReason(),
                request.getChangedBy()
        );
        return ResponseEntity.ok(CandidatureRecrutementMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        candidatureRecrutementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-recrutement/{recrutementId}")
    public List<CandidatureRecrutementDto> byRecrutement(@PathVariable Long recrutementId) {
        return candidatureRecrutementService.findByRecrutementId(recrutementId).stream()
                .map(CandidatureRecrutementMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/history")
    public List<CandidatureStatusHistoryDto> history(@PathVariable Long id) {
        return candidatureRecrutementService.history(id).stream()
                .map(CandidatureStatusHistoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/scores")
    public List<CandidatureScoreDto> listScores(@PathVariable Long id) {
        return candidatureRecrutementService.listScores(id).stream()
                .map(CandidatureScoreMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/scores")
    public ResponseEntity<CandidatureScoreDto> addScore(@PathVariable Long id,
                                                        @Valid @RequestBody CandidatureScoreRequest request) {
        CandidatureScore saved = candidatureRecrutementService.addScore(id, CandidatureScoreMapper.toEntity(id, request));
        return ResponseEntity.ok(CandidatureScoreMapper.toDto(saved));
    }

    @GetMapping("/{id}/entretiens")
    public List<EntretienDto> listEntretiens(@PathVariable Long id) {
        return candidatureRecrutementService.listEntretiens(id).stream()
                .map(EntretienMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/entretiens")
    public ResponseEntity<EntretienDto> createEntretien(@PathVariable Long id,
                                                        @Valid @RequestBody EntretienRequest request) {
        Entretien created = candidatureRecrutementService.createEntretien(id, EntretienMapper.toEntity(id, request));
        return ResponseEntity.created(URI.create("/api/v1/candidatures-recrutement/" + id + "/entretiens/" + created.getId()))
                .body(EntretienMapper.toDto(created));
    }

    @GetMapping("/entretiens/{entretienId}/scores")
    public List<EntretienScoreDto> listEntretienScores(@PathVariable Long entretienId) {
        return candidatureRecrutementService.listEntretienScores(entretienId).stream()
                .map(EntretienScoreMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/entretiens/{entretienId}/scores")
    public ResponseEntity<EntretienScoreDto> addEntretienScore(@PathVariable Long entretienId,
                                                               @Valid @RequestBody EntretienScoreRequest request) {
        EntretienScore saved = candidatureRecrutementService.addEntretienScore(entretienId, EntretienScoreMapper.toEntity(entretienId, request));
        return ResponseEntity.ok(EntretienScoreMapper.toDto(saved));
    }
}
