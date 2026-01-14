-- Update besoin status values to use French accents
-- ACCEPTE → VALIDÉ
-- REFUSE → REFUSÉ  
-- APPROUVE → APPROUVÉ

UPDATE besoins SET statut = 'VALIDÉ' WHERE statut = 'ACCEPTE';
UPDATE besoins SET statut = 'REFUSÉ' WHERE statut = 'REFUSE';
UPDATE besoins SET statut = 'APPROUVÉ' WHERE statut = 'APPROUVE';
