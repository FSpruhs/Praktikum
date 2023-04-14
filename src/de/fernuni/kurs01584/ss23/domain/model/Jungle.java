package de.fernuni.kurs01584.ss23.domain.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.sql.RowSet;

public class Jungle {
	
	private final int rows;
	private final int columns;
	private final String characters;
	private final JungleField[][] jungleFields;
	
	public Jungle(int rows, int columns, String characters, JungleField[][]jungleFields) {
		this.rows = rows;
		this.columns = columns;
		this.characters = characters;
		this.jungleFields = jungleFields;
	}
	
	public JungleField[][] getJungleFields() {
		return jungleFields;
	}
	
	public JungleField getJungleField(int row, int colum) {
		return jungleFields[row][colum];
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(jungleFields);
		result = prime * result + Objects.hash(characters, columns, rows);
		return result;
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
				&& Arrays.deepEquals(jungleFields, other.jungleFields) && rows == other.rows;
	}

	public JungleField getJungleField(String fieldId) {
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				if (jungleFields[row][column].getId().equals(fieldId)) {
					return jungleFields[row][column];
				}
			}
		}
		return null;
	}

	public void placeSnakePart(SnakePart snakePart, int row, int column) {
		jungleFields[row][column].placeSnakePart(snakePart);
	}

	public int getJungleFieldUsability(int row, int column) {
		return jungleFields[row][column].getUsability();
	}

	public char getJungleFieldSign(int row, int column) {
		return jungleFields[row][column].getCharachter();
	}

	public void removeAllSnakeParts() {
		// TODO Auto-generated method stub
		
	}

}
