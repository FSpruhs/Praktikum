package de.fernuni.kurs01584.ss23.domain.model;

import java.util.Objects;

public class SnakePart {
	private final String fieldId;
	private char charachter;
	private Coordinate coordinate;
	
	public SnakePart(String fieldId, char charachter, Coordinate coordinate) {
		this.fieldId = fieldId;
		this.charachter = charachter;
		this.coordinate = coordinate;
	}
	

	public int getRow() {
		return coordinate.row();
	}


	public int getColumn() {
		return coordinate.column();
	}


	public String getFieldId() {
		return fieldId;
	}


	public char getCharachter() {
		return charachter;
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


	public void loadJungleFieldData(Coordinate coordinate, char charachter) {
		this.coordinate = coordinate;
		this.charachter = charachter;	
	}


	public Coordinate getCoordinate() {
		return coordinate;
	}
	
	
}
