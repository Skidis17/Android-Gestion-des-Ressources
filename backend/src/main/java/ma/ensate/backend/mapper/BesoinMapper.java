package ma.ensate.backend.mapper;

import ma.ensate.backend.domain.Besoin;
import ma.ensate.backend.dto.BesoinDto;
import ma.ensate.backend.dto.BesoinRequest;

public class BesoinMapper {
    public static BesoinDto toDto(Besoin b) {
        if (b == null) return null;
        return BesoinDto.builder()
                .id(b.getId())
                .personnelId(b.getPersonnelId())
                .typeBesoin(b.getTypeBesoin())
                .description(b.getDescription())
                .quantite(b.getQuantite())
                .montantEstime(b.getMontantEstime())
                .priorite(b.getPriorite())
                .statut(b.getStatut())
                .commentaireAdmin(b.getCommentaireAdmin())
                .traitePar(b.getTraitePar())
                .dateDemande(b.getDateDemande())
                .dateTraitement(b.getDateTraitement())
                .dateLivraison(b.getDateLivraison())
                .build();
    }

    public static Besoin toEntity(BesoinRequest r) {
        if (r == null) return null;
        return Besoin.builder()
                .personnelId(r.getPersonnelId())
                .typeBesoin(r.getTypeBesoin())
                .description(r.getDescription())
                .quantite(r.getQuantite())
                .montantEstime(r.getMontantEstime())
                .priorite(r.getPriorite())
                .dateLivraison(r.getDateLivraison())
                .build();
    }
}