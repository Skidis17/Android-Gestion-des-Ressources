package ma.ensate.backend.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensate.backend.Enum.Role;
import ma.ensate.backend.domain.Commande;
import ma.ensate.backend.domain.User;
import ma.ensate.backend.dto.CommandeDto;
import ma.ensate.backend.dto.CommandeRequest;
import ma.ensate.backend.mapper.CommandeMapper;
import ma.ensate.backend.service.CommandeService;
import ma.ensate.backend.service.UserService;

@RestController
@RequestMapping("/api/v1/commandes")
@RequiredArgsConstructor
@Validated
public class CommandeController {

    private final CommandeService commandeService;
    private final UserService userService;

    private boolean hasCommandeAccess(User user) {
        if (user == null || user.getRole() == null) return false;
        Role role = user.getRole();
        return role == Role.secretaire_general || role == Role.Directeur_adjoint || 
               role == Role.directeur || role == Role.admin;
    }

    @GetMapping
    public ResponseEntity<List<CommandeDto>> listAll() {
        User currentUser = userService.getCurrentUser();
        if (!hasCommandeAccess(currentUser)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(commandeService.findAll().stream().map(CommandeMapper::toDto).collect(Collectors.toList()));
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
        User currentUser = userService.getCurrentUser();
        if (!hasCommandeAccess(currentUser)) {
            return ResponseEntity.status(403).build();
        }
        Commande updated = commandeService.update(id, CommandeMapper.toEntity(request));
        return ResponseEntity.ok(CommandeMapper.toDto(updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommandeDto> updateLimited(@PathVariable Long id, @Valid @RequestBody ma.ensate.backend.dto.CommandeUpdateRequest request) {
        User currentUser = userService.getCurrentUser();
        if (!hasCommandeAccess(currentUser)) {
            return ResponseEntity.status(403).build();
        }
        Commande updated = commandeService.updateLimited(id, request.getFournisseur(), request.getStatut(), request.getNotes());
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

    @PostMapping("/from-besoin/{besoinId}")
    public ResponseEntity<CommandeDto> createFromBesoin(@PathVariable Long besoinId, @RequestBody java.util.Map<String, String> request) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }
        
        Commande created = commandeService.createFromBesoin(besoinId, request, currentUser);
        return ResponseEntity.created(URI.create("/api/v1/commandes/" + created.getId())).body(CommandeMapper.toDto(created));
    }
}