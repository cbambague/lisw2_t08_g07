package co.unicauca.taller08.decorator;

/** Asocia la pregunta a una competencia evaluada. */
public class CompetencyQuestion extends QuestionDecorator {

    private final String competency;

    public CompetencyQuestion(QuestionComponent question, String competency) {
        super(question);
        if (competency == null || competency.isBlank()) {
            throw new IllegalArgumentException("La competencia no puede estar vacía");
        }
        this.competency = competency.trim();
    }

    @Override
    public String getDescription() {
        return question.getDescription() + " [COMPETENCIA: " + competency + "]";
    }
}
