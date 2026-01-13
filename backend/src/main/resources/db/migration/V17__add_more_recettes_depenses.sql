-- Add more recettes and depenses for better visualization in reports

INSERT INTO recettes (source, categorie, montant, date_recette, description, reference_document, enregistre_par)
VALUES
('Partenariat Entreprise X', 'Partenariat', 45000.00, '2026-01-08', 'Contrat partenariat annuel', 'PART-2026-01', 2),
('Vente Matériel Ancien', 'Vente Actifs', 8500.00, '2026-01-11', 'Liquidation équipement obsolète', 'VMA-2026-01', 2),
('Projet Recherche', 'Subvention', 62000.00, '2026-01-14', 'Financement projet innovation', 'PROJ-2026-01', 2),
('Formation Certifiante', 'Formation Continue', 28000.00, '2026-01-16', 'Programme certification professionnelle', 'FC-2026-02', 2),
('Don Philanthropique', 'Don', 15000.00, '2026-01-18', 'Don anonyme pour équipement', 'DON-2026-01', 2);

INSERT INTO depenses (categorie, montant, date_depense, fournisseur, description, facture_numero)
VALUES
('Achat Matériel', 85000.00, '2026-01-09', 'Tech Solutions SARL', 'Serveurs et équipement réseau', 'FAC-1003'),
('Salaires', 120000.00, '2026-01-20', 'RH Interne', 'Salaires janvier 2026', 'SAL-2026-01'),
('Formation Personnel', 22000.00, '2026-01-13', 'Centre Formation Pro', 'Formation développement compétences', 'FAC-1004'),
('Maintenance', 18000.00, '2026-01-17', 'Maintenance Plus', 'Contrat maintenance annuel', 'FAC-1005'),
('Fournitures Bureau', 6500.00, '2026-01-19', 'Office Supplies Co', 'Fournitures trimestrielles', 'FAC-1006'),
('Services Externes', 32000.00, '2026-01-21', 'Consulting Group', 'Audit et conseil stratégique', 'FAC-1007'),
('Frais Divers', 12000.00, '2026-01-22', 'Divers Fournisseurs', 'Dépenses administratives', 'FAC-1008');

-- Update budget for 2026
UPDATE budget
SET montant_depense = (SELECT COALESCE(SUM(montant),0) FROM depenses WHERE strftime('%Y', date_depense) = '2026'),
    montant_disponible = NULL
WHERE annee = 2026;
