package de.fernuni.kurs01584.ss23.users;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

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
		List<JungleField> expectedJungleFields = new LinkedList<>();
		expectedJungleFields.add(new JungleField("F0", 0, 0, 1, 1, 'F'));
		expectedJungleFields.add(new JungleField("F1", 0, 1, 1, 1, 'E'));
		expectedJungleFields.add(new JungleField("F2", 0, 2, 1, 1, 'R'));
		expectedJungleFields.add(new JungleField("F3", 0, 3, 1, 1, 'N'));
		expectedJungleFields.add(new JungleField("F4", 1, 0, 1, 1, 'X'));
		expectedJungleFields.add(new JungleField("F5", 1, 1, 1, 1, 'I'));
		expectedJungleFields.add(new JungleField("F6", 1, 2, 1, 1, 'N'));
		expectedJungleFields.add(new JungleField("F7", 1, 3, 1, 1, 'U'));
		String expectedSignString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Jungle expectedJungle = new Jungle(2, 4, expectedSignString, expectedJungleFields);
		
		assertEquals(expectedJungle, xmlSnakeHuntReader.readJungle());
	}
	
	@Test
	@DisplayName("Testing read Snaktype with Jump and Distance Neighborhood structure")
	void readSnakeType() {
		xmlSnakeHuntReader = new XMLSnakeHuntReader("./res/sj_p5_probleminstanz.xml");
		
		List<SnakeType> expected = new LinkedList<>();
		expected.add(new SnakeType("A0", 1, 3, "FERNUNI", new Distance(1)));
		expected.add(new SnakeType("A1", 1, 3, "HAGEN", new Jump(2, 1)));
		expected.add(new SnakeType("A2", 1, 3, "ANACONDA", new Jump(3, 2)));
		
		assertEquals(expected, xmlSnakeHuntReader.readSnakeTypes());
	}
	
	@Test
	@DisplayName("Testing read Solution with Solution")
	void readSolution( ) {
		xmlSnakeHuntReader = new XMLSnakeHuntReader("./res/sj_p1_loesung.xml");
		
		List<Snake> expectedSnakes = new LinkedList<>();
		expectedSnakes.add(new Snake("A0", List.of(
				new SnakePart("F0"),
				new SnakePart("F1"),
				new SnakePart("F2"),
				new SnakePart("F3"),
				new SnakePart("F7"),
				new SnakePart("F6"),
				new SnakePart("F5")
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
