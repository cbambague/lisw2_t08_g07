package co.unicauca.taller08.adapter;

/** El JSON externo no tiene la información necesaria para construir una pregunta. */
public class ExternalQuestionFormatException extends RuntimeException {

    public ExternalQuestionFormatException(String message) {
        super(message);
    }
}
