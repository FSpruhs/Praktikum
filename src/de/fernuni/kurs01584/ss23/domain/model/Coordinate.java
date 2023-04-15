package de.fernuni.kurs01584.ss23.domain.model;

public record Coordinate(int row, int column) {
	
	public Coordinate(int row, int column) {
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException();
		}
		this.row = row;
		this.column = column;
	}

}
