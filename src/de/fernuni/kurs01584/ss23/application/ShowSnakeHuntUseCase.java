package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.application.ports.in.ShowSnakeHuntIntPort;
import de.fernuni.kurs01584.ss23.domain.model.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Use case to show the data of a snake hunt instance.
 */
public class ShowSnakeHuntUseCase implements ShowSnakeHuntIntPort {

    private final SnakeHunt snakeHunt;

    public ShowSnakeHuntUseCase() {
        this.snakeHunt = SnakeHunt.getInstance();
    }

    /**
     * Returns data of the snake types to be shown.
     *
     * @return map withe snake type id and snake types.
     */
    @Override
    public List<SnakeTypeDTO>  showSnakeTypes() {
        return toSnakeTypeDTO(snakeHunt.getSnakeTypes());
    }

    private List<SnakeTypeDTO> toSnakeTypeDTO(Map<SnakeTypeId, SnakeType> snakeTypes) {
        return snakeTypes.values().stream()
                .map(snakeType -> new SnakeTypeDTO(
                        snakeType.snakeTypeId().value(),
                        snakeType.snakeValue(),
                        snakeType.count(),
                        snakeType.characterBand(),
                        snakeType.neighborhoodStructure().getName()

                ))
                .toList();
    }

    /**
     * Returns data of the solution to be shown.
     *
     * @return data of the solution to be shown.
     */
    @Override
    public List<SnakeDTO> showSolutionSnakes() {
        return toSnakeDTO(snakeHunt.getSolution());
    }

    private List<SnakeDTO> toSnakeDTO(Solution solution) {
        return solution.snakes().stream()
                .map(snake -> new SnakeDTO(
                        snake.snakeTypeId().value(),
                        toSnakePartDTO(snake.snakeParts()),
                        snake.neighborhoodStructure().getName())
                )
                .toList();
    }

    private List<SnakePartDTO> toSnakePartDTO(List<SnakePart> snakeParts) {
        return snakeParts.stream()
                .map(snakePart -> new SnakePartDTO(
                        snakePart.fieldId().value(),
                        snakePart.character(),
                        snakePart.coordinate().row(),
                        snakePart.coordinate().column()
                ))
                .toList();
    }

    /**
     * Returns data of the jungle to be shown.
     *
     * @return data of the jungle to be shown.
     */
    @Override
    public Jungle showJungle() {
        return snakeHunt.getJungle();
    }
}
