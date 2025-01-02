# Spécifications Fonctionnelles – Students City

## 1. Présentation
**Students City** est une application mobile destinée aux étudiants, disponible uniquement sur **Android** et accessible **sur invitation**. Elle permet de trouver et partager les meilleurs lieux (restaurants, bars, salles de sport, etc.) autour de soi, recommandés par la communauté étudiante.

## 2. Plateformes & Accès
- **Application Android** (invitation nécessaire pour créer un compte).
- **API HTTP Backend** pour la gestion des données et la synchronisation.
- **Interface Web (optionnelle)** pour l'administration et la modération.

## 3. Fonctionnalités Principales

### 3.1 Authentification & Invitations
- **Système d’invitation** (par email ou autre moyen) pour créer un compte.
- **Connexion / Inscription** réservée aux étudiants invités.
- **Gestion de profil** : photo de profil, infos basiques (pseudo, email).

### 3.2 Recherche & Navigation
- **Carte interactive** affichant les lieux recommandés (restaurants, bars, salles de sport, etc.).
- **Liste des établissements** avec distance en kilomètres.
- **Filtres de recherche** (ex. type de lieu, fourchette de prix, horaires).
- **Localisation** : utilisation de la position pour afficher les établissements à proximité.

### 3.3 Contributions & Avis
- **Ajout de nouveaux établissements** par les utilisateurs (soumis à modération) et directement uniquement par l'admin.
- **Commentaires** : possibilité de laisser un avis textuel sur chaque lieu (ex. ambiance, prix, horaires).
- **Notations : système de notes (ex. étoiles) pour compléter les commentaires.

### 3.4 Modération & Administration
- **Modération des commentaires** (via l’app ou un back-office Web).
- **Gestion des invitations** : envoi, validation ou refus des inscriptions.

### 3.5 Autres Évolutions Potentielles
- **Notifications push** : informer de nouveaux commentaires ou de lieux à découvrir.
- **Système de favoris** : enregistrer des établissements préférés.
- **Statistiques** : consultation des lieux les plus populaires auprès de la communauté.
- **Chat** : discussion entre étudiants
- **Organisation de soirée**: création d'événement lié à un établissement.

## 4. Contraintes Techniques
- **Application Android Native** (Java).
- **API REST** pour la communication avec le backend.
- **Base de données** : stockage des utilisateurs, établissements, commentaires et invitations (SQL).

## 5. Conclusion
Students City vise à devenir un outil pratique et communautaire pour faciliter la vie étudiante : trouver rapidement les bons plans, partager ses retours, et renforcer la communauté via une plateforme sécurisée et exclusivement réservée aux étudiants.
Lancement uniquement sur la ville de Dax pour commencer.
