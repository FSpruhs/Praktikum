package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;
import java.util.Objects;

import javax.sql.RowSet;

public class Jungle {
	
	private final int rows;
	private final int columns;
	private final String characters;
	private final List<JungleField> jungleFields;
	
	public Jungle(int rows, int columns, String characters, List<JungleField> jungleFields) {
		this.rows = rows;
		this.columns = columns;
		this.characters = characters;
		this.jungleFields = jungleFields;
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
	
	

}
