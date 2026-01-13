-- Track candidature status changes.
CREATE TABLE IF NOT EXISTS candidature_status_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    candidature_id INTEGER NOT NULL,
    from_status VARCHAR(30),
    to_status VARCHAR(30) NOT NULL,
    reason TEXT,
    changed_by VARCHAR(100),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (candidature_id) REFERENCES candidatures_recrutement(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_candidature_status_history_candidature
    ON candidature_status_history(candidature_id);
