package co.unicauca.taller08.decorator;

/** Decorador abstracto: delega en el componente envuelto. */
public abstract class QuestionDecorator implements QuestionComponent {

    protected QuestionComponent question;

    public QuestionDecorator(QuestionComponent question) {
        if (question == null) {
            throw new IllegalArgumentException("El componente a decorar no puede ser null");
        }
        this.question = question;
    }

    @Override
    public String getDescription() {
        return question.getDescription();
    }

    @Override
    public double getScore() {
        return question.getScore();
    }
}
