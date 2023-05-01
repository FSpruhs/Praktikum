package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleException;

public class Jungle {

	private final JungleSize jungleSize;
	private final String characters;
	private final List<JungleField> jungleFields;
	
	public Jungle(JungleSize jungleSize, String characters, List<JungleField> jungleFields) {
		this.jungleSize = jungleSize;
		this.characters = characters;
		this.jungleFields = jungleFields;
		validateJungle();
	}

	private void validateJungle() {
		validateCharacters();
		validateJungleFields();
	}

	private void validateJungleFields() {
		int counter = 0;
		for (JungleField jungleField : jungleFields) {
			validateJungleField(counter, jungleField);
			counter++;
		}
	}

	private void validateJungleField(int counter, JungleField jungleField) {
		validateJungleFieldIsNull(jungleField);
		validateJungleFieldPosition(counter, jungleField);
	}

	private void validateJungleFieldPosition(int counter, JungleField jungleField) {
		if (Integer.parseInt(jungleField.getId().value().substring(1)) != counter || jungleField.getId().value().charAt(0) != 'F') {
			throw new InvalidJungleException("Jungle field is Invalid");
		}
	}

	private void validateJungleFieldIsNull(JungleField jungleField) {
		if (jungleField == null) {
			throw new InvalidJungleException("Jungle field is Null!");
		}
	}

	private void validateCharacters() {
		if (characters == null) {
			throw new InvalidJungleException("Characters is Null!");
		}
	}

	public JungleField getJungleField(Coordinate coordinate) {
		return jungleFields.get(mapCoordinateToIndex(coordinate));
	}

	public void placeSnakePart(SnakePart snakePart) {
		jungleFields.get(mapCoordinateToIndex(snakePart.coordinate())).placeSnakePart(snakePart);
	}

	public int getJungleFieldUsability(Coordinate coordinate) {
		return jungleFields.get(mapCoordinateToIndex(coordinate)).getUsability();
	}

	public char getJungleFieldSign(Coordinate coordinate) {
		return jungleFields.get(mapCoordinateToIndex(coordinate)).getCharacter();
	}
	
	public int getFieldValue(Coordinate coordinate) {
		return jungleFields.get(mapCoordinateToIndex(coordinate)).getFieldValue();
	}

	public void removeAllSnakeParts() {
		jungleFields.forEach(JungleField::removeSnakeParts);
	}

	public JungleSize getJungleSize() {
		return jungleSize;
	}

	public void removeSnakePart(SnakePart snakePart) {
		jungleFields.get(mapCoordinateToIndex(snakePart.coordinate())).removeSnakePart(snakePart);
	}

	private int mapCoordinateToIndex(Coordinate coordinate) {
		return coordinate.row() * jungleSize.columns() + coordinate.column();
	}

	public String getCharacters() {
		return characters;
	}

	@Override
	public String toString() {
		return "Jungle{" +
				"jungleSize=" + jungleSize +
				", characters='" + characters + '\'' +
				", jungleFields=" + jungleFields +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Jungle jungle = (Jungle) o;
		return Objects.equals(jungleSize, jungle.jungleSize) && Objects.equals(characters, jungle.characters) && Objects.equals(jungleFields, jungle.jungleFields);
	}

	@Override
	public int hashCode() {
		return Objects.hash(jungleSize, characters, jungleFields);
	}

    public List<JungleField> getJungleFields() {
		return jungleFields;
    }
}
