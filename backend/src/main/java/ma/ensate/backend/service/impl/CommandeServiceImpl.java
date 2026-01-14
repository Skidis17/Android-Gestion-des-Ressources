package ma.ensate.backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.Enum.Role;
import ma.ensate.backend.domain.Besoin;
import ma.ensate.backend.domain.Commande;
import ma.ensate.backend.domain.Depense;
import ma.ensate.backend.domain.User;
import ma.ensate.backend.exception.ResourceNotFoundException;
import ma.ensate.backend.repository.CommandeRepository;
import ma.ensate.backend.service.BesoinService;
import ma.ensate.backend.service.CommandeService;
import ma.ensate.backend.service.DepenseService;

@Service
@RequiredArgsConstructor
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;
    private final BesoinService besoinService;
    private final DepenseService depenseService;

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
    public Commande updateLimited(Long id, String fournisseur, String statut, String notes) {
        Commande existing = findById(id);
        
        String oldStatut = existing.getStatut();
        
        if (fournisseur != null) {
            existing.setFournisseur(fournisseur);
        }
        if (statut != null) {
            existing.setStatut(statut);
            // Automatically set delivery date when marked as LIVRÉ
            if ("LIVRÉ".equals(statut) && !"LIVRÉ".equals(oldStatut)) {
                existing.setDateLivraisonEffective(LocalDate.now());
            }
        }
        if (notes != null) {
            existing.setNotes(notes);
        }
        
        Commande updated = commandeRepository.save(existing);
        
        // Auto-create depense when commande is marked as LIVRÉ
        if (statut != null && "LIVRÉ".equals(statut) && !"LIVRÉ".equals(oldStatut)) {
            createDepenseFromCommande(updated);
        }
        
        return updated;
    }
    
    private void createDepenseFromCommande(Commande commande) {
        // Check if depense already exists for this commande
        List<Depense> existingDepenses = depenseService.findAll().stream()
            .filter(d -> commande.getId().equals(d.getBesoinId()))
            .toList();
        
        if (existingDepenses.isEmpty()) {
            Depense depense = Depense.builder()
                .besoinId(commande.getBesoinId())
                .categorie("Commande")
                .montant(commande.getMontantTotal())
                .dateDepense(commande.getDateLivraisonEffective() != null ? commande.getDateLivraisonEffective() : LocalDate.now())
                .fournisseur(commande.getFournisseur())
                .factureNumero(commande.getBonCommandeNumero())
                .description("Dépense automatique pour commande #" + commande.getId() + (commande.getNotes() != null ? ": " + commande.getNotes() : ""))
                .modePaiement("À définir")
                .enregistrePar(commande.getCreatedBy())
                .createdAt(LocalDateTime.now())
                .build();
            
            depenseService.create(depense);
        }
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

    @Override
    @Transactional
    public Commande createFromBesoin(Long besoinId, java.util.Map<String, String> request, User currentUser) {
        // Validate user role - only Secretaire_general can create commandes
        if (currentUser == null || currentUser.getRole() == null) {
            throw new IllegalStateException("Utilisateur non authentifié");
        }
        
        if (currentUser.getRole() != Role.secretaire_general && currentUser.getRole() != Role.admin) {
            throw new IllegalStateException("Seul le Secrétaire Général peut créer des commandes");
        }
        
        // Get required fournisseur
        String fournisseur = request.get("fournisseur");
        if (fournisseur == null || fournisseur.trim().isEmpty()) {
            throw new IllegalArgumentException("Le fournisseur est obligatoire");
        }
        
        // Get the besoin
        Besoin besoin = besoinService.findById(besoinId);
        
        // Validate besoin status
        if (!"APPROUVÉ".equals(besoin.getStatut())) {
            throw new IllegalStateException("Seuls les besoins approuvés peuvent être transformés en commandes. Statut actuel: " + besoin.getStatut());
        }
        
        // Check if commande already exists for this besoin
        List<Commande> existingCommandes = commandeRepository.findByBesoinId(besoinId);
        if (!existingCommandes.isEmpty()) {
            throw new IllegalStateException("Une commande existe déjà pour ce besoin");
        }
        
        // Parse optional fields
        java.math.BigDecimal montantTotal = besoin.getMontantEstime();
        if (request.containsKey("montantTotal") && !request.get("montantTotal").isEmpty()) {
            try {
                montantTotal = new java.math.BigDecimal(request.get("montantTotal"));
            } catch (NumberFormatException e) {
                // Keep besoin value if parsing fails
            }
        }
        
        LocalDate dateLivraisonPrevue = besoin.getDateLivraison();
        if (request.containsKey("dateLivraisonPrevue") && !request.get("dateLivraisonPrevue").isEmpty()) {
            try {
                dateLivraisonPrevue = LocalDate.parse(request.get("dateLivraisonPrevue"));
            } catch (Exception e) {
                // Keep besoin value if parsing fails
            }
        }
        
        String bonCommandeNumero = request.getOrDefault("bonCommandeNumero", null);
        String notes = request.getOrDefault("notes", "Commande " + besoin.getDescription());
        
        // Create commande from besoin
        Commande commande = Commande.builder()
                .besoinId(besoinId)
                .fournisseur(fournisseur.trim())
                .montantTotal(montantTotal)
                .dateCommande(LocalDate.now())
                .dateLivraisonPrevue(dateLivraisonPrevue)
                .bonCommandeNumero(bonCommandeNumero)
                .statut("EN_COURS")
                .notes(notes)
                .createdBy(currentUser.getPersonnelId())
                .createdAt(LocalDateTime.now())
                .build();
        
        Commande saved = commandeRepository.save(commande);
        
        // Mark besoin as transmitted
        besoinService.markAsTransmitted(besoin.getId());
        
        return saved;
    }
}