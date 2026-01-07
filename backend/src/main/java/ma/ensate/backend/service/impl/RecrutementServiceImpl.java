package ma.ensate.backend.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Recrutement;
import ma.ensate.backend.exception.ResourceNotFoundException;
import ma.ensate.backend.repository.RecrutementRepository;
import ma.ensate.backend.service.RecrutementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecrutementServiceImpl implements RecrutementService {

    private final RecrutementRepository recrutementRepository;

    @Override
    public List<Recrutement> findAll() {
        return recrutementRepository.findAll();
    }

    @Override
    public Recrutement findById(Long id) {
        return recrutementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recrutement not found: " + id));
    }

    @Override
    @Transactional
    public Recrutement create(Recrutement recrutement) {
        if (recrutement.getNombrePostes() == null) {
            recrutement.setNombrePostes(1);
        }
        if (recrutement.getStatut() == null) {
            recrutement.setStatut("OUVERT");
        }
        if (recrutement.getDateOuverture() == null) {
            recrutement.setDateOuverture(LocalDate.now());
        }
        if (recrutement.getCreatedAt() == null) {
            recrutement.setCreatedAt(LocalDateTime.now());
        }
        return recrutementRepository.save(recrutement);
    }

    @Override
    @Transactional
    public Recrutement update(Long id, Recrutement update) {
        Recrutement existing = findById(id);
        existing.setPoste(update.getPoste());
        existing.setTypeContrat(update.getTypeContrat());
        existing.setDepartement(update.getDepartement());
        existing.setNombrePostes(update.getNombrePostes());
        existing.setDescription(update.getDescription());
        existing.setDateOuverture(update.getDateOuverture());
        existing.setDateCloture(update.getDateCloture());
        existing.setCreatedBy(update.getCreatedBy());
        return recrutementRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Recrutement existing = findById(id);
        recrutementRepository.delete(existing);
    }

    @Override
    @Transactional
    public Recrutement changeStatus(Long id, String statut) {
        Recrutement existing = findById(id);
        existing.setStatut(statut);
        return recrutementRepository.save(existing);
    }
}
