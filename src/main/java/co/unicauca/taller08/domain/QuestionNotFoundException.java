package co.unicauca.taller08.domain;

/** No existe una pregunta con el identificador solicitado. */
public class QuestionNotFoundException extends RuntimeException {

    public QuestionNotFoundException(long id) {
        super("No existe una pregunta con id " + id);
    }
}
