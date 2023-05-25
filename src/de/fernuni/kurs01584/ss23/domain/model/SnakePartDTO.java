package de.fernuni.kurs01584.ss23.domain.model;

/**
 * DTO representing the data of a snake part of a snake.
 *
 * @param fieldId The jungle field id on which the snake part lies.
 * @param character The character of the snake part.
 * @param row The row of the jungle field on which the snake part lies.
 * @param column The column of the jungle field on which the snake part lies.
 */
public record SnakePartDTO(String fieldId, char character, int row, int column) {
}
