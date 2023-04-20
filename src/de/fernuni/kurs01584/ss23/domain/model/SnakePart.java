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
	

	public int getRow() {
		return coordinate.row();
	}


	public int getColumn() {
		return coordinate.column();
	}

	public char getCharacter() {
		return character;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SnakePart other = (SnakePart) obj;
		return Objects.equals(fieldId, other.fieldId);
	}

	@Override
	public String toString() {
		return "SnakePart [fieldId=" + fieldId + "]";
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

}
