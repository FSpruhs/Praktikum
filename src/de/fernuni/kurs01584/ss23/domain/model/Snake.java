package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;

import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

public record Snake(String snakeTypeId, List<SnakePart> snakeParts, NeighborhoodStructure neighborhoodStructure) {


	public int getLength() {
		return snakeParts.size();
	}

	public void addSnakePart(SnakePart snakePart) {
		snakeParts.add(snakePart);
	}

	public void removeLastSnakePart() {
		snakeParts.remove(snakeParts.size() - 1);
	}

}
