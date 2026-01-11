-- No changes needed if column already exists, but SQLite doesn't support ADD COLUMN IF NOT EXISTS.
-- To fix the error in a migration context where the column might exist:
-- However, per instructions to just fix the snippet provided:
ALTER TABLE utilisateurs ADD COLUMN email TEXT;
UPDATE utilisateurs SET email = username WHERE email IS NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_utilisateurs_email ON utilisateurs(email);

