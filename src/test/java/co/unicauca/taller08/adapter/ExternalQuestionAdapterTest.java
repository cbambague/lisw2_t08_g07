package co.unicauca.taller08.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExternalQuestionAdapterTest {

    @Test
    void adaptsTheDefaultExternalQuestion() {
        Question q = new ExternalQuestionAdapter(new ExternalQuestionService()).getQuestion();
        assertEquals("¿Cuál es la capital de Colombia?", q.getStatement());
        assertEquals(QuestionType.SINGLE_CHOICE, q.getType());
        assertEquals(List.of("Bogotá", "Medellín", "Cali", "Barranquilla"), q.getOptions());
        assertEquals(List.of("Bogotá"), List.copyOf(q.getCorrectAnswers()));
        assertEquals("Borrador", q.getState().getName());
    }

    @Test
    void adaptsTrueFalseQuestions() {
        String json = "{\"statement\": \"La Tierra es plana\", \"answer\": \"false\"}";
        Question q = new ExternalQuestionAdapter(new ExternalQuestionService(json)).getQuestion();
        assertEquals(QuestionType.TRUE_FALSE, q.getType());
        assertEquals(List.of("Falso"), List.copyOf(q.getCorrectAnswers()));
    }

    @Test
    void adaptsMultipleAnswerQuestions() {
        String json = "{\"statement\": \"Colores primarios\", \"answers\": [\"Rojo\", \"Azul\"],"
                + " \"options\": [\"Rojo\", \"Azul\", \"Verde\"]}";
        Question q = new ExternalQuestionAdapter(new ExternalQuestionService(json)).getQuestion();
        assertEquals(QuestionType.MULTIPLE_CHOICE, q.getType());
        assertEquals(2, q.getCorrectAnswers().size());
    }

    @Test
    void rejectsPayloadsWithoutStatementOrAnswer() {
        assertThrows(ExternalQuestionFormatException.class,
                () -> new ExternalQuestionAdapter(new ExternalQuestionService("{\"answer\": \"x\"}")).getQuestion());
        assertThrows(ExternalQuestionFormatException.class,
                () -> new ExternalQuestionAdapter(new ExternalQuestionService("{\"statement\": \"x\"}")).getQuestion());
    }
}
