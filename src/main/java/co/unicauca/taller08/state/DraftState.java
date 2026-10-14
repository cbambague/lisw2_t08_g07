package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionValidator;

/** Borrador: la pregunta se crea y se edita; al completarla pasa a Pendiente de revisión. */
public class DraftState extends AbstractQuestionState {

    @Override
    public void submitForReview(Question question) {
        new QuestionValidator().validate(question);
        question.setState(new PendingReviewState());
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public String getName() {
        return "Borrador";
    }
}
