# 🍊 Orange Money Pay API

API REST complète simulant Orange Money (Application de paiement mobile #1 au Sénégal avec 5M+ utilisateurs).

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)
![MapStruct](https://img.shields.io/badge/MapStruct-FF6600?style=for-the-badge&logo=java&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)


## 📋 Table des Matières

- [Fonctionnalités](#-fonctionnalités)
- [Stack Technique](#️-stack-technique)
- [Architecture](#-architecture)
- [Installation](#-installation)
- [Configuration](#️-configuration)
- [Endpoints API](#-endpoints-api)
- [Tests](#-tests)
- [Documentation](#-documentation)
- [Déploiement](#-déploiement)
- [Contribuer](#-contribuer)

---

## 🚀 Fonctionnalités

### ✅ Modules Implémentés

#### 🔐 Authentification & Sécurité
- Inscription avec validation personnalisée (numéro Orange, PIN 4 chiffres)
- Connexion avec JWT Authentication
- Vérification email (simulée en dev)
- Gestion du refresh token
- Sécurité Spring Security complète

#### 💰 Gestion de Compte
- Consultation du solde et détails du compte
- Mise à jour du profil utilisateur
- Historique complet des transactions
- Limites journalières configurables
- Niveaux KYC (Know Your Customer)

#### 💸 Transferts d'Argent
- Transfert national entre utilisateurs Orange Money
- 2 transferts gratuits/jour (1-2000 FCFA)
- Frais dégressifs selon le montant
- Validation OTP pour montants > 50,000 FCFA
- Historique des transferts avec filtres

#### 🏦 Services Financiers
- **Dépôts & Retraits** via agents
- **Paiement de factures** (SENELEC, SEN'EAU, Canal+, etc.)
- **Recharges** (Crédit téléphonique, Internet, Illimix)
- **Paiements marchands** avec QR Code
- **Liaison comptes bancaires** avec transferts bidirectionnels

#### 📱 QR Code
- Génération de QR Code personnel (recevoir de l'argent)
- Génération de QR Code marchand (demande de paiement)
- Scan et paiement via QR Code
- Expiration automatique des QR Codes

---

## 🛠️ Stack Technique

### Backend
- **Framework** : Spring Boot 3.5.7
- **Langage** : Java 21 (LTS)
- **Build Tool** : Maven 3.9+
- **Base de données** : PostgreSQL 16

### Sécurité
- **Authentification** : JWT (JSON Web Tokens)
- **Authorization** : Spring Security 6
- **Cryptage** : BCrypt pour mots de passe et PIN

### Mapping & Validation
- **DTO Mapping** : MapStruct 1.6.3
- **Validation** : Bean Validation (Jakarta)
- **Custom Validators** : `@ValidPhoneNumber`, `@ValidPIN`, `@ValidAmount`, `@MatchingFields`

### Documentation
- **API Docs** : SpringDoc OpenAPI 3.0 (Swagger UI)
- **Format** : OpenAPI Specification

### ORM & Persistence
- **ORM** : Hibernate 6.6
- **JPA** : Spring Data JPA
- **Projections** : Interface-based & Class-based
- **Auditing** : Automatic `createdAt` / `updatedAt`

---

## 🏗️ Architecture

### Clean Architecture
```
src/main/java/com/orangemoney/api/
├── common/                # Constantes, Enums, Utils
│   ├── constants/         # Business rules (limites, frais, messages)
│   ├── enums/             # TransactionType, Status, KycLevel, etc.
│   └── util/              # Helpers (generators, calculators, encoders)
├── config/                # Configuration Spring
│   ├── JpaConfig.java
│   ├── JwtProperties.java
│   ├── SecurityConfig.java
│   ├── SwaggerConfig.java
│   └── CorsConfig.java
├── entity/                # Entités JPA (12 tables)
│   ├── BaseEntity.java    # Classe abstraite avec audit
│   ├── User.java
│   ├── Account.java
│   ├── Transfer.java
│   └── ...
├── dto/                   # Data Transfer Objects
│   ├── request/           # DTOs pour les requêtes
│   ├── response/          # DTOs pour les réponses
│   └── projection/        # JPA Projections optimisées
├── mapper/                # MapStruct Mappers
│   ├── UserMapper.java
│   ├── AccountMapper.java
│   └── TransferMapper.java
├── validation/            # Custom Validators
│   ├── annotation/        # Annotations de validation
│   └── validator/         # Implémentations des validateurs
├── repository/            # Spring Data JPA Repositories
├── service/               # Business Logic
│   └── impl/              # Implémentations des services
├── controller/            # REST Controllers
├── security/              # JWT & Security
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   ├── UserDetailsServiceImpl.java
│   └── UserPrincipal.java
└── exception/             # Exception Handling
    ├── GlobalExceptionHandler.java
    └── Custom Exceptions
```

### Base de Données (12 Tables)
```sql
users
accounts
transfers
cash_transactions
bills
recharges
merchant_payments
linked_bank_accounts
bank_transfers
qr_codes
verification_tokens
```

---

## 📦 Installation

### Prérequis

- Java 21+ ([OpenJDK](https://openjdk.org/))
- Maven 3.9+
- PostgreSQL 14+
- Git

### Étapes
```bash
# 1. Cloner le repository
git clone https://github.com/ton-username/om-pay-api.git
cd om-pay-api

# 2. Créer la base de données PostgreSQL
psql -U postgres
CREATE DATABASE ompay_db;
CREATE ROLE ompay_user WITH LOGIN PASSWORD 'votre_mot_de_passe';
GRANT ALL PRIVILEGES ON DATABASE ompay_db TO ompay_user;
\c ompay_db
GRANT ALL ON SCHEMA public TO ompay_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO ompay_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO ompay_user;
\q

# 3. Configurer application-dev.yml
# Éditer src/main/resources/application-dev.yml avec tes credentials

# 4. Compiler le projet
mvn clean install -DskipTests

# 5. Lancer l'application
mvn spring-boot:run
```

L'API sera accessible sur : **http://localhost:8080**

---

## ⚙️ Configuration

### Profiles Spring

**Dev** (Local) : `spring.profiles.active=dev`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ompay_db
    username: ompay_user
    password: votre_mot_de_passe
```

**Prod** (Neon Cloud) : `spring.profiles.active=prod`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://your-neon-host/ompay_db?sslmode=require
    username: ompay_user
    password: ${DB_PASSWORD}
```

### Variables d'Environnement
```bash
export JWT_SECRET=votre-secret-key-super-securise
export DB_PASSWORD=votre-mot-de-passe-db
```

---

## 📡 Endpoints API

### Base URL : `/api/v1`

### 🔐 Authentification

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/auth/register` | Inscription | ❌ |
| POST | `/auth/login` | Connexion | ❌ |
| GET | `/auth/verify?token=XXX` | Vérifier email | ❌ |
| POST | `/auth/resend-verification` | Renvoyer email | ❌ |

### 👤 Compte

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/account/profile` | Mon profil | ✅ |
| PUT | `/account/profile` | Modifier profil | ✅ |
| GET | `/account/balance` | Consulter solde | ✅ |
| GET | `/account/details` | Détails du compte | ✅ |

### 💸 Transferts

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/transfers/initiate` | Envoyer de l'argent | ✅ |
| GET | `/transfers/history` | Historique | ✅ |
| GET | `/transfers/{reference}` | Détails transfert | ✅ |

### 💵 Cash (Dépôts/Retraits)

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/cash/deposit` | Dépôt via agent | ✅ |
| POST | `/cash/withdrawal` | Retrait via agent | ✅ |
| GET | `/cash/history` | Historique cash | ✅ |

### 🧾 Factures

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/bills/pay` | Payer une facture | ✅ |
| GET | `/bills/history` | Historique factures | ✅ |

### 📱 Recharges

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/recharge` | Recharge crédit/internet | ✅ |
| GET | `/recharge/history` | Historique recharges | ✅ |

### 🏪 Marchands

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/merchant/pay` | Payer un marchand | ✅ |
| GET | `/merchant/history` | Historique paiements | ✅ |

### 🏦 Banque

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/bank/link` | Lier compte bancaire | ✅ |
| GET | `/bank/accounts` | Comptes liés | ✅ |
| DELETE | `/bank/unlink/{id}` | Délier compte | ✅ |
| POST | `/bank/transfer/to-bank` | Vers banque | ✅ |
| POST | `/bank/transfer/from-bank` | Depuis banque | ✅ |
| GET | `/bank/transfers/history` | Historique | ✅ |

### 📷 QR Code

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/qrcode/generate` | Mon QR Code | ✅ |
| POST | `/qrcode/generate/merchant` | QR Code marchand | ✅ |
| POST | `/qrcode/scan` | Scanner & payer | ✅ |

---

## 📚 Documentation

### Swagger UI

Une fois l'application lancée, accède à :

**http://localhost:8080/swagger-ui.html**

### OpenAPI JSON

**http://localhost:8080/api-docs**

---

## 🧪 Tests

### Exemples avec cURL

#### 1. Inscription
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Mohamed",
    "lastName": "Kouyate",
    "phoneNumber": "771234567",
    "email": "mohamed@example.com",
    "pin": "5678",
    "confirmPin": "5678",
    "password": "SecurePass123"
  }'
```

#### 2. Connexion
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "771234567",
    "password": "SecurePass123"
  }'
```

**Copie le `token` de la réponse pour les requêtes suivantes.**

#### 3. Consulter le Solde
```bash
curl -X GET http://localhost:8080/api/v1/account/balance \
  -H "Authorization: Bearer TON_TOKEN_JWT"
```

#### 4. Faire un Transfert
```bash
curl -X POST http://localhost:8080/api/v1/transfers/initiate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TON_TOKEN_JWT" \
  -d '{
    "amount": 5000,
    "receiverPhoneNumber": "781234567",
    "description": "Remboursement"
  }'
```

---

## 🚢 Déploiement

### Docker (Recommandé)
```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```
```bash
docker build -t om-pay-api .
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod om-pay-api
```

### Heroku
```bash
heroku create om-pay-api
heroku addons:create heroku-postgresql:mini
git push heroku main
```

### Railway / Render

Connecte ton repo GitHub et déploie automatiquement.

---

## 🤝 Contribuer

Les contributions sont les bienvenues ! Voici comment procéder :

1. Fork le projet
2. Crée une branche (`git checkout -b feature/AmazingFeature`)
3. Commit tes changements (`git commit -m 'Add: Amazing Feature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvre une Pull Request

---

## 📄 License

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

## 👤 Auteur

**Mohamed Kouyate**  
Développeur Fullstack | Spring Boot & Flutter  
📍 Dakar, Sénégal

- GitHub: [@ton-username](https://github.com/ton-username)
- LinkedIn: [Mohamed Kouyate](https://linkedin.com/in/ton-profil)
- Email: mohamed@example.com

---

## 🙏 Remerciements

- Orange Money Sénégal pour l'inspiration
- Spring Boot & Hibernate pour les frameworks robustes
- La communauté open-source

---

## 📊 Statistiques du Projet

- **Lignes de code** : ~10,000+
- **Endpoints** : 50+
- **Tables DB** : 12
- **Temps de développement** : 2 semaines
- **Couverture de tests** : À implémenter

---

**⭐ Si ce projet t'aide, n'hésite pas à le star sur GitHub !**