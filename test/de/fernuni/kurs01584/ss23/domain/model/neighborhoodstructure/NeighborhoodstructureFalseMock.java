package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

import java.util.List;

import de.fernuni.kurs01584.ss23.domain.model.Coordinate;
import de.fernuni.kurs01584.ss23.domain.model.JungleSize;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

public class NeighborhoodstructureFalseMock implements NeighborhoodStructure{

	@Override
	public boolean isNotNeighbour(Coordinate actualCoordinate, Coordinate previousCoordinate) {
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
