package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleException;


/**
 * Domain object that represents the jungle of snake hunt.
 */
public class Jungle {

	private final JungleSize jungleSize;
	private final String characters;
	private List<JungleField> jungleFields;

	/**
	 * Constructor of the Jungle.
	 *
	 * @param jungleSize Jungle Size value Object.
	 * @param characters These characters are present in the jungle. No empty String allowed.
	 * @param jungleFields List with the fields of the jungle. The size of the list corresponds to the rows of the
	 *                     jungle multiplied by the columns of the jungle.
	 *                     The jungle fields must be sorted by the ID in ascending order.
	 *                     The value of the id must match their index in the list.
	 */
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
		if (Integer.parseInt(jungleField.fieldId().value().substring(1)) != counter || jungleField.fieldId().value().charAt(0) != 'F') {
			throw new InvalidJungleException("Jungle field is Invalid");
		}
	}

	private void validateJungleFieldIsNull(JungleField jungleField) {
		if (jungleField == null) {
			throw new InvalidJungleException("Jungle field is Null!");
		}
	}

	private void validateCharacters() {
		if (characters == null || characters.isEmpty()) {
			throw new InvalidJungleException("Characters is Null!");
		}
	}

	/**
	 * Returns the jungle field at the coordinate.
	 *
	 * @param coordinate The coordinate of the jungle field to be returned.
	 * @return The jungle field at the coordinate.
	 */
	public JungleField getJungleField(Coordinate coordinate) {
		return jungleFields.get(mapCoordinateToIndex(coordinate));
	}

	/**
	 * Places the snake part in the jungle
	 *
	 * @param snakePart The snake part to be placed.
	 */
	public void placeSnakePart(SnakePart snakePart) {
		jungleFields.get(mapCoordinateToIndex(snakePart.coordinate())).placeSnakePart(snakePart);
	}

	public int getJungleFieldUsability(Coordinate coordinate) {
		return jungleFields.get(mapCoordinateToIndex(coordinate)).remainingUsability();
	}

	public char getJungleFieldSign(Coordinate coordinate) {
		return jungleFields.get(mapCoordinateToIndex(coordinate)).character();
	}
	
	public int getFieldValue(Coordinate coordinate) {
		return jungleFields.get(mapCoordinateToIndex(coordinate)).fieldValue();
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

	public int mapCoordinateToIndex(Coordinate coordinate) {
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

	public void setJungleFields(List<JungleField> jungleFields) {
		this.jungleFields = jungleFields;
	}

	public void removeJungleFields() {
		this.jungleFields = null;
	}

}
