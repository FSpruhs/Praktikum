package de.fernuni.kurs01584.ss23.domain.model;

/**
 * Value object representing a snake part of a snake.
 *
 * @param fieldId The jungle field id on which the snake part lies.
 * @param character The character of the snake part.
 * @param coordinate The coordinates of the jungle field on which the snake part lies.
 */
public record SnakePart(FieldId fieldId, char character, Coordinate coordinate) {

}
