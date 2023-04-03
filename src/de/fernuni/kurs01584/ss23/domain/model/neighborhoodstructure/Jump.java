package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

public class Jump implements NeighborhoodStructure{
	
	private final int row;
	private final int column;
	
	public Jump(int row, int column) {
		this.row = row;
		this.column = column;
	}

}
