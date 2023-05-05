package de.fernuni.kurs01584.ss23.domain.model;

import java.time.Duration;
import java.util.Map;

public interface SnakeSearchAlgorithmus {
	Solution solveSnakeHuntInstance(
			Jungle jungle,
			Map<SnakeTypeId, SnakeType> snakeTypes,
			Duration durationInSeconds
	);
}
