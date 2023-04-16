package de.fernuni.kurs01584.ss23.domain.model;

import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

public class SnakeType {
	
	private final String id;
	private final int snakeValue;
	private final int count;
	private final String characterband;
	private final NeighborhoodStructure neighborhoodStructure;
	
	public SnakeType(String id, int snakeValue, int count, String characterband,
			NeighborhoodStructure neighborhoodStructure) {
		
		if (characterband == null || neighborhoodStructure == null) {
			throw new InvalidSnakeTypesException("Characterband and NeigborhoodStructure must not be null!");
		}
		
		if (snakeValue <= 0 || count <= 0) {
			throw new InvalidSnakeTypesException("Count and Snake Value must be greater than 0!");
		}
		
		if (!id.substring(0, 1).equals("A") || !id.substring(1).matches("\\d+")) {
			throw new InvalidSnakeTypesException("Invalid id");
		}
		
		
		this.id = id;
		this.snakeValue = snakeValue;
		this.count = count;
		this.characterband = characterband;
		this.neighborhoodStructure = neighborhoodStructure;
	}
	
	public int getSnakeLength() {
		return characterband.length();
	}

	@Override
	public int hashCode() {
		return Objects.hash(characterband, count, id, neighborhoodStructure, snakeValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SnakeType other = (SnakeType) obj;
		return Objects.equals(characterband, other.characterband) && count == other.count
				&& Objects.equals(id, other.id) && Objects.equals(neighborhoodStructure, other.neighborhoodStructure)
				&& snakeValue == other.snakeValue;
	}

	public boolean isNotSuccessor(SnakePart actualSnakePart, SnakePart previousSnakePart) {
		return neighborhoodStructure.isNotNeighbour(actualSnakePart.getCoordinate(), previousSnakePart.getCoordinate());
	}

	public char getCharachterAt(int counter) {
		return characterband.charAt(counter);
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
		return characterband;
	}

	public NeighborhoodStructure getNeighborhoodStructure() {
		return neighborhoodStructure;
	}

	
	

}
