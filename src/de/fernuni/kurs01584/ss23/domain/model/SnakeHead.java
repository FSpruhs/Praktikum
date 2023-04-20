package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

import java.util.Objects;

public class SnakeHead implements Comparable<SnakeHead>{
	
	private String id;
	private int snakeValue;
	private char firstChar;
	private final NeighborhoodStructure neighborhoodStructure;

	public SnakeHead(int snakeValue, String id, char firstChar, NeighborhoodStructure neighborhoodStructure) {
		this.id = id;
		this.snakeValue = snakeValue;
		this.firstChar = firstChar;
		this.neighborhoodStructure = neighborhoodStructure;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSnakeValue() {
		return snakeValue;
	}


	public char getFirstChar() {
		return firstChar;
	}

	public NeighborhoodStructure getNeighborhoodStructure() {
		return neighborhoodStructure;
	}

	@Override
	public int compareTo(SnakeHead s) {
		return Integer.compare(snakeValue, s.getSnakeValue());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SnakeHead snakeHead = (SnakeHead) o;
		return snakeValue == snakeHead.snakeValue && firstChar == snakeHead.firstChar && Objects.equals(id, snakeHead.id) && Objects.equals(neighborhoodStructure, snakeHead.neighborhoodStructure);
	}

	@Override
	public String toString() {
		return "SnakeHead{" +
				"id='" + id + '\'' +
				", snakeValue=" + snakeValue +
				", firstChar=" + firstChar +
				", neighborhoodStructure=" + neighborhoodStructure +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, snakeValue, firstChar, neighborhoodStructure);
	}
}
