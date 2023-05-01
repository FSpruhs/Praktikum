package de.fernuni.kurs01584.ss23.domain.algorithm;

import de.fernuni.kurs01584.ss23.domain.model.*;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SecondAlgorithem implements SnakeSearchAlgorithmus {

    private static final Logger log = Logger.getLogger(SecondAlgorithem.class.getName());

    private Solution finalSolution = new Solution();
    private final Solution tempSolution = new Solution();
    private Jungle jungle;
    private long startTimer;
    private Duration durationInSeconds;
    private int totalPoints = 0;
    private Map<String, SnakeType> snakeTypes;
    private List<SnakeHead> snakeHeads;


    @Override
    public Solution solveSnakeHuntInstance(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds) {
        initializeSnakeSearch(jungle, snakeTypes, durationInSeconds);
        searchSnake();
        return finalSolution;
    }

    private int searchSnake() {
        int tempSolutionPoints = evaluateTotalPoints(tempSolution);
        if (totalPoints < tempSolutionPoints) {
            totalPoints = tempSolutionPoints;
            saveSolution(tempSolution);
        }
        if (System.nanoTime() - startTimer >= durationInSeconds.toNanos()) {
            log.info("Snake search finished. Time is over. Duration: %s ns".formatted(System.nanoTime() - startTimer));
            return -1;
        }
        for (JungleField startField : createStartFields()) {
            for (SnakeHead snakeHead : createStartHeads(startField)) {
                int solution = startSnakeSearch(snakeTypes, startField, snakeHead);
                if (solution < 0) {
                    return -1;
                }
            }
        }
        return 0;
    }

    private int startSnakeSearch(Map<String, SnakeType> snakeTypes, JungleField startField, SnakeHead snakeHead) {
        if (startField.getUsability() > 0) {
            Snake snake = new Snake(snakeHead.getId(), snakeHead.getNeighborhoodStructure(), new LinkedList<>());
            SnakePart snakePart = placeSnakePart(startField, snake);
            int solution = searchNextSnakePart(snake, getCharacterBandWithoutFirstChar(snakeTypes, snake));
            this.jungle.removeSnakePart(snakePart);
            snake.removeLastSnakePart();
            if (solution < 0) {
                return -1;
            }
        }
        return 0;
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

    private void saveSolution(Solution solution) {
        this.finalSolution = new Solution();
        for (Snake snake : solution.getSnakes()) {
            Snake newSnake = new Snake(snake.getSnakeTypeId(), new LinkedList<>(), snake.getNeighborhoodStructure());
            for (SnakePart snakePart : snake.getSnakeParts()) {
                newSnake.addSnakePart(new SnakePart(snakePart.fieldId(), snakePart.character(), snakePart.coordinate()));
            }
            finalSolution.addNewSnake(newSnake);
        }

    }

    private void initializeSnakeSearch(Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds) {
        this.startTimer = System.nanoTime();
        log.info("Start snake hunt search at %s".formatted(startTimer));
        this.jungle = jungle;
        this.durationInSeconds = durationInSeconds;
        this.snakeTypes = snakeTypes;
        this.snakeHeads = createSnakeHeads();
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

    private int searchNextSnakePart(Snake snake, String substring) {
        if (substring.equals("")) {
            tempSolution.insertSnake(snake);
            snakeHeads.remove(snakeHeads.stream().filter(snakeHead -> snakeHead.getId().equals(snake.getSnakeTypeId())).findFirst().orElseThrow());
            int solution = searchSnake();
            if (solution < 0) {
                return -1;
            }
            SnakeType snakeType = snakeTypes.get(snake.getSnakeTypeId());
            snakeHeads.add(new SnakeHead(snakeType.getSnakeValue(), snakeType.getId(), snakeType.getCharacterBand().charAt(0),snakeType.getNeighborhoodStructure()));
            tempSolution.removeSnake(snake);
            return 0;
        }
        List<JungleField> jungleFields = createJungleFields(snake, substring);
        if (jungleFields.isEmpty()) {
            return 0;
        }
        for (JungleField jungleField : jungleFields) {
            SnakePart snakePart = placeSnakePart(jungleField, snake);
            int solution = searchNextSnakePart(snake, substring.substring(1));
            snake.removeLastSnakePart();
            jungle.removeSnakePart(snakePart);
            if (solution < 0) {
                return -1;
            }
        }
        return 0;
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

    private List<SnakeHead> createSnakeHeads() {
        List<SnakeHead> snakeHeads = new LinkedList<>();
        for (SnakeType snakeType : snakeTypes.values()) {
            for (int i = 0; i < snakeType.getCount(); i++) {
                snakeHeads.add(new SnakeHead(snakeType.getSnakeValue(),
                        snakeType.getId(),
                        snakeType.getCharacterBand().charAt(0),
                        snakeType.getNeighborhoodStructure()
                ));
            }
        }
        return snakeHeads;
    }

    public int evaluateTotalPoints(Solution solution) {
        int result = 0;
        for (Snake snake : solution.getSnakes()) {
            result += snakeTypes.get(snake.getSnakeTypeId()).getSnakeValue() + sumSnakePartValues(snake);
        }
        return result;
    }

    private int sumSnakePartValues(Snake snake) {
        return snake.getSnakeParts().stream()
                .mapToInt(snakePart -> jungle.getFieldValue(snakePart.coordinate()))
                .sum();
    }
}
