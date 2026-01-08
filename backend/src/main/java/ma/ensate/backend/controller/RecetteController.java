package ma.ensate.backend.controller;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.dto.RecetteDto;
import ma.ensate.backend.dto.RecetteRequest;
import ma.ensate.backend.mapper.RecetteMapper;
import ma.ensate.backend.domain.Recette;
import ma.ensate.backend.service.RecetteService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/recettes")
@RequiredArgsConstructor
@Validated
public class RecetteController {

    private final RecetteService recetteService;

    @GetMapping
    public List<RecetteDto> listAll() {
        return recetteService.findAll().stream().map(RecetteMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<RecetteDto> create(@Valid @RequestBody RecetteRequest request) {
        Recette created = recetteService.create(RecetteMapper.toEntity(request));
        return ResponseEntity.created(URI.create("/api/v1/recettes/" + created.getId()))
                .body(RecetteMapper.toDto(created));
    }
}
