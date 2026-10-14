package co.unicauca.taller08.domain;

/** La respuesta del usuario no es válida para la pregunta (paso 1 de la evaluación). */
public class InvalidAnswerException extends RuntimeException {

    public InvalidAnswerException(String message) {
        super(message);
    }
}
