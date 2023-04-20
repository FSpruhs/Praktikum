package de.fernuni.kurs01584.ss23.domain.model;

import java.util.Objects;

public class SnakePart {
	private final String fieldId;
	private final char character;
	private final Coordinate coordinate;
	
	public SnakePart(String fieldId, char character, Coordinate coordinate) {
		this.fieldId = fieldId;
		this.character = character;
		this.coordinate = coordinate;
	}

	@Override
	public String toString() {
		return "SnakePart{" +
				"fieldId='" + fieldId + '\'' +
				", character=" + character +
				", coordinate=" + coordinate +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SnakePart snakePart = (SnakePart) o;
		return character == snakePart.character && Objects.equals(fieldId, snakePart.fieldId) && Objects.equals(coordinate, snakePart.coordinate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldId, character, coordinate);
	}

	public char getCharacter() {
		return character;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

}
