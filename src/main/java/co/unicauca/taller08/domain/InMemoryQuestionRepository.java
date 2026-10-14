package co.unicauca.taller08.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Repositorio en memoria (suficiente para el taller). */
public class InMemoryQuestionRepository implements QuestionRepository {

    private final Map<Long, Question> store = new LinkedHashMap<>();
    private long nextId = 1;

    @Override
    public Question save(Question question) {
        if (question.getId() == null) {
            question.setId(nextId++);
        }
        store.put(question.getId(), question);
        return question;
    }

    @Override
    public Optional<Question> findById(long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Question> findAll() {
        return new ArrayList<>(store.values());
    }
}
