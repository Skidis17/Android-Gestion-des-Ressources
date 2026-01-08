package ma.ensate.backend.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Recette;
import ma.ensate.backend.repository.RecetteRepository;
import ma.ensate.backend.service.RecetteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecetteServiceImpl implements RecetteService {
    private final RecetteRepository recetteRepository;

    @Override
    public List<Recette> findAll() {
        return recetteRepository.findAll();
    }

    @Override
    public Recette findById(Long id) {
        return recetteRepository.findById(id).orElse(null);
    }

    @Override
    public Recette create(Recette recette) {
        recette.setCreatedAt(LocalDateTime.now());
        return recetteRepository.save(recette);
    }
}
