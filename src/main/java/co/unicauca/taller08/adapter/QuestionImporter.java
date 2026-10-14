package co.unicauca.taller08.adapter;

import co.unicauca.taller08.domain.Question;

/** Subsistema de importación: obtiene preguntas de cualquier QuestionProvider. */
public class QuestionImporter {

    public Question importFrom(QuestionProvider provider) {
        Question question = provider.getQuestion();
        if (question == null) {
            throw new IllegalStateException("La fuente externa no devolvió ninguna pregunta");
        }
        return question;
    }
}
