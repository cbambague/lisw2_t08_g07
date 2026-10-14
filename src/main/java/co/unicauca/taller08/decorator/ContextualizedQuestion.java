package co.unicauca.taller08.decorator;

/** Agrega un texto de contexto a la pregunta. */
public class ContextualizedQuestion extends QuestionDecorator {

    private final String context;

    public ContextualizedQuestion(QuestionComponent question, String context) {
        super(question);
        if (context == null || context.isBlank()) {
            throw new IllegalArgumentException("El contexto no puede estar vacío");
        }
        this.context = context.trim();
    }

    @Override
    public String getDescription() {
        return question.getDescription() + " [CONTEXTO: " + context + "]";
    }
}
