-- Add interview and scoring tables for recruitment candidatures.
CREATE TABLE IF NOT EXISTS entretiens (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    candidature_id INTEGER NOT NULL,
    type VARCHAR(50) NOT NULL,
    scheduled_at TIMESTAMP NOT NULL,
    mode VARCHAR(50),
    location VARCHAR(255),
    status VARCHAR(30),
    notes TEXT,
    score_total DECIMAL(5,2),
    created_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (candidature_id) REFERENCES candidatures_recrutement(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS candidature_scores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    candidature_id INTEGER NOT NULL,
    stage VARCHAR(50),
    criterion VARCHAR(100) NOT NULL,
    score DECIMAL(5,2) NOT NULL,
    weight DECIMAL(5,2),
    reviewer VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (candidature_id) REFERENCES candidatures_recrutement(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS entretien_scores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entretien_id INTEGER NOT NULL,
    criterion VARCHAR(100) NOT NULL,
    score DECIMAL(5,2) NOT NULL,
    weight DECIMAL(5,2),
    reviewer VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (entretien_id) REFERENCES entretiens(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_entretiens_candidature
    ON entretiens(candidature_id);

CREATE INDEX IF NOT EXISTS idx_candidature_scores_candidature
    ON candidature_scores(candidature_id);

CREATE INDEX IF NOT EXISTS idx_entretien_scores_entretien
    ON entretien_scores(entretien_id);
