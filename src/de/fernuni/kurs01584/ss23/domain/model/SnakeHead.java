package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

import java.util.Objects;

public class SnakeHead implements Comparable<SnakeHead>{
	
	private SnakeTypeId snakeTypeId;
	private final int snakeValue;
	private final char firstChar;

	public SnakeHead(int snakeValue, SnakeTypeId snakeTypeId, char firstChar, NeighborhoodStructure neighborhoodStructure) {
		this.snakeTypeId = snakeTypeId;
		this.snakeValue = snakeValue;
		this.firstChar = firstChar;
	}

	public SnakeTypeId getSnakeTypeId() {
		return snakeTypeId;
	}

	public int getSnakeValue() {
		return snakeValue;
	}


	public char getFirstChar() {
		return firstChar;
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
		return snakeValue == snakeHead.snakeValue && firstChar == snakeHead.firstChar && Objects.equals(snakeTypeId, snakeHead.snakeTypeId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(snakeTypeId, snakeValue, firstChar);
	}

	@Override
	public String toString() {
		return "SnakeHead{" +
				"value='" + snakeTypeId + '\'' +
				", snakeValue=" + snakeValue +
				", firstChar=" + firstChar +
				'}';
	}

}
