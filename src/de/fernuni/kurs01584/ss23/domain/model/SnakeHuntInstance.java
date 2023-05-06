package de.fernuni.kurs01584.ss23.domain.model;

import java.io.File;
import java.time.Duration;
import java.util.*;

import de.fernuni.kurs01584.ss23.application.ports.in.*;
import de.fernuni.kurs01584.ss23.application.algorithm.DoubleRecursionAlgorithm;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidDurationException;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleException;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;
import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.application.ports.out.SaveSnakeHuntInstanceOutPort;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;

public class SnakeHuntInstance implements ValidationInPort,
		ShowSnakeHuntIntPort,
		EvaluateSolutionInPort,
		CreateSnakeHuntInPort,
		SolveInPort {
	
	private final Jungle jungle;
	private final Map<SnakeTypeId, SnakeType> snakeTypes;
	private final Duration durationInSeconds;
	private Solution solution;
	private final SnakeSearchAlgorithmus snakeSearchAlgorithmus = new DoubleRecursionAlgorithm();
	private final SaveSnakeHuntInstanceOutPort repository;
	
	
	public SnakeHuntInstance(Jungle jungle, Map<SnakeTypeId, SnakeType> snakeTypes, Duration durationInSeconds, Solution solution, SaveSnakeHuntInstanceOutPort saveSnakeHuntInstanceOutPort) {
		this.jungle = jungle;
		this.snakeTypes = snakeTypes;
		this.durationInSeconds = durationInSeconds;
		this.solution = solution;
		this.repository = saveSnakeHuntInstanceOutPort;

		validateSnakeHuntInstance();
	}

	public SnakeHuntInstance(Jungle jungle, Map<SnakeTypeId, SnakeType> snakeTypes, Duration durationInSeconds, SaveSnakeHuntInstanceOutPort saveSnakeHuntInstanceOutPort) {
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
			throw new InvalidSnakeTypesException("Snake Type with value %s does not exist!".formatted(snakeTypeId));
		}
		return snakeTypes.get(snakeTypeId);
	}

	private void findLengthError(List<Fehlertyp> result, Snake snake) {
		if (getSnakeType(snake.snakeTypeId().value()).getSnakeLength() != snake.getLength()) {
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
		solution = snakeSearchAlgorithmus.solveSnakeHuntInstance(jungle, snakeTypes, durationInSeconds);
		return !solution.getSnakes().isEmpty();
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
				durationInSeconds,
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
	public void create() {
		solution = null;
		List<JungleField> jungleFields = new ArrayList<>();
		for (int i = 0; i < jungle.getJungleSize().rows() * jungle.getJungleSize().columns(); i++) {
			jungleFields.add(null);
		}
		Random random = new Random();
		boolean found = false;
		while (!found) {
			found = startSearchNextJungleField(jungleFields, random);
		}
		for (int i = 0; i < jungleFields.size(); i++) {
			if (jungleFields.get(i) == null) {
				JungleField jungleField = new JungleField(new FieldId("F" + i), mapIndexToCoordinate(i),1, 1, jungle.getCharacters().charAt(random.nextInt(jungle.getCharacters().length())));
				jungleFields.set(i, jungleField);
			}
		}
		jungle.setJungleFields(jungleFields);

	}

	private boolean startSearchNextJungleField(List<JungleField> jungleFields, Random random) {
		for (SnakeType snakeType : snakeTypes.values()) {
			for (int i = 0; i < snakeType.count(); i++) {
				int startField = random.nextInt(jungle.getJungleSize().rows() * jungle.getJungleSize().columns() + 1);
				jungleFields.add(startField, new JungleField(new FieldId("F" + startField), mapIndexToCoordinate(startField), 1, 1, snakeType.characterBand().charAt(0)));
				boolean found = searchNextJungleField(jungleFields, snakeType.neighborhoodStructure(), snakeType.characterBand().substring(1), startField, random);
				if (!found) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean searchNextJungleField(List<JungleField> jungleFields, NeighborhoodStructure neighborhoodStructure, String substring, int startField, Random random) {
		if (substring.equals("")) {
			return true;
		}
		List<Coordinate> nextFields = neighborhoodStructure.nextFields(mapIndexToCoordinate(startField), jungle.getJungleSize());
		while (!nextFields.isEmpty()) {
			int nextFieldPosition = random.nextInt(nextFields.size());
			JungleField nextJungleField = new JungleField(new FieldId("F" + jungle.mapCoordinateToIndex(nextFields.get(nextFieldPosition))), nextFields.get(nextFieldPosition), 1, 1, substring.charAt(0));
			jungleFields.add(jungle.mapCoordinateToIndex(nextFields.get(nextFieldPosition)), nextJungleField);
			boolean found = searchNextJungleField(jungleFields, neighborhoodStructure, substring.substring(1), nextFieldPosition, random);
			if (found) {
				return true;
			}
			nextFields.remove(jungle.mapCoordinateToIndex(nextFields.get(nextFieldPosition)));
			jungleFields.set(jungle.mapCoordinateToIndex(nextFields.get(nextFieldPosition)), null);
		}
		return false;
	}

	private Coordinate mapIndexToCoordinate(int index) {
		return new Coordinate(jungle.getJungleSize().rows() * jungle.getJungleSize().columns() / index, jungle.getJungleSize().rows() * jungle.getJungleSize().columns() % index);
	}

}
