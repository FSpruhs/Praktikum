package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;
import java.util.Objects;

public class Snake {
	
	private final String snakeTypeId;
	private final List<SnakePart> snakeParts;
	
	public Snake(String snakeTypeId, List<SnakePart> snakeParts) {
		this.snakeTypeId = snakeTypeId;
		this.snakeParts = snakeParts;
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

}
