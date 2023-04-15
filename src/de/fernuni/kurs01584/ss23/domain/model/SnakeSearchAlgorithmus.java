package de.fernuni.kurs01584.ss23.domain.model;

import java.time.Duration;
import java.util.Map;

public interface SnakeSearchAlgorithmus {
	int solveSnakeHuntInstance(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds);
}
