package co.unicauca.taller08.decorator;

/**
 * Componente base del patrón Decorator.
 * Tanto la pregunta real como todos sus decoradores implementan esta interfaz.
 */
public interface QuestionComponent {

    String getDescription();

    double getScore();
}
