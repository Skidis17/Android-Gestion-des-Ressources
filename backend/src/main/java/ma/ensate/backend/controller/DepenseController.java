package ma.ensate.backend.controller;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Depense;
import ma.ensate.backend.dto.DepenseDto;
import ma.ensate.backend.dto.DepenseRequest;
import ma.ensate.backend.mapper.DepenseMapper;
import ma.ensate.backend.service.DepenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/depenses")
@RequiredArgsConstructor
@Validated
public class DepenseController {

    private final DepenseService depenseService;

    @GetMapping
    public List<DepenseDto> listAll() {
        return depenseService.findAll().stream().map(DepenseMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepenseDto> getById(@PathVariable Long id) {
        Depense d = depenseService.findById(id);
        return ResponseEntity.ok(DepenseMapper.toDto(d));
    }

    @PostMapping
    public ResponseEntity<DepenseDto> create(@Valid @RequestBody DepenseRequest request) {
        Depense created = depenseService.create(DepenseMapper.toEntity(request));
        return ResponseEntity.created(URI.create("/api/v1/depenses/" + created.getId())).body(DepenseMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepenseDto> update(@PathVariable Long id, @Valid @RequestBody DepenseRequest request) {
        Depense updated = depenseService.update(id, DepenseMapper.toEntity(request));
        return ResponseEntity.ok(DepenseMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        depenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-besoin/{besoinId}")
    public List<DepenseDto> byBesoin(@PathVariable Long besoinId) {
        return depenseService.findByBesoinId(besoinId).stream().map(DepenseMapper::toDto).collect(Collectors.toList());
    }
}