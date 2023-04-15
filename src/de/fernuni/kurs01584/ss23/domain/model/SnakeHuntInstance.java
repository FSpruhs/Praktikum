package de.fernuni.kurs01584.ss23.domain.model;

import java.time.Duration;
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
	
	private final Jungle jungle;
	private final Map<String, SnakeType> snakeTypes;
	private final Duration durationInSeconds;
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
		List<Fehlertyp> result = new LinkedList<>();
		solutionNullCheck();
		for (Snake snake : solution.getSnakes()) {
			findSnakeErrors(result, snake);
		}
		return result;
	}

	private void findSnakeErrors(List<Fehlertyp> result, Snake snake) {
		findLengthError(result, snake);
		SnakePart previousSnakePart = null;
		for (SnakePart snakePart : snake.getSnakeParts()) {
			jungle.placeSnakePart(snakePart, snakePart.getRow(), snakePart.getColumn());
			findUsageError(result, snakePart);
			findAllocationError(result, snakePart);
			findNeighborhoodError(result, snakePart, previousSnakePart, snakeTypes.get(snake.getSnakeTypeId()));
			previousSnakePart = snakePart;
		}
		jungle.removeAllSnakeParts();
	}

	private void solutionNullCheck() {
		if (solution == null) {
			throw new NoSolutionException();
		}
	}

	private SnakeType getSnakeType(String snakeTypeId) {
		if (snakeTypes.get(snakeTypeId) == null) {
			throw new InvalidSnakeTypesException("Snake Type with id %s does not exist!".formatted(snakeTypeId));
		}
		return snakeTypes.get(snakeTypeId);
	}

	private void findLengthError(List<Fehlertyp> result, Snake snake) {
		if (getSnakeType(snake.getSnakeTypeId()).getSnakeLength() != snake.getLength()) {
			result.add(Fehlertyp.GLIEDER);
		}
	}

	private void findNeighborhoodError(List<Fehlertyp> result, SnakePart snakePart, SnakePart previousSnakePart, SnakeType snakeType) {
		if (previousSnakePart != null) {
			if (snakeType.isNotSuccessor(snakePart, previousSnakePart)) {
				result.add(Fehlertyp.NACHBARSCHAFT);
			}
		}
	}

	private void findAllocationError(List<Fehlertyp> result, SnakePart snakePart) {
		if (jungle.getJungleFieldSign(snakePart.getRow(), snakePart.getColumn()) != snakePart.getCharachter()) {
			result.add(Fehlertyp.ZUORDNUNG);
		}
		
	}

	private void findUsageError(List<Fehlertyp> result, SnakePart snakePart) {
		if (jungle.getJungleFieldUsability(snakePart.getRow(), snakePart.getColumn()) < 0) {
			result.add(Fehlertyp.VERWENDUNG);
		}
	}
}
