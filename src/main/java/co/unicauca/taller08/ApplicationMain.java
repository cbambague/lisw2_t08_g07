package co.unicauca.taller08;

import co.unicauca.taller08.adapter.ExternalQuestionAdapter;
import co.unicauca.taller08.adapter.ExternalQuestionService;
import co.unicauca.taller08.decorator.QuestionFeature;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import co.unicauca.taller08.facade.QuestionBankFacade;

import java.util.List;

/**
 * Banco de Preguntas Saber PRO - Taller 8.
 * <pre>
 *   sin argumentos -> demostración automática de los 10 requerimientos
 *   menu           -> menú interactivo por consola
 * </pre>
 */
public class ApplicationMain {

    public static void main(String[] args) {
        QuestionBankFacade facade = new QuestionBankFacade();
        if (args.length > 0 && args[0].equalsIgnoreCase("menu")) {
            new ConsoleMenu(facade).run();
        } else {
            runDemo(facade);
        }
    }

    private static void runDemo(QuestionBankFacade facade) {
        title("1-4. Crear preguntas de cada tipo, con opciones y respuestas correctas");
        Question q1 = facade.createDraft(QuestionType.SINGLE_CHOICE,
                "¿Qué patrón permite agregar responsabilidades a un objeto dinámicamente?");
        for (String o : List.of("Decorator", "Adapter", "Facade", "State")) {
            facade.addOption(q1.getId(), o);
        }
        facade.defineCorrectAnswers(q1.getId(), List.of("Decorator"));

        Question q2 = facade.createDraft(QuestionType.MULTIPLE_CHOICE,
                "¿Cuáles de los siguientes son patrones estructurales?");
        for (String o : List.of("Decorator", "Adapter", "Facade", "State")) {
            facade.addOption(q2.getId(), o);
        }
        facade.defineCorrectAnswers(q2.getId(), List.of("Decorator", "Adapter", "Facade"));

        Question q3 = facade.createDraft(QuestionType.TRUE_FALSE,
                "Template Method es un patrón de comportamiento.");
        facade.defineCorrectAnswers(q3.getId(), List.of("Verdadero"));

        title("5. Consultar preguntas");
        facade.findAll().forEach(System.out::println);

        title("6. Cambiar el estado (State) - transiciones permitidas y no permitidas");
        attempt("Borrador -> Publicada (NO permitido)", () -> facade.publishQuestion(q1.getId()));
        facade.submitForReview(q1.getId());
        facade.assignReviewer(q1.getId(), "Prof. Revisor");
        facade.approveQuestion(q1.getId());
        attempt("Aprobada -> Borrador (NO permitido)", () -> facade.returnToDraft(q1.getId()));
        facade.publishQuestion(q1.getId());
        attempt("Publicada -> Borrador (NO permitido)", () -> facade.returnToDraft(q1.getId()));
        System.out.println("q1 historial: " + facade.findById(q1.getId()).getStateHistory());

        publishFlow(facade, q2.getId());

        // Camino de rechazo: En revisión -> Rechazada -> Borrador -> ... -> Publicada
        facade.submitForReview(q3.getId());
        facade.assignReviewer(q3.getId(), "Prof. Revisor");
        facade.rejectQuestion(q3.getId(), "Falta contexto en el enunciado");
        facade.returnToDraft(q3.getId());
        facade.findById(q3.getId()).setStatement("En el contexto de GoF, Template Method es un patrón de comportamiento.");
        publishFlow(facade, q3.getId());
        System.out.println("q3 historial: " + facade.findById(q3.getId()).getStateHistory());

        title("7. Evaluar respuestas (Template Method)");
        evaluate(facade, q1.getId(), List.of("Decorator"));
        evaluate(facade, q1.getId(), List.of("Adapter"));
        evaluate(facade, q1.getId(), List.of("Decorator", "Adapter"));
        evaluate(facade, q2.getId(), List.of("Decorator", "Adapter", "Facade"));
        evaluate(facade, q2.getId(), List.of("Decorator", "State"));
        evaluate(facade, q3.getId(), List.of("verdadero"));
        evaluate(facade, q3.getId(), List.of("Falso"));

        title("8. Agregar características con Decorator");
        facade.addFeature(q1.getId(), QuestionFeature.HIGH_DIFFICULTY, null);
        facade.addFeature(q1.getId(), QuestionFeature.COMPETENCY, "Diseño de software");
        facade.addFeature(q1.getId(), QuestionFeature.MARKED_FOR_REVIEW, null);
        System.out.println(facade.describe(q1.getId()));
        System.out.println("Puntaje con decoradores: " + facade.scoreOf(q1.getId()));

        title("9. Importar una pregunta externa con Adapter");
        Question imported = facade.importQuestion(new ExternalQuestionAdapter(new ExternalQuestionService()));
        System.out.println("Importada: " + imported);
        publishFlow(facade, imported.getId());
        evaluate(facade, imported.getId(), List.of("Bogotá"));

        title("10. Todas las operaciones anteriores se hicieron a través de QuestionBankFacade");
        facade.archiveQuestion(q2.getId());
        facade.findAll().forEach(System.out::println);
    }

    private static void publishFlow(QuestionBankFacade facade, long id) {
        Question q = facade.findById(id);
        if (q.getState().getName().equals("Borrador")) {
            facade.submitForReview(id);
        }
        if (q.getState().getName().equals("Pendiente de revisión")) {
            facade.assignReviewer(id, "Prof. Revisor");
        }
        facade.approveQuestion(id);
        facade.publishQuestion(id);
    }

    private static void evaluate(QuestionBankFacade facade, long id, List<String> answers) {
        attempt("Pregunta #" + id + " respuesta " + answers,
                () -> System.out.println("    -> " + facade.evaluateAnswer(id, answers)));
    }

    private static void attempt(String label, Runnable action) {
        System.out.println("  * " + label);
        try {
            action.run();
        } catch (RuntimeException e) {
            System.out.println("    ! " + e.getMessage());
        }
    }

    private static void title(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }
}
