package ma.ensate.backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Commande;
import ma.ensate.backend.exception.ResourceNotFoundException;
import ma.ensate.backend.repository.CommandeRepository;
import ma.ensate.backend.service.CommandeService;

@Service
@RequiredArgsConstructor
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;

    @Override
    public List<Commande> findAll() {
        return commandeRepository.findAll();
    }

    @Override
    public Commande findById(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande not found: " + id));
    }

    @Override
    @Transactional
    public Commande create(Commande commande) {
        if (commande.getCreatedAt() == null) {
            commande.setCreatedAt(LocalDateTime.now());
        }
        return commandeRepository.save(commande);
    }

    @Override
    @Transactional
    public Commande update(Long id, Commande update) {
        Commande existing = findById(id);
        existing.setFournisseur(update.getFournisseur());
        existing.setMontantTotal(update.getMontantTotal());
        existing.setDateCommande(update.getDateCommande());
        existing.setDateLivraisonPrevue(update.getDateLivraisonPrevue());
        existing.setDateLivraisonEffective(update.getDateLivraisonEffective());
        existing.setStatut(update.getStatut());
        existing.setBonCommandeNumero(update.getBonCommandeNumero());
        existing.setNotes(update.getNotes());
        existing.setCreatedBy(update.getCreatedBy());
        return commandeRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Commande existing = findById(id);
        commandeRepository.delete(existing);
    }

    @Override
    public List<Commande> findByBesoinId(Long besoinId) {
        return commandeRepository.findByBesoinId(besoinId);
    }
}