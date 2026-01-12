-- Create notifications table for user notifications
CREATE TABLE IF NOT EXISTS notifications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    utilisateur_id INTEGER NOT NULL,
    titre VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL, -- INFO, SUCCESS, WARNING, ERROR
    est_lu BOOLEAN DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_notifications_user ON notifications(utilisateur_id);
CREATE INDEX IF NOT EXISTS idx_notifications_read ON notifications(est_lu);

-- Insert sample notifications for testing
INSERT INTO notifications (utilisateur_id, titre, message, type, est_lu) VALUES
(1, 'Bienvenue', 'Bienvenue sur la plateforme de gestion des ressources ENSA Tétouan', 'INFO', 0),
(1, 'Budget 2026', 'Le budget pour l''année 2026 a été validé', 'SUCCESS', 0),
(2, 'Nouvelle demande', 'Une nouvelle demande nécessite votre attention', 'WARNING', 0);
