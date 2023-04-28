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

	public Snake(String snakeTypeId, List<SnakePart> snakeParts, NeighborhoodStructure neighborhoodStructure) {
		this.snakeTypeId = snakeTypeId;
		this.snakeParts = snakeParts;
		this.neighborhoodStructure = neighborhoodStructure;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Snake snake = (Snake) o;
		return Objects.equals(snakeTypeId, snake.snakeTypeId) && Objects.equals(snakeParts, snake.snakeParts) && Objects.equals(neighborhoodStructure, snake.neighborhoodStructure);
	}

	@Override
	public int hashCode() {
		return Objects.hash(snakeTypeId, snakeParts, neighborhoodStructure);
	}

	@Override
	public String toString() {
		return "Snake{" +
				"snakeTypeId='" + snakeTypeId + '\'' +
				", snakeParts=" + snakeParts +
				", neighborhoodStructure=" + neighborhoodStructure +
				'}';
	}

	public void addSnakePart(SnakePart snakePart) {
		snakeParts.add(snakePart);
	}
	
	public NeighborhoodStructure getNeighborhoodStructure() {
		return neighborhoodStructure;
	}

	public void removeLastSnakePart() {
		snakeParts.remove(snakeParts.size() - 1);
	}

}
