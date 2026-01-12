-- Additional migration for recrutement module (indexes + sample data)

-- Indexes to speed up common lookups
CREATE INDEX IF NOT EXISTS idx_recrutements_statut ON recrutements(statut);
CREATE INDEX IF NOT EXISTS idx_recrutements_departement ON recrutements(departement);
CREATE INDEX IF NOT EXISTS idx_candidatures_recrutement_id ON candidatures_recrutement(recrutement_id);

-- Seed sample recrutement sessions (idempotent)
INSERT INTO recrutements (
    poste, type_contrat, departement, nombre_postes, description,
    date_ouverture, date_cloture, statut, created_by, created_at
) SELECT
    'Ingénieur DevOps', 'CDI', 'Informatique', 2,
    'Mise en place CI/CD, observabilité, et fiabilité des déploiements.',
    '2026-02-01', '2026-02-28', 'OUVERT', 1, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM recrutements WHERE poste = 'Ingénieur DevOps' AND date_ouverture = '2026-02-01');

INSERT INTO recrutements (
    poste, type_contrat, departement, nombre_postes, description,
    date_ouverture, date_cloture, statut, created_by, created_at
) SELECT
    'Technicien support', 'CDD', 'Support', 3,
    'Support N1/N2, gestion des tickets et escalade des incidents.',
    '2026-02-10', '2026-03-10', 'OUVERT', 2, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM recrutements WHERE poste = 'Technicien support' AND date_ouverture = '2026-02-10');

-- Seed sample candidatures linked to the above recrutements (idempotent)
INSERT INTO candidatures_recrutement (
    recrutement_id, nom, prenom, cin, email, telephone,
    cv_url, lettre_motivation_url, statut, score_ecrit, score_oral, commentaires, date_candidature
) SELECT
    1, 'Doe', 'Jane', 'AB123456', 'jane.doe@example.com', '+33123456789',
    'https://example.com/cv/jane-doe.pdf', 'https://example.com/lm/jane-doe.pdf',
    'EN_ATTENTE', 85.5, 90.0, 'Bon profil, expérience CI/CD', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM candidatures_recrutement WHERE recrutement_id = 1 AND cin = 'AB123456');

INSERT INTO candidatures_recrutement (
    recrutement_id, nom, prenom, cin, email, telephone,
    cv_url, lettre_motivation_url, statut, score_ecrit, score_oral, commentaires, date_candidature
) SELECT
    2, 'Smith', 'John', 'CD789012', 'john.smith@example.com', '+33987654321',
    'https://example.com/cv/john-smith.pdf', 'https://example.com/lm/john-smith.pdf',
    'EN_ATTENTE', 78.0, 82.0, 'Solide expérience support N2', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM candidatures_recrutement WHERE recrutement_id = 2 AND cin = 'CD789012');
