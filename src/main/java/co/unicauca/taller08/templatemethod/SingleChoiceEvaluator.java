package co.unicauca.taller08.templatemethod;

import co.unicauca.taller08.domain.InvalidAnswerException;
import co.unicauca.taller08.domain.Question;

import java.util.List;
import java.util.Set;

/** Selección única: el estudiante debe seleccionar exactamente una respuesta. */
public class SingleChoiceEvaluator extends QuestionEvaluator {

    @Override
    protected void validate(Question question, List<String> answers) {
        requireAnswers(answers);
        if (answers.size() != 1) {
            throw new InvalidAnswerException("Esta pregunta admite exactamente una respuesta");
        }
        requireKnownOptions(question, answers);
    }

    @Override
    protected boolean compareAnswers(Question question, Set<String> selected) {
        return selected.equals(question.getCorrectAnswers());
    }
}
