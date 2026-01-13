-- Indexes and constraints for recrutement/candidature flows.
CREATE UNIQUE INDEX IF NOT EXISTS ux_candidatures_recrutement_email
    ON candidatures_recrutement(recrutement_id, email);

CREATE INDEX IF NOT EXISTS idx_candidatures_recrutement_statut
    ON candidatures_recrutement(statut);

CREATE INDEX IF NOT EXISTS idx_candidatures_recrutement_date
    ON candidatures_recrutement(date_candidature);
