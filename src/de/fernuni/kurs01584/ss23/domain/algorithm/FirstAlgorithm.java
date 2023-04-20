package de.fernuni.kurs01584.ss23.domain.algorithm;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.fernuni.kurs01584.ss23.domain.model.Coordinate;
import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.JungleField;
import de.fernuni.kurs01584.ss23.domain.model.Snake;
import de.fernuni.kurs01584.ss23.domain.model.SnakeHead;
import de.fernuni.kurs01584.ss23.domain.model.SnakePart;
import de.fernuni.kurs01584.ss23.domain.model.SnakeSearchAlgorithmus;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.Solution;

public class FirstAlgorithm implements SnakeSearchAlgorithmus{
	
	private int totalPoints;
	private List<SnakeHead> snakeHeads;
	private Solution tempSolution;
	private Solution finalSolution;
	private Jungle jungle;


	@Override
	public int solveSnakeHuntInstance(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds) {
		long startTimer = System.nanoTime();
		this.jungle = jungle;
		totalPoints = 0;
		int actualPoints = 0;
		for (SnakeType snakeType : snakeTypes.values()) {
			for (int i = 0; i < snakeType.getCount() ; i++) {
				snakeHeads.add(new SnakeHead(snakeType.getSnakeValue(),
						snakeType.getId(),
						snakeType.getCharacterBand().charAt(0),
						snakeType.getNeighborhoodStructure()
						));
			}
			
		}
		while (!snakeHeads.isEmpty()) {
			if (actualPoints > totalPoints) {
				saveSolution();
				totalPoints = actualPoints;
			}
			if (System.nanoTime() - startTimer <= durationInSeconds.getNano()) {
				return totalPoints;
			}
			List<JungleField> startFields = new LinkedList<>();
			List<Character> chars = new LinkedList<>();
			for (SnakeHead snakeHeads : snakeHeads) {
				if (!chars.contains(snakeHeads.getFirstChar()) ) {
					List<JungleField> jungleFields = jungle.getUsabilityFieldsByChar(snakeHeads.getFirstChar());
					startFields.addAll(jungleFields);
				}
			}
			Collections.sort(startFields);
			for (JungleField startField : startFields) {
				List<SnakeHead> startHeads =  new LinkedList<>();
				for (SnakeHead snakeHead2 : snakeHeads) {
					if (snakeHead2.getFirstChar() == startField.getCharacter()) {
						startHeads.add(snakeHead2);
					}
				}
				Collections.sort(startHeads);
				for (SnakeHead snakeHead : startHeads) {
					Snake snake = new Snake(snakeHead.getId(), snakeHead.getNeighborhoodStructure());
					SnakePart snakePart = new SnakePart(startField.getId(),
							startField.getCharacter(),
							new Coordinate(Integer.parseInt(startField.getId().substring(1)) / jungle.getColumns(),
									Integer.parseInt(startField.getId().substring(1)) % jungle.getColumns()));
					jungle.placeSnakePart(snakePart, snakePart.getCoordinate());
					snake.addSnakePart(snakePart);
					int totalPoints = searchNextSnakePart(snake, snakeTypes.get(snake.getSnakeTypeId()).getCharacterBand().substring(1));
					if (totalPoints < 0) {
						jungle.removeSnakePart(snakePart);
						snake.removeLastSnakePart();
					} else {
						actualPoints = actualPoints + totalPoints + snakeTypes.get(snake.getSnakeTypeId()).getSnakeValue();
					}
					snakeHeads.remove(snakeHead);
				}
	
			}
			

		}
		
		return totalPoints;
	}


	private int searchNextSnakePart(Snake snake, String substring) {
		if (substring.equals("")) {
			tempSolution.insertSnake(snake);
			return 0;
		}
		List<JungleField> jungleFields = new LinkedList<>();
		List<Coordinate> fieldCoordinates = snake.getNeighborhoodStructure().nextFields(snake.getSnakeParts().get(snake.getSnakeParts().size() - 1).getCoordinate(), jungle.getRows(), jungle.getColumns());
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
			SnakePart snakePart = new SnakePart(jungleField.getId(),
					jungleField.getCharacter(),
					new Coordinate(Integer.parseInt(jungleField.getId().substring(1)) / jungle.getColumns(),
							Integer.parseInt(jungleField.getId().substring(1)) % jungle.getColumns()));
			jungle.placeSnakePart(snakePart, snakePart.getCoordinate());
			snake.addSnakePart(snakePart);
			int totalPoints = searchNextSnakePart(snake, substring.substring(1));
			if (totalPoints < 0) {
				jungle.removeSnakePart(snakePart);
				snake.removeLastSnakePart();
			} else {
				return totalPoints + jungle.getFieldValue(snakePart.getCoordinate());
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
