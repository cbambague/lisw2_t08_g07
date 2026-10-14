package co.unicauca.taller08.adapter;

import co.unicauca.taller08.domain.Question;

/** Interfaz que el Banco de Preguntas espera de cualquier fuente de preguntas. */
public interface QuestionProvider {

    Question getQuestion();
}
