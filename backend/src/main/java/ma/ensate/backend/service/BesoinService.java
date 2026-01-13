package ma.ensate.backend.service;

import java.util.List;

import ma.ensate.backend.domain.Besoin;
import ma.ensate.backend.domain.User;

public interface BesoinService {
    List<Besoin> findAll();
    Besoin findById(Long id);
    Besoin create(Besoin besoin);
    Besoin update(Long id, Besoin besoin);
    void delete(Long id);
    Besoin changeStatus(Long id, String statut, Long traitePar, String commentaireAdmin);
    Besoin changeStatusWithUser(Long id, String statut, User currentUser, String commentaireAdmin);
    List<Besoin> findByPersonnelId(Long personnelId);
    Besoin markAsTransmitted(Long id);
}