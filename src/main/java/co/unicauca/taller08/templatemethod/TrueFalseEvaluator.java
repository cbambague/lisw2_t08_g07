package co.unicauca.taller08.templatemethod;

import co.unicauca.taller08.domain.InvalidAnswerException;
import co.unicauca.taller08.domain.Question;

import java.util.List;
import java.util.Set;

/** Verdadero/Falso: el estudiante selecciona Verdadero o Falso. */
public class TrueFalseEvaluator extends QuestionEvaluator {

    @Override
    protected void validate(Question question, List<String> answers) {
        requireAnswers(answers);
        if (answers.size() != 1) {
            throw new InvalidAnswerException("Debe responder únicamente Verdadero o Falso");
        }
        requireKnownOptions(question, answers);
    }

    @Override
    protected boolean compareAnswers(Question question, Set<String> selected) {
        return selected.equals(question.getCorrectAnswers());
    }

    @Override
    protected String buildFeedback(Question question, Set<String> selected, boolean correct) {
        return correct
                ? "¡Correcto! La afirmación es " + selected.iterator().next().toLowerCase() + "."
                : "Incorrecto. La respuesta correcta es "
                        + question.getCorrectAnswers().iterator().next() + ".";
    }
}
