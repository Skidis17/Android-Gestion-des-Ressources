package ma.ensate.backend.service;

import java.util.List;

import ma.ensate.backend.domain.Depense;

public interface DepenseService {
    List<Depense> findAll();
    Depense findById(Long id);
    Depense create(Depense depense);
    Depense update(Long id, Depense depense);
    void delete(Long id);
    List<Depense> findByBesoinId(Long besoinId);
}