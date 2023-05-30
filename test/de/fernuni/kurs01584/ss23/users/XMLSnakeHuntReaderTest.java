package de.fernuni.kurs01584.ss23.users;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.fernuni.kurs01584.ss23.adapters.users.XMLSnakeHuntReader;
import de.fernuni.kurs01584.ss23.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.Distance;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.Jump;

public class XMLSnakeHuntReaderTest {

	private XMLSnakeHuntReader xmlSnakeHuntReader;

	@BeforeEach
	void setUp() {
		xmlSnakeHuntReader = new XMLSnakeHuntReader(new File("./res/sj_p1_probleminstanz.xml"));
	}

	@Test
	@DisplayName("Positiv Test for Read Duration")
	void readDurationInSeconds() {
		assertEquals(Duration.ofSeconds(60), xmlSnakeHuntReader.readDuration());
	}

	@Test
	@DisplayName("Testing reading jungle")
	void readJungle() {
		List<JungleField> expectedJungleFields = new ArrayList<>();
		expectedJungleFields.add(0, new JungleField(new FieldId("F0"), new Coordinate(0, 0), 1, 1, 'F', new LinkedList<>()));
		expectedJungleFields.add(1, new JungleField(new FieldId("F1"), new Coordinate(0, 1), 1, 1, 'E', new LinkedList<>()));
		expectedJungleFields.add(2, new JungleField(new FieldId("F2"), new Coordinate(0, 2), 1, 1, 'R', new LinkedList<>()));
		expectedJungleFields.add(3, new JungleField(new FieldId("F3"), new Coordinate(0, 3), 1, 1, 'N', new LinkedList<>()));
		expectedJungleFields.add(4, new JungleField(new FieldId("F4"), new Coordinate(1, 0), 1, 1, 'X', new LinkedList<>()));
		expectedJungleFields.add(5, new JungleField(new FieldId("F5"), new Coordinate(1, 1), 1, 1, 'I', new LinkedList<>()));
		expectedJungleFields.add(6, new JungleField(new FieldId("F6"), new Coordinate(1, 2), 1, 1, 'N', new LinkedList<>()));
		expectedJungleFields.add(7, new JungleField(new FieldId("F7"), new Coordinate(1, 3), 1, 1, 'U', new LinkedList<>()));
		String expectedSignString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Jungle expectedJungle = new Jungle(new JungleSize(2, 4), expectedSignString, expectedJungleFields);

		assertEquals(expectedJungle, xmlSnakeHuntReader.readJungle());
	}

	@Test
	@DisplayName("Testing read Snaktype with Jump and Distance Neighborhood structure")
	void readSnakeType() {
		xmlSnakeHuntReader = new XMLSnakeHuntReader(new File("./res/sj_p5_probleminstanz.xml"));

		Map<SnakeTypeId, SnakeType> expected = new HashMap<>();
		expected.put(new SnakeTypeId("A0"), new SnakeType(new SnakeTypeId("A0"), 1, 3, "FERNUNI", new Distance(1)));
		expected.put(new SnakeTypeId("A1"), new SnakeType(new SnakeTypeId("A1"), 1, 3, "HAGEN", new Jump(2, 1)));
		expected.put(new SnakeTypeId("A2"), new SnakeType(new SnakeTypeId("A2"), 1, 3, "ANACONDA", new Jump(3, 2)));

		assertEquals(expected, xmlSnakeHuntReader.readSnakeTypes());
	}

	@Test
	@DisplayName("Testing read Solution with Solution")
	void readSolution( ) {
		xmlSnakeHuntReader = new XMLSnakeHuntReader(new File("./res/sj_p1_loesung.xml"));

		List<Snake> expectedSnakes = new LinkedList<>();
		expectedSnakes.add(new Snake(new SnakeTypeId("A0"), List.of(
				new SnakePart(new FieldId("F0"), 'F', new Coordinate(0, 0)),
				new SnakePart(new FieldId("F1"), 'E', new Coordinate(0, 1)),
				new SnakePart(new FieldId("F2"), 'R', new Coordinate(0, 2)),
				new SnakePart(new FieldId("F3"), 'N', new Coordinate(0, 3)),
				new SnakePart(new FieldId("F7"), 'U', new Coordinate(1, 3)),
				new SnakePart(new FieldId("F6"), 'N', new Coordinate(1, 2)),
				new SnakePart(new FieldId("F5"), 'I', new Coordinate(1, 1))
				),
                new Distance(1)));

		Solution expectedSolution = new Solution(new LinkedList<>());
		expectedSolution.loadSnakes(expectedSnakes);

		assertEquals(expectedSolution, xmlSnakeHuntReader.readSolution());
	}

	@Test
	@DisplayName("Testing read Solution without Solution")
	void readSolutionWithoutSolution( ) {
		xmlSnakeHuntReader = new XMLSnakeHuntReader(new File("./res/sj_p1_probleminstanz.xml"));
		assertNull(xmlSnakeHuntReader.readSolution());
	}
}
