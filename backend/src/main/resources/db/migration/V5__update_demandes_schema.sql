PRAGMA foreign_keys = OFF;

CREATE TABLE IF NOT EXISTS demandes__new (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type VARCHAR(30) NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    motif TEXT NOT NULL,
    statut VARCHAR(20) NOT NULL DEFAULT 'EN_ATTENTE',
    created_by INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO demandes__new (id, type, date_debut, date_fin, motif, statut, created_by, created_at)
SELECT
    id,
    CASE
        WHEN type_demande IN ('CONGE','PERMISSION','AUTORISATION') THEN type_demande
        ELSE 'AUTORISATION'
    END,
    date_debut,
    date_fin,
    motif,
    CASE
        WHEN statut IN ('EN_ATTENTE','ACCEPTEE','REFUSEE') THEN statut
        ELSE 'EN_ATTENTE'
    END,
    COALESCE(personnel_id, 0),
    COALESCE(date_demande, CURRENT_TIMESTAMP)
FROM demandes;

DROP TABLE demandes;
ALTER TABLE demandes__new RENAME TO demandes;

PRAGMA foreign_keys = ON;
