package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidCoordinateException;

/**
 * Value Object representing a
 *
 * Value Object representing a two dimension coordinate. The values must not be negative.
 *
 * @param row row of the coordinate.
 * @param column column of the coordinate.
 */
public record Coordinate(int row, int column) {
	
	public Coordinate {
		if (row < 0 || column < 0) {
			throw new InvalidCoordinateException(row, column);
		}
	}
}
