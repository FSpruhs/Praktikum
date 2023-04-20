package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleException;

public record JungleSize(int rows, int columns) {

    public JungleSize {
        if (rows < 0 || columns < 0) {
            throw new InvalidJungleException("Rows and Columns must be greater than 0!");
        }
    }

}
