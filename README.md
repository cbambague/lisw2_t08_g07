# Taller 8 – Banco de Preguntas Saber PRO

Laboratorio de Ingeniería de Software II · Universidad del Cauca · Periodo 2-2026

Aplicación Java que gestiona preguntas de evaluación aplicando los patrones **State**,
**Template Method**, **Decorator**, **Adapter** y **Facade**.

## Cómo ejecutarlo

Requiere JDK 17 o superior.

**Desde el IDE** (IntelliJ / Eclipse / NetBeans / VS Code): abrir la carpeta como proyecto Maven y ejecutar
`co.unicauca.taller08.ApplicationMain`.

**Desde la terminal sin Maven:**

```bash
mkdir -p target/classes
javac -encoding UTF-8 -d target/classes $(find src/main/java -name '*.java')
java -cp target/classes co.unicauca.taller08.ApplicationMain        # demostración automática
java -cp target/classes co.unicauca.taller08.ApplicationMain menu   # menú interactivo
```

**Con Maven:** `mvn test` (pruebas JUnit 5) y luego ejecutar `ApplicationMain` desde el IDE.

## Estructura

```
lisw2_t08_g07/
└── src/
    ├── main/java/co/unicauca/taller08/
    │   ├── ApplicationMain.java      demo de los 10 requerimientos / menú
    │   ├── ConsoleMenu.java          menú interactivo (usa solo la Facade)
    │   ├── domain/                   Question, QuestionType, validador, repositorio, servicio, publicador
    │   ├── state/                    State: QuestionState + 6 estados
    │   ├── templatemethod/           Template Method: QuestionEvaluator + 3 evaluadores
    │   ├── decorator/                Decorator: QuestionComponent + 5 decoradores
    │   ├── adapter/                  Adapter: servicio externo, adaptador, importador
    │   └── facade/                   Facade: QuestionBankFacade
    └── test/java/                    32 pruebas JUnit 5
```

## Patrones aplicados

| # | Patrón | Dónde | Idea clave |
|---|--------|-------|------------|
| 1 | **State** | `state/` | `Question` delega en su `QuestionState`; cada estado permite solo sus transiciones (sin `if/else` en `Question`). |
| 2 | **Template Method** | `templatemethod/` | `QuestionEvaluator.evaluate()` es `final` y fija el algoritmo; cada tipo implementa `validate` y `compareAnswers`. |
| 3 | **Decorator** | `decorator/` | Se agregan características (alta dificultad, contexto, retroalimentación, competencia, marcada) sin tocar `Question`. |
| 4 | **Adapter** | `adapter/` | `ExternalQuestionAdapter` convierte el JSON externo al modelo `Question` mediante `QuestionProvider`. |
| 5 | **Facade** | `facade/` | `QuestionBankFacade` oculta servicio, validador, evaluadores, publicador e importador. |

## Ciclo de vida (State)

```
Borrador → Pendiente de revisión → En revisión → Aprobada → Publicada → Archivada
   ↑                                    │
   └──────────── Rechazada ←────────────┘
```

- Una pregunta solo puede usarse en una prueba (evaluarse) si está **Publicada**.
- El contenido solo se edita en **Borrador**.
- `submitForReview` valida que la pregunta esté completa antes de salir de Borrador.
- Operaciones no permitidas (p. ej. Borrador → Publicada, Publicada → Borrador) lanzan `IllegalStateTransitionException`.

## Cómo agregar un nuevo tipo de pregunta

1. Agregar la constante en `QuestionType` (con sus reglas de opciones y respuestas correctas).
2. Crear su evaluador extendiendo `QuestionEvaluator` y registrarlo en `QuestionEvaluatorFactory`.

No hay que modificar `Question`, los estados, la Facade ni el validador.
