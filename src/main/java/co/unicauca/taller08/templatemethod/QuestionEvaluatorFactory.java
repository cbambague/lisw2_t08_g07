package co.unicauca.taller08.templatemethod;

import co.unicauca.taller08.domain.QuestionType;

import java.util.EnumMap;
import java.util.Map;

/**
 * Entrega el evaluador de cada tipo de pregunta. Para incorporar un tipo nuevo basta
 * con crear su evaluador y registrarlo; el resto del sistema no cambia.
 */
public class QuestionEvaluatorFactory {

    private final Map<QuestionType, QuestionEvaluator> evaluators = new EnumMap<>(QuestionType.class);

    public QuestionEvaluatorFactory() {
        register(QuestionType.SINGLE_CHOICE, new SingleChoiceEvaluator());
        register(QuestionType.MULTIPLE_CHOICE, new MultipleChoiceEvaluator());
        register(QuestionType.TRUE_FALSE, new TrueFalseEvaluator());
    }

    public void register(QuestionType type, QuestionEvaluator evaluator) {
        evaluators.put(type, evaluator);
    }

    public QuestionEvaluator forType(QuestionType type) {
        QuestionEvaluator evaluator = evaluators.get(type);
        if (evaluator == null) {
            throw new IllegalArgumentException("No hay evaluador para el tipo " + type);
        }
        return evaluator;
    }
}
