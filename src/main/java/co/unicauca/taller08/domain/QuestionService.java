package co.unicauca.taller08.domain;

import co.unicauca.taller08.decorator.QuestionComponent;
import co.unicauca.taller08.decorator.QuestionFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Subsistema de gestión de preguntas: guarda, consulta, cambia el estado
 * y agrega características (decoradores) a las preguntas.
 */
public class QuestionService {

    private final QuestionRepository repository;
    private final Map<Long, QuestionComponent> components = new HashMap<>();

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public Question save(Question question) {
        Question saved = repository.save(question);
        components.putIfAbsent(saved.getId(), saved);
        return saved;
    }

    public Question findById(long id) {
        return repository.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
    }

    public List<Question> findAll() {
        return repository.findAll();
    }

    public List<Question> findByState(String stateName) {
        return repository.findAll().stream()
                .filter(q -> q.getState().getName().equalsIgnoreCase(stateName))
                .collect(Collectors.toList());
    }

    // --------------------------------------------------- Cambios de estado

    public void submitForReview(long id) {
        findById(id).submitForReview();
    }

    public void assignReviewer(long id, String reviewer) {
        findById(id).assignReviewer(reviewer);
    }

    public void approve(long id) {
        findById(id).approve();
    }

    public void reject(long id, String reason) {
        findById(id).reject(reason);
    }

    public void returnToDraft(long id) {
        findById(id).returnToDraft();
    }

    public void archive(long id) {
        findById(id).archive();
    }

    // ------------------------------------------------------------ Decorator

    public void addFeature(long id, QuestionFeature feature, String value) {
        QuestionComponent current = getComponent(id);
        components.put(id, feature.applyTo(current, value));
    }

    public QuestionComponent getComponent(long id) {
        Question question = findById(id);
        return components.getOrDefault(id, question);
    }
}
