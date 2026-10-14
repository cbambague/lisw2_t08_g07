package co.unicauca.taller08.domain;

import java.util.List;

/**
 * Tipos de pregunta soportados. Las reglas de estructura de cada tipo viven aquí
 * (datos), así el validador no necesita un if/else por tipo.
 */
public enum QuestionType {

    SINGLE_CHOICE("Selección múltiple con única respuesta", 2, 1, 1, null),
    MULTIPLE_CHOICE("Selección múltiple con múltiples respuestas", 2, 1, Integer.MAX_VALUE, null),
    TRUE_FALSE("Verdadero/Falso", 2, 1, 1, List.of("Verdadero", "Falso"));

    private final String displayName;
    private final int minOptions;
    private final int minCorrect;
    private final int maxCorrect;
    private final List<String> fixedOptions;

    QuestionType(String displayName, int minOptions, int minCorrect, int maxCorrect,
                 List<String> fixedOptions) {
        this.displayName = displayName;
        this.minOptions = minOptions;
        this.minCorrect = minCorrect;
        this.maxCorrect = maxCorrect;
        this.fixedOptions = fixedOptions;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMinOptions() {
        return minOptions;
    }

    public int getMinCorrect() {
        return minCorrect;
    }

    public int getMaxCorrect() {
        return maxCorrect;
    }

    public boolean hasFixedOptions() {
        return fixedOptions != null;
    }

    public List<String> getFixedOptions() {
        return fixedOptions == null ? List.of() : fixedOptions;
    }
}
