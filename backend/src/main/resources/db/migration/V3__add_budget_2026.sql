-- Insert budget row for 2026 if it doesn't exist

INSERT OR IGNORE INTO budget (annee, montant_total, montant_depense, montant_disponible, statut, created_at, updated_at)
VALUES (2026, 500000.00, 0, 0, 'EN_COURS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
