package de.fernuni.kurs01584.ss23.domain.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleFieldException;

public class JungleField {
	
	private final String id;
	private final Coordinate coordinate;
	private final int fieldValue;
	private final int usability;
	private final char charachter;
	private List<SnakePart> snakeParts = new LinkedList<>();
	
	
	public JungleField(String id, Coordinate coordinate, int fieldValue, int usability, char charachter) {
		
		if (fieldValue < 0 || usability < 0) {
			throw new InvalidJungleFieldException("FieldValue and usability must be greater than 0!");
		}
		
		if (id == null) {
			throw new InvalidJungleFieldException("Charachter is Null!");
		}
		
		if (!id.substring(0,1).equals("F") || !id.substring(1).matches("\\d+")) {
			throw new InvalidJungleFieldException("Invalid id!");
		}
		
		
		this.id = id;
		this.coordinate = coordinate;
		this.fieldValue = fieldValue;
		this.usability = usability;
		this.charachter = charachter;
	}


	@Override
	public int hashCode() {
		return Objects.hash(charachter, coordinate, fieldValue, id, snakeParts, usability);
	}


	@Override
	public String toString() {
		return "JungleField [id=" + id + ", coordinate=" + coordinate + ", fieldValue=" + fieldValue + ", usability="
				+ usability + ", charachter=" + charachter + ", snakeParts=" + snakeParts + "]";
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
		return charachter == other.charachter && Objects.equals(coordinate, other.coordinate)
				&& fieldValue == other.fieldValue && Objects.equals(id, other.id)
				&& Objects.equals(snakeParts, other.snakeParts) && usability == other.usability;
	}


	public void placeSnakePart(SnakePart snakePart) {
		snakeParts.add(snakePart);
	}


	public int getUsability() {
		return usability - snakeParts.size();
	}


	public char getCharachter() {
		return charachter;
	}


	public int getRow() {
		return coordinate.row();
	}


	public int getColumn() {
		return coordinate.column();
	}


	public String getId() {
		return id;
	}


	public int getFieldValue() {
		return fieldValue;
	}
	
	

}
