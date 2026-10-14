package co.unicauca.taller08.state;

/** Archivada: estado final, ya no se utiliza activamente. No permite ninguna operación. */
public class ArchivedState extends AbstractQuestionState {

    @Override
    public String getName() {
        return "Archivada";
    }
}
