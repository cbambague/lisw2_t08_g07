package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

/** En revisión: el revisor analiza la pregunta y la aprueba o la rechaza. */
public class ReviewState extends AbstractQuestionState {

    @Override
    public void approve(Question question) {
        question.setRejectionReason(null);
        question.setState(new ApprovedState());
    }

    @Override
    public void reject(Question question, String reason) {
        question.setRejectionReason(reason);
        question.setState(new RejectedState());
    }

    @Override
    public String getName() {
        return "En revisión";
    }
}
