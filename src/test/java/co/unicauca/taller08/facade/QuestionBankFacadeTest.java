package co.unicauca.taller08.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.unicauca.taller08.TestSupport;
import co.unicauca.taller08.adapter.ExternalQuestionAdapter;
import co.unicauca.taller08.adapter.ExternalQuestionService;
import co.unicauca.taller08.decorator.QuestionFeature;
import co.unicauca.taller08.domain.InvalidQuestionException;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionNotFoundException;
import co.unicauca.taller08.domain.QuestionType;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuestionBankFacadeTest {

    private final QuestionBankFacade facade = new QuestionBankFacade();

    @Test
    void createQuestionValidatesAndSaves() {
        Question saved = facade.createQuestion(TestSupport.singleChoice());
        assertEquals(1L, saved.getId());
        assertThrows(InvalidQuestionException.class,
                () -> facade.createQuestion(new Question(QuestionType.SINGLE_CHOICE, "Incompleta")));
    }

    @Test
    void completeFlowThroughTheFacade() {
        Question q = facade.createDraft(QuestionType.MULTIPLE_CHOICE, "¿Cuáles son lenguajes JVM?");
        for (String o : List.of("Java", "Kotlin", "Python")) {
            facade.addOption(q.getId(), o);
        }
        facade.defineCorrectAnswers(q.getId(), List.of("Java", "Kotlin"));
        facade.submitForReview(q.getId());
        facade.assignReviewer(q.getId(), "Revisor");
        facade.approveQuestion(q.getId());
        facade.publishQuestion(q.getId());

        assertTrue(facade.evaluateAnswer(q.getId(), List.of("Java", "Kotlin")).isCorrect());
        assertEquals(1, facade.findByState("Publicada").size());
    }

    @Test
    void unpublishedQuestionsCannotBeEvaluated() {
        Question q = facade.createQuestion(TestSupport.singleChoice());
        assertThrows(IllegalStateException.class, () -> facade.evaluateAnswer(q.getId(), List.of("Decorator")));
    }

    @Test
    void publishRequiresApproval() {
        Question q = facade.createQuestion(TestSupport.singleChoice());
        assertThrows(IllegalStateException.class, () -> facade.publishQuestion(q.getId()));
    }

    @Test
    void featuresAreAppliedThroughTheFacade() {
        Question q = facade.createQuestion(TestSupport.singleChoice());
        facade.addFeature(q.getId(), QuestionFeature.HIGH_DIFFICULTY, null);
        facade.addFeature(q.getId(), QuestionFeature.CONTEXT, "Patrones GoF");
        assertTrue(facade.describe(q.getId()).contains("[ALTA DIFICULTAD]"));
        assertTrue(facade.describe(q.getId()).contains("[CONTEXTO: Patrones GoF]"));
        assertEquals(1.5, facade.scoreOf(q.getId()));
    }

    @Test
    void importedQuestionsEnterTheBankAsDrafts() {
        Question q = facade.importQuestion(new ExternalQuestionAdapter(new ExternalQuestionService()));
        assertEquals("Borrador", facade.findById(q.getId()).getState().getName());
    }

    @Test
    void unknownIdsAreReported() {
        assertThrows(QuestionNotFoundException.class, () -> facade.findById(99));
    }
}
