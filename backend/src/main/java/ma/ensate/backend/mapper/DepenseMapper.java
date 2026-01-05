package ma.ensate.backend.mapper;

import ma.ensate.backend.domain.Depense;
import ma.ensate.backend.dto.DepenseDto;
import ma.ensate.backend.dto.DepenseRequest;

public class DepenseMapper {
    public static DepenseDto toDto(Depense d) {
        if (d == null) return null;
        return DepenseDto.builder()
                .id(d.getId())
                .besoinId(d.getBesoinId())
                .categorie(d.getCategorie())
                .montant(d.getMontant())
                .dateDepense(d.getDateDepense())
                .fournisseur(d.getFournisseur())
                .factureNumero(d.getFactureNumero())
                .description(d.getDescription())
                .modePaiement(d.getModePaiement())
                .enregistrePar(d.getEnregistrePar())
                .createdAt(d.getCreatedAt())
                .build();
    }

    public static Depense toEntity(DepenseRequest r) {
        if (r == null) return null;
        return Depense.builder()
                .besoinId(r.getBesoinId())
                .categorie(r.getCategorie())
                .montant(r.getMontant())
                .dateDepense(r.getDateDepense())
                .fournisseur(r.getFournisseur())
                .factureNumero(r.getFactureNumero())
                .description(r.getDescription())
                .modePaiement(r.getModePaiement())
                .enregistrePar(r.getEnregistrePar())
                .build();
    }
}