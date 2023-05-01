package de.fernuni.kurs01584.ss23.domain.algorithm;

import de.fernuni.kurs01584.ss23.domain.model.*;

import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

public class SecondAlgorithm implements SnakeSearchAlgorithmus {

    private static final Logger log = Logger.getLogger(SecondAlgorithm.class.getName());
    private Solution finalSolution = new Solution();
    private final Solution tempSolution = new Solution();
    private Jungle jungle;
    private long startTimer;
    private Duration durationInSeconds;
    private int totalPoints = 0;
    private Map<String, SnakeType> snakeTypes;
    private List<SnakeHead> snakeHeads;
    private Map<Character, List<JungleField>> jungleFieldMap;
    private SolutionValueCalculator solutionValueCalculator;


    @Override
    public Solution solveSnakeHuntInstance(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds, SolutionValueCalculator solutionValueCalculator) {
        initializeSnakeSearch(jungle, snakeTypes, durationInSeconds, solutionValueCalculator);
        searchSnake();
        return finalSolution;
    }

    private int searchSnake() {
        int tempSolutionPoints = solutionValueCalculator.evaluateTotalPoints(tempSolution, snakeTypes, jungle);
        if (totalPoints < tempSolutionPoints) {
            totalPoints = tempSolutionPoints;
            saveSolution(tempSolution);
        }
        if (System.nanoTime() - startTimer >= durationInSeconds.toNanos()) {
            log.info("Snake search finished. Time is over. Duration: %s ns".formatted(System.nanoTime() - startTimer));
            return -1;
        }
        for (JungleField startField : createStartFields()) {
            for (SnakeHead snakeHead : createStartHeads(startField.getCharacter())) {
                if (startSnakeSearch(startField, snakeHead) < 0) {
                    return -1;
                }
            }
        }
        return 0;
    }

    private int startSnakeSearch(JungleField startField, SnakeHead snakeHead) {
        if (startField.getUsability() > 0) {
            Snake snake = toSnake(snakeHead.getId());
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

    private Snake toSnake(String snakeTypeId) {
        return new Snake(snakeTypeId, new LinkedList<>(), snakeTypes.get(snakeTypeId).getNeighborhoodStructure());
    }

    private List<SnakeHead> createStartHeads(char character) {
        return snakeHeads.stream()
                .filter(snakeHead -> snakeHead.getFirstChar() == character)
                .sorted().
                toList();
    }

    private List<JungleField> createStartFields() {
        List<JungleField> result = new LinkedList<>();
        List<Character> chars = new LinkedList<>();
        for (SnakeHead snakeHead : snakeHeads) {
            if (!chars.contains(snakeHead.getFirstChar()) ) {
                result.addAll(getUsableStartFields(snakeHead));
                chars.add(snakeHead.getFirstChar());
            }
        }
        return result;
    }

    private List<JungleField> getUsableStartFields(SnakeHead snakeHead) {
        return jungleFieldMap.get(snakeHead.getFirstChar()).stream()
                .filter(jungleField -> jungleField.getUsability() > 0)
                .sorted()
                .toList();
    }

    private void saveSolution(Solution solution) {
        this.finalSolution = new Solution(solution.getSnakes().stream().map(this::createNewSnake).toList());
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

    private void initializeSnakeSearch(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds, SolutionValueCalculator solutionValueCalculator) {
        this.startTimer = System.nanoTime();
        log.info("Start snake hunt search at %s".formatted(startTimer));
        this.jungle = jungle;
        this.durationInSeconds = durationInSeconds;
        this.snakeTypes = snakeTypes;
        this.snakeHeads = createSnakeHeads();
        this.jungleFieldMap = createJungleFieldMap();
        this.solutionValueCalculator = solutionValueCalculator;
    }

    private Map<Character, List<JungleField>> createJungleFieldMap() {
        Map<Character, List<JungleField>> result = new HashMap<>();
        for (JungleField jungleField : jungle.getJungleFields()) {
            List<JungleField> jungleFields = result.computeIfAbsent(jungleField.getCharacter(), k -> new LinkedList<>());
            jungleFields.add(jungleField);
        }
        return result;
    }

    private String getCharacterBandWithoutFirstChar(Snake snake) {
        return snakeTypes.get(snake.snakeTypeId()).getCharacterBand().substring(1);
    }

    private SnakePart placeSnakePart(JungleField jungleField, Snake snake) {
        SnakePart snakePart = new SnakePart(
                jungleField.getId(),
                jungleField.getCharacter(),
                jungleField.getCoordinate());
        jungle.placeSnakePart(snakePart);
        snake.addSnakePart(snakePart);
        return snakePart;
    }

    private int searchNextSnakePart(Snake snake, String remainingChars) {
        if (remainingChars.equals("")) {
            tempSolution.insertSnake(snake);
            snakeHeads.remove(snakeHeads.stream()
                    .filter(snakeHead -> snakeHead.getId().equals(snake.snakeTypeId()))
                    .findFirst()
                    .orElseThrow());
            if (searchSnake() < 0) {
                return -1;
            }
            SnakeType snakeType = snakeTypes.get(snake.snakeTypeId());
            snakeHeads.add(new SnakeHead(snakeType.getSnakeValue(), snakeType.getId(), snakeType.getCharacterBand().charAt(0), snakeType.getNeighborhoodStructure()));
            tempSolution.removeSnake(snake);
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

    private List<JungleField> createJungleFields(Snake snake, char nextChar) {
        return getNeighbors(snake).stream()
                .filter(coordinate -> jungle.getJungleField(coordinate).getUsability() > 0)
                .filter(coordinate -> jungle.getJungleField(coordinate).getCharacter() == nextChar)
                .map(coordinate -> jungle.getJungleField(coordinate))
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
            for (int i = 0; i < snakeType.getCount(); i++) {
                result.add(new SnakeHead(
                        snakeType.getSnakeValue(),
                        snakeType.getId(),
                        snakeType.getCharacterBand().charAt(0),
                        snakeType.getNeighborhoodStructure()
                ));
            }
        }
        return result;
    }
}
