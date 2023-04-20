package de.fernuni.kurs01584.ss23.domain.model;

import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

public class SnakeType {
	
	private final String id;
	private final int snakeValue;
	private final int count;
	private final String characterBand;
	private final NeighborhoodStructure neighborhoodStructure;
	
	public SnakeType(String id, int snakeValue, int count, String characterBand,
			NeighborhoodStructure neighborhoodStructure) {

		this.id = id;
		this.snakeValue = snakeValue;
		this.count = count;
		this.characterBand = characterBand;
		this.neighborhoodStructure = neighborhoodStructure;
		validateSnakeType();
	}

	private void validateSnakeType() {
		validateCharacterBand();
		validateNeighborhoodStructure();
		validateSnakeValue();
		validateCount();
		validateId();
	}

	private void validateCount() {
		if (count <= 0) {
			throw new InvalidSnakeTypesException("Count must be greater than 0!");
		}
	}

	private void validateSnakeValue() {
		if (snakeValue <= 0) {
			throw new InvalidSnakeTypesException("Snake Value must be greater than 0!");
		}
	}

	private void validateNeighborhoodStructure() {
		if (neighborhoodStructure == null) {
			throw new InvalidSnakeTypesException("Neighborhood Structure must not be null!");
		}
	}

	private void validateCharacterBand() {
		if (characterBand == null ) {
			throw new InvalidSnakeTypesException("Character Band Structure must not be null!");
		}
	}

	private void validateId() {
		if (id.charAt(0) != 'A' || !id.substring(1).matches("\\d+")) {
			throw new InvalidSnakeTypesException("Invalid id");
		}
	}

	public int getSnakeLength() {
		return characterBand.length();
	}

	public boolean isNotSuccessor(SnakePart actualSnakePart, SnakePart previousSnakePart) {
		return neighborhoodStructure.isNotNeighbour(actualSnakePart.coordinate(), previousSnakePart.coordinate());
	}

	public int getSnakeValue() {
		return snakeValue;
	}
	
	public int getCount() {
		return count;
	}
	
	public String getId() {
		return id;
	}
	
	public String getCharacterBand() {
		return characterBand;
	}

	public NeighborhoodStructure getNeighborhoodStructure() {
		return neighborhoodStructure;
	}

	@Override
	public String toString() {
		return "SnakeType{" +
				"id='" + id + '\'' +
				", snakeValue=" + snakeValue +
				", count=" + count +
				", characterBand='" + characterBand + '\'' +
				", neighborhoodStructure=" + neighborhoodStructure +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SnakeType snakeType = (SnakeType) o;
		return snakeValue == snakeType.snakeValue && count == snakeType.count && Objects.equals(id, snakeType.id) && Objects.equals(characterBand, snakeType.characterBand) && Objects.equals(neighborhoodStructure, snakeType.neighborhoodStructure);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, snakeValue, count, characterBand, neighborhoodStructure);
	}
}
