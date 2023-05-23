package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;

public record SnakeDTO(
        String snakeTypeId,
        List<SnakePartDTO> snakeParts,
        String neighborhoodStructure
) {
}
