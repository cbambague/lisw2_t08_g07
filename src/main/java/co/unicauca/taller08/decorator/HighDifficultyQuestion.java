package co.unicauca.taller08.decorator;

/** Marca la pregunta como de alta dificultad y aumenta su puntaje x1.5. */
public class HighDifficultyQuestion extends QuestionDecorator {

    public static final double SCORE_MULTIPLIER = 1.5;

    public HighDifficultyQuestion(QuestionComponent question) {
        super(question);
    }

    @Override
    public String getDescription() {
        return question.getDescription() + " [ALTA DIFICULTAD]";
    }

    @Override
    public double getScore() {
        return question.getScore() * SCORE_MULTIPLIER;
    }
}
