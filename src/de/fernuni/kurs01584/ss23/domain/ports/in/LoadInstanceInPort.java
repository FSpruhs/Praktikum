package de.fernuni.kurs01584.ss23.domain.ports.in;

import java.time.Duration;
import java.util.List;

import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.Solution;

public interface LoadInstanceInPort {
	
	void loadInstance(Jungle jungle, List<SnakeType> snakeTypes, Duration durationInSeconds, Solution solution);
	void loadInstance(Jungle jungle, List<SnakeType> snakeTypes, Duration durationInSeconds);

}
