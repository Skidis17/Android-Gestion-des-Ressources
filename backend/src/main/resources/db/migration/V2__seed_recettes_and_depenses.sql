-- Seed sample recettes and depenses for development/testing

INSERT INTO recettes (source, categorie, montant, date_recette, description, reference_document, enregistre_par)
VALUES
('Subvention Etat', 'Subvention', 120000.00, '2026-01-05', 'Affectation initiale', 'SUBV-2026-01', 2),
('Formation Continue', 'Formation Continue', 15000.00, '2026-01-10', 'Formation externe', 'FC-2026-01', 2);

INSERT INTO depenses (categorie, montant, date_depense, fournisseur, description, facture_numero)
VALUES
('Achat Matériel', 30000.00, '2026-01-12', 'Fournisseur A', 'Ordinateurs', 'FAC-1001'),
('Frais Divers', 5000.00, '2026-01-15', 'Fournisseur B', 'Frais divers', 'FAC-1002');

-- Update budget row for 2026 so montant_disponible is computed dynamically by the controller
UPDATE budget
SET montant_depense = (SELECT COALESCE(SUM(montant),0) FROM depenses WHERE strftime('%Y', date_depense) = '2026'),
    montant_disponible = NULL
WHERE annee = 2026;
