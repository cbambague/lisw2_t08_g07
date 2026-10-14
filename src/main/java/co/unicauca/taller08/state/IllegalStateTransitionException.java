package co.unicauca.taller08.state;

/** La operación no está permitida en el estado actual de la pregunta. */
public class IllegalStateTransitionException extends IllegalStateException {

    public IllegalStateTransitionException(String stateName, String operation) {
        super("Operación no permitida: no se puede " + operation
                + " cuando la pregunta está en estado " + stateName);
    }
}
