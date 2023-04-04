package de.fernuni.kurs01584.ss23.domain.model;

import java.util.Objects;

public class JungleField {
	
	private final String id;
	private final int row;
	private final int column;
	private final int fieldValue;
	private final int usability;
	private final char charachter;
	
	
	public JungleField(String id, int row, int column, int fieldValue, int usability, char charachter) {
		this.id = id;
		this.row = row;
		this.column = column;
		this.fieldValue = fieldValue;
		this.usability = usability;
		this.charachter = charachter;
	}


	@Override
	public int hashCode() {
		return Objects.hash(charachter, column, fieldValue, id, row, usability);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JungleField other = (JungleField) obj;
		return charachter == other.charachter && column == other.column && fieldValue == other.fieldValue
				&& Objects.equals(id, other.id) && row == other.row && usability == other.usability;
	}
	
	

}
