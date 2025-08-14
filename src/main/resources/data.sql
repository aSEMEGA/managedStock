-- Script d'initialisation de la base de données
-- Insérer des catégories de test
INSERT INTO category (id, libelle, is_active) VALUES 
(1, 'Consommables', 'ACTIVATED'),
(2, 'Ordinateurs', 'ACTIVATED'),
(3, 'Meubles', 'ACTIVATED'),
(4, 'Électronique', 'ACTIVATED');

-- Insérer des produits de test
INSERT INTO product (id, nom, quantity, price, image_path, category_id, seuil_stock, is_active, date_creation) VALUES 
(1, 'Stylos Bic', 100, 2, NULL, 1, 20, 'ACTIVATED', NOW()),
(2, 'Cahiers A4', 50, 5, NULL, 1, 10, 'ACTIVATED', NOW()),
(3, 'Ordinateur Portable HP', 10, 800, NULL, 2, 3, 'ACTIVATED', NOW()),
(4, 'Bureau en bois', 5, 200, NULL, 3, 2, 'ACTIVATED', NOW()),
(5, 'Écran 24 pouces', 15, 150, NULL, 4, 5, 'ACTIVATED', NOW());

-- Insérer des utilisateurs de test (mot de passe: password123)
INSERT INTO users (id, username, password, email, role, is_first_login, is_active, date_creation) VALUES 
(1, 'admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin@example.com', 'ADMIN', false, true, NOW()),
(2, 'vendeur1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'vendeur1@example.com', 'VENDEUR', false, true, NOW()),
(3, 'vendeur2', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'vendeur2@example.com', 'VENDEUR', false, true, NOW());

-- Insérer des clients de test
INSERT INTO client (id, nom, email, telephone, adresse, date_creation, is_active) VALUES 
(1, 'Jean Dupont', 'jean.dupont@email.com', '+22501234567', 'Abidjan, Cocody', NOW(), true),
(2, 'Marie Martin', 'marie.martin@email.com', '+22501234568', 'Abidjan, Marcory', NOW(), true),
(3, 'Pierre Durand', 'pierre.durand@email.com', '+22501234569', 'Abidjan, Treichville', NOW(), true);

-- Insérer des mouvements de stock initiaux
INSERT INTO mouv_stock (id, product_id, quantity, type_mouvement, date) VALUES 
(1, 1, 100, 'ENTREE', NOW()),
(2, 2, 50, 'ENTREE', NOW()),
(3, 3, 10, 'ENTREE', NOW()),
(4, 4, 5, 'ENTREE', NOW()),
(5, 5, 15, 'ENTREE', NOW());
