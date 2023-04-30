package de.fernuni.kurs01584.ss23.domain.algorithm;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.users.CLIAdapter;

public class FirstAlgorithm implements SnakeSearchAlgorithmus{

	private static final Logger log = Logger.getLogger(FirstAlgorithm.class.getName());

	private int totalPoints;
	private final List<SnakeHead> snakeHeads = new LinkedList<>();
	private final Solution tempSolution = new Solution();
	private Solution finalSolution;
	private Jungle jungle;


	@Override
	public Solution solveSnakeHuntInstance(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds) {
		long startTimer = System.nanoTime();
		log.info("Start snake hunt search at %s".formatted(startTimer));
		this.jungle = jungle;
		totalPoints = 0;
		int actualPoints = 0;
		createSnakeHeads(snakeTypes);
		while (!snakeHeads.isEmpty()) {
			saveSolutionWithMorePoints(actualPoints);
			if (System.nanoTime() - startTimer >= durationInSeconds.toNanos()) {
				log.info("Snake search finished. Time is over.");
				return tempSolution;
			}
			List<JungleField> startFields = new LinkedList<>();
			List<Character> chars = new LinkedList<>();
			for (SnakeHead snakeHeads : snakeHeads) {
				if (!chars.contains(snakeHeads.getFirstChar()) ) {
					List<JungleField> jungleFields = jungle.getUsabilityFieldsByChar(snakeHeads.getFirstChar());
					startFields.addAll(jungleFields);
					chars.add(snakeHeads.getFirstChar());
				}
			}
			Collections.sort(startFields);
			for (JungleField startField : startFields) {
				List<SnakeHead> startHeads = new LinkedList<>();
				for (SnakeHead snakeHead : snakeHeads) {
					if (snakeHead.getFirstChar() == startField.getCharacter()) {
						startHeads.add(snakeHead);
					}
				}
				Collections.sort(startHeads);
				for (SnakeHead snakeHead : startHeads) {
					if (startField.getUsability() > 0) {
						Snake snake = new Snake(snakeHead.getId(), snakeHead.getNeighborhoodStructure(), new LinkedList<>());
						SnakePart snakePart = new SnakePart(new FieldId(startField.getId()),
								startField.getCharacter(),
								new Coordinate(Integer.parseInt(startField.getId().substring(1)) / jungle.getJungleSize().columns(),
										Integer.parseInt(startField.getId().substring(1)) % jungle.getJungleSize().columns()));
						jungle.placeSnakePart(snakePart, snakePart.coordinate());
						snake.addSnakePart(snakePart);
						int totalPoints = searchNextSnakePart(snake, snakeTypes.get(snake.getSnakeTypeId()).getCharacterBand().substring(1));
						if (totalPoints < 0) {
							jungle.removeSnakePart(snakePart);
							snake.removeLastSnakePart();
						} else {
							actualPoints = actualPoints + totalPoints + snakeTypes.get(snake.getSnakeTypeId()).getSnakeValue();
							snakeHeads.remove(snakeHead);
						}

					}

				}
			}
			

		}
		log.info("Snake search finished. All Snakes are found.");
		return tempSolution;
	}

	private void saveSolutionWithMorePoints(int actualPoints) {
		if (actualPoints > totalPoints) {
			saveSolution();
			totalPoints = actualPoints;
		}
	}

	private void createSnakeHeads(Map<String, SnakeType> snakeTypes) {
		for (SnakeType snakeType : snakeTypes.values()) {
			for (int i = 0; i < snakeType.getCount(); i++) {
				snakeHeads.add(new SnakeHead(snakeType.getSnakeValue(),
						snakeType.getId(),
						snakeType.getCharacterBand().charAt(0),
						snakeType.getNeighborhoodStructure()
						));
			}

		}
	}


	private int searchNextSnakePart(Snake snake, String substring) {
		if (substring.equals("")) {
			tempSolution.insertSnake(snake);
			return 0;
		}
		List<JungleField> jungleFields = new LinkedList<>();
		List<Coordinate> fieldCoordinates = snake.getNeighborhoodStructure().nextFields(snake.getSnakeParts().get(snake.getSnakeParts().size() - 1).coordinate(), jungle.getJungleSize());
		for (Coordinate fieldCoordinate : fieldCoordinates) {
			if (jungle.getJungleField(fieldCoordinate).getUsability() > 0 && jungle.getJungleField(fieldCoordinate).getCharacter() == substring.charAt(0)) {
				jungleFields.add(jungle.getJungleField(fieldCoordinate));
			}
		}
		if (jungleFields.isEmpty()) {
			return -1;
		}
		Collections.sort(jungleFields);
		for (JungleField jungleField : jungleFields) {
			SnakePart snakePart = new SnakePart(new FieldId(jungleField.getId()),
					jungleField.getCharacter(),
					new Coordinate(Integer.parseInt(jungleField.getId().substring(1)) / jungle.getJungleSize().columns(),
							Integer.parseInt(jungleField.getId().substring(1)) % jungle.getJungleSize().columns()));
			jungle.placeSnakePart(snakePart, snakePart.coordinate());
			snake.addSnakePart(snakePart);
			int totalPoints = searchNextSnakePart(snake, substring.substring(1));
			if (totalPoints < 0) {
				jungle.removeSnakePart(snakePart);
				snake.removeLastSnakePart();
			} else {
				return totalPoints + jungle.getFieldValue(snakePart.coordinate());
			}
		}
		return -1;
	}


	private void saveSolution() {
		finalSolution = new Solution();
		finalSolution.setTotalPoints(totalPoints);
		for (Snake snake : tempSolution.getSnakes()) {
			finalSolution.insertSnake(snake);
		}
	}

}
