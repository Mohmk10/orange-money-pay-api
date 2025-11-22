# 🍊 Orange Money Pay API - Clone Complet

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Deploy](https://img.shields.io/badge/Deploy-Render-46E3B7.svg)](https://orange-money-pay-api.onrender.com)
[![API Status](https://img.shields.io/badge/API-Live-success.svg)](https://orange-money-pay-api.onrender.com/swagger-ui.html)

> Clone complet de l'API Orange Money Pay du Sénégal - Système de paiement mobile utilisé par 5 millions d'utilisateurs

## 🚀 Démonstration en Ligne

- **API Base URL**: `https://orange-money-pay-api.onrender.com/api/v1`
- **Documentation Swagger**: [https://orange-money-pay-api.onrender.com/swagger-ui.html](https://orange-money-pay-api.onrender.com/swagger-ui.html)
- **Status**: ✅ Production

⚠️ **Note**: Le service gratuit Render se met en veille après 15 minutes d'inactivité. Le premier appel après veille peut prendre 30-60 secondes.

## 📋 Table des Matières

- [Fonctionnalités](#-fonctionnalités)
- [Architecture](#-architecture)
- [Technologies](#-technologies)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Utilisation](#-utilisation)
- [Endpoints API](#-endpoints-api)
- [Déploiement](#-déploiement)
- [Tests](#-tests)
- [Contribution](#-contribution)

## ✨ Fonctionnalités

### 🔐 Authentification & Sécurité
- ✅ Inscription avec validation email
- ✅ Connexion sécurisée avec JWT
- ✅ Refresh token
- ✅ Vérification en 2 étapes
- ✅ Gestion des sessions
- ✅ Réinitialisation de mot de passe

### 💰 Gestion des Comptes
- ✅ Création automatique de compte OM
- ✅ Numérotation unique (OM + téléphone + 3 chiffres)
- ✅ Soldes et historiques de transactions
- ✅ Niveaux KYC (LEVEL_0 à LEVEL_3)
- ✅ Limites de transaction personnalisées
- ✅ Blocage/déblocage de compte

### 💸 Transactions
- ✅ Transfert d'argent (P2P)
- ✅ Dépôt via agent/distributeur
- ✅ Retrait via agent/distributeur
- ✅ Transfert international
- ✅ Paiement marchand
- ✅ Paiement de factures (eau, électricité, internet, etc.)
- ✅ Achat de crédit téléphonique
- ✅ Paiement QR Code

### 🎯 Fonctionnalités Avancées
- ✅ Favoris de bénéficiaires
- ✅ Transactions planifiées
- ✅ Demandes d'argent
- ✅ Notifications en temps réel
- ✅ Historique détaillé
- ✅ Export de relevés (PDF/CSV)
- ✅ Multi-devise (XOF, EUR, USD)

## 🏗️ Architecture

### Modèle de Domaine
```
User (Utilisateur)
├── Account (Compte Orange Money)
├── Transactions (Historique)
├── Favorites (Bénéficiaires favoris)
├── ScheduledTransactions (Planifiées)
└── MoneyRequests (Demandes d'argent)

Transaction
├── Sender (Émetteur)
├── Receiver (Destinataire)
├── Type (TRANSFER, DEPOSIT, WITHDRAWAL, etc.)
└── Status (PENDING, COMPLETED, FAILED)
```

### Architecture en Couches
```
┌─────────────────────────────────────┐
│     Controllers (REST API)          │
├─────────────────────────────────────┤
│     Services (Business Logic)       │
├─────────────────────────────────────┤
│     Repositories (Data Access)      │
├─────────────────────────────────────┤
│     Entities (Domain Models)        │
├─────────────────────────────────────┤
│     PostgreSQL Database             │
└─────────────────────────────────────┘
```

## 🛠️ Technologies

### Backend
- **Framework**: Spring Boot 3.5.7
- **Langage**: Java 21
- **Base de données**: PostgreSQL 14+
- **ORM**: Spring Data JPA / Hibernate
- **Sécurité**: Spring Security + JWT
- **Validation**: Bean Validation (JSR-380)
- **Documentation**: Swagger/OpenAPI 3.0
- **Build**: Maven

### Dépendances Principales
```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.6</version>
    </dependency>
    
    <!-- PostgreSQL -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
    
    <!-- MapStruct -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>1.6.3</version>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    
    <!-- Swagger -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.8.4</version>
    </dependency>
</dependencies>
```

## 💻 Installation

### Prérequis
- Java 21 ou supérieur
- Maven 3.8+
- PostgreSQL 14+
- Git

### Cloner le Projet
```bash
git clone https://github.com/Mohmk10/orange-money-pay-api.git
cd orange-money-pay-api
```

### Configuration de la Base de Données

1. **Créer la base de données** :
```sql
CREATE DATABASE ompay_db;
CREATE USER ompay_user WITH PASSWORD 'votre_mot_de_passe';
GRANT ALL PRIVILEGES ON DATABASE ompay_db TO ompay_user;
```

2. **Configurer** `src/main/resources/application-dev.yml` :
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ompay_db
    username: ompay_user
    password: votre_mot_de_passe
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

app:
  jwt:
    secret: votre_cle_secrete_jwt_minimum_32_caracteres
    expiration: 86400000
    refresh-expiration: 604800000
```

### Compiler et Lancer
```bash
# Compiler
mvn clean install

# Lancer en mode dev
mvn spring-boot:run

# Ou lancer le JAR
java -jar target/api-0.0.1-SNAPSHOT.jar
```

L'API sera accessible sur `http://localhost:8080`

## ⚙️ Configuration

### Générer une Clé JWT Sécurisée
```bash
openssl rand -base64 32
```

### Profils Spring

- **dev** : Développement local (logs détaillés, show-sql activé)
- **prod** : Production (logs minimaux, optimisations activées)
```bash
# Lancer en mode production
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## 🎯 Utilisation

### Inscription
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "771234567",
    "email": "john.doe@example.com",
    "pin": "1587",
    "confirmPin": "1587",
    "password": "SecurePass123"
  }'
```

### Connexion
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "771234567",
    "password": "SecurePass123"
  }'
```

**Réponse** :
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "user": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "771234567",
    "account": {
      "accountNumber": "OM771234567123",
      "balance": 0,
      "status": "ACTIVE"
    }
  }
}
```

### Transfert d'Argent
```bash
curl -X POST http://localhost:8080/api/v1/transactions/transfer \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "receiverPhoneNumber": "779876543",
    "amount": 5000,
    "pin": "1587",
    "description": "Remboursement"
  }'
```

## 📚 Endpoints API

### Authentification (`/api/v1/auth`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/register` | Inscription utilisateur |
| POST | `/login` | Connexion |
| POST | `/refresh` | Rafraîchir le token |
| POST | `/verify-email` | Vérifier l'email |
| POST | `/forgot-password` | Mot de passe oublié |
| POST | `/reset-password` | Réinitialiser mot de passe |

### Utilisateurs (`/api/v1/users`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/profile` | Profil utilisateur |
| PUT | `/profile` | Mettre à jour profil |
| PUT | `/change-password` | Changer mot de passe |
| PUT | `/change-pin` | Changer PIN |

### Transactions (`/api/v1/transactions`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/transfer` | Transfert P2P |
| POST | `/deposit` | Dépôt |
| POST | `/withdrawal` | Retrait |
| POST | `/international-transfer` | Transfert international |
| POST | `/merchant-payment` | Paiement marchand |
| POST | `/bill-payment` | Paiement facture |
| POST | `/airtime-purchase` | Achat crédit |
| POST | `/qr-payment` | Paiement QR |
| GET | `/history` | Historique |
| GET | `/{id}` | Détails transaction |

### Comptes (`/api/v1/accounts`)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/` | Détails compte |
| GET | `/balance` | Solde |
| PUT | `/limits` | Modifier limites |
| GET | `/statement` | Relevé de compte |

### Plus d'Endpoints...

Voir la **documentation Swagger complète** : [https://orange-money-pay-api.onrender.com/swagger-ui.html](https://orange-money-pay-api.onrender.com/swagger-ui.html)

## 🚀 Déploiement

### Déploiement sur Render

1. **Fork ce repository**

2. **Créer une base PostgreSQL** sur Render

3. **Créer un Web Service** :
    - Runtime: Docker
    - Branch: main
    - Variables d'environnement :
```
     SPRING_PROFILES_ACTIVE=prod
     APP_JWT_SECRET=votre_cle_jwt_32_caracteres_minimum
```

4. **Déployer** - Render va automatiquement builder et déployer

### URL de Production
```
https://orange-money-pay-api.onrender.com
```

## 🧪 Tests
```bash
# Tests unitaires
mvn test

# Tests avec couverture
mvn test jacoco:report

# Tests d'intégration
mvn verify
```

## 📊 Statistiques du Projet

- **80+ Endpoints REST**
- **11 Entités JPA**
- **15+ Services métier**
- **Validation complète** avec annotations personnalisées
- **Gestion d'erreurs** centralisée
- **Pagination** et **filtrage** avancés
- **Soft delete** sur toutes les entités

## 🤝 Contribution

Malheureusement ou heureusement, ce projet est fini.

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## 👨‍💻 Auteur

**Mohamed Makan KOUYATÉ**
- GitHub: [@Mohmk10](https://github.com/Mohmk10)
- LinkedIn: https://www.linkedin.com/in/mohamed-makan-kouyat%C3%A9-925414262/
- Email: kouyatemakan100@gmail.com

## 🙏 Remerciements

- Coach Birane Baila Wane Architecte Logiciel : email : douvewane85@gmail.com
- Orange Money Sénégal pour l'inspiration
- La communauté Spring Boot

---

⭐ **Si ce projet vous a plus, n'hésitez pas à lui donner une étoile !**