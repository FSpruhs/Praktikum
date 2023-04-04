package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

import java.util.Objects;

public class Jump implements NeighborhoodStructure{
	
	private final int row;
	private final int column;
	
	public Jump(int row, int column) {
		this.row = row;
		this.column = column;
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jump other = (Jump) obj;
		return column == other.column && row == other.row;
	}

}
