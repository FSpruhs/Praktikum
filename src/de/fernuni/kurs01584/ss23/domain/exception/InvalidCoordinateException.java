package de.fernuni.kurs01584.ss23.domain.exception;

public class InvalidCoordinateException extends InvalidDataException{

    public InvalidCoordinateException(int row, int column) {
        super("Row %s and column %s must cant be negative".formatted(row, column));
    }
}
