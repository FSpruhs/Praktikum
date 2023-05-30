package de.fernuni.kurs01584.ss23.application.junglegenerator;

import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

import java.time.Duration;
import java.util.*;

/**
 * Generates a new jungle by recursively placing the snake parts as jungle fields in the jungle.
 * The beginning of each snake is randomly determined.
 * Each additional snake part is then randomly selected from the possible next possible fields.
 * If not all snake parts can be placed because the space is used up,
 * all jungle patches are removed from the jungle and the generator starts again.
 * All jungle fields have the value 1 and the usability 1.
 */
public class SimpleJungleGenerator implements JungleGenerator{

    private final static long DEFAULT_RUNTIME = 30;

    private final Jungle jungle;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;
    private final Random random = new Random(42);
    private final List<JungleField> jungleFields = new ArrayList<>();

    /**
     * Constructor for the jungle generator.
     *
     * @param jungle Jungle to be regenerated. Already existing jungle fields are replaced.
     *               The dimensions of the jungle and the characters of the jungle are preserved.
     * @param snakeTypes The snake types that will be included in the newly generated jungle.
     */
    public SimpleJungleGenerator(Jungle jungle, Map<SnakeTypeId, SnakeType> snakeTypes) {
        this.jungle = jungle;
        this.snakeTypes = snakeTypes;
    }

    /**
     * Generates the new jungle. Aborts the generation when the default time is exceeded.
     */
    @Override
    public void generate() {
        boolean found = false;
        long start = System.nanoTime();
        fillJungleWithNull();
        while (!found && inTime(start)) {
            resetJungle();
            found = startSearchNextJungleField();
        }
        processResult(found);
    }

    private void processResult(boolean found) {
        if (found) {
            fillJungleWithDefaults();
            jungle.setJungleFields(jungleFields);
        } else {
            jungle.setJungleFields(null);
        }
    }

    private boolean inTime(long start) {
        return System.nanoTime() - start < Duration.ofSeconds(DEFAULT_RUNTIME).toNanos();
    }

    /**
     * Creates a new empty jungle with the number of fields the new jungle contains.
     */
    private void fillJungleWithNull() {
        for (int field = 0; field < jungleFieldCount(); field++) {
            jungleFields.add(null);
        }
    }

    /**
     * Resets all fields of a jungle.
     */
    private void resetJungle() {
        for (int field = 0; field < jungleFieldCount(); field++) {
            jungleFields.set(field, null);
        }
    }

    /**
     * Go through the jungle and create a random jungle field for every jungle field you haven't set yet.
     */
    private void fillJungleWithDefaults() {
        for (int fieldId = 0; fieldId < jungleFields.size(); fieldId++) {
            if (jungleFields.get(fieldId) == null) {
                jungleFields.set(fieldId, createDefaultField(fieldId));
            }
        }
    }

    /**
     * Creates jungle field with a random character, value 1 and usability 1.
     *
     * @param fieldId The field id of the new jungle field.
     * @return The created jungle field.
     */
    private JungleField createDefaultField(int fieldId) {
        return new JungleField(new FieldId("F" + fieldId),
                mapIndexToCoordinate(fieldId),
                1,
                1,
                jungle.getCharacters().charAt(random.nextInt(jungle.getCharacters().length())),
                new LinkedList<>());
    }

    /**
     *
     * For each snake type, start placing the corresponding jungle fields in the jungle.
     * Go through all the snake types. For each snake type, place one snake in the jungle.
     * For each snake type, specify how many of that type will be placed in the jungle.
     * First randomly determine a starting point for the snake. The remaining snake is then recursively placed in the jungle.
     * @return Returns true if all snakes could be placed, otherwise false.
     */
    private boolean startSearchNextJungleField() {
        for (SnakeType snakeType : snakeTypes.values()) {
            for (int i = 0; i < snakeType.count(); i++) {
                int startField = generateStartField(jungleFields);
                jungleFields.set(startField, createNextJungleField(snakeType.characterBand().charAt(0), startField));
                if (!searchNextJungleField(snakeType.neighborhoodStructure(), snakeType.characterBand().substring(1), startField)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Creates the next jungle field with corresponding character and jungle field id.
     *
     * @param character of the new jungle field.
     * @param fieldId of the new jungle field.
     * @return the created jungle field.
     */
    private JungleField createNextJungleField(char character, int fieldId) {
        return new JungleField(
                new FieldId("F" + fieldId),
                mapIndexToCoordinate(fieldId),
                1,
                1,
                character,
                new LinkedList<>()
        );
    }

    /**
     * Randomly generates the number of an empty start field.
     *
     * @param jungleFields List of jungle fields in which an empty one should be randomly selected.
     * @return The number of the randomly found field.
     */
    private int generateStartField(List<JungleField> jungleFields) {
        int result;
        do {
            result = random.nextInt(jungleFieldCount());
        } while (jungleFields.get(result) != null);
        return result;
    }

    /**
     * Calculates the number of jungle fields.
     * @return the number of jungle fields.
     */
    private int jungleFieldCount() {
        return getJungleRows() * getJungleColumns();
    }


    /**
     *
     * Searches recursively one snake part after the next.
     * Aborts when all snake parts of a snake have been found and returns true.
     * First creates a list with all possible sequence coordinates.
     * Then randomly selects a coordinate and calls the function again with this coordinate.
     * If the recursive call to the function continues until the snake has been found completely,
     * true is returned from each called function.
     * If the list of possible sequence coordinates is empty, it means that the snake was not found completely.
     * Then the current function returns false.
     * If the recursive call of a function returns false and the current function still has possible sequence coordinates in the list,
     * then the function is called again with a new random coordinate. This is done until all coordinates have been passed.
     *
     * @param neighborhoodStructure The neighborhood structure with which the next coordinates are selected.
     * @param substring The remaining sign chain from the not yet found snake parts.
     * @param startField The index of the current field from which the method is called.
     * @return Returns true if the snake was found completely, otherwise false.
     */
    private boolean searchNextJungleField(NeighborhoodStructure neighborhoodStructure,
                                          String substring,
                                          int startField) {
        if (substring.equals("")) {
            return true;
        }
        List<Coordinate> nextFields = createNextFields(jungleFields, neighborhoodStructure, startField);
        while (!nextFields.isEmpty()) {
            int nextFieldPosition = random.nextInt(nextFields.size());
            jungleFields.set(
                    mapToJunglePosition(nextFields, nextFieldPosition),
                    createNextJungleField(substring.charAt(0), mapToJunglePosition(nextFields, nextFieldPosition)
                    ));
            if (searchNextJungleField(neighborhoodStructure, substring.substring(1), mapToJunglePosition(nextFields, nextFieldPosition))) {
                return true;
            }
            jungleFields.set(mapToJunglePosition(nextFields, nextFieldPosition), null);
            nextFields.remove(nextFieldPosition);
        }
        return false;
    }

    /**
     *
     * Maps a coordinate of a field into the number of the field.
     *
     * @param nextFields List with coordinates.
     * @param nextFieldPosition Position of the coordinate in the list.
     * @return The field number.
     */
    private int mapToJunglePosition(List<Coordinate> nextFields, int nextFieldPosition) {
        return jungle.mapCoordinateToIndex(nextFields.get(nextFieldPosition));
    }

    /**
     * Creates a list with the coordinates of the possible next fields.
     *
     * @param jungleFields List with coordinates.
     * @param neighborhoodStructure The neighborhood structure according to which the following fields are found.
     * @param startField Position in the list of the jungle field for which the neighbors are to be searched.
     * @return A list of all the following coordinates.
     */
    private List<Coordinate> createNextFields(List<JungleField> jungleFields, NeighborhoodStructure neighborhoodStructure, int startField) {
        List<Coordinate> result = new LinkedList<>();
        for (Coordinate coordinate : neighborhoodStructure.nextFields(mapIndexToCoordinate(startField), jungle.getJungleSize())) {
            if (jungleFields.get(jungle.mapCoordinateToIndex(coordinate)) == null) {
                result.add(coordinate);
            }
        }
        return result;
    }

    /**
     *
     * Maps the index of a jungle field into the coordinate.
     *
     * @param index of the jungle field.
     * @return coordinate of the jungle field.
     */
    private Coordinate mapIndexToCoordinate(int index) {
        return new Coordinate(index / getJungleColumns(), index % getJungleColumns());
    }

    private int getJungleColumns() {
        return jungle.getJungleSize().columns();
    }

    private int getJungleRows() {
        return jungle.getJungleSize().rows();
    }
}
