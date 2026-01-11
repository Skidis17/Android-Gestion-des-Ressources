CREATE UNIQUE INDEX IF NOT EXISTS ux_utilisateurs_email
    ON utilisateurs(email)
    WHERE email IS NOT NULL;
