package ma.ensate.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Demande;
import ma.ensate.backend.domain.DemandeStatut;
import ma.ensate.backend.dto.DemandeDto;
import ma.ensate.backend.dto.DemandeRequest;
import ma.ensate.backend.mapper.DemandeMapper;
import ma.ensate.backend.service.DemandeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/demandes")
@RequiredArgsConstructor
public class DemandeController {

    private final DemandeService demandeService;

    @PostMapping
    public ResponseEntity<DemandeDto> create(@Valid @RequestBody DemandeRequest request) {
        Demande created = demandeService.create(DemandeMapper.toEntity(request));
        return ResponseEntity.created(URI.create("/api/v1/demandes/" + created.getId()))
                .body(DemandeMapper.toDto(created));
    }

    @GetMapping
    public List<DemandeDto> list(@RequestParam(required = false) String statut) {
        List<Demande> demandes = (statut == null || statut.isBlank())
                ? demandeService.findAll()
                : demandeService.findByStatut(DemandeStatut.valueOf(statut));
        return demandes.stream().map(DemandeMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandeDto> getById(@PathVariable Long id) {
        Demande demande = demandeService.findById(id);
        return ResponseEntity.ok(DemandeMapper.toDto(demande));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<DemandeDto> updateStatus(@PathVariable Long id, @RequestParam String statut) {
        Demande updated = demandeService.updateStatus(id, DemandeStatut.valueOf(statut));
        return ResponseEntity.ok(DemandeMapper.toDto(updated));
    }
}
