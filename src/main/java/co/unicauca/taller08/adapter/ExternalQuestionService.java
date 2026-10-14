package co.unicauca.taller08.adapter;

/**
 * Servicio de una institución externa (clase adaptada). Devuelve la pregunta en
 * JSON con un formato distinto al del Banco de Preguntas.
 * Campos: "statement", "answer" (o "answers" si son varias) y, opcionalmente, "options".
 */
public class ExternalQuestionService {

    private static final String DEFAULT_PAYLOAD = """
            {
                "statement": "¿Cuál es la capital de Colombia?",
                "answer": "Bogotá",
                "options": ["Bogotá", "Medellín", "Cali", "Barranquilla"]
            }
            """;

    private final String payload;

    public ExternalQuestionService() {
        this(DEFAULT_PAYLOAD);
    }

    public ExternalQuestionService(String payload) {
        this.payload = payload;
    }

    public String getQuestion() {
        return payload;
    }
}
