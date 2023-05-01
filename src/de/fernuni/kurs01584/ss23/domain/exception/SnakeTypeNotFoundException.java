package de.fernuni.kurs01584.ss23.domain.exception;

public class SnakeTypeNotFoundException extends RuntimeException{

	public SnakeTypeNotFoundException(String snakeTypeId) {
		super("Snake type with value %s not found!".formatted(snakeTypeId));
	}

}
