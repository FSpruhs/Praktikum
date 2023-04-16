package de.fernuni.kurs01584.ss23.users;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.fernuni.kurs01584.ss23.domain.model.Coordinate;
import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.JungleField;
import de.fernuni.kurs01584.ss23.domain.model.Snake;
import de.fernuni.kurs01584.ss23.domain.model.SnakePart;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.Solution;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.Distance;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.Jump;

public class XMLSnakeHuntReaderTest {
	
	private XMLSnakeHuntReader xmlSnakeHuntReader;
	
	@BeforeEach
	void setUp() {
		xmlSnakeHuntReader = new XMLSnakeHuntReader("./res/sj_p1_probleminstanz.xml");
	}

	@Test
	@DisplayName("Positiv Test for Read Duration")
	void readDurationInSeconds() {
		assertEquals(Duration.ofSeconds(60), xmlSnakeHuntReader.readDurationInSeconds());
	}
	
	@Test
	@DisplayName("Testing reading jungle")
	void readJungle() {
		List<JungleField> expectedJungleFields = new ArrayList<>();
		expectedJungleFields.add(0, new JungleField("F0", new Coordinate(0, 0), 1, 1, 'F'));
		expectedJungleFields.add(1, new JungleField("F1", new Coordinate(0, 1), 1, 1, 'E'));
		expectedJungleFields.add(2, new JungleField("F2", new Coordinate(0, 2), 1, 1, 'R'));
		expectedJungleFields.add(3, new JungleField("F3", new Coordinate(0, 3), 1, 1, 'N'));
		expectedJungleFields.add(4, new JungleField("F4", new Coordinate(1, 0), 1, 1, 'X'));
		expectedJungleFields.add(5, new JungleField("F5", new Coordinate(1, 1), 1, 1, 'I'));
		expectedJungleFields.add(6, new JungleField("F6", new Coordinate(1, 2), 1, 1, 'N'));
		expectedJungleFields.add(7, new JungleField("F7", new Coordinate(1, 3), 1, 1, 'U'));
		String expectedSignString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Jungle expectedJungle = new Jungle(2, 4, expectedSignString, expectedJungleFields);
		
		assertEquals(expectedJungle, xmlSnakeHuntReader.readJungle());
	}
	
	@Test
	@DisplayName("Testing read Snaktype with Jump and Distance Neighborhood structure")
	void readSnakeType() {
		xmlSnakeHuntReader = new XMLSnakeHuntReader("./res/sj_p5_probleminstanz.xml");
		
		Map<String, SnakeType> expected = new HashMap<>();
		expected.put("A0", new SnakeType("A0", 1, 3, "FERNUNI", new Distance(1)));
		expected.put("A1", new SnakeType("A1", 1, 3, "HAGEN", new Jump(2, 1)));
		expected.put("A2", new SnakeType("A2", 1, 3, "ANACONDA", new Jump(3, 2)));
		
		assertEquals(expected, xmlSnakeHuntReader.readSnakeTypes());
	}
	
	@Test
	@DisplayName("Testing read Solution with Solution")
	void readSolution( ) {
		xmlSnakeHuntReader = new XMLSnakeHuntReader("./res/sj_p1_loesung.xml");
		
		List<Snake> expectedSnakes = new LinkedList<>();
		expectedSnakes.add(new Snake("A0", List.of(
				new SnakePart("F0", 'F', new Coordinate(0, 0)),
				new SnakePart("F1", 'E', new Coordinate(0, 1)),
				new SnakePart("F2", 'R', new Coordinate(0, 2)),
				new SnakePart("F3", 'N', new Coordinate(0, 3)),
				new SnakePart("F7", 'U', new Coordinate(1, 3)),
				new SnakePart("F6", 'N', new Coordinate(1, 2)),
				new SnakePart("F5", 'I', new Coordinate(1, 1))
				)));
		
		Solution expectedSolution = new Solution();
		expectedSolution.loadSnakes(expectedSnakes);
		
		assertEquals(expectedSolution, xmlSnakeHuntReader.readSolution());
	}
	
	@Test
	@DisplayName("Testing read Solution without Solution")
	void readSolutionWithoutSolution( ) {
		xmlSnakeHuntReader = new XMLSnakeHuntReader("./res/sj_p1_probleminstanz.xml");
		assertNull(xmlSnakeHuntReader.readSolution());
	}
}
