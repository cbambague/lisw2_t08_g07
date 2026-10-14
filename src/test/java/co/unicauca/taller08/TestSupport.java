package co.unicauca.taller08;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;

import java.util.List;

/** Fábrica de preguntas de ejemplo para las pruebas. */
public final class TestSupport {

    private TestSupport() {
    }

    public static Question singleChoice() {
        Question q = new Question(QuestionType.SINGLE_CHOICE, "¿Cuál es un patrón estructural?");
        q.addOption("Decorator");
        q.addOption("State");
        q.addOption("Template Method");
        q.setCorrectAnswers(List.of("Decorator"));
        return q;
    }

    public static Question multipleChoice() {
        Question q = new Question(QuestionType.MULTIPLE_CHOICE, "¿Cuáles son patrones estructurales?");
        q.addOption("Decorator");
        q.addOption("Adapter");
        q.addOption("Facade");
        q.addOption("State");
        q.setCorrectAnswers(List.of("Decorator", "Adapter", "Facade"));
        return q;
    }

    public static Question trueFalse() {
        Question q = new Question(QuestionType.TRUE_FALSE, "State es un patrón de comportamiento.");
        q.setCorrectAnswers(List.of("Verdadero"));
        return q;
    }

    /** Lleva la pregunta hasta Publicada. */
    public static Question publish(Question q) {
        q.submitForReview();
        q.assignReviewer("Revisor");
        q.approve();
        q.publish();
        return q;
    }
}
