package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;

import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

public class NeighborhoodstructureTrueMock implements NeighborhoodStructure{

	@Override
	public boolean isNotNeighbour(Coordinate actualCoordinate, Coordinate previousCoordinate) {
		if (actualCoordinate.row() == 1 && actualCoordinate.column() == 1) {
			return true;
		}
		if (actualCoordinate.row() == 0 && actualCoordinate.column() == 1) {
			return true;
		}
		return false;
	}

	@Override
	public List<Coordinate> nextFields(Coordinate coordinate, JungleSize jungleSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "Mock";
	}

	@Override
	public List<Integer> getParameter() {
		return null;
	}

}
