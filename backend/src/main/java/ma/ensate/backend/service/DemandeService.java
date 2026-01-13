package ma.ensate.backend.service;

import ma.ensate.backend.domain.Demande;
import ma.ensate.backend.domain.DemandeStatut;

import java.util.List;

public interface DemandeService {
    Demande create(Demande demande);
    List<Demande> findAll();
    List<Demande> findByStatut(DemandeStatut statut);
    Demande findById(Long id);
    Demande updateStatus(Long id, DemandeStatut statut);
}
