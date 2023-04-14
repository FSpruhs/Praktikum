package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

public class NeighborhoodstructureTrueMock implements NeighborhoodStructure{

	@Override
	public boolean isNotNeighbour(SnakePart actualSnakePart, SnakePart previousSnakePart) {
		if (actualSnakePart.getRow() == 1 && actualSnakePart.getColumn() == 1) {
			return true;
		}
		if (actualSnakePart.getRow() == 0 && actualSnakePart.getColumn() == 1) {
			return true;
		}
		return false;
	}

}
