package de.fernuni.kurs01584.ss23.domain.model;

import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.application.algorithm.SnakeHuntAlgorithm;
import de.fernuni.kurs01584.ss23.application.junglegenerator.JungleGenerator;
import de.fernuni.kurs01584.ss23.application.junglegenerator.SimpleJungleGenerator;
import de.fernuni.kurs01584.ss23.application.ports.in.*;
import de.fernuni.kurs01584.ss23.application.algorithm.DoubleRecursionAlgorithm;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleException;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;
import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.application.ports.out.SaveSnakeHuntInstanceOutPort;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;

public class SnakeHuntInstance implements ValidationInPort,
		ShowSnakeHuntIntPort,
		EvaluateSolutionInPort,
		CreateSnakeHuntInPort,
		SolveInPort {
	private static final Logger log = Logger.getLogger(SnakeHuntInstance.class.getName());
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

	private SnakeType getSnakeType(SnakeTypeId snakeTypeId) {
		if (snakeTypes.get(snakeTypeId) == null) {
			throw new InvalidSnakeTypesException("Snake Type with value %s does not exist!".formatted(snakeTypeId));
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
		return evaluateTotalPoints(solution, snakeTypes, jungle);
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
	public boolean solveSnakeHuntInstance(File file) {
		if (jungle.getJungleFields().isEmpty()) {
			log.info("Can not solve jungle, because jungle is empty.");
			return false;
		}
		SnakeHuntAlgorithm snakeHuntAlgorithm = new DoubleRecursionAlgorithm(jungle, snakeTypes, targetDuration);
		long start = System.nanoTime();
		solution = snakeHuntAlgorithm.solveSnakeHuntInstance();
		actualDuration = Duration.ofNanos(System.nanoTime() - start);
		save(file);
		return !solution.getSnakes().isEmpty();
	}

	private void solveForTimeMeasurement() {
		if (jungle.getJungleFields().isEmpty()) {
			log.info("Can not solve jungle, because jungle is empty.");
		}
		SnakeHuntAlgorithm snakeHuntAlgorithm = new DoubleRecursionAlgorithm(jungle, snakeTypes, targetDuration);
		snakeHuntAlgorithm.solveSnakeHuntInstance();
	}

	@Override
	public List<SnakeType> showSnakeTypes() {
		return snakeTypes.values().stream().toList();
	}

	@Override
	public SnakeType showSnakeTypesById(SnakeTypeId snakeTypeId) {
		return snakeTypes.get(snakeTypeId);
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

	public int evaluateTotalPoints(Solution solution, Map<SnakeTypeId ,SnakeType> snakeTypes, Jungle jungle) {
		int result = 0;
		for (Snake snake : solution.getSnakes()) {
			result += snakeTypes.get(snake.snakeTypeId()).snakeValue() + sumSnakePartValues(snake, jungle);
		}
		return result;
	}

	private int sumSnakePartValues(Snake snake, Jungle jungle) {
		return snake.snakeParts().stream()
				.mapToInt(snakePart -> jungle.getFieldValue(snakePart.coordinate()))
				.sum();
	}

	@Override
	public boolean create(File file) {
		solution = null;
		jungle.removeJungleFields();
		JungleGenerator jungleGenerator = new SimpleJungleGenerator(jungle, snakeTypes);
		log.info("Start create jungle");
		jungleGenerator.generate();
		log.info("New jungle created");
		setDuration();
		save(file);
		return jungle.getJungleFields() != null;
	}

	private void setDuration() {
		long begin = System.nanoTime();
		solveForTimeMeasurement();
		this.targetDuration = Duration.ofNanos((System.nanoTime() - begin) * 2);
	}

}
