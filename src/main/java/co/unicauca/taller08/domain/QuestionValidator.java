package co.unicauca.taller08.domain;

import java.util.ArrayList;
import java.util.List;

/** Valida que una pregunta esté completa según las reglas de su tipo. */
public class QuestionValidator {

    public void validate(Question question) {
        if (question == null) {
            throw new InvalidQuestionException("La pregunta no puede ser null");
        }
        List<String> errors = new ArrayList<>();
        QuestionType type = question.getType();

        if (question.getStatement() == null || question.getStatement().isBlank()) {
            errors.add("el enunciado es obligatorio");
        }
        if (question.getOptions().size() < type.getMinOptions()) {
            errors.add("se requieren al menos " + type.getMinOptions() + " opciones");
        }
        int correct = question.getCorrectAnswers().size();
        if (correct < type.getMinCorrect()) {
            errors.add("debe definir al menos " + type.getMinCorrect() + " respuesta(s) correcta(s)");
        }
        if (correct > type.getMaxCorrect()) {
            errors.add("admite como máximo " + type.getMaxCorrect() + " respuesta(s) correcta(s)");
        }
        for (String answer : question.getCorrectAnswers()) {
            if (question.findOption(answer) == null) {
                errors.add("la respuesta correcta '" + answer + "' no está entre las opciones");
            }
        }
        if (!errors.isEmpty()) {
            throw new InvalidQuestionException(
                    "Pregunta inválida (" + type.getDisplayName() + "): " + String.join("; ", errors));
        }
    }
}
