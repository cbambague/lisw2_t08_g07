package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

/** Pendiente de revisión: contenido completo, a la espera de que se asigne un revisor. */
public class PendingReviewState extends AbstractQuestionState {

    @Override
    public void assignReviewer(Question question, String reviewer) {
        if (reviewer == null || reviewer.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el revisor");
        }
        question.setReviewer(reviewer.trim());
        question.setState(new ReviewState());
    }

    @Override
    public String getName() {
        return "Pendiente de revisión";
    }
}
