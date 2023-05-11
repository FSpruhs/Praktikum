package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.Distance;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SolveUseCaseTest {

    @Test
    void solveSnakeHuntInstance() {

        List<JungleField> jungleFields = new ArrayList<>();
        jungleFields.add(0, new JungleField(new FieldId("F0"), new Coordinate(0, 0), 1, 1, 'F', new LinkedList<>()));
        jungleFields.add(1, new JungleField(new FieldId("F1"), new Coordinate(0, 1), 1, 1, 'E', new LinkedList<>()));
        jungleFields.add(2, new JungleField(new FieldId("F2"), new Coordinate(0, 2), 1, 1, 'R', new LinkedList<>()));
        jungleFields.add(3, new JungleField(new FieldId("F3"), new Coordinate(0, 3), 1, 1, 'N', new LinkedList<>()));
        jungleFields.add(4, new JungleField(new FieldId("F4"), new Coordinate(1, 0), 1, 1, 'X', new LinkedList<>()));
        jungleFields.add(5, new JungleField(new FieldId("F5"), new Coordinate(1, 1), 1, 1, 'I', new LinkedList<>()));
        jungleFields.add(6, new JungleField(new FieldId("F6"), new Coordinate(1, 2), 1, 1, 'N', new LinkedList<>()));
        jungleFields.add(7, new JungleField(new FieldId("F7"), new Coordinate(1, 3), 1, 1, 'U', new LinkedList<>()));
        String signString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Jungle jungle = new Jungle(new JungleSize(2, 4), signString, jungleFields);

        SolveUseCase solveUseCase = new SolveUseCase(
                jungle,
                Duration.ofSeconds(10),
                Map.of(new SnakeTypeId("A0"), new SnakeType(new SnakeTypeId("A0"), 9, 3, "FERNUNI", new Distance(1)))
        );

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
        Solution solution = new Solution(new LinkedList<>());
        solution.loadSnakes(snakes);

        assertEquals(solution, solveUseCase.solveSnakeHuntInstance());
    }

}