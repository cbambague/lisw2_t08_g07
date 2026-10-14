package co.unicauca.taller08.templatemethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.unicauca.taller08.TestSupport;
import co.unicauca.taller08.domain.InvalidAnswerException;
import co.unicauca.taller08.domain.QuestionType;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuestionEvaluatorTest {

    private final QuestionEvaluatorFactory factory = new QuestionEvaluatorFactory();

    @Test
    void singleChoiceCorrectAndIncorrect() {
        QuestionEvaluator ev = factory.forType(QuestionType.SINGLE_CHOICE);
        assertTrue(ev.evaluate(TestSupport.singleChoice(), List.of("Decorator")).isCorrect());
        assertFalse(ev.evaluate(TestSupport.singleChoice(), List.of("State")).isCorrect());
    }

    @Test
    void singleChoiceRejectsSeveralAnswersAndUnknownOptions() {
        QuestionEvaluator ev = factory.forType(QuestionType.SINGLE_CHOICE);
        assertThrows(InvalidAnswerException.class,
                () -> ev.evaluate(TestSupport.singleChoice(), List.of("Decorator", "State")));
        assertThrows(InvalidAnswerException.class,
                () -> ev.evaluate(TestSupport.singleChoice(), List.of("Observer")));
        assertThrows(InvalidAnswerException.class, () -> ev.evaluate(TestSupport.singleChoice(), List.of()));
    }

    @Test
    void multipleChoiceRequiresExactlyTheCorrectSet() {
        QuestionEvaluator ev = factory.forType(QuestionType.MULTIPLE_CHOICE);
        assertTrue(ev.evaluate(TestSupport.multipleChoice(), List.of("Facade", "Decorator", "Adapter")).isCorrect());
        assertFalse(ev.evaluate(TestSupport.multipleChoice(), List.of("Decorator", "Adapter")).isCorrect());
        assertFalse(ev.evaluate(TestSupport.multipleChoice(), List.of("Decorator", "Adapter", "Facade", "State"))
                .isCorrect());
    }

    @Test
    void multipleChoiceRejectsDuplicates() {
        QuestionEvaluator ev = factory.forType(QuestionType.MULTIPLE_CHOICE);
        assertThrows(InvalidAnswerException.class,
                () -> ev.evaluate(TestSupport.multipleChoice(), List.of("Decorator", "decorator")));
    }

    @Test
    void trueFalseIsCaseInsensitive() {
        QuestionEvaluator ev = factory.forType(QuestionType.TRUE_FALSE);
        assertTrue(ev.evaluate(TestSupport.trueFalse(), List.of("verdadero")).isCorrect());
        assertFalse(ev.evaluate(TestSupport.trueFalse(), List.of("Falso")).isCorrect());
        assertThrows(InvalidAnswerException.class,
                () -> ev.evaluate(TestSupport.trueFalse(), List.of("Tal vez")));
    }

    @Test
    void scoreAndFeedbackAreGenerated() {
        EvaluationResult ok = factory.forType(QuestionType.SINGLE_CHOICE)
                .evaluate(TestSupport.singleChoice(), List.of("Decorator"));
        assertEquals(1.0, ok.getScore());
        EvaluationResult bad = factory.forType(QuestionType.SINGLE_CHOICE)
                .evaluate(TestSupport.singleChoice(), List.of("State"));
        assertEquals(0.0, bad.getScore());
        assertTrue(bad.getFeedback().contains("Decorator"));
    }
}
