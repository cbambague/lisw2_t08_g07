package co.unicauca.taller08.facade;

import co.unicauca.taller08.adapter.QuestionImporter;
import co.unicauca.taller08.adapter.QuestionProvider;
import co.unicauca.taller08.decorator.QuestionFeature;
import co.unicauca.taller08.domain.InMemoryQuestionRepository;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionPublisher;
import co.unicauca.taller08.domain.QuestionService;
import co.unicauca.taller08.domain.QuestionType;
import co.unicauca.taller08.domain.QuestionValidator;
import co.unicauca.taller08.templatemethod.EvaluationResult;
import co.unicauca.taller08.templatemethod.QuestionEvaluatorFactory;

import java.util.List;

/**
 * Facade: punto de entrada único al Banco de Preguntas. El cliente no necesita
 * conocer QuestionService, QuestionValidator, QuestionEvaluator, QuestionPublisher
 * ni QuestionImporter.
 */
public class QuestionBankFacade {

    private final QuestionService questionService;
    private final QuestionValidator validator;
    private final QuestionEvaluatorFactory evaluators;
    private final QuestionPublisher publisher;
    private final QuestionImporter importer;

    public QuestionBankFacade(QuestionService questionService, QuestionValidator validator,
                              QuestionEvaluatorFactory evaluators, QuestionPublisher publisher,
                              QuestionImporter importer) {
        this.questionService = questionService;
        this.validator = validator;
        this.evaluators = evaluators;
        this.publisher = publisher;
        this.importer = importer;
    }

    /** Configuración por defecto con repositorio en memoria. */
    public QuestionBankFacade() {
        this(new QuestionService(new InMemoryQuestionRepository()), new QuestionValidator(),
                new QuestionEvaluatorFactory(), new QuestionPublisher(), new QuestionImporter());
    }

    // ------------------------------------------------------------ Crear

    /** Valida que la pregunta esté completa y la guarda (como Borrador). */
    public Question createQuestion(Question question) {
        validator.validate(question);
        return questionService.save(question);
    }

    /** Crea un borrador vacío para completarlo con opciones y respuestas correctas. */
    public Question createDraft(QuestionType type, String statement) {
        return questionService.save(new Question(type, statement));
    }

    public void addOption(long id, String option) {
        questionService.findById(id).addOption(option);
    }

    public void defineCorrectAnswers(long id, List<String> answers) {
        questionService.findById(id).setCorrectAnswers(answers);
    }

    // ---------------------------------------------------------- Consultar

    public Question findById(long id) {
        return questionService.findById(id);
    }

    public List<Question> findAll() {
        return questionService.findAll();
    }

    public List<Question> findByState(String stateName) {
        return questionService.findByState(stateName);
    }

    // -------------------------------------------------- Ciclo de vida (State)

    public void submitForReview(long id) {
        questionService.submitForReview(id);
    }

    public void assignReviewer(long id, String reviewer) {
        questionService.assignReviewer(id, reviewer);
    }

    public void approveQuestion(long id) {
        questionService.approve(id);
    }

    public void rejectQuestion(long id, String reason) {
        questionService.reject(id, reason);
    }

    public void returnToDraft(long id) {
        questionService.returnToDraft(id);
    }

    /** Publica una pregunta aprobada (valida y delega en el publicador). */
    public void publishQuestion(long id) {
        Question question = questionService.findById(id);
        validator.validate(question);
        publisher.publish(question);
    }

    public void archiveQuestion(long id) {
        questionService.archive(id);
    }

    // -------------------------------------------------- Evaluar (Template Method)

    /** Evalúa una respuesta; solo se puede usar una pregunta aprobada y publicada. */
    public EvaluationResult evaluateAnswer(long id, List<String> answers) {
        Question question = questionService.findById(id);
        if (!question.getState().canBeUsedInTest()) {
            throw new IllegalStateException("La pregunta #" + id + " no puede usarse en una prueba: está en estado "
                    + question.getState().getName() + " (debe estar aprobada y publicada)");
        }
        return evaluators.forType(question.getType()).evaluate(question, answers);
    }

    // ------------------------------------------------------ Decorator

    public void addFeature(long id, QuestionFeature feature, String value) {
        questionService.addFeature(id, feature, value);
    }

    /** Descripción de la pregunta incluyendo las características agregadas. */
    public String describe(long id) {
        return questionService.getComponent(id).getDescription();
    }

    public double scoreOf(long id) {
        return questionService.getComponent(id).getScore();
    }

    // -------------------------------------------------------- Adapter

    /** Importa una pregunta de una fuente externa; queda guardada como Borrador. */
    public Question importQuestion(QuestionProvider provider) {
        Question question = importer.importFrom(provider);
        validator.validate(question);
        return questionService.save(question);
    }
}
