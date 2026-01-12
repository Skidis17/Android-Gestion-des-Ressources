package ma.ensate.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecrutementPipelineDto {
    private Long recrutementId;
    private int total;
    private int enAttente;
    private int preselection;
    private int test;
    private int entretien;
    private int retenu;
    private int refuse;
}
