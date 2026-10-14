package co.unicauca.taller08.decorator;

/**
 * Catálogo de características que se pueden agregar dinámicamente a una pregunta.
 * Cada constante sabe cómo envolver un componente con su decorador, de modo que
 * agregar una característica nueva no obliga a modificar el resto del sistema.
 */
public enum QuestionFeature {

    HIGH_DIFFICULTY("Alta dificultad", false) {
        @Override
        public QuestionComponent applyTo(QuestionComponent component, String value) {
            return new HighDifficultyQuestion(component);
        }
    },
    CONTEXT("Contextualizada", true) {
        @Override
        public QuestionComponent applyTo(QuestionComponent component, String value) {
            return new ContextualizedQuestion(component, value);
        }
    },
    FEEDBACK("Con retroalimentación", true) {
        @Override
        public QuestionComponent applyTo(QuestionComponent component, String value) {
            return new FeedbackQuestion(component, value);
        }
    },
    COMPETENCY("Asociada a una competencia", true) {
        @Override
        public QuestionComponent applyTo(QuestionComponent component, String value) {
            return new CompetencyQuestion(component, value);
        }
    },
    MARKED_FOR_REVIEW("Marcada para revisión", false) {
        @Override
        public QuestionComponent applyTo(QuestionComponent component, String value) {
            return new MarkedForReviewQuestion(component);
        }
    };

    private final String displayName;
    private final boolean needsValue;

    QuestionFeature(String displayName, boolean needsValue) {
        this.displayName = displayName;
        this.needsValue = needsValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean needsValue() {
        return needsValue;
    }

    public abstract QuestionComponent applyTo(QuestionComponent component, String value);
}
