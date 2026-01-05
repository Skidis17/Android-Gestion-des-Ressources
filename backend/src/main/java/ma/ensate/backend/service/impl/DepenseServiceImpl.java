package ma.ensate.backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.ensate.backend.domain.Depense;
import ma.ensate.backend.exception.ResourceNotFoundException;
import ma.ensate.backend.repository.DepenseRepository;
import ma.ensate.backend.service.DepenseService;

@Service
@RequiredArgsConstructor
public class DepenseServiceImpl implements DepenseService {

    private final DepenseRepository depenseRepository;

    @Override
    public List<Depense> findAll() {
        return depenseRepository.findAll();
    }

    @Override
    public Depense findById(Long id) {
        return depenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Depense not found: " + id));
    }

    @Override
    @Transactional
    public Depense create(Depense depense) {
        if (depense.getCreatedAt() == null) {
            depense.setCreatedAt(LocalDateTime.now());
        }
        return depenseRepository.save(depense);
    }

    @Override
    @Transactional
    public Depense update(Long id, Depense update) {
        Depense existing = findById(id);
        existing.setCategorie(update.getCategorie());
        existing.setMontant(update.getMontant());
        existing.setDateDepense(update.getDateDepense());
        existing.setFournisseur(update.getFournisseur());
        existing.setFactureNumero(update.getFactureNumero());
        existing.setDescription(update.getDescription());
        existing.setModePaiement(update.getModePaiement());
        existing.setEnregistrePar(update.getEnregistrePar());
        return depenseRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Depense existing = findById(id);
        depenseRepository.delete(existing);
    }

    @Override
    public List<Depense> findByBesoinId(Long besoinId) {
        return depenseRepository.findByBesoinId(besoinId);
    }
}