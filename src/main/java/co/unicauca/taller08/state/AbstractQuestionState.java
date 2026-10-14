package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

/**
 * Comportamiento por defecto: toda operación está prohibida. Cada estado concreto
 * sobrescribe únicamente las transiciones que sí permite.
 */
public abstract class AbstractQuestionState implements QuestionState {

    @Override
    public void submitForReview(Question question) {
        deny("enviar a revisión");
    }

    @Override
    public void assignReviewer(Question question, String reviewer) {
        deny("asignar revisor");
    }

    @Override
    public void approve(Question question) {
        deny("aprobar");
    }

    @Override
    public void reject(Question question, String reason) {
        deny("rechazar");
    }

    @Override
    public void returnToDraft(Question question) {
        deny("devolver a borrador");
    }

    @Override
    public void publish(Question question) {
        deny("publicar");
    }

    @Override
    public void archive(Question question) {
        deny("archivar");
    }

    protected void deny(String operation) {
        throw new IllegalStateTransitionException(getName(), operation);
    }
}
