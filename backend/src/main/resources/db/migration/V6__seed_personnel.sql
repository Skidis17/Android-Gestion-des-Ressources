-- 1) Table (si pas déjà créée)
CREATE TABLE IF NOT EXISTS personnel (
                                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                                         cin VARCHAR(20) UNIQUE NOT NULL,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    type_personnel VARCHAR(20) NOT NULL,
    grade VARCHAR(50),
    echelon VARCHAR(10),
    departement VARCHAR(50),
    statut VARCHAR(20) DEFAULT 'ACTIF',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- 2) Seed (évite doublons si tu relances)
INSERT OR IGNORE INTO personnel (
    cin, nom, prenom, email, telephone, type_personnel, grade, echelon, departement
) VALUES
    ('AA123456', 'Touicha',   'Aya',  'ayaa15029@gmail.com',        '0600000000', 'ADMINISTRATIF', 'Ingénieur',  'E1', 'Informatique'),
    ('BB654321', 'El Amrani', 'Sara', 'sara.elamrani@ensate.ma',    '0611111111', 'ADMINISTRATIF', 'Assistante','E2', 'Scolarité'),
    ('CC987654', 'Aya', 'Aya',  'touicha.aya@etu.uae.ac.ma',  '0770946035', 'ADMINISTRATIF', 'Assistante','E2', 'Scolarité'),
    ('DD456789', 'El Malki',  'Imane','aya86jd@gmail.com',         '0622222222', 'ENSEIGNANT',    'Professeur','E3', 'Mathématiques'),
    ('EE789123', 'Achraf',    'Ben',  'achraf87567@gmail.com',      '0633333333', 'ENSEIGNANT',    'Maître de Conférences','E4', 'Informatique');
