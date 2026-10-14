package co.unicauca.taller08.templatemethod;

import java.util.Collections;
import java.util.Set;

/** Resultado de evaluar la respuesta de un estudiante. */
public class EvaluationResult {

    private final boolean correct;
    private final double score;
    private final String feedback;
    private final Set<String> selectedAnswers;

    public EvaluationResult(boolean correct, double score, String feedback, Set<String> selectedAnswers) {
        this.correct = correct;
        this.score = score;
        this.feedback = feedback;
        this.selectedAnswers = Collections.unmodifiableSet(selectedAnswers);
    }

    public boolean isCorrect() {
        return correct;
    }

    public double getScore() {
        return score;
    }

    public String getFeedback() {
        return feedback;
    }

    public Set<String> getSelectedAnswers() {
        return selectedAnswers;
    }

    @Override
    public String toString() {
        return (correct ? "CORRECTA" : "INCORRECTA") + " | puntaje " + score + " | " + feedback;
    }
}
