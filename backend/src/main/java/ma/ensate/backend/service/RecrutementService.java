package ma.ensate.backend.service;

import ma.ensate.backend.domain.Recrutement;

import java.util.List;

public interface RecrutementService {
    List<Recrutement> findAll();
    Recrutement findById(Long id);
    Recrutement create(Recrutement recrutement);
    Recrutement update(Long id, Recrutement recrutement);
    void delete(Long id);
    Recrutement changeStatus(Long id, String statut);
}
