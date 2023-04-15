package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

import java.util.List;

import de.fernuni.kurs01584.ss23.domain.model.SnakePart;

public interface NeighborhoodStructure {

	boolean isNotNeighbour(SnakePart actualSnakePart, SnakePart previousSnakePart);

	List<String> nextFields(SnakePart snakePart, int rows, int columns);

}
