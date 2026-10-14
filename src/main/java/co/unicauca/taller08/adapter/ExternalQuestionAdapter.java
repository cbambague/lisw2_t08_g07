package co.unicauca.taller08.adapter;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adapter: convierte la pregunta externa (JSON) al modelo interno Question,
 * de modo que el Banco de Preguntas la trate como cualquier otra.
 * <ul>
 *   <li>"answers" (lista) -> selección múltiple con múltiples respuestas</li>
 *   <li>"answer" verdadero/falso sin opciones -> Verdadero/Falso</li>
 *   <li>"answer" con "options" -> selección múltiple con única respuesta</li>
 * </ul>
 * La pregunta importada queda en estado Borrador.
 */
public class ExternalQuestionAdapter implements QuestionProvider {

    private static final String STRING = "\"((?:[^\"\\\\]|\\\\.)*)\"";

    private final ExternalQuestionService service;

    public ExternalQuestionAdapter(ExternalQuestionService service) {
        this.service = service;
    }

    @Override
    public Question getQuestion() {
        String data = service.getQuestion();

        String statement = readString(data, "statement");
        if (statement == null || statement.isBlank()) {
            throw new ExternalQuestionFormatException("La pregunta externa no tiene 'statement'");
        }
        String answer = readString(data, "answer");
        List<String> answers = readArray(data, "answers");
        List<String> options = readArray(data, "options");

        Question question;
        if (!answers.isEmpty()) {
            question = new Question(QuestionType.MULTIPLE_CHOICE, statement);
            addOptions(question, options);
            question.setCorrectAnswers(answers);
        } else if (answer != null && options.isEmpty() && isTrueFalse(answer)) {
            question = new Question(QuestionType.TRUE_FALSE, statement);
            question.setCorrectAnswers(List.of(normalizeTrueFalse(answer)));
        } else if (answer != null && !options.isEmpty()) {
            question = new Question(QuestionType.SINGLE_CHOICE, statement);
            addOptions(question, options);
            question.setCorrectAnswers(List.of(answer));
        } else {
            throw new ExternalQuestionFormatException(
                    "No se puede determinar el tipo de la pregunta externa (faltan 'answer' u 'options')");
        }
        return question;
    }

    private void addOptions(Question question, List<String> options) {
        for (String option : options) {
            question.addOption(option);
        }
    }

    private boolean isTrueFalse(String value) {
        String v = value.trim().toLowerCase();
        return v.equals("true") || v.equals("false") || v.equals("verdadero") || v.equals("falso");
    }

    private String normalizeTrueFalse(String value) {
        String v = value.trim().toLowerCase();
        return (v.equals("true") || v.equals("verdadero")) ? "Verdadero" : "Falso";
    }

    // ----------------------------------------------------- JSON mínimo

    private String readString(String json, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*" + STRING).matcher(json);
        return m.find() ? unescape(m.group(1)) : null;
    }

    private List<String> readArray(String json, String key) {
        List<String> values = new ArrayList<>();
        Matcher array = Pattern.compile("\"" + key + "\"\\s*:\\s*\\[(.*?)]", Pattern.DOTALL).matcher(json);
        if (array.find()) {
            Matcher item = Pattern.compile(STRING).matcher(array.group(1));
            while (item.find()) {
                values.add(unescape(item.group(1)));
            }
        }
        return values;
    }

    private String unescape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char n = s.charAt(++i);
                switch (n) {
                    case 'n' -> sb.append('\n');
                    case 't' -> sb.append('\t');
                    case 'u' -> {
                        if (i + 4 < s.length()) {
                            sb.append((char) Integer.parseInt(s.substring(i + 1, i + 5), 16));
                            i += 4;
                        }
                    }
                    default -> sb.append(n);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
