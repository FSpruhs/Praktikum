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
		this.id = id;
		this.coordinate = coordinate;
		this.fieldValue = fieldValue;
		this.usability = usability;
		this.character = character;
		validateJungleFields();
	}

	private void validateJungleFields() {
		validateUsability();
		validateFieldValue();
		validateIdIsNotNull();
		validateId();
	}

	private void validateUsability() {
		if (usability < 0) {
			throw new InvalidJungleFieldException("Usability must be greater than 0!");
		}
	}

	private void validateFieldValue() {
		if (fieldValue < 0) {
			throw new InvalidJungleFieldException("FieldValue must be greater than 0!");
		}
	}

	private void validateIdIsNotNull() {
		if (id == null) {
			throw new InvalidJungleFieldException("Id is Null!");
		}
	}

	private void validateId() {
		if (id.charAt(0) != 'F' || !id.substring(1).matches("\\d+")) {
			throw new InvalidJungleFieldException("Invalid id!");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		JungleField that = (JungleField) o;
		return fieldValue == that.fieldValue && usability == that.usability && character == that.character && Objects.equals(id, that.id) && Objects.equals(coordinate, that.coordinate) && Objects.equals(snakeParts, that.snakeParts);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, coordinate, fieldValue, usability, character, snakeParts);
	}

	@Override
	public String toString() {
		return "JungleField{" +
				"id='" + id + '\'' +
				", coordinate=" + coordinate +
				", fieldValue=" + fieldValue +
				", usability=" + usability +
				", character=" + character +
				", snakeParts=" + snakeParts +
				'}';
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
