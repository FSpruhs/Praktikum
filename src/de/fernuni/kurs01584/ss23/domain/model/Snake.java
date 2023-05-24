package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;

import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

/**
 * Value Object of a snake. The snake represents a concrete snake type found in a jungle.
 *
 * @param snakeTypeId The id to the snake type the snake represents.
 * @param snakeParts The snake parts that make up the snake.
 * @param neighborhoodStructure The neighborhood structure according to which the parts of the snake are arranged in the jungle.
 */
public record Snake(SnakeTypeId snakeTypeId,
					List<SnakePart> snakeParts,
					NeighborhoodStructure neighborhoodStructure) {


	/**
	 * Returns the number of parts that make up the snake.
	 *
	 * @return the number of snake parts.
	 */
	public int getLength() {
		return snakeParts.size();
	}

	/**
	 * Add another snake part to the snake.
	 *
	 * @param snakePart the new snake part to be added.
	 */
	public void addSnakePart(SnakePart snakePart) {
		snakeParts.add(snakePart);
	}

	/**
	 * Removes the last snake part from the list of snake parts.
	 */
	public void removeLastSnakePart() {
		snakeParts.remove(snakeParts.size() - 1);
	}

}
