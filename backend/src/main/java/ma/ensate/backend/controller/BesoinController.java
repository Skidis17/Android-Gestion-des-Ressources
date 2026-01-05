package ma.ensate.backend.controller;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Besoin;
import ma.ensate.backend.dto.BesoinDto;
import ma.ensate.backend.dto.BesoinRequest;
import ma.ensate.backend.mapper.BesoinMapper;
import ma.ensate.backend.service.BesoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/besoins")
@RequiredArgsConstructor
@Validated
public class BesoinController {

    private final BesoinService besoinService;

    @GetMapping
    public List<BesoinDto> listAll() {
        return besoinService.findAll().stream().map(BesoinMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BesoinDto> getById(@PathVariable Long id) {
        Besoin b = besoinService.findById(id);
        return ResponseEntity.ok(BesoinMapper.toDto(b));
    }

    @PostMapping
    public ResponseEntity<BesoinDto> create(@Valid @RequestBody BesoinRequest request) {
        Besoin created = besoinService.create(BesoinMapper.toEntity(request));
        return ResponseEntity.created(URI.create("/api/v1/besoins/" + created.getId())).body(BesoinMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BesoinDto> update(@PathVariable Long id, @Valid @RequestBody BesoinRequest request) {
        Besoin updated = besoinService.update(id, BesoinMapper.toEntity(request));
        return ResponseEntity.ok(BesoinMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        besoinService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<BesoinDto> changeStatus(@PathVariable Long id,
                                                  @RequestParam String statut,
                                                  @RequestParam(required = false) Long traitePar,
                                                  @RequestParam(required = false) String commentaire) {
        Besoin updated = besoinService.changeStatus(id, statut, traitePar, commentaire);
        return ResponseEntity.ok(BesoinMapper.toDto(updated));
    }
}