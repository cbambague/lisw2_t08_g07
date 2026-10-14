package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

/**
 * Estado del ciclo de vida de una pregunta (Figura 1 de la guía).
 * Cada estado decide qué operaciones permite y a qué estado transita.
 */
public interface QuestionState {

    /** Borrador -> Pendiente de revisión (valida que la pregunta esté completa). */
    void submitForReview(Question question);

    /** Pendiente de revisión -> En revisión (se asigna un revisor). */
    void assignReviewer(Question question, String reviewer);

    /** En revisión -> Aprobada. */
    void approve(Question question);

    /** En revisión -> Rechazada. */
    void reject(Question question, String reason);

    /** Rechazada -> Borrador (para realizar los ajustes). */
    void returnToDraft(Question question);

    /** Aprobada -> Publicada. */
    void publish(Question question);

    /** Publicada -> Archivada. */
    void archive(Question question);

    String getName();

    /** Solo en Borrador se puede modificar el contenido de la pregunta. */
    default boolean isEditable() {
        return false;
    }

    /** Solo una pregunta aprobada y publicada puede usarse en una prueba. */
    default boolean canBeUsedInTest() {
        return false;
    }
}
