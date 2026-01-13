package ma.ensate.backend.service;

import java.util.List;

import ma.ensate.backend.domain.Commande;
import ma.ensate.backend.domain.User;

public interface CommandeService {
    List<Commande> findAll();
    Commande findById(Long id);
    Commande create(Commande commande);
    Commande update(Long id, Commande commande);
    Commande updateLimited(Long id, String fournisseur, String statut, String notes);
    void delete(Long id);
    List<Commande> findByBesoinId(Long besoinId);
    Commande createFromBesoin(Long besoinId, java.util.Map<String, String> request, User currentUser);
}