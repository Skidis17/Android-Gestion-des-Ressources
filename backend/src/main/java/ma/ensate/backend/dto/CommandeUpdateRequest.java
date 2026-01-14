package ma.ensate.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeUpdateRequest {
    private String fournisseur;
    
    @NotNull
    private String statut;
    
    private String notes;
}
