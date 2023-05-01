package de.fernuni.kurs01584.ss23.domain.model;

import java.util.Map;

public class SolutionValueCalculator {



    public int evaluateTotalPoints(Solution solution, Map<SnakeTypeId ,SnakeType> snakeTypes, Jungle jungle) {
        int result = 0;
        for (Snake snake : solution.getSnakes()) {
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
