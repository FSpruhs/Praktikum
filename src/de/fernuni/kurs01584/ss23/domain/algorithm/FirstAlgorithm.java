package de.fernuni.kurs01584.ss23.domain.algorithm;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
	private Solution solution;
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
		for (SnakeHead snakeHead : snakeHeads) {
			if (actualPoints > totalPoints) {
				saveSolution();
			}
			if (System.nanoTime() - startTimer <= durationInSeconds.getNano()) {
				return totalPoints;
			}
			List<JungleField> startFields = new LinkedList<>();
			List<Character> chars = new LinkedList<>();
			for (SnakeHead snakeHeads2 : snakeHeads) {
				if (!chars.contains(snakeHeads2.getFirstChar()) ) {
					List<JungleField> jungleFields = jungle.getUsabiliyFieldsByChar(snakeHeads2.getFirstChar());
					startFields.addAll(jungleFields);
				}
			}
			Collections.sort(startFields);
			for (JungleField startField : startFields) {
				List<SnakeHead> startHeads =  new LinkedList<>();
				for (SnakeHead snakeHead2 : snakeHeads) {
					if (snakeHead2.getFirstChar() == startField.getCharachter()) {
						startHeads.add(snakeHead2);
					}
				}
				Collections.sort(startHeads);
				for (SnakeHead snakeHead2 : startHeads) {
					Snake snake = new Snake(snakeHead2.getId(), snakeHead2.getNeighborhoodStructure());
					SnakePart snakePart = new SnakePart(startField.getId(),
							startField.getCharachter(),
							new Coordinate(Integer.parseInt(startField.getId().substring(1)) / jungle.getColumns(),
									Integer.parseInt(startField.getId().substring(1)) % jungle.getColumns()));
					jungle.placeSnakePart(snakePart, snakePart.getCoordinate());
					snake.addSnakePart(snakePart);
					searchNextSnakePart(snake, snakeTypes.get(snake.getSnakeTypeId()).getCharacterBand().substring(1));
				
				}
	
			}

		}
		
		return totalPoints;
	}


	private void searchNextSnakePart(Snake snake, String substring) {
		if (substring.equals("")) {
			return;
		}
		List<JungleField> jungleFields = new LinkedList<>();
		List<String> fieldStrings = List.of(snake.getNeighborhoodStructure().nextFields(snake.getSnakeParts().get(snake.getSnakeParts().size() - 1), jungle.getRows(), jungle.getColumns()));
		
	}


	private void saveSolution() {
		// TODO Auto-generated method stub
		
	}

}
