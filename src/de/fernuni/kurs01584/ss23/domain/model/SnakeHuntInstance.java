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
import de.fernuni.kurs01584.ss23.domain.ports.in.EvaluateSolutionInPort;
import de.fernuni.kurs01584.ss23.domain.ports.in.ShowJungleInPort;
import de.fernuni.kurs01584.ss23.domain.ports.in.ShowSolutionInPort;
import de.fernuni.kurs01584.ss23.domain.ports.in.SolveInPort;
import de.fernuni.kurs01584.ss23.domain.ports.in.ValidationInPort;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;

public class SnakeHuntInstance implements ValidationInPort, ShowJungleInPort, ShowSolutionInPort, EvaluateSolutionInPort, SolveInPort {
	
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
		jungle.removeAllSnakeParts();
		return result;
	}

	private void findSnakeErrors(List<Fehlertyp> result, Snake snake) {
		findLengthError(result, snake);
		SnakePart previousSnakePart = null;
		for (SnakePart snakePart : snake.getSnakeParts()) {
			jungle.placeSnakePart(snakePart, snakePart.getCoordinate());
			findUsageError(result, snakePart);
			findAllocationError(result, snakePart);
			findNeighborhoodError(result, snakePart, previousSnakePart, snakeTypes.get(snake.getSnakeTypeId()));
			previousSnakePart = snakePart;
		}
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
		if (jungle.getJungleFieldSign(snakePart.getCoordinate()) != snakePart.getCharachter()) {
			result.add(Fehlertyp.ZUORDNUNG);
		}
		
	}

	private void findUsageError(List<Fehlertyp> result, SnakePart snakePart) {
		System.out.println(jungle.getJungleFieldUsability(snakePart.getCoordinate()));
		if (jungle.getJungleFieldUsability(snakePart.getCoordinate()) < 0) {
			result.add(Fehlertyp.VERWENDUNG);
		}
	}

	@Override
	public int evaluateTotalPoints() {
		if (solution == null) {
			throw new NoSolutionException();
		}
		List<Snake> snakes = solution.getSnakes();
		int result = 0;
		for (Snake snake : snakes) {
			result += snakeTypes.get(snake.getSnakeTypeId()).getSnakeValue();
			for (SnakePart snakePart : snake.getSnakeParts()) {
				result += jungle.getFieldValue(snakePart.getCoordinate()) ;
			}
		}
		return result;
	}

	@Override
	public Solution showSolution() {
		return solution;
	}

	@Override
	public Jungle showJungle() {
		return jungle;
	}

	@Override
	public void solveSnakeHuntInstance() {
		snakeSearchAlgorithmus.solveSnakeHuntInstance(jungle, snakeTypes, durationInSeconds);
	}
}
