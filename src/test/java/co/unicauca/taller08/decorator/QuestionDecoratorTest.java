package co.unicauca.taller08.decorator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.unicauca.taller08.TestSupport;
import co.unicauca.taller08.domain.Question;
import org.junit.jupiter.api.Test;

class QuestionDecoratorTest {

    @Test
    void decoratorAddsDescriptionWithoutModifyingTheQuestion() {
        Question q = TestSupport.singleChoice();
        QuestionComponent decorated = new HighDifficultyQuestion(q);
        assertEquals(q.getStatement() + " [ALTA DIFICULTAD]", decorated.getDescription());
        assertEquals(q.getStatement(), q.getDescription());
    }

    @Test
    void decoratorsCanBeStackedInAnyOrder() {
        QuestionComponent c = new MarkedForReviewQuestion(
                new CompetencyQuestion(new HighDifficultyQuestion(TestSupport.singleChoice()), "Arquitectura"));
        String description = c.getDescription();
        assertTrue(description.contains("[ALTA DIFICULTAD]"));
        assertTrue(description.contains("[COMPETENCIA: Arquitectura]"));
        assertTrue(description.endsWith("[MARCADA PARA REVISIÓN]"));
    }

    @Test
    void highDifficultyRaisesTheScore() {
        assertEquals(1.5, new HighDifficultyQuestion(TestSupport.singleChoice()).getScore());
    }

    @Test
    void featuresThatNeedAValueRejectBlankValues() {
        assertThrows(IllegalArgumentException.class,
                () -> QuestionFeature.CONTEXT.applyTo(TestSupport.singleChoice(), " "));
    }

    @Test
    void featureEnumBuildsTheRightDecorator() {
        QuestionComponent c = QuestionFeature.FEEDBACK.applyTo(TestSupport.trueFalse(), "Repasar State");
        assertTrue(c.getDescription().contains("[RETROALIMENTACIÓN: Repasar State]"));
    }
}
