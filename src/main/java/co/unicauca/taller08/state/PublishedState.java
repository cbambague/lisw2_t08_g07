package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

/** Publicada: disponible para su uso en pruebas. */
public class PublishedState extends AbstractQuestionState {

    @Override
    public void archive(Question question) {
        question.setState(new ArchivedState());
    }

    @Override
    public boolean canBeUsedInTest() {
        return true;
    }

    @Override
    public String getName() {
        return "Publicada";
    }
}
