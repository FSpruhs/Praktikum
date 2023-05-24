package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleException;

/**
 * Value object that represents the size of a jungle: The size is given as number of rows and number of columns.
 *
 * @param rows The number of rows of the jungle. The number cant be negative.
 * @param columns The number of columns of the jungle. The number cant be negative.
 */
public record JungleSize(int rows, int columns) {

    public JungleSize {
        if (rows < 0 || columns < 0) {
            throw new InvalidJungleException("Rows and Columns must be greater than 0!");
        }
    }

}
