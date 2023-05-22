package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.application.ports.in.ShowSnakeHuntIntPort;
import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.SnakeTypeId;
import de.fernuni.kurs01584.ss23.domain.model.Solution;

import java.util.Map;

/**
 * Use case to show the data of a snake hunt instance.
 */
public class ShowSnakeHuntUseCase implements ShowSnakeHuntIntPort {

    private final Jungle jungle;
    private final Solution solution;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;

    /**
     * Constructor of the show snake hunt use case.
     *
     * @param jungle jungle data to be shown.
     * @param solution solution data to be shown.
     * @param snakeTypes map of snake id and snake typed to be shown.
     */
    public ShowSnakeHuntUseCase(
            Jungle jungle,
            Solution solution,
            Map<SnakeTypeId, SnakeType> snakeTypes
    ) {
        this.jungle = jungle;
        this.solution = solution;
        this.snakeTypes = snakeTypes;
    }

    /**
     * Returns data of the snake types to be shown.
     *
     * @return map withe snake type id and snake types.
     */
    @Override
    public Map<SnakeTypeId, SnakeType>  showSnakeTypes() {
        return snakeTypes;
    }

    /**
     * Returns data of the solution to be shown.
     *
     * @return data of the solution to be shown.
     */
    @Override
    public Solution showSolution() {
        return solution;
    }

    /**
     * Returns data of the jungle to be shown.
     *
     * @return data of the jungle to be shown.
     */

    @Override
    public Jungle showJungle() {
        return jungle;
    }
}
