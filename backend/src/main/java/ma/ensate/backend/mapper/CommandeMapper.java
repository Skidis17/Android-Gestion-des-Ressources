package ma.ensate.backend.mapper;

import ma.ensate.backend.domain.Commande;
import ma.ensate.backend.dto.CommandeDto;
import ma.ensate.backend.dto.CommandeRequest;

public class CommandeMapper {
    public static CommandeDto toDto(Commande c) {
        if (c == null) return null;
        return CommandeDto.builder()
                .id(c.getId())
                .besoinId(c.getBesoinId())
                .fournisseur(c.getFournisseur())
                .montantTotal(c.getMontantTotal())
                .dateCommande(c.getDateCommande())
                .dateLivraisonPrevue(c.getDateLivraisonPrevue())
                .dateLivraisonEffective(c.getDateLivraisonEffective())
                .statut(c.getStatut())
                .bonCommandeNumero(c.getBonCommandeNumero())
                .notes(c.getNotes())
                .createdBy(c.getCreatedBy())
                .createdAt(c.getCreatedAt())
                .build();
    }

    public static Commande toEntity(CommandeRequest r) {
        if (r == null) return null;
        return Commande.builder()
                .besoinId(r.getBesoinId())
                .fournisseur(r.getFournisseur())
                .montantTotal(r.getMontantTotal())
                .dateCommande(r.getDateCommande())
                .dateLivraisonPrevue(r.getDateLivraisonPrevue())
                .dateLivraisonEffective(r.getDateLivraisonEffective())
                .statut(r.getStatut())
                .bonCommandeNumero(r.getBonCommandeNumero())
                .notes(r.getNotes())
                .createdBy(r.getCreatedBy())
                .build();
    }
}