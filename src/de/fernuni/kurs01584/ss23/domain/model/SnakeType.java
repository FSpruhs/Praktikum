package de.fernuni.kurs01584.ss23.domain.model;


import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

public record SnakeType(SnakeTypeId snakeTypeId,
						int snakeValue,
						int count,
						String characterBand,
						NeighborhoodStructure neighborhoodStructure) {

	public SnakeType {
		if (characterBand == null) {
			throw new InvalidSnakeTypesException("Character Band Structure must not be null!");
		}

		if (count <= 0) {
			throw new InvalidSnakeTypesException("Count must be greater than 0!");
		}

		if (snakeValue <= 0) {
			throw new InvalidSnakeTypesException("Snake Value must be greater than 0!");
		}

		if (neighborhoodStructure == null) {
			throw new InvalidSnakeTypesException("Neighborhood Structure must not be null!");
		}

	}

	public int getSnakeLength() {
		return characterBand.length();
	}

	public boolean isNotSuccessor(SnakePart actualSnakePart, SnakePart previousSnakePart) {
		return neighborhoodStructure.isNotNeighbour(actualSnakePart.coordinate(), previousSnakePart.coordinate());
	}

}
