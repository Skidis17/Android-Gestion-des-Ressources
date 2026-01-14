package ma.ensate.backend.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Demande;
import ma.ensate.backend.domain.DemandeStatut;
import ma.ensate.backend.exception.ResourceNotFoundException;
import ma.ensate.backend.repository.DemandeRepository;
import ma.ensate.backend.service.DemandeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandeServiceImpl implements DemandeService {
    private final DemandeRepository repository;

    @Override
    @Transactional
    public Demande create(Demande demande) {
        if (demande.getStatut() == null) {
            demande.setStatut(DemandeStatut.EN_ATTENTE);
        }
        if (demande.getCreatedAt() == null) {
            demande.setCreatedAt(LocalDateTime.now());
        }
        return repository.save(demande);
    }

    @Override
    public List<Demande> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Demande> findByStatut(DemandeStatut statut) {
        return repository.findByStatut(statut);
    }

    @Override
    public Demande findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found: " + id));
    }

    @Override
    @Transactional
    public Demande updateStatus(Long id, DemandeStatut statut) {
        Demande demande = findById(id);
        demande.setStatut(statut);
        return repository.save(demande);
    }
}
