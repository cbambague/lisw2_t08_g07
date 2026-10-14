package co.unicauca.taller08;

import co.unicauca.taller08.adapter.ExternalQuestionAdapter;
import co.unicauca.taller08.adapter.ExternalQuestionService;
import co.unicauca.taller08.decorator.QuestionFeature;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import co.unicauca.taller08.facade.QuestionBankFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/** Menú interactivo por consola; todas las operaciones pasan por la Facade. */
public class ConsoleMenu {

    private final QuestionBankFacade facade;
    private final Scanner in = new Scanner(System.in);

    public ConsoleMenu(QuestionBankFacade facade) {
        this.facade = facade;
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("""

                    ===== Banco de Preguntas Saber PRO =====
                    1. Crear pregunta (tipo, opciones y respuestas correctas)
                    2. Consultar preguntas
                    3. Cambiar estado de una pregunta
                    4. Evaluar una respuesta
                    5. Agregar característica (Decorator)
                    6. Importar pregunta externa (Adapter)
                    0. Salir""");
            String option = ask("Opción: ");
            try {
                switch (option) {
                    case "1" -> create();
                    case "2" -> list();
                    case "3" -> changeState();
                    case "4" -> evaluate();
                    case "5" -> decorate();
                    case "6" -> importExternal();
                    case "0" -> exit = true;
                    default -> System.out.println("Opción no válida");
                }
            } catch (RuntimeException e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }

    private void create() {
        System.out.println("Tipos: 1=Única respuesta, 2=Múltiples respuestas, 3=Verdadero/Falso");
        QuestionType type = switch (ask("Tipo: ")) {
            case "1" -> QuestionType.SINGLE_CHOICE;
            case "2" -> QuestionType.MULTIPLE_CHOICE;
            case "3" -> QuestionType.TRUE_FALSE;
            default -> throw new IllegalArgumentException("Tipo no válido");
        };
        Question q = facade.createDraft(type, ask("Enunciado: "));
        if (!type.hasFixedOptions()) {
            System.out.println("Escriba las opciones (línea vacía para terminar):");
            String option;
            while (!(option = ask("  opción: ")).isBlank()) {
                facade.addOption(q.getId(), option);
            }
        }
        facade.defineCorrectAnswers(q.getId(), split(ask("Respuesta(s) correcta(s), separadas por coma: ")));
        System.out.println("Creada: " + q);
    }

    private void list() {
        List<Question> all = facade.findAll();
        if (all.isEmpty()) {
            System.out.println("No hay preguntas.");
        }
        for (Question q : all) {
            System.out.println(q);
            System.out.println("    " + facade.describe(q.getId()));
            System.out.println("    opciones: " + q.getOptions() + " | correctas: " + q.getCorrectAnswers());
        }
    }

    private void changeState() {
        long id = Long.parseLong(ask("Id de la pregunta: "));
        System.out.println("1=Enviar a revisión 2=Asignar revisor 3=Aprobar 4=Rechazar "
                + "5=Devolver a borrador 6=Publicar 7=Archivar");
        switch (ask("Operación: ")) {
            case "1" -> facade.submitForReview(id);
            case "2" -> facade.assignReviewer(id, ask("Revisor: "));
            case "3" -> facade.approveQuestion(id);
            case "4" -> facade.rejectQuestion(id, ask("Motivo: "));
            case "5" -> facade.returnToDraft(id);
            case "6" -> facade.publishQuestion(id);
            case "7" -> facade.archiveQuestion(id);
            default -> throw new IllegalArgumentException("Operación no válida");
        }
        System.out.println("Estado actual: " + facade.findById(id).getState().getName());
    }

    private void evaluate() {
        long id = Long.parseLong(ask("Id de la pregunta: "));
        List<String> answers = split(ask("Su respuesta (varias separadas por coma): "));
        System.out.println(facade.evaluateAnswer(id, answers));
    }

    private void decorate() {
        long id = Long.parseLong(ask("Id de la pregunta: "));
        QuestionFeature[] features = QuestionFeature.values();
        for (int i = 0; i < features.length; i++) {
            System.out.println((i + 1) + "=" + features[i].getDisplayName());
        }
        QuestionFeature feature = features[Integer.parseInt(ask("Característica: ")) - 1];
        String value = feature.needsValue() ? ask("Valor: ") : null;
        facade.addFeature(id, feature, value);
        System.out.println(facade.describe(id));
    }

    private void importExternal() {
        Question q = facade.importQuestion(new ExternalQuestionAdapter(new ExternalQuestionService()));
        System.out.println("Importada: " + q);
    }

    private List<String> split(String text) {
        List<String> parts = new ArrayList<>();
        Arrays.stream(text.split(",")).map(String::trim).filter(s -> !s.isEmpty()).forEach(parts::add);
        return parts;
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return in.hasNextLine() ? in.nextLine().trim() : "0";
    }
}
