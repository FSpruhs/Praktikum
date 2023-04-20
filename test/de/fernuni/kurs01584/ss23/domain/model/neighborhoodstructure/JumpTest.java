package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;


import java.util.List;

import de.fernuni.kurs01584.ss23.domain.model.JungleSize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidNeighboorhoodStructureException;
import de.fernuni.kurs01584.ss23.domain.model.Coordinate;

import static org.junit.jupiter.api.Assertions.*;

public class JumpTest {
	
	@Test
	@DisplayName("Test if throw exception with negative input")
	void negativeInput() {
		assertThrows(InvalidNeighboorhoodStructureException.class, () -> {
			new Jump(0, -1);
		});
	}
	
	@Test
	@DisplayName("Test if throw exception with both zero")
	void bothZero() {
		assertThrows(InvalidNeighboorhoodStructureException.class, () -> {
			new Jump(0, 0);
		});
	}
	
	@DisplayName("positive Test for Jump(2,1).")
	@ParameterizedTest
	@CsvSource({"1, 0", "0, 1", "1, 4", "4, 1", "3, 0", "0, 3", "3, 4", "4, 3"})
	void isNeighbourPositiv(int row, int column) {
		Jump jump = new Jump(2, 1);
		Coordinate actualCoordinate = new Coordinate(2, 2);
		assertTrue(jump.isNotNeighbour(actualCoordinate, new Coordinate(row, column)));
	}
	
	@DisplayName("negative Test for Jump(2,1).")
	@ParameterizedTest
	@CsvSource({"0, 0", "0, 2", "0, 4", "1, 1", "1, 2", "1, 3", "2, 0", "2, 1", "2, 3", "2, 4", "3, 1", "3, 2", "3, 3", "4, 0", "4, 2", "4, 4"})
	void isNeighbourNegative(int row, int column) {
		Jump jump = new Jump(2, 1);
		Coordinate actualCoordinate = new Coordinate(2, 2);
		assertFalse(jump.isNotNeighbour(actualCoordinate, new Coordinate(row, column)));
	}
	@Test
	@DisplayName("Next Fields when actual is in the middle with jump 2,1.")
	void nextFieldsMiddle() {
		Jump jump = new Jump(2, 1);
		List<Coordinate> expected = List.of(
				new Coordinate(1, 0),
				new Coordinate(0, 1),
				new Coordinate(1, 4),
				new Coordinate(4, 1),
				new Coordinate(0, 3),
				new Coordinate(3, 0),
				new Coordinate(3, 4),
				new Coordinate(4, 3)
				);
		List<Coordinate> actual = jump.nextFields(new Coordinate(2, 2), new JungleSize(5, 5));
		assertTrue(actual.size() == expected.size() && actual.containsAll(expected) && expected.containsAll(actual));
	}
	
	@Test
	@DisplayName("Next Fields when actual is in the top left with jump 2,1.")
	void nextFieldsTopLeft() {
		Jump jump = new Jump(2, 1);
		List<Coordinate> expected = List.of(
				new Coordinate(0, 3),
				new Coordinate(2, 3),
				new Coordinate(3, 0),
				new Coordinate(3, 2)
				);
		List<Coordinate> actual = jump.nextFields(new Coordinate(1, 1), new JungleSize(5, 5));
		assertTrue(actual.size() == expected.size() && actual.containsAll(expected) && expected.containsAll(actual));
	}
	
	@Test
	@DisplayName("Next Fields when actual is in the bottom right with jump 2,1.")
	void nextFieldsBottomRight() {
		Jump jump = new Jump(2, 1);
		List<Coordinate> expected = List.of(
				new Coordinate(2, 1),
				new Coordinate(1, 2),
				new Coordinate(4, 1),
				new Coordinate(1, 4)
				);
		List<Coordinate> actual = jump.nextFields(new Coordinate(3, 3), new JungleSize(5, 5));
		assertTrue(actual.size() == expected.size() && actual.containsAll(expected) && expected.containsAll(actual));
	}
	
	@Test
	@DisplayName("Next Fields when actual is in the middle with jump 2,0.")
	void nextFieldsMiddleWithZero() {
		Jump jump = new Jump(2, 0);
		List<Coordinate> expected = List.of(
				new Coordinate(2, 4),
				new Coordinate(0, 2),
				new Coordinate(2, 0),
				new Coordinate(4, 2)
				);
		List<Coordinate> actual = jump.nextFields(new Coordinate(2, 2), new JungleSize(5, 5));
		assertTrue(actual.size() == expected.size() && actual.containsAll(expected) && expected.containsAll(actual));
	}
	
	@Test
	@DisplayName("Next Fields when actual is in the top left with jump 2,0.")
	void nextFieldsTopLeftWithZero() {
		Jump jump = new Jump(2, 0);
		List<Coordinate> expected = List.of(
				new Coordinate(1, 3),
				new Coordinate(3, 1)
				);
		List<Coordinate> actual = jump.nextFields(new Coordinate(1, 1), new JungleSize(5, 5));
		assertTrue(actual.size() == expected.size() && actual.containsAll(expected) && expected.containsAll(actual));
	}
	
	@Test
	@DisplayName("Next Fields when actual is in the bottom right with jump 2,0.")
	void nextFieldsBottomRightWithZero() {
		Jump jump = new Jump(2, 0);
		List<Coordinate> expected = List.of(
				new Coordinate(1, 3),
				new Coordinate(3, 1)
				);
		List<Coordinate> actual = jump.nextFields(new Coordinate(3, 3), new JungleSize(5, 5));
		assertTrue(actual.size() == expected.size() && actual.containsAll(expected) && expected.containsAll(actual));
	}
	
	@Test
	@DisplayName("Next Fields when actual is in the middle with jump 2,2.")
	void nextFieldsMiddleWithSame() {
		Jump jump = new Jump(2, 2);
		List<Coordinate> expected = List.of(
				new Coordinate(0, 0),
				new Coordinate(0, 4),
				new Coordinate(4, 0),
				new Coordinate(4, 4)
				);
		List<Coordinate> actual = jump.nextFields(new Coordinate(2, 2), new JungleSize(5, 5));
		assertTrue(actual.size() == expected.size() && actual.containsAll(expected) && expected.containsAll(actual));
	}
	
	@Test
	@DisplayName("Next Fields when actual is in the top left with jump 2,2.")
	void nextFieldsTopLeftWithSame() {
		Jump jump = new Jump(2, 2);
		List<Coordinate> expected = List.of(
				new Coordinate(3, 3)
				);
		List<Coordinate> actual = jump.nextFields(new Coordinate(1, 1), new JungleSize(5, 5));
		assertTrue(actual.size() == expected.size() && actual.containsAll(expected) && expected.containsAll(actual));
	}
	
	@Test
	@DisplayName("Next Fields when actual is in the bottom right with jump 2,2.")
	void nextFieldsBottomRightWithSame() {
		Jump jump = new Jump(2, 2);
		List<Coordinate> expected = List.of(
				new Coordinate(1, 1)
				);
		List<Coordinate> actual = jump.nextFields(new Coordinate(3, 3), new JungleSize(5, 5));
		assertTrue(actual.size() == expected.size() && actual.containsAll(expected) && expected.containsAll(actual));
	}

}
