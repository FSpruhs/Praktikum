package de.fernuni.kurs01584.ss23.domain.model;

import java.io.File;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.fernuni.kurs01584.ss23.domain.algorithm.SecondAlgorithm;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidDurationException;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleException;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;
import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.domain.ports.in.*;
import de.fernuni.kurs01584.ss23.domain.ports.out.SaveSnakeHuntInstanceOutPort;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;

public class SnakeHuntInstance implements ValidationInPort,
		ShowJungleInPort,
		ShowSolutionInPort,
		ShowSnakeTypesInPort,
		EvaluateSolutionInPort,
		SolveInPort {
	
	private final Jungle jungle;
	private final Map<String, SnakeType> snakeTypes;
	private final Duration durationInSeconds;
	private Solution solution;
	private final SnakeSearchAlgorithmus snakeSearchAlgorithmus = new SecondAlgorithm();
	private final SaveSnakeHuntInstanceOutPort repository;
	
	
	public SnakeHuntInstance(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds, Solution solution, SaveSnakeHuntInstanceOutPort saveSnakeHuntInstanceOutPort) {
		this.jungle = jungle;
		this.snakeTypes = snakeTypes;
		this.durationInSeconds = durationInSeconds;
		this.solution = solution;
		this.repository = saveSnakeHuntInstanceOutPort;

		validateSnakeHuntInstance();
	}

	public SnakeHuntInstance(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds, SaveSnakeHuntInstanceOutPort saveSnakeHuntInstanceOutPort) {
		this.jungle = jungle;
		this.snakeTypes = snakeTypes;
		this.durationInSeconds = durationInSeconds;
		this.repository = saveSnakeHuntInstanceOutPort;
		validateSnakeHuntInstance();
	}
	
	private void validateSnakeHuntInstance() {
		validateJungle();
		validateSnakeTypes();
		validateDuration();
	}

	private void validateDuration() {
		if (durationInSeconds == null) {
			throw new InvalidDurationException("Duration in Seconds is Null!");
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
		for (SnakePart snakePart : snake.snakeParts()) {
			jungle.placeSnakePart(snakePart);
			findUsageError(result, snakePart);
			findAllocationError(result, snakePart);
			findNeighborhoodError(result, snakePart, previousSnakePart, snakeTypes.get(snake.snakeTypeId()));
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
		if (getSnakeType(snake.snakeTypeId()).getSnakeLength() != snake.getLength()) {
			result.add(Fehlertyp.GLIEDER);
		}
	}

	private void findNeighborhoodError(List<Fehlertyp> result, SnakePart snakePart, SnakePart previousSnakePart, SnakeType snakeType) {
		if (previousSnakePart != null && snakeType.isNotSuccessor(snakePart, previousSnakePart)) {
			result.add(Fehlertyp.NACHBARSCHAFT);
		}
	}

	private void findAllocationError(List<Fehlertyp> result, SnakePart snakePart) {
		if (jungle.getJungleFieldSign(snakePart.coordinate()) != snakePart.character()) {
			result.add(Fehlertyp.ZUORDNUNG);
		}
		
	}

	private void findUsageError(List<Fehlertyp> result, SnakePart snakePart) {
		if (jungle.getJungleFieldUsability(snakePart.coordinate()) < 0) {
			result.add(Fehlertyp.VERWENDUNG);
		}
	}

	@Override
	public int evaluateTotalPoints() {
		solutionNullCheck();
		int result = 0;
		for (Snake snake : solution.getSnakes()) {
			result += snakeTypes.get(snake.snakeTypeId()).getSnakeValue() + sumSnakePartValues(snake);
		}
		return result;
	}

	private int sumSnakePartValues(Snake snake) {
		return snake.snakeParts().stream()
				.mapToInt(snakePart -> jungle.getFieldValue(snakePart.coordinate()))
				.sum();
	}

	@Override
	public Solution showSolution() {
		return solution;
	}

	@Override
	public Jungle showJungle() {
		//jungle.clearJungle();
		return jungle;
	}

	@Override
	public void solveSnakeHuntInstance() {
		solution = snakeSearchAlgorithmus.solveSnakeHuntInstance(jungle, snakeTypes, durationInSeconds);
	}

	@Override
	public List<SnakeType> showSnakeTypes() {
		return snakeTypes.values().stream().toList();
	}

	@Override
	public SnakeType showSnakeTypesById(String snakeTypeId) {
		return snakeTypes.get(snakeTypeId);
	}


	public void save(File file) {
		repository.save(
				file,
				jungle,
				snakeTypes,
				durationInSeconds,
				solution
		);
	}
}
