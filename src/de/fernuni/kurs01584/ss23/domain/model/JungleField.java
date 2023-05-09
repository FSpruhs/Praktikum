package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleFieldException;

public record JungleField(
		FieldId fieldId,
		Coordinate coordinate,
		int fieldValue,
		int usability,
		char character,
		List<SnakePart> snakeParts
) implements Comparable<JungleField> {

	public JungleField {
		if (usability < 0) {
			throw new InvalidJungleFieldException("Usability must be greater than 0!");
		}
		if (fieldValue < 0) {
			throw new InvalidJungleFieldException("FieldValue must be greater than 0!");
		}
		if (snakeParts == null) {
			throw new InvalidJungleFieldException("Snake parts is null!");
		}
		if (coordinate == null) {
			throw new InvalidJungleFieldException("Coordinate is null!");
		}

		if (fieldId == null) {
			throw new InvalidJungleFieldException("Field id is null!");
		}
	}

	public void placeSnakePart(SnakePart snakePart) {
		snakeParts.add(snakePart);
	}

	public int getUsability() {
		return usability - snakeParts.size();
	}

	public void removeSnakeParts() {
		snakeParts.clear();
	}

	@Override
	public int compareTo(JungleField j) {
		return Integer.compare(fieldValue, j.fieldValue());
	}

	public void removeSnakePart(SnakePart snakePart) {
		snakeParts.remove(snakePart);
	}

}
