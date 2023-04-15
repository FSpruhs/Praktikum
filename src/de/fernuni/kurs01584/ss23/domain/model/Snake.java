package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

public class Snake {
	
	private final String snakeTypeId;
	private List<SnakePart> snakeParts;
	private NeighborhoodStructure neighborhoodStructure;
	
	public Snake(String snakeTypeId, List<SnakePart> snakeParts) {
		this.snakeTypeId = snakeTypeId;
		this.snakeParts = snakeParts;
	}
	
	public Snake(String snakeTypeId, NeighborhoodStructure neighborhoodStructure) {
		this.snakeTypeId = snakeTypeId;
		this.neighborhoodStructure = neighborhoodStructure;
	}

	public String getSnakeTypeId() {
		return snakeTypeId;
	}
	
	public List<SnakePart> getSnakeParts() {
		return snakeParts;
	}
	
	public int getLength() {
		return snakeParts.size();
	}

	@Override
	public int hashCode() {
		return Objects.hash(snakeParts, snakeTypeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Snake other = (Snake) obj;
		return Objects.equals(snakeParts, other.snakeParts) && Objects.equals(snakeTypeId, other.snakeTypeId);
	}

	@Override
	public String toString() {
		return "Snake [snakeTypeId=" + snakeTypeId + ", snakeParts=" + snakeParts + "]";
	}

	public void addSnakePart(SnakePart snakePart) {
		snakeParts.add(snakePart);
	}
	
	public NeighborhoodStructure getNeighborhoodStructure() {
		return neighborhoodStructure;
	}

}
