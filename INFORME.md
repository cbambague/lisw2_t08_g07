# Informe – Taller 8: Patrones de Diseño, de Comportamiento y Estructurales

**Asignatura:** Laboratorio de Ingeniería de Software II – Programa de Ingeniería de Sistemas, Universidad del Cauca (2-2026)
**Integrantes:** Carlos Bambague
**Repositorio GitHub:** https://github.com/cbambague/lisw2_t08_g07

## 1. Resumen del proyecto de clase

El Banco de Preguntas Saber PRO gestiona preguntas que se usan para construir instrumentos de evaluación.
Soporta preguntas de selección múltiple con única respuesta, de selección múltiple con múltiples respuestas
y de Verdadero/Falso, y está diseñado para incorporar nuevos tipos sin modificar el código existente.
Las preguntas pasan por un ciclo de vida (Borrador, Pendiente de revisión, En revisión, Aprobada/Rechazada,
Publicada, Archivada) y solo las publicadas pueden usarse en una prueba.

## 2. Patrones aplicados

**State – ciclo de vida de una pregunta.** `Question` mantiene un `QuestionState` y delega en él las
operaciones (`submitForReview`, `assignReviewer`, `approve`, `reject`, `returnToDraft`, `publish`, `archive`).
Cada estado (`DraftState`, `PendingReviewState`, `ReviewState`, `RejectedState`, `ApprovedState`,
`PublishedState`, `ArchivedState`) permite solo sus transiciones y rechaza las demás con
`IllegalStateTransitionException`, evitando cadenas de `if/else` en `Question`.

**Template Method – evaluación de respuestas.** `QuestionEvaluator.evaluate()` es un método `final` con el
algoritmo general (validar, obtener respuestas seleccionadas, comparar, determinar puntaje, generar
retroalimentación). `SingleChoiceEvaluator`, `MultipleChoiceEvaluator` y `TrueFalseEvaluator` implementan
únicamente los pasos que cambian (`validate` y `compareAnswers`) y, opcionalmente, la retroalimentación.

**Decorator – características adicionales.** `QuestionComponent` es implementado por `Question` y por
`QuestionDecorator`. Los decoradores `HighDifficultyQuestion`, `ContextualizedQuestion`, `FeedbackQuestion`,
`CompetencyQuestion` y `MarkedForReviewQuestion` se apilan dinámicamente sin modificar `Question`.

**Adapter – preguntas de sistemas externos.** `ExternalQuestionService` devuelve JSON con otro formato.
`ExternalQuestionAdapter` implementa `QuestionProvider` y lo transforma al modelo interno `Question`
(única respuesta, múltiples respuestas o Verdadero/Falso según los campos recibidos).

**Facade – interacción simplificada.** `QuestionBankFacade` ofrece las operaciones principales (crear,
consultar, cambiar estado, evaluar, decorar, importar) y oculta `QuestionService`, `QuestionValidator`,
`QuestionEvaluator`, `QuestionPublisher` e `QuestionImporter`.

## 3. Requerimientos funcionales

| # | Requerimiento | Dónde se cumple |
|---|---------------|-----------------|
| 1–2 | Crear pregunta y seleccionar tipo | `facade.createDraft(type, enunciado)` |
| 3–4 | Opciones y respuestas correctas | `facade.addOption`, `facade.defineCorrectAnswers` |
| 5 | Consultar preguntas | `facade.findAll / findById / findByState` |
| 6 | Cambiar el estado | `facade.submitForReview … archiveQuestion` (State) |
| 7 | Evaluar una respuesta | `facade.evaluateAnswer` (Template Method) |
| 8 | Agregar características | `facade.addFeature` (Decorator) |
| 9 | Importar una pregunta externa | `facade.importQuestion(new ExternalQuestionAdapter(...))` |
| 10 | Usar la Facade | `ApplicationMain` y `ConsoleMenu` solo usan `QuestionBankFacade` |

## 4. Pruebas

32 pruebas JUnit 5 cubren las transiciones permitidas y no permitidas, la evaluación por tipo, los
decoradores, el adaptador y la Facade.
