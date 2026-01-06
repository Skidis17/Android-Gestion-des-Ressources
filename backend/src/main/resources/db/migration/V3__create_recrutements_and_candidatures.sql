PRAGMA foreign_keys = ON;

-- Recrutements table (idempotent creation)
CREATE TABLE IF NOT EXISTS recrutements (
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

-- Candidatures table (idempotent creation)
CREATE TABLE IF NOT EXISTS candidatures_recrutement (
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

-- Helpful index for FK lookups
CREATE INDEX IF NOT EXISTS idx_candidatures_recrutement_id ON candidatures_recrutement(recrutement_id);
