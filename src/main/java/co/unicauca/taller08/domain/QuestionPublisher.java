package co.unicauca.taller08.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Subsistema de publicación: deja la pregunta disponible para su uso en pruebas. */
public class QuestionPublisher {

    private final List<Long> publishedIds = new ArrayList<>();

    /** Publica la pregunta; el estado actual decide si la operación está permitida. */
    public void publish(Question question) {
        question.publish();
        publishedIds.add(question.getId());
    }

    public List<Long> getPublishedIds() {
        return Collections.unmodifiableList(publishedIds);
    }
}
