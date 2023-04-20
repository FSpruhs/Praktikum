package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

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

	public void setSnakeValue(int snakeValue) {
		this.snakeValue = snakeValue;
	}

	public char getFirstChar() {
		return firstChar;
	}

	public void setFirstChar(char firstChar) {
		this.firstChar = firstChar;
	}

	public NeighborhoodStructure getNeighborhoodStructure() {
		return neighborhoodStructure;
	}

	@Override
	public int compareTo(SnakeHead s) {
		return Integer.compare(snakeValue, s.getSnakeValue());
	}

}
