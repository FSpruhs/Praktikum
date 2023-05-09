package de.fernuni.kurs01584.ss23.adapters.infrastructure;

import de.fernuni.kurs01584.ss23.adapters.users.XMLSnakeHuntReader;
import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.Distance;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SnakeHuntRepositoryAdapterTest {

    @Test
    void save() {
        File file = new File("res/test.xml");
        List<JungleField> jungleFields = new ArrayList<>();
        jungleFields.add(0, new JungleField(new FieldId("F0"), new Coordinate(0, 0), 1, 2, 'F', new LinkedList<>()));
        jungleFields.add(1, new JungleField(new FieldId("F1"), new Coordinate(0, 1), 1, 2, 'E', new LinkedList<>()));
        jungleFields.add(2, new JungleField(new FieldId("F2"), new Coordinate(0, 2), 1, 2, 'R', new LinkedList<>()));
        jungleFields.add(3, new JungleField(new FieldId("F3"), new Coordinate(0, 3), 1, 2, 'N', new LinkedList<>()));
        jungleFields.add(4, new JungleField(new FieldId("F4"), new Coordinate(1, 0), 1, 2, 'X', new LinkedList<>()));
        jungleFields.add(5, new JungleField(new FieldId("F5"), new Coordinate(1, 1), 1, 2, 'I', new LinkedList<>()));
        jungleFields.add(6, new JungleField(new FieldId("F6"), new Coordinate(1, 2), 1, 2, 'N', new LinkedList<>()));
        jungleFields.add(7, new JungleField(new FieldId("F7"), new Coordinate(1, 3), 1, 2, 'U', new LinkedList<>()));
        String signString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Jungle jungle = new Jungle(new JungleSize(2, 4), signString, jungleFields);
        Map<SnakeTypeId, SnakeType> snakeTypes = Map.of(new SnakeTypeId("A0"), new SnakeType(new SnakeTypeId("A0"), 1, 3, "FERNUNI", new Distance(1)));
        Duration targetDuration = Duration.ofSeconds(10);
        Duration actualDuration = Duration.ofSeconds(5);
        List<Snake> snakes = new LinkedList<>();
        snakes.add(new Snake(new SnakeTypeId("A0"), List.of(
                new SnakePart(new FieldId("F0"), 'F', new Coordinate(0, 0)),
                new SnakePart(new FieldId("F1"), 'E', new Coordinate(0, 1)),
                new SnakePart(new FieldId("F2"), 'R', new Coordinate(0, 2)),
                new SnakePart(new FieldId("F3"), 'N', new Coordinate(0, 3)),
                new SnakePart(new FieldId("F7"), 'U', new Coordinate(1, 3)),
                new SnakePart(new FieldId("F6"), 'N', new Coordinate(1, 2)),
                new SnakePart(new FieldId("F5"), 'I', new Coordinate(1, 1))
        ),
                new Distance(1)));
        Solution solution = new Solution();
        solution.loadSnakes(snakes);
        SnakeHuntRepositoryAdapter repository = new SnakeHuntRepositoryAdapter();
        repository.save(file, jungle, snakeTypes, targetDuration, actualDuration, solution);
        XMLSnakeHuntReader xmlSnakeHuntReader = new XMLSnakeHuntReader(file);
        assertEquals(jungle, xmlSnakeHuntReader.readJungle());
        assertEquals(solution, xmlSnakeHuntReader.readSolution());
        assertEquals(snakeTypes, xmlSnakeHuntReader.readSnakeTypes());
        assertEquals(targetDuration, xmlSnakeHuntReader.readDurationInSeconds());

    }
}