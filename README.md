# Application de Gestion de Stock et de Ventes

## 🎯 Description

Application web backend développée en Kotlin avec Spring Boot pour la gestion de stock et de ventes. L'application permet la gestion des catégories, produits, ventes et mouvements de stock avec authentification Admin/Vendeur.

## 🛠️ Technologies utilisées

- **Langage** : Kotlin
- **Framework** : Spring Boot 3.5.4
- **Base de données** : MySQL
- **ORM** : Spring Data JPA
- **Sécurité** : Spring Security + JWT
- **Documentation API** : Swagger/OpenAPI
- **Build** : Maven

## 📋 Fonctionnalités

### 🔐 Authentification avancée
- Système JWT avec rôles (Admin/Vendeur)
- CRUD utilisateurs (admin peut créer vendeurs)
- Envoi automatique des identifiants par email lors de la création
- Obligation de changement de mot de passe au premier login
- Notification par email lors du changement de mot de passe
- Middleware de sécurité pour protéger les routes

### 📂 Gestion des catégories
- CRUD catégories (nom, description)
- Exemples : consommables, ordinateurs, meubles

### 📦 Gestion des produits
- CRUD produits (nom, catégorie, prix, quantité, description)
- Relation avec catégories
- Upload d'images

### 📊 Gestion du stock
- Entrée et sortie de stock
- Décrémentation automatique après une vente
- Alerte si stock ≤ seuil défini

### 💰 Système de vente avancé
- Ajout d'une vente avec négociation de prix (réduction possible)
- Vente à crédit avec date d'échéance
- Paiement partiel (montant payé + montant à crédit)
- Gestion des clients (nom, email, téléphone, adresse)
- Vérification "stock suffisant" avant la vente
- Calcul automatique du total avec réduction
- Suivi des ventes par vendeur

### 📈 Historique et statistiques
- Historique des ventes et mouvements de stock
- Chiffre d'affaires total sur une période
- Produit le plus vendu
- Stock bas à surveiller
- Suivi des ventes à crédit
- Total des crédits en attente
- Historique des paiements partiels

### 📤 Exportation des données
- Export des ventes en Excel
- Export du stock en Excel
- Génération de rapports textuels
- Export des ventes à crédit
- Rapports de paiements

## 🚀 Installation et démarrage

### Prérequis
- Java 17+
- MySQL 8.0+
- Maven 3.6+

### Configuration de la base de données
1. Créer une base de données MySQL nommée `managedStock`
2. Modifier les paramètres de connexion dans `src/main/resources/application.properties`

### Démarrage de l'application
```bash
# Cloner le projet
git clone <repository-url>
cd managedStock

# Compiler et démarrer
./mvnw spring-boot:run
```

L'application sera accessible sur `http://localhost:8080`

## 📚 Documentation API

### Swagger UI
La documentation interactive de l'API est disponible sur :
- **URL** : `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON** : `http://localhost:8080/api-docs`

### Endpoints principaux

#### Authentification
- `POST /api/v1/auth/register` - Inscription d'un utilisateur
- `POST /api/v1/auth/login` - Connexion
- `POST /api/v1/auth/vendeur` - Création d'un vendeur (Admin uniquement)
- `POST /api/v1/auth/change-password` - Changement de mot de passe

#### Catégories
- `GET /api/v1/categories` - Liste des catégories
- `POST /api/v1/categories` - Créer une catégorie
- `PUT /api/v1/categories/{id}` - Modifier une catégorie
- `DELETE /api/v1/categories/{id}` - Supprimer une catégorie

#### Produits
- `GET /api/v1/products` - Liste des produits
- `POST /api/v1/products` - Créer un produit
- `PUT /api/v1/products/{id}` - Modifier un produit
- `DELETE /api/v1/products/{id}` - Supprimer un produit

#### Clients
- `GET /api/v1/clients` - Liste des clients
- `POST /api/v1/clients` - Créer un client
- `PUT /api/v1/clients/{id}` - Modifier un client
- `DELETE /api/v1/clients/{id}` - Supprimer un client
- `GET /api/v1/clients/search` - Rechercher des clients

#### Ventes
- `GET /api/v1/ventes` - Liste des ventes
- `POST /api/v1/ventes` - Créer une vente (avec négociation et crédit)
- `GET /api/v1/ventes/revenue` - Chiffre d'affaires
- `GET /api/v1/ventes/most-sold` - Produits les plus vendus
- `GET /api/v1/ventes/low-stock` - Produits en stock bas
- `GET /api/v1/ventes/credit` - Ventes à crédit
- `GET /api/v1/ventes/credit/client/{clientId}` - Ventes à crédit d'un client
- `POST /api/v1/ventes/{venteId}/payer-credit` - Payer un crédit
- `POST /api/v1/ventes/credit/rappels` - Envoyer les rappels de crédit
- `GET /api/v1/ventes/credit/total` - Total des crédits en attente

#### Export
- `GET /api/v1/export/ventes/excel` - Export des ventes en Excel
- `GET /api/v1/export/stock/excel` - Export du stock en Excel
- `GET /api/v1/export/ventes/report` - Rapport des ventes
- `GET /api/v1/export/stock/report` - Rapport du stock

## 🔧 Configuration

### Variables d'environnement
- `SPRING_DATASOURCE_URL` - URL de la base de données
- `SPRING_DATASOURCE_USERNAME` - Nom d'utilisateur MySQL
- `SPRING_DATASOURCE_PASSWORD` - Mot de passe MySQL
- `JWT_SECRET` - Clé secrète pour JWT
- `SPRING_MAIL_USERNAME` - Email pour l'envoi des notifications
- `SPRING_MAIL_PASSWORD` - Mot de passe de l'email

### Données de test
L'application inclut des données de test automatiquement insérées :
- **Utilisateurs** : admin/password123, vendeur1/password123, vendeur2/password123
- **Catégories** : Consommables, Ordinateurs, Meubles, Électronique
- **Produits** : Stylos, Cahiers, Ordinateurs, Bureaux, Écrans
- **Clients** : Jean Dupont, Marie Martin, Pierre Durand

## 🧪 Tests

```bash
# Exécuter les tests
./mvnw test

# Exécuter les tests avec couverture
./mvnw test jacoco:report
```

## 📦 Structure du projet

```
src/main/kotlin/com/example/managedStock/
├── config/                 # Configurations (Security, OpenAPI)
├── controllers/           # Contrôleurs REST
├── dto/                  # Objets de transfert de données
├── entities/             # Entités JPA
├── enums/                # Énumérations
├── exception/            # Gestion des exceptions
├── mappers/              # Mappers entre entités et DTOs
├── repository/           # Repositories Spring Data
└── services/             # Services métier
```

## 🔒 Sécurité

- **JWT** : Authentification par token
- **BCrypt** : Hashage des mots de passe
- **CORS** : Configuration pour les requêtes cross-origin
- **Validation** : Validation des données avec Jakarta Validation
- **Email** : Notifications automatiques par email
- **Premier login** : Obligation de changement de mot de passe

## 📊 Monitoring

- **Actuator** : Endpoints de monitoring sur `/actuator`
- **Logs** : Configuration des logs dans `application.properties`
- **Planification** : Rappels automatiques de crédit quotidiens
- **Notifications** : Système d'alertes par email

## 🤝 Contribution

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 📞 Support

Pour toute question ou problème :
- Créer une issue sur GitHub
- Contacter l'équipe de développement : dev@example.com
