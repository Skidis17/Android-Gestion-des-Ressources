package ma.ensate.backend.service;

import java.util.List;

import ma.ensate.backend.domain.Besoin;

public interface BesoinService {
    List<Besoin> findAll();
    Besoin findById(Long id);
    Besoin create(Besoin besoin);
    Besoin update(Long id, Besoin besoin);
    void delete(Long id);
    Besoin changeStatus(Long id, String statut, Long traitePar, String commentaireAdmin);
    List<Besoin> findByPersonnelId(Long personnelId);
}