package de.fernuni.kurs01584.ss23.domain.model;




import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.fernuni.kurs01584.ss23.adapters.infrastructure.SnakeHuntRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.Distance;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;

import static org.junit.jupiter.api.Assertions.*;

public class SnakeHuntInstanceTest {


	@Test
	@DisplayName("Test Exception if Solution is null")
	void solutionIsNull( ) {
		List<JungleField> jungleFields = new ArrayList<>();
		jungleFields.add(new JungleField(new FieldId("F0"), new Coordinate(0, 0), 1, 1, 'F', new LinkedList<>())) ;
		String signString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Jungle jungle = new Jungle(new JungleSize(2, 4), signString, jungleFields);
		SnakeHuntInstance snakeHuntInstance = new SnakeHuntInstance(
				jungle,
				Map.of(new SnakeTypeId("A0"), new SnakeType(new SnakeTypeId("A0"), 1, 3, "FERNUNI", new Distance(1))),
				Duration.ofSeconds(1),
				new SnakeHuntRepositoryAdapter()
				);
		assertThrows(NoSolutionException.class, snakeHuntInstance::isValid);
	}

	@Test
	@DisplayName("Test if multiple \"Glieder\" error is found!")
	void findLengthError() {
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

		List<Snake> snakes = new LinkedList<>();
		snakes.add(new Snake(new SnakeTypeId("A0"), List.of(
				new SnakePart(new FieldId("F0"), 'F', new Coordinate(0, 0)),
				new SnakePart(new FieldId("F1"), 'E', new Coordinate(0, 1)),
				new SnakePart(new FieldId("F2"), 'R', new Coordinate(0, 2)),
				new SnakePart(new FieldId("F3"), 'N', new Coordinate(0, 3)),
				new SnakePart(new FieldId("F7"), 'U', new Coordinate(1, 3)),
				new SnakePart(new FieldId("F6"), 'N', new Coordinate(1, 2))
				),
                new Distance(1)));
		snakes.add(new Snake(new SnakeTypeId("A0"), List.of(
				new SnakePart(new FieldId("F0"), 'F', new Coordinate(0, 0)),
				new SnakePart(new FieldId("F1"), 'E', new Coordinate(0, 1)),
				new SnakePart(new FieldId("F2"), 'R', new Coordinate(0, 2)),
				new SnakePart(new FieldId("F3"), 'N', new Coordinate(0, 3)),
				new SnakePart(new FieldId("F7"), 'U', new Coordinate(1, 3)),
				new SnakePart(new FieldId("F6"), 'N', new Coordinate(1, 2))
				),
                new Distance(1)));
		Solution solution = new Solution();
		solution.loadSnakes(snakes);

		SnakeHuntInstance snakeHuntInstance = new SnakeHuntInstance(
				jungle,
				Map.of(new SnakeTypeId("A0"), new SnakeType(new SnakeTypeId("A0"), 1, 3, "FERNUNI", new NeighborhoodstructureFalseMock())),
				Duration.ofSeconds(1),
				solution,
				new SnakeHuntRepositoryAdapter());
		assertEquals(List.of(Fehlertyp.GLIEDER, Fehlertyp.GLIEDER), snakeHuntInstance.isValid());
	}

	@Test
	@DisplayName("Test if multiple \"ZUORDNUNG\" error is found!")
	void findWrongCharacterError() {
		List<JungleField> jungleFields = new ArrayList<>();
		jungleFields.add(0, new JungleField(new FieldId("F0"), new Coordinate(0, 0), 1, 2, 'F', new LinkedList<>()));
		jungleFields.add(1, new JungleField(new FieldId("F1"), new Coordinate(0, 1), 1, 2, 'E', new LinkedList<>()));
		jungleFields.add(2, new JungleField(new FieldId("F2"), new Coordinate(0, 2), 1, 2, 'R', new LinkedList<>()));
		jungleFields.add(3, new JungleField(new FieldId("F3"), new Coordinate(0, 3), 1, 2, 'N', new LinkedList<>()));
		jungleFields.add(4, new JungleField(new FieldId("F4"), new Coordinate(1, 0), 1, 2, 'X', new LinkedList<>()));
		jungleFields.add(5, new JungleField(new FieldId("F5"), new Coordinate(1, 1), 1, 2, 'X', new LinkedList<>()));
		jungleFields.add(6, new JungleField(new FieldId("F6"), new Coordinate(1, 2), 1, 2, 'N', new LinkedList<>()));
		jungleFields.add(7, new JungleField(new FieldId("F7"), new Coordinate(1, 3), 1, 2, 'U', new LinkedList<>()));
		String signString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Jungle jungle = new Jungle(new JungleSize(2, 4), signString, jungleFields);

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

		SnakeHuntInstance snakeHuntInstance = new SnakeHuntInstance(
				jungle,
				Map.of(new SnakeTypeId("A0"), new SnakeType(new SnakeTypeId("A0"), 1, 3, "FERNUNI", new NeighborhoodstructureFalseMock())),
				Duration.ofSeconds(1),
				solution,
				new SnakeHuntRepositoryAdapter());
		assertEquals(List.of(Fehlertyp.ZUORDNUNG, Fehlertyp.ZUORDNUNG), snakeHuntInstance.isValid());
	}

	@Test
	@DisplayName("Test if multiple \"VERWENDUNG\" error is found!")
	void findUsabilityError() {
		List<JungleField> jungleFields = new ArrayList<>();
		jungleFields.add(0, new JungleField(new FieldId("F0"), new Coordinate(0, 0), 1, 2, 'F', new LinkedList<>()));
		jungleFields.add(1, new JungleField(new FieldId("F1"), new Coordinate(0, 1), 1, 2, 'E', new LinkedList<>()));
		jungleFields.add(2, new JungleField(new FieldId("F2"), new Coordinate(0, 2), 1, 1, 'R', new LinkedList<>()));
		jungleFields.add(3, new JungleField(new FieldId("F3"), new Coordinate(0, 3), 1, 2, 'N', new LinkedList<>()));
		jungleFields.add(4, new JungleField(new FieldId("F4"), new Coordinate(1, 0), 1, 2, 'X', new LinkedList<>()));
		jungleFields.add(5, new JungleField(new FieldId("F5"), new Coordinate(1, 1), 1, 2, 'I', new LinkedList<>()));
		jungleFields.add(6, new JungleField(new FieldId("F6"), new Coordinate(1, 2), 1, 1, 'N', new LinkedList<>()));
		jungleFields.add(7, new JungleField(new FieldId("F7"), new Coordinate(1, 3), 1, 2, 'U', new LinkedList<>()));
		String signString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Jungle jungle = new Jungle(new JungleSize(2, 4), signString, jungleFields);

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

		SnakeHuntInstance snakeHuntInstance = new SnakeHuntInstance(
				jungle,
				Map.of(new SnakeTypeId("A0"), new SnakeType(new SnakeTypeId("A0"), 1, 3, "FERNUNI", new NeighborhoodstructureFalseMock())),
				Duration.ofSeconds(1),
				solution,
				new SnakeHuntRepositoryAdapter());
		assertEquals(List.of(Fehlertyp.VERWENDUNG, Fehlertyp.VERWENDUNG), snakeHuntInstance.isValid());
	}

	@Test
	@DisplayName("Test if multiple \"NACHBARSCHAFT\" error is found!")
	void findNeighbourhoodError() {
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

		SnakeHuntInstance snakeHuntInstance = new SnakeHuntInstance(
				jungle,
				Map.of(new SnakeTypeId("A0"), new SnakeType(new SnakeTypeId("A0"), 1, 3, "FERNUNI", new NeighborhoodstructureTrueMock())),
				Duration.ofSeconds(1),
				solution,
				new SnakeHuntRepositoryAdapter());
		assertEquals(List.of(Fehlertyp.NACHBARSCHAFT, Fehlertyp.NACHBARSCHAFT), snakeHuntInstance.isValid());
	}

	@Test
	@DisplayName("Evaluate total points of solution.")
	void evaluateTotalPoints() {
		List<JungleField> jungleFields = new ArrayList<>();
		jungleFields.add(0, new JungleField(new FieldId("F0"), new Coordinate(0, 0), 1, 2, 'F', new LinkedList<>()));
		jungleFields.add(1, new JungleField(new FieldId("F1"), new Coordinate(0, 1), 2, 2, 'E', new LinkedList<>()));
		jungleFields.add(2, new JungleField(new FieldId("F2"), new Coordinate(0, 2), 3, 2, 'R', new LinkedList<>()));
		jungleFields.add(3, new JungleField(new FieldId("F3"), new Coordinate(0, 3), 4, 2, 'N', new LinkedList<>()));
		jungleFields.add(4, new JungleField(new FieldId("F4"), new Coordinate(1, 0), 5, 2, 'X', new LinkedList<>()));
		jungleFields.add(5, new JungleField(new FieldId("F5"), new Coordinate(1, 1), 6, 2, 'I', new LinkedList<>()));
		jungleFields.add(6, new JungleField(new FieldId("F6"), new Coordinate(1, 2), 7, 2, 'N', new LinkedList<>()));
		jungleFields.add(7, new JungleField(new FieldId("F7"), new Coordinate(1, 3), 8, 2, 'U', new LinkedList<>()));
		String signString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Jungle jungle = new Jungle(new JungleSize(2, 4), signString, jungleFields);

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

		SnakeHuntInstance snakeHuntInstance = new SnakeHuntInstance(
				jungle,
				Map.of(new SnakeTypeId("A0"), new SnakeType(new SnakeTypeId("A0"), 9, 3, "FERNUNI", new NeighborhoodstructureFalseMock())),
				Duration.ofSeconds(1),
				solution,
				new SnakeHuntRepositoryAdapter());
		assertEquals(80, snakeHuntInstance.evaluateTotalPoints());
	}

	@Test
	@DisplayName("Evaluate total points of solution with solution null.")
	void evaluateTotalPointsSolutionNull() {
		List<JungleField> jungleFields = new ArrayList<>();
		jungleFields.add(new JungleField(new FieldId("F0"), new Coordinate(0, 0), 1, 1, 'F', new LinkedList<>())) ;
		String signString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Jungle jungle = new Jungle(new JungleSize(2, 4), signString, jungleFields);
		SnakeHuntInstance snakeHuntInstance = new SnakeHuntInstance(
				jungle,
				Map.of(new SnakeTypeId("A0"), new SnakeType(new SnakeTypeId("A0"), 1, 3, "FERNUNI", new Distance(1))),
				Duration.ofSeconds(1),
				new SnakeHuntRepositoryAdapter());
		assertThrows(NoSolutionException.class, snakeHuntInstance::evaluateTotalPoints);
	}

}
