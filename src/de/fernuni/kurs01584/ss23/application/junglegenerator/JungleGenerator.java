package de.fernuni.kurs01584.ss23.application.junglegenerator;

/**
 * The jungle generator generates a new jungle.
 * It uses the values of the current jungle and the values of the snake types.
 * Already existing jungle fields will be overwritten.
 */
public interface JungleGenerator {

    /**
     * Generates a new jungle.
     */
    void generate();

}
