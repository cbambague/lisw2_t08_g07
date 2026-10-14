package co.unicauca.taller08.templatemethod;

import co.unicauca.taller08.domain.InvalidAnswerException;
import co.unicauca.taller08.domain.Question;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Template Method: define el algoritmo general de evaluación (Figura del proceso):
 * 1. validar respuesta, 2. obtener respuestas seleccionadas, 3. comparar con las
 * correctas, 4. determinar resultado, 5. generar retroalimentación.
 * <p>
 * El algoritmo vive aquí y es final; las subclases solo implementan los pasos que
 * cambian según el tipo de pregunta (validate y compareAnswers) y pueden ajustar
 * los ganchos (hooks) opcionales.
 */
public abstract class QuestionEvaluator {

    public final EvaluationResult evaluate(Question question, List<String> answers) {
        validate(question, answers);                                        // paso 1
        Set<String> selected = obtainSelectedAnswers(question, answers);    // paso 2
        boolean correct = compareAnswers(question, selected);               // paso 3
        double score = determineScore(question, correct);                   // paso 4
        String feedback = buildFeedback(question, selected, correct);       // paso 5
        return new EvaluationResult(correct, score, feedback, selected);
    }

    // ---- Pasos que cambian según el tipo de pregunta ----

    /** Lanza InvalidAnswerException si la respuesta no es válida para este tipo. */
    protected abstract void validate(Question question, List<String> answers);

    protected abstract boolean compareAnswers(Question question, Set<String> selected);

    // ---- Pasos comunes (ganchos que se pueden sobrescribir) ----

    protected Set<String> obtainSelectedAnswers(Question question, List<String> answers) {
        Set<String> selected = new LinkedHashSet<>();
        for (String answer : answers) {
            String option = question.findOption(answer);
            selected.add(option != null ? option : answer.trim());
        }
        return selected;
    }

    protected double determineScore(Question question, boolean correct) {
        return correct ? question.getScore() : 0.0;
    }

    protected String buildFeedback(Question question, Set<String> selected, boolean correct) {
        if (correct) {
            return "¡Respuesta correcta!";
        }
        return "Respuesta incorrecta. Respuesta(s) correcta(s): "
                + String.join(", ", question.getCorrectAnswers());
    }

    // ---- Utilidades de validación para las subclases ----

    protected void requireAnswers(List<String> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new InvalidAnswerException("Debe seleccionar al menos una respuesta");
        }
    }

    protected void requireKnownOptions(Question question, List<String> answers) {
        for (String answer : answers) {
            if (question.findOption(answer) == null) {
                throw new InvalidAnswerException(
                        "La respuesta '" + answer + "' no es una de las opciones de la pregunta");
            }
        }
    }
}
