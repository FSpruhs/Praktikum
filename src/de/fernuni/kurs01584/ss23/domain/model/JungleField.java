package de.fernuni.kurs01584.ss23.domain.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleFieldException;

public class JungleField implements Comparable<JungleField>{
	
	private final String id;
	private final Coordinate coordinate;
	private final int fieldValue;
	private final int usability;
	private final char character;
	private final List<SnakePart> snakeParts = new LinkedList<>();
	
	
	public JungleField(String id, Coordinate coordinate, int fieldValue, int usability, char character) {
		
		if (fieldValue < 0 || usability < 0) {
			throw new InvalidJungleFieldException("FieldValue and usability must be greater than 0!");
		}
		
		if (id == null) {
			throw new InvalidJungleFieldException("Character is Null!");
		}
		
		if (id.charAt(0) != 'F' || !id.substring(1).matches("\\d+")) {
			throw new InvalidJungleFieldException("Invalid id!");
		}
		
		this.id = id;
		this.coordinate = coordinate;
		this.fieldValue = fieldValue;
		this.usability = usability;
		this.character = character;
	}

	@Override
	public int hashCode() {
		return Objects.hash(character, coordinate, fieldValue, id, snakeParts, usability);
	}


	@Override
	public String toString() {
		return "JungleField [id=" + id + ", coordinate=" + coordinate + ", fieldValue=" + fieldValue + ", usability="
				+ usability + ", character=" + character + ", snakeParts=" + snakeParts + "]";
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
		return character == other.character && Objects.equals(coordinate, other.coordinate)
				&& fieldValue == other.fieldValue && Objects.equals(id, other.id)
				&& Objects.equals(snakeParts, other.snakeParts) && usability == other.usability;
	}

	public void placeSnakePart(SnakePart snakePart) {
		snakeParts.add(snakePart);
	}


	public int getUsability() {
		return usability - snakeParts.size();
	}


	public char getCharacter() {
		return character;
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


	public void removeSnakeParts() {
		snakeParts.clear();
	}


	@Override
	public int compareTo(JungleField j) {
		return Integer.compare(fieldValue, j.getFieldValue());
	}

	public void removeSnakePart(SnakePart snakePart) {
		snakeParts.remove(snakePart);
	}
}
