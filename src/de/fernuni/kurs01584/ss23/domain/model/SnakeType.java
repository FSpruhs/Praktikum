package de.fernuni.kurs01584.ss23.domain.model;


import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

/**
 * Value object representing a snake type to be searched in the jungle.
 *
 * @param snakeTypeId The snake type id of the snake type.
 * @param snakeValue The value of the snake type. Must be positive.
 * @param count The number of times the snake type occurs in the jungle. Must be positive.
 * @param characterBand The sequence of signs of the snake type. Cant be null.
 * @param neighborhoodStructure The neighborhood structure after which the characters are distributed on the jungle.
 *                              Cant be null.
 */
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

	/**
	 * Returns the length of the snake type. This corresponds to the length of the character band.
	 *
	 * @return lenght of the snake type.
	 */
	public int getSnakeLength() {
		return characterBand.length();
	}

	/**
	 * Checks whether the current snake pat can follow on the previous snake part.
	 * The rules of the neighborhood structure are used for the check.
	 *
	 * @param actualSnakePart  the actual snake part.
	 * @param previousSnakePart the previous snake part.
	 * @return Returns true if the current snake part can follow the previous snake part, otherwise false.
	 */
	public boolean isNotSuccessor(SnakePart actualSnakePart, SnakePart previousSnakePart) {
		return neighborhoodStructure.isNotNeighbour(actualSnakePart.coordinate(), previousSnakePart.coordinate());
	}

}
