package ma.ensate.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.CandidatureRecrutement;
import ma.ensate.backend.dto.CandidatureRecrutementDto;
import ma.ensate.backend.dto.CandidatureRecrutementRequest;
import ma.ensate.backend.mapper.CandidatureRecrutementMapper;
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
}
