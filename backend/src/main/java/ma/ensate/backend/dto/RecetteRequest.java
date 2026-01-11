package ma.ensate.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RecetteRequest {
    @NotBlank
    private String source;

    private String categorie;

    @NotNull
    private BigDecimal montant;

    @NotBlank
    private String date; // accept various formats (yyyy-MM-dd, dd/MM/yyyy)

    private String description;

    private String reference;

    private Long enregistrePar;
}
