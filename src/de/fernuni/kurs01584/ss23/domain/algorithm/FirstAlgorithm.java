package de.fernuni.kurs01584.ss23.domain.algorithm;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.domain.model.*;

public class FirstAlgorithm implements SnakeSearchAlgorithmus{

	private static final Logger log = Logger.getLogger(FirstAlgorithm.class.getName());

	private final List<SnakeHead> snakeHeads = new LinkedList<>();
	private final Solution solution = new Solution();
	private Jungle jungle;
	private long startTimer;


	@Override
	public Solution solveSnakeHuntInstance(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds) {
		initializeSnakeSearch(jungle, snakeTypes);
		for (int i = 0; i <= snakeHeads.size(); i++) {
			if (System.nanoTime() - startTimer >= durationInSeconds.toNanos()) {
				log.info("Snake search finished. Time is over. Duration: %s ns".formatted(System.nanoTime() - startTimer));
				return solution;
			}
			for (JungleField startField : createStartFields()) {
				for (SnakeHead snakeHead : createStartHeads(startField)) {
					startSnakeSearch(snakeTypes, startField, snakeHead);
				}
			}
		}
		log.info("Snake search finished.");
		return solution;
	}

	private void initializeSnakeSearch(Jungle jungle, Map<String, SnakeType> snakeTypes) {
		this.startTimer = System.nanoTime();
		log.info("Start snake hunt search at %s".formatted(startTimer));
		this.jungle = jungle;
		createSnakeHeads(snakeTypes);
	}

	private void startSnakeSearch(Map<String, SnakeType> snakeTypes, JungleField startField, SnakeHead snakeHead) {
		if (startField.getUsability() > 0) {
			Snake snake = new Snake(snakeHead.getId(), snakeHead.getNeighborhoodStructure(), new LinkedList<>());
			SnakePart snakePart = placeSnakePart(startField, snake);
			if (searchNextSnakePart(snake, getCharacterBandWithoutFirstChar(snakeTypes, snake)) < 0) {
				this.jungle.removeSnakePart(snakePart);
				snake.removeLastSnakePart();
			} else {
				snakeHeads.remove(snakeHead);
			}
		}
	}

	private String getCharacterBandWithoutFirstChar(Map<String, SnakeType> snakeTypes, Snake snake) {
		return snakeTypes.get(snake.getSnakeTypeId()).getCharacterBand().substring(1);
	}

	private SnakePart placeSnakePart(JungleField startField, Snake snake) {
		SnakePart snakePart = new SnakePart(new FieldId(startField.getId()),
				startField.getCharacter(),
				new Coordinate(Integer.parseInt(startField.getId().substring(1)) / jungle.getJungleSize().columns(),
						Integer.parseInt(startField.getId().substring(1)) % jungle.getJungleSize().columns()));
		jungle.placeSnakePart(snakePart, snakePart.coordinate());
		snake.addSnakePart(snakePart);
		return snakePart;
	}

	private List<SnakeHead> createStartHeads(JungleField startField) {
		List<SnakeHead> startHeads = new LinkedList<>();
		for (SnakeHead snakeHead : snakeHeads) {
			if (snakeHead.getFirstChar() == startField.getCharacter()) {
				startHeads.add(snakeHead);
			}
		}
		Collections.sort(startHeads);
		return startHeads;
	}

	private List<JungleField> createStartFields() {
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
		return startFields;
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
			solution.insertSnake(snake);
			return 0;
		}
		List<JungleField> jungleFields = createJungleFields(snake, substring);
		for (JungleField jungleField : jungleFields) {
			SnakePart snakePart = placeSnakePart(jungleField, snake);
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

	private List<JungleField> createJungleFields(Snake snake, String substring) {
		List<JungleField> jungleFields = new LinkedList<>();
		List<Coordinate> fieldCoordinates = snake.getNeighborhoodStructure().nextFields(getCoordinate(snake), jungle.getJungleSize());
		for (Coordinate fieldCoordinate : fieldCoordinates) {
			if (jungle.getJungleField(fieldCoordinate).getUsability() > 0 && jungle.getJungleField(fieldCoordinate).getCharacter() == substring.charAt(0)) {
				jungleFields.add(jungle.getJungleField(fieldCoordinate));
			}
		}
		Collections.sort(jungleFields);
		return jungleFields;
	}

	private Coordinate getCoordinate(Snake snake) {
		return snake.getSnakeParts().get(snake.getSnakeParts().size() - 1).coordinate();
	}


}
