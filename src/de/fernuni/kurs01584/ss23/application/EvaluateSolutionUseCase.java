package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.application.ports.in.EvaluateSolutionInPort;
import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.domain.model.*;

import java.util.Map;

/**
 * Use case to evaluate the value of the solution of the snake hunt.
 */
public class EvaluateSolutionUseCase implements EvaluateSolutionInPort {

    private final Solution solution;
    private final Jungle jungle;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;

    /**
     * Constructor of the evaluate solution use case.
     *
     * @param solution the solution to evaluate.
     * @param jungle the Jungle with which the solution is to be evaluated.
     * @param snakeTypes a map with the snake types with which the solution is to be evaluated.
     */
    public EvaluateSolutionUseCase(
            Solution solution,
            Jungle jungle,
            Map<SnakeTypeId, SnakeType> snakeTypes
    ) {
        this.solution = solution;
        this.jungle = jungle;
        this.snakeTypes = snakeTypes;
    }


    /**
     * Evaluates the total points of the given solution.
     *
     * @return the total points of the given solution.
     */
    @Override
    public int evaluateTotalPoints() {
        solutionNullCheck();
        int result = 0;
        for (Snake snake : solution.snakes()) {
            result += snakeValue(snake) + sumSnakePartValues(snake, jungle);
        }
        return result;
    }

    private int snakeValue(Snake snake) {
        return snakeTypes.get(snake.snakeTypeId()).snakeValue();
    }

    private void solutionNullCheck() {
        if (solution == null) {
            throw new NoSolutionException();
        }
    }

    private int sumSnakePartValues(Snake snake, Jungle jungle) {
        return snake.snakeParts().stream()
                .mapToInt(snakePart -> jungle.getFieldValue(snakePart.coordinate()))
                .sum();
    }
}
