package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;

import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

public class NeighborhoodstructureFalseMock implements NeighborhoodStructure{

	@Override
	public boolean isNotNeighbour(Coordinate actualCoordinate, Coordinate previousCoordinate) {
		return false;
	}

	@Override
	public List<Coordinate> nextFields(Coordinate coordinate, int rows, int columns) {
		// TODO Auto-generated method stub
		return null;
	}

}
