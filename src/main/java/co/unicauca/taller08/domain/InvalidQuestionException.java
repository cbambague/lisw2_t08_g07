package co.unicauca.taller08.domain;

/** La pregunta no cumple las reglas de su tipo. */
public class InvalidQuestionException extends RuntimeException {

    public InvalidQuestionException(String message) {
        super(message);
    }
}
