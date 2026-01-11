PRAGMA foreign_keys=off;
BEGIN TRANSACTION;

ALTER TABLE utilisateurs RENAME TO utilisateurs_old;

CREATE TABLE utilisateurs (
                              created_at date,
                              derniere_connexion date,
                              is_active boolean,
                              id integer,
                              personnel_id bigint,
                              email varchar(255),
                              password_hash varchar(255),
                              role varchar(255) CHECK (role IN (
                                                                'directeur',
                                                                'RH',
                                                                'secretaire_general',
                                                                'recruteur',
                                                                'Directeur_adjoint',
                                                                'admin'
                                  )),
                              username varchar(255),
                              PRIMARY KEY (id)
);

INSERT INTO utilisateurs (
    created_at, derniere_connexion, is_active, id, personnel_id,
    email, password_hash, role, username
)
SELECT
    created_at, derniere_connexion, is_active, id, personnel_id,
    email, password_hash, role, username
FROM utilisateurs_old;

DROP TABLE utilisateurs_old;

COMMIT;
PRAGMA foreign_keys=on;
