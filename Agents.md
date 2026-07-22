# AGENTS

Das ist das Repository zu kmpRSS. Ein Open-Source RSS Reader für iOS & Android.
Alles ist hier in best practice für Kotlin-Multiplatform geschrieben. UI wird ausschliesslich mittels Compose-Multiplatform geschrieben.
Die App MUSS Accessability berücksichtigen. Wir halten uns an best practices für das gebaute OS. iOS app kann UND SOLL eine andere UX haben als die Android app.

## Generelle Regeln

- Code gehört dokumentiert
- Pflege einen /docs Ordner.
    - Schreibe eine .md, die für Agents gedacht ist. Schreibe eine .html Version dieser md, die für Menschen gedacht ist. HTML ist übersichtlich und information dense. Sie zeigt auch schaubilder, falls welche gibt. Für Architektur nutzen wir Graphen.
- Verwende eine .editorConfig mit Regeln, die wir zum Formatieren nutzen.
- Code kommt auf GitHub -> Github Actions werden für Pipeline verwendet.
- Android richtet sich nach Material 3 Expressive
- iOS richtet sich nach Liquid Glass
- UI Komponenten müssen nicht immer geteilt werden
- Entkoppel deinen Code, sodass er Unit-Testbar ist.
- Feature-Driven entwicklung.
- TDD entwickeln.
- Erklärende Commit-Messages und Commits. Nicht einen riesigen, sondern mehrere kleine.
- Auf Android MUSS Proguard aktiv sein, um diese Optimierungen zu verwenden.
- Nicht unnötig viele Dependencies ziehen, ist es nur 1-2 Funktionen die da herkommen würden, implementieren wir es selbst.

## Libraries

- Coil für Images
- Multi Platform Markdown Renderer
- KTOR
- Corotuines
- ...?
