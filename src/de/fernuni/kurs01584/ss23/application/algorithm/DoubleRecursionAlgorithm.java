package de.fernuni.kurs01584.ss23.application.algorithm;

import de.fernuni.kurs01584.ss23.domain.model.*;

import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;


/**
 * Try to find the solution of the snake hunt with the highest score within the given time.
 * Two interleaved recursive searches are used for this purpose.
 * The first recursion tries to find a snake with the highest possible score.
 * When the snake is found, the second search starts and tries to place the found snakes in the jungle in such a way
 * that the solution reaches the highest possible score. The two searches always alternate.
 * When the time is up, the solution with the most points is returned.
 */
public class DoubleRecursionAlgorithm implements SnakeHuntAlgorithm {

    private static final Logger log = Logger.getLogger(DoubleRecursionAlgorithm.class.getName());
    private Solution finalSolution = new Solution(new LinkedList<>());
    private final Solution tempSolution = new Solution(new LinkedList<>());
    private final Jungle jungle;
    private long startTimer;
    private final Duration targetDuration;
    private int totalPoints = 0;
    private int tempPoints = 0;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;
    private List<SnakeHead> snakeHeads;
    private Map<Character, List<JungleField>> jungleFieldMap;

    /**
     * Constructor of the algorithm.
     *
     * @param jungle The jungle in which the snakes are sought.
     * @param snakeTypes The snake types that need to be searched.
     * @param targetDuration The time in which to search.
     */
    public DoubleRecursionAlgorithm(Jungle jungle, Map<SnakeTypeId, SnakeType> snakeTypes, Duration targetDuration) {
        this.jungle = jungle;
        this.targetDuration = targetDuration;
        this.snakeTypes = snakeTypes;
    }

    /**
     * Starts the snake search.
     *
     * @return The found solution with the highest value.
     */
    @Override
    public Solution solveSnakeHuntInstance() {
        initializeSnakeSearch();
        searchSnake();
        return finalSolution;
    }

    /**
     * Begins to search for a snake. First check if the temporary solution has more solution than the best solution.
     * If yes, then the temporary solution becomes the best solution.
     * Then check if the time has expired. If the time has expired, -1 is returned. This aborts the algorithm.
     * After that, the possible starting fields are determined and a snake copy is created for each snake that has to be searched.
     * Then the algorithm starts and for each start field each snake head is tried.
     * When all start fields with all snake heads have been traversed, the function returns -1. This terminates the algorithm.
     *
     * @return -1 back if the search is aborted, 0 otherwise.
     */
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

    /**
     * Checks if the value of the temporary solution is higher than the value of the actual solution.
     *
     * @return true it the temporary solution is higher, otherwise false.
     */
    private boolean tempSolutionHasMorePoints() {
        return totalPoints < tempPoints;
    }

    /**
     * Calculates whether the time has expired.
     *
     * @return True if the time has expired, otherwise false.
     */
    private boolean timeIsOver() {
        return System.nanoTime() - startTimer >= targetDuration.toNanos();
    }

    /**
     * Starts searching for a specific snake from a given starting field.
     * If the start field has no more usability, the search is skipped.
     * Converts the snake head into a snake and places the first part of the snake on the start field.
     * Then start searching the rest of the snake recursively. When returned from the recursive search.
     * The snake is removed from the jungle.
     *
     * @param startField The starting field from which the snake starts.
     * @param snakeHead The snake head for which the snake is to be searched.
     * @return -1 back if the search is aborted, 0 otherwise.
     */
    private int startSnakeSearch(JungleField startField, SnakeHead snakeHead) {
        if (startField.remainingUsability() > 0) {
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

    /**
     *
     * Creates a snake for a snake type id.
     *
     * @param snakeTypeId The type id for which the snake is to be created.
     * @return the created snake.
     */
    private Snake toSnake(SnakeTypeId snakeTypeId) {
        return new Snake(snakeTypeId, new LinkedList<>(), snakeTypes.get(snakeTypeId).neighborhoodStructure());
    }

    /**
     *
     * Creates a list of snake heads that start with a given character.
     * The list is sorted in descending order by the highest value of the snake head.
     *
     * @param character The start character with which the snake heads must begin.
     * @return the sorted list with snake heads.
     */
    private List<SnakeHead> createStartHeads(char character) {
        return snakeHeads.stream()
                .filter(snakeHead -> snakeHead.firstChar() == character)
                .sorted().
                toList();
    }

    /**
     *
     * Go through all the snake heads and make a list of starting field markers for each snake head.
     *
     * @return list of start fields.
     */
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

    /**
     *
     * Returns for a snake head all possible starting fields that still have availability.
     * Sorts the list in descending order of value.
     *
     * @param snakeHead The snake head for which the start fields are to be searched.
     * @return The sorted list of snake heads.
     */
    private List<JungleField> getUsableStartFields(SnakeHead snakeHead) {
        return jungleFieldMap.get(snakeHead.firstChar()).stream()
                .filter(jungleField -> jungleField.remainingUsability() > 0)
                .sorted()
                .toList();
    }

    /**
     * Saves the temporary solution.
     */
    private void saveSolution() {
        totalPoints = tempPoints;
        this.finalSolution = new Solution(tempSolution.snakes().stream()
                .map(this::createNewSnake).
                toList());
    }

    /**
     *
     * Creates a deep copy of a snake with all snake parts.
     *
     * @param snake the snake to be copied.
     * @return the copied snake.
     */
    private Snake createNewSnake(Snake snake) {
        Snake result = getDeepCopyOfSnake(snake);
        for (SnakePart snakePart : snake.snakeParts()) {
            result.addSnakePart(getDeepCopyOfSnakePart(snakePart));
        }
        return result;
    }

    /**
     *
     * Creates a deep copy of a snake.
     *
     * @param snake the snake to be copied.
     * @return the copied snake.
     */
    private Snake getDeepCopyOfSnake(Snake snake) {
        return new Snake(snake.snakeTypeId(), new LinkedList<>(), snake.neighborhoodStructure());
    }

    /**
     *
     * Creates a deep copy of a snake part.
     *
     * @param snakePart the snake part to be copied.
     * @return the copied snake part.
     */
    private SnakePart getDeepCopyOfSnakePart(SnakePart snakePart) {
        return new SnakePart(snakePart.fieldId(), snakePart.character(), snakePart.coordinate());
    }

    private void initializeSnakeSearch() {
        this.startTimer = System.nanoTime();
        log.info("Start snake hunt search at %s".formatted(startTimer));
        this.snakeHeads = createSnakeHeads();
        this.jungleFieldMap = createJungleFieldMap();
    }

    /**
     *
     * Creates a map of characters and jungle field to access the jungle fields faster.
     *
     * @return a map of characters and jungle field.
     */
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

    /**
     *
     * Creates a snake part and adds it to a jungle field and a snake.
     *
     * @param jungleField the field to add the snake part.
     * @param snake the snake to add the snake part.
     * @return the created snake part.
     */
    private SnakePart placeSnakePart(JungleField jungleField, Snake snake) {
        SnakePart snakePart = new SnakePart(
                jungleField.fieldId(),
                jungleField.character(),
                jungleField.coordinate());
        jungle.placeSnakePart(snakePart);
        snake.addSnakePart(snakePart);
        return snakePart;
    }

    /**
     *
     * Is the heart of the algorithm.
     * Searches recursively for snakes and manages the snakes heads and the temporary solution.
     *
     * @param snake The snake to be found.
     * @param remainingChars The remaining chars of the snake.
     * @return -1 back if the search is aborted, 0 otherwise.
     */
    private int searchNextSnakePart(Snake snake, String remainingChars) {
        /*
        When a snake is found, the snake is added to the solution and the next snake is searched for.
        When the search for the new snake is finished, the snake is removed from the solution.
         */
        if (remainingChars.equals("")) {
            int snakeValue = getSnakeValue(snake);
            addNewSnake(snake, snakeValue);
            if (searchSnake() < 0) {
                return -1;
            }
            removeSnake(snake, snakeValue);
            return 0;
        }
        // Creates a list with possible following jungle fields.
        List<JungleField> jungleFields = createJungleFields(snake, remainingChars.charAt(0));
        if (jungleFields.isEmpty()) {
            return 0;
        }
        /*
        If there are more fields to be found, the list of the following fields is scrolled through
         and for each field recursively searched for further snake fields.
         On the way back of the algorithm these snake parts are removed again.
         */
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

    /**
     *
     * Removes a snake from the jungle, from the solution and subtracts the value of the snake from the temporary point.
     * creates a snake head for the snake and adds it to the list of snake heads.
     *
     * @param snake the snake to be removed.
     * @param snakeValue the value of the snake.
     */
    private void removeSnake(Snake snake, int snakeValue) {
        SnakeType snakeType = snakeTypes.get(snake.snakeTypeId());
        snakeHeads.add(new SnakeHead(snakeType.snakeValue(), snakeType.snakeTypeId(), snakeType.characterBand().charAt(0)));
        tempSolution.removeSnake(snake);
        tempPoints -= snakeValue;
    }

    /**
     *
     * Adds a snake to the solution and adds the snake's value to the temporary points.
     * Removes a corresponding snake head from the list of snake heads.
     *
     * @param snake the snake to be added.
     * @param snakeValue the value of the snake.
     */
    private void addNewSnake(Snake snake, int snakeValue) {
        tempSolution.insertSnake(snake);
        snakeHeads.remove(snakeHeads.stream()
                .filter(snakeHead -> snakeHead.snakeTypeId().equals(snake.snakeTypeId()))
                .findFirst()
                .orElseThrow());
        tempPoints += snakeValue;
    }

    /**
     *
     * Calculates the value of a snake.
     *
     * @param snake the snake to calculate the value.
     * @return the value of the snake.
     */
    private int getSnakeValue(Snake snake) {
        int result = 0;
        for (SnakePart snakePart: snake.snakeParts()) {
            result += jungle.getFieldValue(snakePart.coordinate());
        }
        return result + snakeTypes.get(snake.snakeTypeId()).snakeValue();
    }

    /**
     *
     * Creates a list of all possible next jungle fields of a snake sorted descending by value.
     *
     * @param snake The snake for which the list is to be created.
     * @param nextChar the next char of the snake.
     * @return the list of next jungle fields for the snake.
     */
    private List<JungleField> createJungleFields(Snake snake, char nextChar) {
        return getNeighbors(snake).stream()
                .filter(coordinate -> jungle.getJungleField(coordinate).remainingUsability() > 0)
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

    /**
     * Creates a list of snake heads for each snake to search for in the jungle.
     *
     * @return the list of snake heads.
     */
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

    /**
     * Auxiliary class that represents an instance of a snake type that needs to be found.
     *
     * @param snakeValue The value of the snake.
     * @param snakeTypeId The id of the snake type.
     * @param firstChar The fist char of the snake type.
     */
    private record SnakeHead(int snakeValue,
                             SnakeTypeId snakeTypeId,
                             char firstChar) implements Comparable<SnakeHead> {

        @Override
        public int compareTo(SnakeHead s) {
            return Integer.compare(snakeValue, s.snakeValue());
        }
    }
}
