package de.fernuni.kurs01584.ss23.domain.model;

import java.io.File;
import java.time.Duration;
import java.util.*;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleException;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;
import de.fernuni.kurs01584.ss23.application.ports.out.SaveSnakeHuntInstanceOutPort;

public class SnakeHuntInstance {
	private final Jungle jungle;
	private final Map<SnakeTypeId, SnakeType> snakeTypes;
	private Duration targetDuration;
	private Duration actualDuration;
	private Solution solution;
	private final SaveSnakeHuntInstanceOutPort repository;
	
	
	public SnakeHuntInstance(Jungle jungle,
							 Map<SnakeTypeId, SnakeType> snakeTypes,
							 Duration targetDuration,
							 Solution solution,
							 SaveSnakeHuntInstanceOutPort saveSnakeHuntInstanceOutPort) {
		this.jungle = jungle;
		this.snakeTypes = snakeTypes;
		this.targetDuration = targetDuration;
		this.solution = solution;
		this.repository = saveSnakeHuntInstanceOutPort;
		validateSnakeHuntInstance();
	}

	public SnakeHuntInstance(Jungle jungle,
							 Map<SnakeTypeId, SnakeType> snakeTypes,
							 Duration targetDuration,
							 SaveSnakeHuntInstanceOutPort saveSnakeHuntInstanceOutPort) {
		this.jungle = jungle;
		this.snakeTypes = snakeTypes;
		this.targetDuration = targetDuration;
		this.repository = saveSnakeHuntInstanceOutPort;
		validateSnakeHuntInstance();
	}
	
	private void validateSnakeHuntInstance() {
		validateJungle();
		validateSnakeTypes();
		setDefaultDuration();
	}

	private void setDefaultDuration() {
		if (targetDuration == null) {
			this.targetDuration = Duration.ofSeconds(30);
		}
	}

	private void validateSnakeTypes() {
		if (snakeTypes == null || snakeTypes.isEmpty()) {
			throw new InvalidSnakeTypesException("Snake Types is Null!");
		}
	}

	private void validateJungle() {
		if (jungle == null) {
			throw new InvalidJungleException("Jungle is Null!");
		}
	}
	public void save(File file) {
		repository.save(
				file,
				jungle,
				snakeTypes,
				targetDuration,
				actualDuration,
				solution
		);
	}

	public Solution getSolution() {
		return solution;
	}

	public Jungle getJungle() {
		return jungle;
	}

	public Map<SnakeTypeId, SnakeType> getSnakeTypes() {
		return snakeTypes;
	}

	public void setSolution(Solution solution) {
		this.solution = solution;
	}

	public void setActualDuration(Duration actualDuration) {
		this.actualDuration = actualDuration;
	}

	public Duration getTargetDuration() {
		return targetDuration;
	}

	public void setTargetDuration(Duration targetDuration) {
		this.targetDuration = targetDuration;
	}
}
