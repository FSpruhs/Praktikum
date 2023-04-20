package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidCoordinateException;

public record Coordinate(int row, int column) {
	
	public Coordinate {
		if (row < 0 || column < 0) {
			throw new InvalidCoordinateException(row, column);
		}
	}
}
