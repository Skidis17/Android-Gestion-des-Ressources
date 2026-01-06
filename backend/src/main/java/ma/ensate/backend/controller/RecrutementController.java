package ma.ensate.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Recrutement;
import ma.ensate.backend.dto.CandidatureRecrutementDto;
import ma.ensate.backend.dto.RecrutementDto;
import ma.ensate.backend.dto.RecrutementRequest;
import ma.ensate.backend.mapper.CandidatureRecrutementMapper;
import ma.ensate.backend.mapper.RecrutementMapper;
import ma.ensate.backend.service.CandidatureRecrutementService;
import ma.ensate.backend.service.RecrutementService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/recrutements")
@RequiredArgsConstructor
@Validated
public class RecrutementController {

    private final RecrutementService recrutementService;
    private final CandidatureRecrutementService candidatureRecrutementService;

    @GetMapping
    public List<RecrutementDto> listAll() {
        return recrutementService.findAll().stream().map(RecrutementMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecrutementDto> getById(@PathVariable Long id) {
        Recrutement recrutement = recrutementService.findById(id);
        return ResponseEntity.ok(RecrutementMapper.toDto(recrutement));
    }

    @PostMapping
    public ResponseEntity<RecrutementDto> create(@Valid @RequestBody RecrutementRequest request) {
        Recrutement created = recrutementService.create(RecrutementMapper.toEntity(request));
        return ResponseEntity.created(URI.create("/api/v1/recrutements/" + created.getId()))
                .body(RecrutementMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecrutementDto> update(@PathVariable Long id, @Valid @RequestBody RecrutementRequest request) {
        Recrutement updated = recrutementService.update(id, RecrutementMapper.toEntity(request));
        return ResponseEntity.ok(RecrutementMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recrutementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<RecrutementDto> changeStatus(@PathVariable Long id, @RequestParam String statut) {
        Recrutement updated = recrutementService.changeStatus(id, statut);
        return ResponseEntity.ok(RecrutementMapper.toDto(updated));
    }

    @GetMapping("/{id}/candidatures")
    public List<CandidatureRecrutementDto> listCandidatures(@PathVariable Long id) {
        return candidatureRecrutementService.findByRecrutementId(id).stream()
                .map(CandidatureRecrutementMapper::toDto)
                .collect(Collectors.toList());
    }
}
