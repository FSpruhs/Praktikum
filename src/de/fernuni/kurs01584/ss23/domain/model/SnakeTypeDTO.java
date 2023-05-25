package de.fernuni.kurs01584.ss23.domain.model;

/**
 * DTO representing the data of a snake type.
 *
 * @param snakeTypeId The snake type id of the snake type.
 * @param snakeValue The value of the snake type.
 * @param count The number of times the snake type occurs in the jungle.
 * @param characterBand The sequence of signs of the snake type.
 * @param neighborhoodStructure The neighborhood structure after which the characters are distributed on the jungle.
 */
public record SnakeTypeDTO(String snakeTypeId,
                           int snakeValue,
                           int count,
                           String characterBand,
                           String neighborhoodStructure) {
}
