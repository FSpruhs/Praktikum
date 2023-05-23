package de.fernuni.kurs01584.ss23.domain.model;

public record SnakeTypeDTO(String snakeTypeId,
                           int snakeValue,
                           int count,
                           String characterBand,
                           String neighborhoodStructure) {
}
