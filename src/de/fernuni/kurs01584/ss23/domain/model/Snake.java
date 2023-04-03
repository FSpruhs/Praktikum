package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;

public class Snake {
	
	private final String snakeTypeId;
	private final List<SnakePart> snakeParts;
	
	public Snake(String snakeTypeId, List<SnakePart> snakeParts) {
		this.snakeTypeId = snakeTypeId;
		this.snakeParts = snakeParts;
	}

}
