package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

public class NeighborhoodstructureFalseMock implements NeighborhoodStructure{

	@Override
	public boolean isNotNeighbour(SnakePart actualSnakePart, SnakePart previousSnakePart) {
		return false;
	}

}
