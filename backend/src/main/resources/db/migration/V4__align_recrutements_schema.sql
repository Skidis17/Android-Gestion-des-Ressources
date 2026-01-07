-- Rebuild recrutements + candidatures tables to match the JPA models
-- (safe for the current DB that had legacy columns).
PRAGMA foreign_keys = OFF;

DROP TABLE IF EXISTS candidatures_recrutement;
DROP TABLE IF EXISTS recrutements;

CREATE TABLE recrutements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    poste TEXT NOT NULL,
    type_contrat TEXT,
    departement TEXT,
    nombre_postes INTEGER,
    description TEXT,
    date_ouverture DATE,
    date_cloture DATE,
    statut TEXT,
    created_by INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE candidatures_recrutement (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    recrutement_id INTEGER NOT NULL,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    cin VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    cv_url VARCHAR(255),
    lettre_motivation_url VARCHAR(255),
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE',
    score_ecrit DECIMAL(5,2),
    score_oral DECIMAL(5,2),
    commentaires TEXT,
    date_candidature TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recrutement_id) REFERENCES recrutements(id) ON DELETE CASCADE
);

-- Recreate indexes
CREATE INDEX IF NOT EXISTS idx_recrutements_statut ON recrutements(statut);
CREATE INDEX IF NOT EXISTS idx_recrutements_departement ON recrutements(departement);
CREATE INDEX IF NOT EXISTS idx_candidatures_recrutement_id ON candidatures_recrutement(recrutement_id);

-- Seed sample data (recreated because the table was rebuilt)
INSERT INTO recrutements (
    poste, type_contrat, departement, nombre_postes, description,
    date_ouverture, date_cloture, statut, created_by, created_at
) VALUES
(
    'Ingénieur DevOps', 'CDI', 'Informatique', 2,
    'Mise en place CI/CD, observabilité, et fiabilité des déploiements.',
    '2026-02-01', '2026-02-28', 'OUVERT', 1, CURRENT_TIMESTAMP
),
(
    'Technicien support', 'CDD', 'Support', 3,
    'Support N1/N2, gestion des tickets et escalade des incidents.',
    '2026-02-10', '2026-03-10', 'OUVERT', 2, CURRENT_TIMESTAMP
);

INSERT INTO candidatures_recrutement (
    recrutement_id, nom, prenom, cin, email, telephone,
    cv_url, lettre_motivation_url, statut, score_ecrit, score_oral, commentaires, date_candidature
) VALUES
(
    1, 'Doe', 'Jane', 'AB123456', 'jane.doe@example.com', '+33123456789',
    'https://example.com/cv/jane-doe.pdf', 'https://example.com/lm/jane-doe.pdf',
    'EN_ATTENTE', 85.5, 90.0, 'Bon profil, expérience CI/CD', CURRENT_TIMESTAMP
),
(
    2, 'Smith', 'John', 'CD789012', 'john.smith@example.com', '+33987654321',
    'https://example.com/cv/john-smith.pdf', 'https://example.com/lm/john-smith.pdf',
    'EN_ATTENTE', 78.0, 82.0, 'Solide expérience support N2', CURRENT_TIMESTAMP
);

PRAGMA foreign_keys = ON;
