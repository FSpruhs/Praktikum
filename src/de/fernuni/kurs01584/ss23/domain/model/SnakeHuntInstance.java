package de.fernuni.kurs01584.ss23.domain.model;

import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidDurationException;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleException;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;
import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.domain.ports.in.ValidationInPort;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;

public class SnakeHuntInstance implements ValidationInPort {
	
	private Jungle jungle;
	private Map<String, SnakeType> snakeTypes;
	private Duration durationInSeconds;
	private Solution solution;
	private SnakeSearchAlgorithmus snakeSearchAlgorithmus;
	
	
	public SnakeHuntInstance(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds, Solution solution) {
		validateInitialData(jungle, snakeTypes, durationInSeconds);
		this.jungle = jungle;
		this.snakeTypes = snakeTypes;
		this.durationInSeconds = durationInSeconds;
		this.solution = solution;
	}

	public SnakeHuntInstance(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds) {
		validateInitialData(jungle, snakeTypes, durationInSeconds);
		this.jungle = jungle;
		this.snakeTypes = snakeTypes;
		this.durationInSeconds = durationInSeconds;
	}
	
	private void validateInitialData(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds) {
		if (jungle == null) {
			throw new InvalidJungleException("Jungle is Null!");
		}
		if (snakeTypes == null || snakeTypes.isEmpty()) {
			throw new InvalidSnakeTypesException("");
		}
		if (durationInSeconds == null) {
			throw new InvalidDurationException("Duration in Seconds is Null!");
		}
		
	}

	@Override
	public List<Fehlertyp> isValid() {
		if (solution == null) {
			throw new NoSolutionException();
		}
		List<Fehlertyp> result = new LinkedList<>();
		List<Snake> solutionSnakes = solution.getSnakes();
		for (Snake snake : solutionSnakes) {
			SnakeType snakeType = snakeTypes.get(snake.getSnakeTypeId());
			if (snakeType == null) {
				throw new InvalidSnakeTypesException("Snake Type with id %s does not exist!".formatted(snake.getSnakeTypeId()));
			}
			if (snakeType.getSnakeLength() != snake.getLength()) {
				result.add(Fehlertyp.GLIEDER);
			}
			
			for (SnakePart snakePart : snake.getSnakeParts()) {
				jungle.placeSnakePart(snakePart, snakePart.getRow(), snakePart.getColumn());
				if (jungle.getJungleFieldUsability(snakePart.getRow(), snakePart.getColumn()) < 0) {
					result.add(Fehlertyp.VERWENDUNG);
				}
				if (jungle.getJungleFieldSign(snakePart.getRow(), snakePart.getColumn()) != snakePart.getCharachter()) {
					result.add(Fehlertyp.ZUORDNUNG);
				}	
			}
			//TODO implement NACHBARSCHAFT Validation
		}
		
		return result;
	}
	

}
