-- Add users for administrative roles
INSERT OR IGNORE INTO personnel (
    cin, nom, prenom, email, telephone, type_personnel, grade, echelon, departement
) VALUES
    ('FF112233', 'Bennani', 'Karim', 'karim.bennani@ensate.ma', '0644444444', 'ADMINISTRATIF', 'Directeur Adjoint', 'E5', 'Direction'),
    ('GG445566', 'Alaoui', 'Fatima', 'fatima.alaoui@ensate.ma', '0655555555', 'ADMINISTRATIF', 'Secrétaire Général', 'E4', 'Administration');


INSERT INTO besoins (personnel_id, type_besoin, description, quantite, montant_estime, priorite, statut, date_demande)
VALUES
(3, 'MATERIEL', 'Ordinateurs portables pour le département GI', 10, 50000.00, 'HAUTE', 'APPROUVÉ', '2026-01-05 10:30:00.000'),
(3, 'LOGICIEL', 'Licences Visual Studio Professional', 15, 15000.00, 'MOYENNE', 'APPROUVÉ', '2026-01-06 14:20:00.000'),
(3, 'MATERIEL', 'Projecteurs pour salles de cours', 3, 12000.00, 'MOYENNE', 'APPROUVÉ', '2026-01-07 09:15:00.000'),
(1, 'FOURNITURE', 'Papier A4 et fournitures de bureau', 50, 2500.00, 'BASSE', 'APPROUVÉ', '2026-01-08 11:00:00.000');


INSERT INTO commandes (besoin_id, fournisseur, montant_total, date_commande, date_livraison_prevue, date_livraison_effective, statut, bon_commande_numero, notes, created_by)
VALUES
(1, 'TechnoMaroc SARL', 48500.00, '2026-01-06 08:00:00.000', '2026-01-20 10:00:00.000', NULL, 'EN_COURS', 'BC-2026-001', 'Commande de 10 ordinateurs HP EliteBook', 2),
(2, 'Microsoft Maroc', 14800.00, '2026-01-07 09:30:00.000', '2026-01-15 12:00:00.000', '2026-01-14 16:45:00.000', 'LIVRÉ', 'BC-2026-002', 'Licences numériques - livraison par email', 2),
(3, 'Épson Maroc', 11700.00, '2026-01-08 10:15:00.000', '2026-01-25 14:00:00.000', NULL, 'EN_COURS', 'BC-2026-003', 'Projecteurs Épson EB-X05', 2),
(4, 'Bureau Plus', 2350.00, '2026-01-09 11:00:00.000', '2026-01-12 15:00:00.000', '2026-01-11 13:30:00.000', 'LIVRÉ', 'BC-2026-004', 'Fournitures de bureau diverses', 2);


INSERT INTO depenses (besoin_id, categorie, montant, date_depense, fournisseur, facture_numero, description, mode_paiement, enregistre_par)
VALUES
(NULL, 'MAINTENANCE', 3500.00, '2026-01-10 09:30:00.000', 'Électro Services', 'FACT-ES-2026-0089', 'Réparation système de climatisation - Bâtiment A', 'VIREMENT', 2),
(NULL, 'ENERGIE', 8200.00, '2026-01-09 08:00:00.000', 'ONEE', 'FACT-ONEE-12-2025', 'Facture électricité - Décembre 2025', 'PRELEVEMENT', 2),
(NULL, 'MAINTENANCE', 1500.00, '2026-01-08 15:30:00.000', 'IT Solutions', 'FACT-ITS-2026-0023', 'Maintenance réseau informatique - Intervention mensuelle', 'CHEQUE', 2);

