package de.fernuni.kurs01584.ss23.domain.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleFieldException;

public class JungleField implements Comparable<JungleField>{

	private final FieldId fieldId;
	private final Coordinate coordinate;
	private final int fieldValue;
	private final int usability;
	private final char character;
	private final List<SnakePart> snakeParts = new LinkedList<>();


	public JungleField(FieldId fieldId, Coordinate coordinate, int fieldValue, int usability, char character) {
		this.fieldId = fieldId;
		this.coordinate = coordinate;
		this.fieldValue = fieldValue;
		this.usability = usability;
		this.character = character;
		validateJungleFields();
	}

	private void validateJungleFields() {
		validateUsability();
		validateFieldValue();
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		JungleField that = (JungleField) o;
		return fieldValue == that.fieldValue && usability == that.usability && character == that.character && Objects.equals(fieldId, that.fieldId) && Objects.equals(coordinate, that.coordinate) && Objects.equals(snakeParts, that.snakeParts);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldId, coordinate, fieldValue, usability, character, snakeParts);
	}

	@Override
	public String toString() {
		return "JungleField{" +
				"fieldId=" + fieldId +
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
		return fieldId.id();
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

	public Coordinate getCoordinate() {
		return coordinate;
	}
}
