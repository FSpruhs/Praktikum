package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

import java.util.List;

import de.fernuni.kurs01584.ss23.domain.model.Coordinate;
import de.fernuni.kurs01584.ss23.domain.model.JungleSize;

public interface NeighborhoodStructure {

	boolean isNotNeighbour(Coordinate actualCoordinate, Coordinate previousCoordinate);

	List<Coordinate> nextFields(Coordinate coordinate, JungleSize jungleSize);

	String getName();

	List<Integer> getParameter();

}
