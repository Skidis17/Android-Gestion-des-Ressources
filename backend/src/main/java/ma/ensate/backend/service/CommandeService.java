package ma.ensate.backend.service;

import java.util.List;

import ma.ensate.backend.domain.Commande;

public interface CommandeService {
    List<Commande> findAll();
    Commande findById(Long id);
    Commande create(Commande commande);
    Commande update(Long id, Commande commande);
    void delete(Long id);
    List<Commande> findByBesoinId(Long besoinId);
}