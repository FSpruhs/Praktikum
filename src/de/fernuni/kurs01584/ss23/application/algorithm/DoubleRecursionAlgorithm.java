package de.fernuni.kurs01584.ss23.application.algorithm;

import de.fernuni.kurs01584.ss23.domain.model.*;

import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

public class DoubleRecursionAlgorithm implements SnakeHuntAlgorithm {

    private static final Logger log = Logger.getLogger(DoubleRecursionAlgorithm.class.getName());
    private Solution finalSolution = new Solution();
    private final Solution tempSolution = new Solution();
    private final Jungle jungle;
    private long startTimer;
    private final Duration targetDuration;
    private int totalPoints = 0;
    private int tempPoints = 0;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;
    private List<SnakeHead> snakeHeads;
    private Map<Character, List<JungleField>> jungleFieldMap;

    public DoubleRecursionAlgorithm(Jungle jungle, Map<SnakeTypeId, SnakeType> snakeTypes, Duration targetDuration) {
        this.jungle = jungle;
        this.targetDuration = targetDuration;
        this.snakeTypes = snakeTypes;
    }


    @Override
    public Solution solveSnakeHuntInstance() {
        initializeSnakeSearch();
        searchSnake();
        return finalSolution;
    }

    private int searchSnake() {
        if (tempSolutionHasMorePoints()) {
            saveSolution();
        }
        if (timeIsOver()) {
            log.info("Snake search finished. Time is over. Duration: %s ns".formatted(System.nanoTime() - startTimer));
            return -1;
        }
        for (JungleField startField : createStartFields()) {
            for (SnakeHead snakeHead : createStartHeads(startField.character())) {
                if (startSnakeSearch(startField, snakeHead) < 0) {
                    return -1;
                }
            }
        }
        return 0;
    }

    private boolean tempSolutionHasMorePoints() {
        return totalPoints < tempPoints;
    }

    private boolean timeIsOver() {
        return System.nanoTime() - startTimer >= targetDuration.toNanos();
    }

    private int startSnakeSearch(JungleField startField, SnakeHead snakeHead) {
        if (startField.getUsability() > 0) {
            Snake snake = toSnake(snakeHead.snakeTypeId());
            SnakePart snakePart = placeSnakePart(startField, snake);
            int solution = searchNextSnakePart(snake, getCharacterBandWithoutFirstChar(snake));
            jungle.removeSnakePart(snakePart);
            snake.removeLastSnakePart();
            if (solution < 0) {
                return -1;
            }
        }
        return 0;
    }

    private Snake toSnake(SnakeTypeId snakeTypeId) {
        return new Snake(snakeTypeId, new LinkedList<>(), snakeTypes.get(snakeTypeId).neighborhoodStructure());
    }

    private List<SnakeHead> createStartHeads(char character) {
        return snakeHeads.stream()
                .filter(snakeHead -> snakeHead.firstChar() == character)
                .sorted().
                toList();
    }

    private List<JungleField> createStartFields() {
        List<JungleField> result = new LinkedList<>();
        List<Character> chars = new LinkedList<>();
        for (SnakeHead snakeHead : snakeHeads) {
            if (!chars.contains(snakeHead.firstChar()) ) {
                result.addAll(getUsableStartFields(snakeHead));
                chars.add(snakeHead.firstChar());
            }
        }
        return result;
    }

    private List<JungleField> getUsableStartFields(SnakeHead snakeHead) {
        return jungleFieldMap.get(snakeHead.firstChar()).stream()
                .filter(jungleField -> jungleField.getUsability() > 0)
                .sorted()
                .toList();
    }

    private void saveSolution() {
        totalPoints = tempPoints;
        this.finalSolution = new Solution(tempSolution.getSnakes().stream().map(this::createNewSnake).toList());
    }

    private Snake createNewSnake(Snake snake) {
        Snake result = getDeepCopyOfSnake(snake);
        for (SnakePart snakePart : snake.snakeParts()) {
            result.addSnakePart(getDeepCopyOfSnakePart(snakePart));
        }
        return result;
    }

    private Snake getDeepCopyOfSnake(Snake snake) {
        return new Snake(snake.snakeTypeId(), new LinkedList<>(), snake.neighborhoodStructure());
    }

    private SnakePart getDeepCopyOfSnakePart(SnakePart snakePart) {
        return new SnakePart(snakePart.fieldId(), snakePart.character(), snakePart.coordinate());
    }

    private void initializeSnakeSearch() {
        this.startTimer = System.nanoTime();
        log.info("Start snake hunt search at %s".formatted(startTimer));
        this.snakeHeads = createSnakeHeads();
        this.jungleFieldMap = createJungleFieldMap();
    }

    private Map<Character, List<JungleField>> createJungleFieldMap() {
        Map<Character, List<JungleField>> result = new HashMap<>();
        for (JungleField jungleField : jungle.getJungleFields()) {
            List<JungleField> jungleFields = result.computeIfAbsent(jungleField.character(), k -> new LinkedList<>());
            jungleFields.add(jungleField);
        }
        return result;
    }

    private String getCharacterBandWithoutFirstChar(Snake snake) {
        return snakeTypes.get(snake.snakeTypeId()).characterBand().substring(1);
    }

    private SnakePart placeSnakePart(JungleField jungleField, Snake snake) {
        SnakePart snakePart = new SnakePart(
                jungleField.fieldId(),
                jungleField.character(),
                jungleField.coordinate());
        jungle.placeSnakePart(snakePart);
        snake.addSnakePart(snakePart);
        return snakePart;
    }

    private int searchNextSnakePart(Snake snake, String remainingChars) {
        if (remainingChars.equals("")) {
            int snakeValue = getSnakeValue(snake);
            addNewSnake(snake, snakeValue);
            if (searchSnake() < 0) {
                return -1;
            }
            removeSnake(snake, snakeValue);
            return 0;
        }
        List<JungleField> jungleFields = createJungleFields(snake, remainingChars.charAt(0));
        if (jungleFields.isEmpty()) {
            return 0;
        }
        for (JungleField jungleField : jungleFields) {
            SnakePart snakePart = placeSnakePart(jungleField, snake);
            int solution = searchNextSnakePart(snake, remainingChars.substring(1));
            snake.removeLastSnakePart();
            jungle.removeSnakePart(snakePart);
            if (solution < 0) {
                return -1;
            }
        }
        return 0;
    }

    private void removeSnake(Snake snake, int snakeValue) {
        SnakeType snakeType = snakeTypes.get(snake.snakeTypeId());
        snakeHeads.add(new SnakeHead(snakeType.snakeValue(), snakeType.snakeTypeId(), snakeType.characterBand().charAt(0)));
        tempSolution.removeSnake(snake);
        tempPoints -= snakeValue;
    }

    private void addNewSnake(Snake snake, int snakeValue) {
        tempSolution.insertSnake(snake);
        snakeHeads.remove(snakeHeads.stream()
                .filter(snakeHead -> snakeHead.snakeTypeId().equals(snake.snakeTypeId()))
                .findFirst()
                .orElseThrow());
        tempPoints += snakeValue;
    }

    private int getSnakeValue(Snake snake) {
        int result = 0;
        for (SnakePart snakePart: snake.snakeParts()) {
            result += jungle.getFieldValue(snakePart.coordinate());
        }
        return result + snakeTypes.get(snake.snakeTypeId()).snakeValue();
    }

    private List<JungleField> createJungleFields(Snake snake, char nextChar) {
        return getNeighbors(snake).stream()
                .filter(coordinate -> jungle.getJungleField(coordinate).getUsability() > 0)
                .filter(coordinate -> jungle.getJungleField(coordinate).character() == nextChar)
                .map(jungle::getJungleField)
                .sorted()
                .toList();
    }

    private List<Coordinate> getNeighbors(Snake snake) {
        return snake.neighborhoodStructure().nextFields(getCoordinate(snake), jungle.getJungleSize());
    }

    private Coordinate getCoordinate(Snake snake) {
        return snake.snakeParts().get(snake.snakeParts().size() - 1).coordinate();
    }

    private List<SnakeHead> createSnakeHeads() {
        List<SnakeHead> result = new LinkedList<>();
        for (SnakeType snakeType : snakeTypes.values()) {
            for (int i = 0; i < snakeType.count(); i++) {
                result.add(new SnakeHead(
                        snakeType.snakeValue(),
                        snakeType.snakeTypeId(),
                        snakeType.characterBand().charAt(0)
                ));
            }
        }
        return result;
    }

    private record SnakeHead(int snakeValue,
                             SnakeTypeId snakeTypeId,
                             char firstChar) implements Comparable<SnakeHead> {

        @Override
        public int compareTo(SnakeHead s) {
            return Integer.compare(snakeValue, s.snakeValue());
        }
    }
}
