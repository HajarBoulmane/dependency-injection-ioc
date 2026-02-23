# Mini-Framework IoC : Injection de Dépendances

> Développement d'un moteur d'Inversion de Contrôle (IoC) personnalisé inspiré de Spring.

![Java](https://img.shields.io/badge/Java-17-orange?logo=java) ![Framework](https://img.shields.io/badge/Framework-Custom--IoC-blue) ![JAXB](https://img.shields.io/badge/XML-JAXB-green)

## Valeur Pédagogique
Ce projet permet de comprendre les mécanismes internes des frameworks modernes en implémentant :
* **Couplage Faible** : Séparation des interfaces et des implémentations.
* **Méta-programmation** : Utilisation intensive de la **Reflection API** pour l'instanciation dynamique.
* **OXM** : Mapping Objet-XML pour la configuration.

---

##  Fonctionnalités Clés

### 1. Configuration par Annotations
Le framework scanne les packages et détecte les composants automatiquement.
* `@Component` : Marque une classe pour être gérée par le framework.
* `@Autowired` : Injecte automatiquement la dépendance (Field Injection).

### 2. Configuration XML
Utilisation d'un fichier `applicationContext.xml` pour définir les beans et leurs liens.
* Support de l'injection par **Setter**.
* Support de l'injection par **Constructeur**.

---

##  Structure du Projet

```text
src/main/java
├── framework/           # Cœur du Framework
│   ├── annotations/     # @Component, @Autowired
│   ├── context/         # AnnotationApplicationContext, XmlApplicationContext
│   └── xml/             # BeanModel, BeansList (JAXB Models)
├── dao/                 # Data Access Layer (Interfaces & Impl)
├── metier/              # Business Layer (Interfaces & Impl)
└── presentation/        # Tests et démonstrations