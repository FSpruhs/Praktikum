package de.fernuni.kurs01584.ss23.domain.model;

import java.time.Duration;
import java.util.List;

import de.fernuni.kurs01584.ss23.domain.ports.in.LoadInstanceInPort;

public class SnakeHuntInstance implements LoadInstanceInPort{
	
	private Jungle jungle;
	private List<SnakeType> snakeTypes;
	private Duration durationInSeconds;
	private Solution solution;
	private SnakeSearchAlgorithmus snakeSearchAlgorithmus;
	
	
	@Override
	public void loadInstance(Jungle jungle, List<SnakeType> snakeTypes, Duration durationInSeconds, Solution solution) {
		this.jungle = jungle;
		this.snakeTypes = snakeTypes;
		this.durationInSeconds = durationInSeconds;
		this.solution = solution;
	}
	
	@Override
	public void loadInstance(Jungle jungle, List<SnakeType> snakeTypes, Duration durationInSeconds) {
		this.jungle = jungle;
		this.snakeTypes = snakeTypes;
		this.durationInSeconds = durationInSeconds;
		this.solution = new Solution();
	}
	

	
	

}
