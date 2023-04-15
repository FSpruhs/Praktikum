package de.fernuni.kurs01584.ss23.domain.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleException;
import de.fernuni.kurs01584.ss23.domain.exception.JungleFieldNotFoundException;

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
			throw new InvalidJungleException("Charachters is Null!");
		}
		
		int counter = 0;
		for (JungleField jungleField : jungleFields) {
			if (jungleField == null) {
				throw new InvalidJungleException("Junglefield is Null!");
			}
			if (Integer.parseInt(jungleField.getId().substring(1)) !=  counter || !jungleField.getId().substring(0,  1).equals("F")) {
				throw new InvalidJungleException("Junglefield is Invalid");
			}
			counter++;
		}
		
		this.rows = rows;
		this.columns = columns;
		this.characters = characters;
		this.jungleFields = jungleFields;
	}
	
	public List<JungleField> getJungleFields() {
		return jungleFields;
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

	public JungleField getJungleField(String fieldId) {
		return jungleFields
				.stream()
				.filter(jungleField -> jungleField.getId().equals(fieldId))
				.findFirst()
				.orElseThrow(() -> new JungleFieldNotFoundException(fieldId));	
	}

	public void placeSnakePart(SnakePart snakePart, Coordinate coordinate) {
		jungleFields.get(coordinate.row() * columns + coordinate.column()).placeSnakePart(snakePart);
	}

	public int getJungleFieldUsability(Coordinate coordinate) {
		return jungleFields.get(coordinate.row() * columns + coordinate.column()).getUsability();
	}

	public char getJungleFieldSign(Coordinate coordinate) {
		return jungleFields.get(coordinate.row() * columns + coordinate.column()).getCharachter();
	}
	
	public int getFieldValue(Coordinate coordinate) {
		return jungleFields.get(coordinate.row() * columns + coordinate.column()).getFieldValue();
	}

	public void removeAllSnakeParts() {
		jungleFields.stream().forEach(jungleField -> jungleField.removeSnakeParts());
	}

	public List<JungleField> getUsabiliyFieldsByChar(char firstChar) {
		LinkedList<JungleField> result = new LinkedList<>();
		for (JungleField jungleField : jungleFields) {
			if (jungleField.getCharachter() == firstChar && jungleField.getUsability() > 0) {
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

}
