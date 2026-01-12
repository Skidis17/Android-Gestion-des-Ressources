PRAGMA foreign_keys = ON;

-- Track candidature status changes
CREATE TABLE IF NOT EXISTS candidature_status_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    candidature_id INTEGER NOT NULL,
    from_status VARCHAR(30),
    to_status VARCHAR(30) NOT NULL,
    reason TEXT,
    changed_by VARCHAR(120),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (candidature_id) REFERENCES candidatures_recrutement(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_candidature_history_candidature
    ON candidature_status_history(candidature_id);

-- Interviews linked to a candidature
CREATE TABLE IF NOT EXISTS entretiens (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    candidature_id INTEGER NOT NULL,
    type VARCHAR(30) NOT NULL,
    scheduled_at TIMESTAMP NOT NULL,
    mode VARCHAR(20),
    location TEXT,
    status VARCHAR(30) DEFAULT 'PLANIFIE',
    notes TEXT,
    score_total DECIMAL(5,2),
    created_by VARCHAR(120),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (candidature_id) REFERENCES candidatures_recrutement(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_entretiens_candidature
    ON entretiens(candidature_id);

-- Interview scoring rubric entries
CREATE TABLE IF NOT EXISTS entretien_scores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entretien_id INTEGER NOT NULL,
    criterion VARCHAR(100) NOT NULL,
    score DECIMAL(5,2) NOT NULL,
    weight DECIMAL(5,2) DEFAULT 1.0,
    reviewer VARCHAR(120),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (entretien_id) REFERENCES entretiens(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_entretien_scores_entretien
    ON entretien_scores(entretien_id);

-- General candidature scoring rubric (written/oral/overall)
CREATE TABLE IF NOT EXISTS candidature_scores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    candidature_id INTEGER NOT NULL,
    stage VARCHAR(20) DEFAULT 'GENERAL',
    criterion VARCHAR(100) NOT NULL,
    score DECIMAL(5,2) NOT NULL,
    weight DECIMAL(5,2) DEFAULT 1.0,
    reviewer VARCHAR(120),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (candidature_id) REFERENCES candidatures_recrutement(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_candidature_scores_candidature
    ON candidature_scores(candidature_id);

-- Prevent duplicate candidature for same recrutement + email
CREATE UNIQUE INDEX IF NOT EXISTS ux_candidatures_recrutement_email
    ON candidatures_recrutement(recrutement_id, email);
