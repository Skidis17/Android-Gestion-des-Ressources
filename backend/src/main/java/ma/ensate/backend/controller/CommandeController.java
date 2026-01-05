package ma.ensate.backend.controller;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Commande;
import ma.ensate.backend.dto.CommandeDto;
import ma.ensate.backend.dto.CommandeRequest;
import ma.ensate.backend.mapper.CommandeMapper;
import ma.ensate.backend.service.CommandeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/commandes")
@RequiredArgsConstructor
@Validated
public class CommandeController {

    private final CommandeService commandeService;

    @GetMapping
    public List<CommandeDto> listAll() {
        return commandeService.findAll().stream().map(CommandeMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDto> getById(@PathVariable Long id) {
        Commande c = commandeService.findById(id);
        return ResponseEntity.ok(CommandeMapper.toDto(c));
    }

    @PostMapping
    public ResponseEntity<CommandeDto> create(@Valid @RequestBody CommandeRequest request) {
        Commande created = commandeService.create(CommandeMapper.toEntity(request));
        return ResponseEntity.created(URI.create("/api/v1/commandes/" + created.getId())).body(CommandeMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeDto> update(@PathVariable Long id, @Valid @RequestBody CommandeRequest request) {
        Commande updated = commandeService.update(id, CommandeMapper.toEntity(request));
        return ResponseEntity.ok(CommandeMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commandeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-besoin/{besoinId}")
    public List<CommandeDto> byBesoin(@PathVariable Long besoinId) {
        return commandeService.findByBesoinId(besoinId).stream().map(CommandeMapper::toDto).collect(Collectors.toList());
    }
}