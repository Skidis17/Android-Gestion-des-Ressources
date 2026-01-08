package ma.ensate.backend.service;

import ma.ensate.backend.domain.Recette;

import java.util.List;

public interface RecetteService {
    List<Recette> findAll();

    Recette findById(Long id);

    Recette create(Recette recette);
}
