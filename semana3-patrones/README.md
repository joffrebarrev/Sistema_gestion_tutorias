# Semana 3 | Implementación comparativa de patrones de diseño

**Universidad Espíritu Santo — Diseño de Software (UCOM0310)**
**Ae2 | Factory Method y Builder aplicados al Sistema de gestión de tutorías**

Estudiante: Joffre Barre Veliz

## Propósito

Implementar y comparar los patrones creacionales **Factory Method** y **Builder**
sobre problemas concretos del Sistema de gestión de tutorías, con trazabilidad
completa entre problema, decisión de diseño, UML e implementación en Java.

## Caso base

El sistema requiere distintos mecanismos de notificación (Email, Push, SMS, Teams)
y una `Reserva` cuya configuración incluye múltiples datos obligatorios y opcionales.

## Estructura del repositorio

```
semana3-patrones/
├── README.md
├── pom.xml
├── docs/
│   ├── factory-method.puml
│   ├── factory-method.png
│   ├── builder.puml
│   └── builder.png
└── src/
    ├── main/
    │   └── java/
    │       └── edu/uees/patrones/
    │           ├── factory/   → Notificador, ConcreteProducts, Creator, ConcreteCreators, AppFactory
    │           └── builder/   → Modalidad, Prioridad, Reserva, ReservaBuilder, AppBuilder
    └── test/
        └── java/
```

## Parte A — Factory Method

Resuelve la creación de distintos mecanismos de notificación (`Notificador`)
sin dispersar por el código el conocimiento de las clases concretas
(`NotificadorEmail`, `NotificadorPush`, `NotificadorSms`, `NotificadorTeams`).

Ejecutar:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="edu.uees.patrones.factory.AppFactory"
```

## Parte B — Builder

Resuelve la construcción progresiva de una `Reserva` con campos obligatorios
(`estudiante`, `docente`, `fechaHora`, `modalidad`) y opcionales con valores
por defecto, validados antes de construir el objeto.

Ejecutar:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="edu.uees.patrones.builder.AppBuilder"
```

## Diagramas UML

Ver `docs/factory-method.png` y `docs/builder.png` (generados a partir de
`docs/*.puml` con PlantUML).

## Comparación técnica

Ver el documento de análisis entregado en Blackboard
(`Ae2_BarreJoffre_ImplementacionComparativaPatrones.pdf`), sección Parte D.

## Declaración de uso de IA

Ver el documento de análisis entregado en Blackboard, sección 12.
