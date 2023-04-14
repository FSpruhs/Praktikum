package de.fernuni.kurs01584.ss23.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.Distance;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;

public class SnakeHuntInstanceTest {
	
	
	@Test
	@DisplayName("Test Exception if Solution is null")
	void solutionIsNull( ) {
		JungleField[][] jungleFields = new JungleField[1][1];
		jungleFields[0][0] = new JungleField("F0", 0, 0, 1, 1, 'F');
		String signString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Jungle jungle = new Jungle(2, 4, signString, jungleFields);
		SnakeHuntInstance snakeHuntInstance = new SnakeHuntInstance(
				jungle,
				Map.of("A0", new SnakeType("A0", 1, 3, "FERNUNI", new Distance(1))),
				Duration.ofSeconds(1));
		assertThrows(NoSolutionException.class, () -> {
			snakeHuntInstance.isValid();
		});
	}
	
	@Test
	@DisplayName("Test if multiple \"Glieder\" error is found!")
	void findLengthError() {
		JungleField[][] jungleFields = new JungleField[2][4];
		jungleFields[0][0] = new JungleField("F0", 0, 0, 1, 2, 'F');
		jungleFields[0][1] = new JungleField("F1", 0, 1, 1, 2, 'E');
		jungleFields[0][2] = new JungleField("F2", 0, 2, 1, 2, 'R');
		jungleFields[0][3] = new JungleField("F3", 0, 3, 1, 2, 'N');
		jungleFields[1][0] = new JungleField("F4", 1, 0, 1, 2, 'X');
		jungleFields[1][1] = new JungleField("F5", 1, 1, 1, 2, 'I');
		jungleFields[1][2] = new JungleField("F6", 1, 2, 1, 2, 'N');
		jungleFields[1][3] = new JungleField("F7", 1, 3, 1, 2, 'U');
		String signString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Jungle jungle = new Jungle(2, 4, signString, jungleFields);
		
		List<Snake> snakes = new LinkedList<>();
		snakes.add(new Snake("A0", List.of(
				new SnakePart("F0"),
				new SnakePart("F1"),
				new SnakePart("F2"),
				new SnakePart("F3"),
				new SnakePart("F7"),
				new SnakePart("F6")
				)));
		snakes.add(new Snake("A0", List.of(
				new SnakePart("F0"),
				new SnakePart("F1"),
				new SnakePart("F2"),
				new SnakePart("F3"),
				new SnakePart("F7"),
				new SnakePart("F6")
				)));
		Solution solution = new Solution();
		solution.loadSnakes(snakes);
		
		SnakeHuntInstance snakeHuntInstance = new SnakeHuntInstance(
				jungle,
				Map.of("A0", new SnakeType("A0", 1, 3, "FERNUNI", new NeighborhoodstructureFalseMock())),
				Duration.ofSeconds(1),
				solution);
		assertEquals(List.of(Fehlertyp.GLIEDER, Fehlertyp.GLIEDER), snakeHuntInstance.isValid());
	}
	
	@Test
	@DisplayName("Test if multiple \"ZUORDNUNG\" error is found!")
	void findWrongCharachterError() {
		JungleField[][] jungleFields = new JungleField[2][4];
		jungleFields[0][0] = new JungleField("F0", 0, 0, 1, 2, 'F');
		jungleFields[0][1] = new JungleField("F1", 0, 1, 1, 2, 'E');
		jungleFields[0][2] = new JungleField("F2", 0, 2, 1, 2, 'R');
		jungleFields[0][3] = new JungleField("F3", 0, 3, 1, 2, 'N');
		jungleFields[1][0] = new JungleField("F4", 1, 0, 1, 2, 'X');
		jungleFields[1][1] = new JungleField("F5", 1, 1, 1, 2, 'X');
		jungleFields[1][2] = new JungleField("F6", 1, 2, 1, 2, 'N');
		jungleFields[1][3] = new JungleField("F7", 1, 3, 1, 2, 'U');
		String signString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Jungle jungle = new Jungle(2, 4, signString, jungleFields);
		
		List<Snake> snakes = new LinkedList<>();
		snakes.add(new Snake("A0", List.of(
				new SnakePart("F0"),
				new SnakePart("F1"),
				new SnakePart("F2"),
				new SnakePart("F3"),
				new SnakePart("F7"),
				new SnakePart("F6"),
				new SnakePart("F5")
				)));
		snakes.add(new Snake("A0", List.of(
				new SnakePart("F0"),
				new SnakePart("F1"),
				new SnakePart("F2"),
				new SnakePart("F3"),
				new SnakePart("F7"),
				new SnakePart("F6"),
				new SnakePart("F5")
				)));
		Solution solution = new Solution();
		solution.loadSnakes(snakes);
		
		SnakeHuntInstance snakeHuntInstance = new SnakeHuntInstance(
				jungle,
				Map.of("A0", new SnakeType("A0", 1, 3, "FERNUNI", new NeighborhoodstructureFalseMock())),
				Duration.ofSeconds(1),
				solution);
		assertEquals(List.of(Fehlertyp.ZUORDNUNG, Fehlertyp.ZUORDNUNG), snakeHuntInstance.isValid());
	}
	
	@Test
	@DisplayName("Test if multiple \"VERWENDUNG\" error is found!")
	void findUsabilityError() {
		JungleField[][] jungleFields = new JungleField[2][4];
		jungleFields[0][0] = new JungleField("F0", 0, 0, 1, 2, 'F');
		jungleFields[0][1] = new JungleField("F1", 0, 1, 1, 2, 'E');
		jungleFields[0][2] = new JungleField("F2", 0, 2, 1, 1, 'R');
		jungleFields[0][3] = new JungleField("F3", 0, 3, 1, 2, 'N');
		jungleFields[1][0] = new JungleField("F4", 1, 0, 1, 2, 'X');
		jungleFields[1][1] = new JungleField("F5", 1, 1, 1, 2, 'I');
		jungleFields[1][2] = new JungleField("F6", 1, 2, 1, 1, 'N');
		jungleFields[1][3] = new JungleField("F7", 1, 3, 1, 2, 'U');
		String signString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Jungle jungle = new Jungle(2, 4, signString, jungleFields);
		
		List<Snake> snakes = new LinkedList<>();
		snakes.add(new Snake("A0", List.of(
				new SnakePart("F0"),
				new SnakePart("F1"),
				new SnakePart("F2"),
				new SnakePart("F3"),
				new SnakePart("F7"),
				new SnakePart("F6"),
				new SnakePart("F5")
				)));
		snakes.add(new Snake("A0", List.of(
				new SnakePart("F0"),
				new SnakePart("F1"),
				new SnakePart("F2"),
				new SnakePart("F3"),
				new SnakePart("F7"),
				new SnakePart("F6"),
				new SnakePart("F5")
				)));
		Solution solution = new Solution();
		solution.loadSnakes(snakes);
		
		SnakeHuntInstance snakeHuntInstance = new SnakeHuntInstance(
				jungle,
				Map.of("A0", new SnakeType("A0", 1, 3, "FERNUNI", new NeighborhoodstructureFalseMock())),
				Duration.ofSeconds(1),
				solution);
		assertEquals(List.of(Fehlertyp.VERWENDUNG, Fehlertyp.VERWENDUNG), snakeHuntInstance.isValid());
	}
	
	@Test
	@DisplayName("Test if multiple \"NACHBARSCHAFT\" error is found!")
	void findNeighbourhoodError() {
		JungleField[][] jungleFields = new JungleField[2][4];
		jungleFields[0][0] = new JungleField("F0", 0, 0, 1, 2, 'F');
		jungleFields[0][1] = new JungleField("F1", 0, 1, 1, 2, 'E');
		jungleFields[0][2] = new JungleField("F2", 0, 2, 1, 1, 'R');
		jungleFields[0][3] = new JungleField("F3", 0, 3, 1, 2, 'N');
		jungleFields[1][0] = new JungleField("F4", 1, 0, 1, 2, 'X');
		jungleFields[1][1] = new JungleField("F5", 1, 1, 1, 2, 'I');
		jungleFields[1][2] = new JungleField("F6", 1, 2, 1, 1, 'N');
		jungleFields[1][3] = new JungleField("F7", 1, 3, 1, 2, 'U');
		String signString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Jungle jungle = new Jungle(2, 4, signString, jungleFields);
		
		List<Snake> snakes = new LinkedList<>();
		snakes.add(new Snake("A0", List.of(
				new SnakePart("F0"),
				new SnakePart("F1"),
				new SnakePart("F2"),
				new SnakePart("F3"),
				new SnakePart("F7"),
				new SnakePart("F6"),
				new SnakePart("F5")
				)));
		Solution solution = new Solution();
		solution.loadSnakes(snakes);
		
		SnakeHuntInstance snakeHuntInstance = new SnakeHuntInstance(
				jungle,
				Map.of("A0", new SnakeType("A0", 1, 3, "FERNUNI", new NeighborhoodstructureTrueMock())),
				Duration.ofSeconds(1),
				solution);
		assertEquals(List.of(Fehlertyp.NACHBARSCHAFT, Fehlertyp.NACHBARSCHAFT), snakeHuntInstance.isValid());
	}

}
