package co.unicauca.taller08.templatemethod;

import co.unicauca.taller08.domain.InvalidAnswerException;
import co.unicauca.taller08.domain.Question;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Selección múltiple: el estudiante puede seleccionar varias respuestas.
 * Es correcta solo si selecciona exactamente el conjunto de respuestas correctas.
 */
public class MultipleChoiceEvaluator extends QuestionEvaluator {

    @Override
    protected void validate(Question question, List<String> answers) {
        requireAnswers(answers);
        requireKnownOptions(question, answers);
        Set<String> distinct = new HashSet<>();
        for (String answer : answers) {
            if (!distinct.add(question.findOption(answer))) {
                throw new InvalidAnswerException("La opción '" + answer + "' está repetida");
            }
        }
    }

    @Override
    protected boolean compareAnswers(Question question, Set<String> selected) {
        return selected.equals(question.getCorrectAnswers());
    }

    @Override
    protected String buildFeedback(Question question, Set<String> selected, boolean correct) {
        if (correct) {
            return "¡Respuesta correcta! Seleccionaste todas las opciones correctas.";
        }
        long hits = selected.stream().filter(question.getCorrectAnswers()::contains).count();
        long wrong = selected.size() - hits;
        long missing = question.getCorrectAnswers().size() - hits;
        return "Respuesta incorrecta: " + hits + " correcta(s), " + wrong + " incorrecta(s), "
                + missing + " faltante(s). Respuesta(s) correcta(s): "
                + String.join(", ", question.getCorrectAnswers());
    }
}
