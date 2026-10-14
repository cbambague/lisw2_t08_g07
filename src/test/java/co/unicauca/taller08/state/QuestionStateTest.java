package co.unicauca.taller08.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.unicauca.taller08.TestSupport;
import co.unicauca.taller08.domain.InvalidQuestionException;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuestionStateTest {

    @Test
    void newQuestionStartsInDraft() {
        assertEquals("Borrador", TestSupport.singleChoice().getState().getName());
    }

    @Test
    void fullLifecycleFollowsTheDiagram() {
        Question q = TestSupport.singleChoice();
        q.submitForReview();
        assertEquals("Pendiente de revisión", q.getState().getName());
        q.assignReviewer("Revisor");
        assertEquals("En revisión", q.getState().getName());
        q.approve();
        assertEquals("Aprobada", q.getState().getName());
        q.publish();
        assertEquals("Publicada", q.getState().getName());
        q.archive();
        assertEquals("Archivada", q.getState().getName());
    }

    @Test
    void rejectedQuestionReturnsToDraft() {
        Question q = TestSupport.singleChoice();
        q.submitForReview();
        q.assignReviewer("Revisor");
        q.reject("Enunciado ambiguo");
        assertEquals("Rechazada", q.getState().getName());
        assertEquals("Enunciado ambiguo", q.getRejectionReason());
        q.returnToDraft();
        assertEquals("Borrador", q.getState().getName());
    }

    @Test
    void draftCannotBePublished() {
        assertThrows(IllegalStateTransitionException.class, () -> TestSupport.singleChoice().publish());
    }

    @Test
    void publishedCannotReturnToDraft() {
        Question q = TestSupport.publish(TestSupport.singleChoice());
        assertThrows(IllegalStateTransitionException.class, q::returnToDraft);
    }

    @Test
    void draftCannotBeApprovedDirectly() {
        assertThrows(IllegalStateTransitionException.class, () -> TestSupport.singleChoice().approve());
    }

    @Test
    void archivedAllowsNothing() {
        Question q = TestSupport.publish(TestSupport.singleChoice());
        q.archive();
        assertThrows(IllegalStateTransitionException.class, q::publish);
        assertThrows(IllegalStateTransitionException.class, q::archive);
        assertThrows(IllegalStateTransitionException.class, q::returnToDraft);
    }

    @Test
    void onlyPublishedQuestionsCanBeUsedInATest() {
        Question q = TestSupport.singleChoice();
        assertFalse(q.getState().canBeUsedInTest());
        TestSupport.publish(q);
        assertTrue(q.getState().canBeUsedInTest());
    }

    @Test
    void incompleteQuestionCannotLeaveDraft() {
        Question q = new Question(QuestionType.SINGLE_CHOICE, "Sin opciones");
        assertThrows(InvalidQuestionException.class, q::submitForReview);
        q.addOption("A");
        q.addOption("B");
        assertThrows(InvalidQuestionException.class, q::submitForReview); // falta respuesta correcta
        q.setCorrectAnswers(List.of("A"));
        q.submitForReview();
        assertEquals("Pendiente de revisión", q.getState().getName());
    }

    @Test
    void contentCanOnlyBeEditedInDraft() {
        Question q = TestSupport.singleChoice();
        q.submitForReview();
        assertThrows(IllegalStateTransitionException.class, () -> q.addOption("Otra"));
    }
}
