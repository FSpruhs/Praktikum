package de.fernuni.kurs01584.ss23.domain.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleException;

public class Jungle {
	
	private final int rows;
	private final int columns;
	private final String characters;
	private final List<JungleField> jungleFields;
	
	public Jungle(int rows, int columns, String characters, List<JungleField> jungleFields) {

		if (rows < 0 || columns < 0) {
			throw new InvalidJungleException("Rows and Columns must be greater than 0!");
		}
		
		if (characters == null) {
			throw new InvalidJungleException("Characters is Null!");
		}
		
		int counter = 0;
		for (JungleField jungleField : jungleFields) {
			if (jungleField == null) {
				throw new InvalidJungleException("Jungle field is Null!");
			}
			if (Integer.parseInt(jungleField.getId().substring(1)) !=  counter || jungleField.getId().charAt(0) != 'F') {
				throw new InvalidJungleException("Jungle field is Invalid");
			}
			counter++;
		}
		
		this.rows = rows;
		this.columns = columns;
		this.characters = characters;
		this.jungleFields = jungleFields;
	}
	
	public JungleField getJungleField(Coordinate coordinate) {
		return jungleFields.get(coordinate.row() * columns + coordinate.column());
	}

	@Override
	public int hashCode() {
		return Objects.hash(characters, columns, jungleFields, rows);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jungle other = (Jungle) obj;
		return Objects.equals(characters, other.characters) && columns == other.columns
				&& Objects.equals(jungleFields, other.jungleFields) && rows == other.rows;
	}

	@Override
	public String toString() {
		return "Jungle [rows=" + rows + ", columns=" + columns + ", characters=" + characters + ", jungleFields="
				+ jungleFields + "]";
	}

	public void placeSnakePart(SnakePart snakePart, Coordinate coordinate) {
		jungleFields.get(coordinate.row() * columns + coordinate.column()).placeSnakePart(snakePart);
	}

	public int getJungleFieldUsability(Coordinate coordinate) {
		return jungleFields.get(coordinate.row() * columns + coordinate.column()).getUsability();
	}

	public char getJungleFieldSign(Coordinate coordinate) {
		return jungleFields.get(coordinate.row() * columns + coordinate.column()).getCharacter();
	}
	
	public int getFieldValue(Coordinate coordinate) {
		return jungleFields.get(coordinate.row() * columns + coordinate.column()).getFieldValue();
	}

	public void removeAllSnakeParts() {
		jungleFields.forEach(JungleField::removeSnakeParts);
	}

	public List<JungleField> getUsabilityFieldsByChar(char firstChar) {
		LinkedList<JungleField> result = new LinkedList<>();
		for (JungleField jungleField : jungleFields) {
			if (jungleField.getCharacter() == firstChar && jungleField.getUsability() > 0) {
				result.add(jungleField);
			}
		}
		return result;
	}
	
	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public void removeSnakePart(SnakePart snakePart) {
		jungleFields.get(snakePart.getRow() * columns + snakePart.getColumn()).removeSnakePart(snakePart);
		
	}

}
