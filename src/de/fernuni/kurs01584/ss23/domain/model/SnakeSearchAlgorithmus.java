package de.fernuni.kurs01584.ss23.domain.model;

import java.time.Duration;
import java.util.Map;

public interface SnakeSearchAlgorithmus {
	Solution solveSnakeHuntInstance(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds, SolutionValueCalculator solutionValueCalculator);
}
