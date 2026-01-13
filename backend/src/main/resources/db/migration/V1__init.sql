PRAGMA foreign_keys = ON;

-- ============================================================
-- BASE DE DONNÉES SQLite - MODULE 3 : GESTION DES RESSOURCES
-- Groupe 4 - ENSA Tétouan
-- ============================================================

-- 1. PERSONNEL
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
    date_embauche DATE,
    statut VARCHAR(20) DEFAULT 'ACTIF',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. UTILISATEURS
CREATE TABLE IF NOT EXISTS utilisateurs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    personnel_id INTEGER NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL,
    derniere_connexion TIMESTAMP,
    is_active BOOLEAN DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (personnel_id) REFERENCES personnel(id) ON DELETE CASCADE
);

-- 3. DEMANDES
CREATE TABLE IF NOT EXISTS demandes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    personnel_id INTEGER NOT NULL,
    type_demande VARCHAR(30) NOT NULL CHECK(type_demande IN ('CONGE','DEMISSION')),
    date_debut DATE,
    date_fin DATE,
    nombre_jours INTEGER,
    motif TEXT,
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE',
    commentaire_admin TEXT,
    traite_par INTEGER,
    date_demande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_traitement TIMESTAMP,
    document_url VARCHAR(255),
    FOREIGN KEY (personnel_id) REFERENCES personnel(id) ON DELETE CASCADE,
    FOREIGN KEY (traite_par) REFERENCES utilisateurs(id)
);

-- 4. BUDGET
CREATE TABLE IF NOT EXISTS budget (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    annee INTEGER NOT NULL UNIQUE,
    montant_total DECIMAL(15,2) NOT NULL,
    montant_depense DECIMAL(15,2) DEFAULT 0,
    montant_disponible DECIMAL(15,2),
    statut VARCHAR(20) DEFAULT 'EN_COURS',
    created_by INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES utilisateurs(id)
);

-- 5. BESOINS
CREATE TABLE IF NOT EXISTS besoins (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    personnel_id INTEGER NOT NULL,
    type_besoin VARCHAR(30) NOT NULL,
    description TEXT NOT NULL,
    quantite INTEGER DEFAULT 1,
    montant_estime DECIMAL(10,2),
    priorite VARCHAR(20) DEFAULT 'MOYENNE',
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE',
    commentaire_admin TEXT,
    traite_par INTEGER,
    date_demande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_traitement TIMESTAMP,
    date_livraison DATE,
    FOREIGN KEY (personnel_id) REFERENCES personnel(id) ON DELETE CASCADE,
    FOREIGN KEY (traite_par) REFERENCES utilisateurs(id)
);

-- 6. RECETTES
CREATE TABLE IF NOT EXISTS recettes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    source VARCHAR(100) NOT NULL,
    categorie VARCHAR(50),
    montant DECIMAL(10,2) NOT NULL,
    date_recette DATE NOT NULL,
    description TEXT,
    reference_document VARCHAR(100),
    enregistre_par INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (enregistre_par) REFERENCES utilisateurs(id)
);

-- 7. DEPENSES
CREATE TABLE IF NOT EXISTS depenses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    besoin_id INTEGER,
    categorie VARCHAR(50) NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    date_depense DATE NOT NULL,
    fournisseur VARCHAR(100),
    facture_numero VARCHAR(50),
    description TEXT NOT NULL,
    mode_paiement VARCHAR(30),
    enregistre_par INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (besoin_id) REFERENCES besoins(id),
    FOREIGN KEY (enregistre_par) REFERENCES utilisateurs(id)
);

-- 8. COMMANDES
CREATE TABLE IF NOT EXISTS commandes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    besoin_id INTEGER NOT NULL,
    fournisseur VARCHAR(100) NOT NULL,
    montant_total DECIMAL(10,2) NOT NULL,
    date_commande DATE NOT NULL,
    date_livraison_prevue DATE,
    date_livraison_effective DATE,
    statut VARCHAR(20) DEFAULT 'EN_COURS',
    bon_commande_numero VARCHAR(50),
    notes TEXT,
    created_by INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (besoin_id) REFERENCES besoins(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES utilisateurs(id)
);

-- 9. RECRUTEMENTS (Session de recrutement)
CREATE TABLE IF NOT EXISTS recrutements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titre VARCHAR(100) NOT NULL,
    annee INTEGER NOT NULL,
    statut VARCHAR(20) DEFAULT 'EN_COURS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 10. CANDIDATURES_RECRUTEMENT
CREATE TABLE IF NOT EXISTS candidatures_recrutement (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    recrutement_id INTEGER NOT NULL,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    cin VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    cv_url VARCHAR(255),
    lettre_motivation_url VARCHAR(255),
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE',
    score_ecrit DECIMAL(5,2),
    score_oral DECIMAL(5,2),
    commentaires TEXT,
    date_candidature TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recrutement_id) REFERENCES recrutements(id) ON DELETE CASCADE
);

-- 11. NOTIFICATIONS
CREATE TABLE IF NOT EXISTS notifications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    utilisateur_id INTEGER NOT NULL,
    titre VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(30),
    est_lue BOOLEAN DEFAULT 0,
    date_notification TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
);

-- 12. HISTORIQUE
CREATE TABLE IF NOT EXISTS historique (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    table_name VARCHAR(50) NOT NULL,
    record_id INTEGER NOT NULL,
    action VARCHAR(20) NOT NULL,
    utilisateur_id INTEGER,
    ancien_etat TEXT,
    nouvel_etat TEXT,
    date_action TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)
);

-- ============================================================
-- DONNÉES INITIALES (exemples)
-- ============================================================
INSERT INTO personnel (cin, nom, prenom, email, telephone, type_personnel, grade, departement, statut)
VALUES
('AB123456', 'Admin', 'RH', 'admin.rh@ensa.ma', '0612345678', 'ADMINISTRATIF', 'Responsable', 'RH', 'ACTIF'),
('CD789012', 'Admin', 'ECO', 'admin.eco@ensa.ma', '0612345679', 'ADMINISTRATIF', 'Responsable', 'Économique', 'ACTIF'),
('EF345678', 'Alaoui', 'Ahmed', 'ahmed.alaoui@ensa.ma', '0612345680', 'ENSEIGNANT', 'Professeur', 'GI', 'ACTIF'),
('GH901234', 'Benali', 'Fatima', 'fatima.benali@ensa.ma', '0612345681', 'ENSEIGNANT', 'Professeur', 'GE', 'ACTIF'),
('IJ567890', 'Tazi', 'Youssef', 'youssef.tazi@ensa.ma', '0612345682', 'ENSEIGNANT', 'Maître de Conférences', 'GM', 'ACTIF'),
('KL234567', 'Mansouri', 'Khadija', 'khadija.mansouri@ensa.ma', '0612345683', 'ADMINISTRATIF', 'Secrétaire', 'Administration', 'ACTIF'),
('MN890123', 'Chakir', 'Mohamed', 'mohamed.chakir@ensa.ma', '0612345684', 'TECHNIQUE', 'Technicien', 'Informatique', 'ACTIF'),
('OP456789', 'Zahraoui', 'Samira', 'samira.zahraoui@ensa.ma', '0612345685', 'ENSEIGNANT', 'Professeur Assistant', 'GI', 'ACTIF'),
('QR012345', 'Idrissi', 'Hassan', 'hassan.idrissi@ensa.ma', '0612345686', 'ADMINISTRATIF', 'Comptable', 'Finance', 'ACTIF'),
('ST678901', 'Amrani', 'Laila', 'laila.amrani@ensa.ma', '0612345687', 'ENSEIGNANT', 'Professeur', 'GC', 'ACTIF'),
('UV234567', 'Bennani', 'Omar', 'omar.bennani@ensa.ma', '0612345688', 'TECHNIQUE', 'Technicien Supérieur', 'Laboratoire', 'ACTIF'),
('WX890123', 'Fassi', 'Salma', 'salma.fassi@ensa.ma', '0612345689', 'ADMINISTRATIF', 'Assistant RH', 'RH', 'ACTIF'),
('YZ456789', 'Rami', 'Karim', 'karim.rami@ensa.ma', '0612345690', 'ENSEIGNANT', 'Maître de Conférences', 'GE', 'ACTIF'),
('AA012345', 'Sefiani', 'Nadia', 'nadia.sefiani@ensa.ma', '0612345691', 'ADMINISTRATIF', 'Responsable Scolarité', 'Scolarité', 'ACTIF'),
('BB678901', 'Ouazzani', 'Rachid', 'rachid.ouazzani@ensa.ma', '0612345692', 'TECHNIQUE', 'Chef Atelier', 'Maintenance', 'ACTIF'),
('CC234567', 'Berrada', 'Zineb', 'zineb.berrada@ensa.ma', '0612345693', 'ENSEIGNANT', 'Professeur', 'GM', 'ACTIF'),
('DD890123', 'Lahlou', 'Mehdi', 'mehdi.lahlou@ensa.ma', '0612345694', 'ADMINISTRATIF', 'Gestionnaire', 'Logistique', 'ACTIF');

INSERT INTO utilisateurs (personnel_id, username, password_hash, role, is_active)
VALUES
(1, 'admin.rh', '$2a$10$dummyhash1', 'directeur',1),
(2, 'admin.eco', '$2a$10$dummyhash2', 'admin',1),
(3, 'ahmed.alaoui', '$2a$10$dummyhash3', 'recruteur',1);

INSERT INTO budget (annee, montant_total, montant_disponible, statut, created_by)
SELECT 2026, 500000.00, 500000.00, 'EN_COURS', u.id
FROM utilisateurs u
WHERE u.username = 'admin.eco';

