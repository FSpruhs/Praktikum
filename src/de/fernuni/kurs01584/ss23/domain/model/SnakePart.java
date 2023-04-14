package de.fernuni.kurs01584.ss23.domain.model;

import java.lang.reflect.Field;
import java.util.Objects;

public class SnakePart {
	private final String fieldId;
	private char charachter;
	private int row;
	private int column;
	
	public SnakePart(String fieldId, char charachter, int row, int column) {
		this.fieldId = fieldId;
		this.charachter = charachter;
		this.row = row;
		this.column = column;
	}
	
	public SnakePart(String fieldId) {
		this.fieldId = fieldId;
	}
	

	public int getRow() {
		return row;
	}


	public int getColumn() {
		return column;
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


	public void loadJungleFieldData(int row, int column, char charachter) {
		this.row = row;
		this.column = column;
		this.charachter = charachter;	
	}
	
	
}
