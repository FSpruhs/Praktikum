package de.fernuni.kurs01584.ss23.application.junglegenerator;

import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

import java.util.*;

public class SimpleJungleGenerator implements JungleGenerator{

    private final Jungle jungle;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;
    private final Random random = new Random(42);
    private final List<JungleField> jungleFields = new ArrayList<>();

    public SimpleJungleGenerator(Jungle jungle, Map<SnakeTypeId, SnakeType> snakeTypes) {
        this.jungle = jungle;
        this.snakeTypes = snakeTypes;
    }


    @Override
    public void generate() {
        boolean found = false;
        fillJungleWithNull();
        while (!found) {
            resetJungle();
            found = startSearchNextJungleField();
        }
        fillJungleWithDefaults();
        jungle.setJungleFields(jungleFields);
    }

    private void fillJungleWithNull() {
        for (int field = 0; field < jungleFieldCount(); field++) {
            jungleFields.add(null);
        }
    }

    private void resetJungle() {
        for (int field = 0; field < jungleFieldCount(); field++) {
            jungleFields.set(field, null);
        }
    }

    private void fillJungleWithDefaults() {
        for (int fieldId = 0; fieldId < jungleFields.size(); fieldId++) {
            if (jungleFields.get(fieldId) == null) {
                jungleFields.set(fieldId, defaultField(fieldId));
            }
        }
    }

    private JungleField defaultField(int fieldId) {
        return new JungleField(new FieldId("F" + fieldId),
                mapIndexToCoordinate(fieldId),
                1,
                1,
                jungle.getCharacters().charAt(random.nextInt(jungle.getCharacters().length())));
    }

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

    private JungleField createNextJungleField(char character, int startField) {
        return new JungleField(new FieldId("F" + startField), mapIndexToCoordinate(startField), 1, 1, character);
    }

    private int generateStartField(List<JungleField> jungleFields) {
        int result;
        do {
            result = random.nextInt(jungleFieldCount());
        } while (jungleFields.get(result) != null);
        return result;
    }

    private int jungleFieldCount() {
        return getJungleRows() * getJungleColumns();
    }

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

    private int mapToJunglePosition(List<Coordinate> nextFields, int nextFieldPosition) {
        return jungle.mapCoordinateToIndex(nextFields.get(nextFieldPosition));
    }

    private List<Coordinate> createNextFields(List<JungleField> jungleFields, NeighborhoodStructure neighborhoodStructure, int startField) {
        List<Coordinate> result = new LinkedList<>();
        for (Coordinate coordinate : neighborhoodStructure.nextFields(mapIndexToCoordinate(startField), jungle.getJungleSize())) {
            if (jungleFields.get(jungle.mapCoordinateToIndex(coordinate)) == null) {
                result.add(coordinate);
            }
        }
        return result;
    }

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
