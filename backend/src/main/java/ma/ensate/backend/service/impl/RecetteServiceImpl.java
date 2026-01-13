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
        return recetteRepository.findAllByOrderByIdDesc();
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

    @Override
    public Recette update(Long id, Recette recette) {
        Recette existing = recetteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recette not found with id: " + id));

        existing.setSource(recette.getSource());
        existing.setCategorie(recette.getCategorie());
        existing.setMontant(recette.getMontant());
        existing.setDateRecette(recette.getDateRecette());
        existing.setDescription(recette.getDescription());
        existing.setReferenceDocument(recette.getReferenceDocument());

        return recetteRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!recetteRepository.existsById(id)) {
            throw new RuntimeException("Recette not found with id: " + id);
        }
        recetteRepository.deleteById(id);
    }
}
