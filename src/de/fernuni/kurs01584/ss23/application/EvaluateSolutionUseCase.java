package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.application.ports.in.EvaluateSolutionInPort;
import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.domain.model.*;

import java.util.List;

/**
 * Use case to evaluate the value of the solution of the snake hunt.
 */
public class EvaluateSolutionUseCase implements EvaluateSolutionInPort {

    private final SnakeHunt snakeHunt;

    public EvaluateSolutionUseCase() {
        this.snakeHunt = SnakeHunt.getInstance();
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
        for (Snake snake : getSnakes()) {
            result += snakeValue(snake) + sumSnakePartValues(snake, snakeHunt.getJungle());
        }
        return result;
    }

    private List<Snake> getSnakes() {
        return snakeHunt.getSolution().snakes();
    }

    private int snakeValue(Snake snake) {
        return snakeHunt.getSnakeTypes().get(snake.snakeTypeId()).snakeValue();
    }

    private void solutionNullCheck() {
        if (snakeHunt.getSolution() == null) {
            throw new NoSolutionException();
        }
    }

    private int sumSnakePartValues(Snake snake, Jungle jungle) {
        return snake.snakeParts().stream()
                .mapToInt(snakePart -> jungle.getFieldValue(snakePart.coordinate()))
                .sum();
    }
}
