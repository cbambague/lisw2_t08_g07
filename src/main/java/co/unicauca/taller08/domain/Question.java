package co.unicauca.taller08.domain;

import co.unicauca.taller08.decorator.QuestionComponent;
import co.unicauca.taller08.state.DraftState;
import co.unicauca.taller08.state.IllegalStateTransitionException;
import co.unicauca.taller08.state.QuestionState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Pregunta del banco. Mantiene su estado (patrón State) y delega en él las
 * operaciones del ciclo de vida, por lo que no contiene cadenas if/else por estado.
 * También es el componente concreto del patrón Decorator.
 */
public class Question implements QuestionComponent {

    private Long id;
    private final QuestionType type;
    private String statement;
    private final List<String> options = new ArrayList<>();
    private final Set<String> correctAnswers = new LinkedHashSet<>();
    private QuestionState state;
    private final List<String> stateHistory = new ArrayList<>();
    private String reviewer;
    private String rejectionReason;

    public Question(QuestionType type, String statement) {
        if (type == null) {
            throw new IllegalArgumentException("El tipo de pregunta es obligatorio");
        }
        this.type = type;
        this.statement = statement;
        this.options.addAll(type.getFixedOptions());
        setState(new DraftState());
    }

    // ----------------------------------------------------------------- State

    public void setState(QuestionState state) {
        this.state = state;
        this.stateHistory.add(state.getName());
    }

    public QuestionState getState() {
        return state;
    }

    public List<String> getStateHistory() {
        return Collections.unmodifiableList(stateHistory);
    }

    public void submitForReview() {
        state.submitForReview(this);
    }

    public void assignReviewer(String reviewer) {
        state.assignReviewer(this, reviewer);
    }

    public void approve() {
        state.approve(this);
    }

    public void reject(String reason) {
        state.reject(this, reason);
    }

    public void returnToDraft() {
        state.returnToDraft(this);
    }

    public void publish() {
        state.publish(this);
    }

    public void archive() {
        state.archive(this);
    }

    // ------------------------------------------------------------- Contenido

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionType getType() {
        return type;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        requireEditable("modificar el enunciado");
        this.statement = statement;
    }

    public List<String> getOptions() {
        return Collections.unmodifiableList(options);
    }

    public Set<String> getCorrectAnswers() {
        return Collections.unmodifiableSet(correctAnswers);
    }

    public void addOption(String text) {
        requireEditable("agregar opciones");
        if (type.hasFixedOptions()) {
            throw new IllegalStateException(
                    "Las preguntas de tipo " + type.getDisplayName() + " tienen opciones fijas");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("La opción no puede estar vacía");
        }
        if (findOption(text) != null) {
            throw new IllegalArgumentException("La opción ya existe: " + text.trim());
        }
        options.add(text.trim());
    }

    public void setCorrectAnswers(Collection<String> answers) {
        requireEditable("definir las respuestas correctas");
        Set<String> resolved = new LinkedHashSet<>();
        for (String answer : answers) {
            String option = findOption(answer);
            if (option == null) {
                throw new IllegalArgumentException(
                        "La respuesta correcta '" + answer + "' no es una de las opciones");
            }
            resolved.add(option);
        }
        correctAnswers.clear();
        correctAnswers.addAll(resolved);
    }

    /** Devuelve la opción con su texto original (ignorando mayúsculas y espacios) o null. */
    public String findOption(String text) {
        if (text == null) {
            return null;
        }
        String wanted = text.trim();
        for (String option : options) {
            if (option.equalsIgnoreCase(wanted)) {
                return option;
            }
        }
        return null;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    private void requireEditable(String operation) {
        if (!state.isEditable()) {
            throw new IllegalStateTransitionException(state.getName(), operation);
        }
    }

    // ------------------------------------------------------------- Decorator

    @Override
    public String getDescription() {
        return statement;
    }

    @Override
    public double getScore() {
        return 1.0;
    }

    @Override
    public String toString() {
        return "#" + id + " [" + type.getDisplayName() + "] (" + state.getName() + ") " + statement;
    }
}
