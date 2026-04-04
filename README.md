# Injection des Dépendances & IoC

**Nom:** Hajar Boulmane

## Description
Application Java démontrant l'injection des dépendances et l'inversion de contrôle (IoC), avec un mini framework maison similaire à Spring.

## Partie 1 — Injection des dépendances

### 1. Instanciation statique (pres1)
Création manuelle des objets avec `new` et injection via setter.

### 2. Instanciation dynamique (pres2)
Utilisation de `Class.forName()` et `newInstance()` pour créer les objets dynamiquement à partir d'un fichier `config.txt`.

### 3. Spring XML (PresSpringXml)
Utilisation de `ClassPathXmlApplicationContext` avec `config.xml`.
Spring crée les objets et fait l'injection via `<property name="dao" ref="d"/>`.

### 4. Spring Annotations (presSpringAnnotation)
Utilisation de `AnnotationConfigApplicationContext`.
Beans déclarés avec `@Component`, injection avec `@Autowired` et `@Qualifier`.

## Partie 2 — Mini Framework IoC

Mini framework maison qui reproduit le comportement de Spring IOC.

### Fonctionnalités
- Lecture d'un fichier XML avec JAXB
- Annotations personnalisées : `@Component`, `@Autowired`, `@Qualifier`
- Injection via constructeur, setter et field

### Résultat
```
version base de donnees
575.0
```