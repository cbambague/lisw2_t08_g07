package co.unicauca.taller08.domain;

import java.util.List;
import java.util.Optional;

/** Almacenamiento de preguntas. */
public interface QuestionRepository {

    Question save(Question question);

    Optional<Question> findById(long id);

    List<Question> findAll();
}
