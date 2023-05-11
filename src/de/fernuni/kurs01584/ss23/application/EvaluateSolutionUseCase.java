package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.application.ports.in.EvaluateSolutionInPort;
import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.domain.model.*;

import java.util.Map;

public class EvaluateSolutionUseCase implements EvaluateSolutionInPort {

    private final Solution solution;
    private final Jungle jungle;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;

    public EvaluateSolutionUseCase(Solution solution, Jungle jungle, Map<SnakeTypeId, SnakeType> snakeTypes) {
        this.solution = solution;
        this.jungle = jungle;
        this.snakeTypes = snakeTypes;
    }


    @Override
    public int evaluateTotalPoints() {
        solutionNullCheck();
        return evaluateTotalPoints(solution, snakeTypes, jungle);
    }

    private void solutionNullCheck() {
        if (solution == null) {
            throw new NoSolutionException();
        }
    }

    public int evaluateTotalPoints(Solution solution, Map<SnakeTypeId ,SnakeType> snakeTypes, Jungle jungle) {
        int result = 0;
        for (Snake snake : solution.snakes()) {
            result += snakeTypes.get(snake.snakeTypeId()).snakeValue() + sumSnakePartValues(snake, jungle);
        }
        return result;
    }

    private int sumSnakePartValues(Snake snake, Jungle jungle) {
        return snake.snakeParts().stream()
                .mapToInt(snakePart -> jungle.getFieldValue(snakePart.coordinate()))
                .sum();
    }
}
