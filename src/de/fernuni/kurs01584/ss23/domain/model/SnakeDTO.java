package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;

/**
 * DTO that represents the values of a snake.
 *
 * @param snakeTypeId The id to the snake type the snake represents.
 * @param snakeParts The snake parts that make up the snake.
 * @param neighborhoodStructure The name of the neighborhood structure according to which the parts
 *                              of the snake are arranged in the jungle.
 */
public record SnakeDTO(
        String snakeTypeId,
        List<SnakePartDTO> snakeParts,
        String neighborhoodStructure
) {
}
