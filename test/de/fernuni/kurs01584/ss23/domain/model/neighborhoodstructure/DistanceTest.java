package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidNeighboorhoodStructureException;
import de.fernuni.kurs01584.ss23.domain.model.Coordinate;
import de.fernuni.kurs01584.ss23.domain.model.JungleSize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DistanceTest {
	
	@Test
	@DisplayName("Test if throw exception with negative distance")
	void negativeDistance() {
		assertThrows(InvalidNeighboorhoodStructureException.class, () -> {
			new Distance(-1);
		});
	}
	
	@Test
	@DisplayName("Test if throw exception with zero distance")
	void zeroDistance() {
		assertThrows(InvalidNeighboorhoodStructureException.class, () -> {
			new Distance(0);
		});
	}
	
	@DisplayName("negative Test for Distance(1).")
	@ParameterizedTest
	@CsvSource({"1, 1", "1, 2", "1, 3", "2, 1", "2, 3", "3, 1", "3, 2", "3, 3"})
	void isNeighbourPositiv(int row, int column) {
		Distance distance= new Distance(1);
		Coordinate actualCoordinate = new Coordinate(2, 2);
		assertFalse(distance.isNotNeighbour(actualCoordinate, new Coordinate(row, column)));
	}
	
	@DisplayName("positive Tests for Distance(1).")
	@ParameterizedTest
	@CsvSource({"0, 0", "0, 1", "0, 2", "0, 3", "0, 4", "1, 0", "2, 0", "3, 0", "4, 0", "4, 1", "4, 2", "4, 3", "4, 4", "1, 4", "2, 4", "3, 4", "2, 2"})
	void isNeighbourNegative(int row, int column) {
		Distance distance= new Distance(1);
		Coordinate actualCoordinate = new Coordinate(2, 2);
		assertTrue(distance.isNotNeighbour(actualCoordinate, new Coordinate(row, column)));
	}
	
	@Test
	@DisplayName("Next Fields when actual is in the middle with distance 2.")
	void nextFieldsMiddle() {
		Distance distance = new Distance(2);
		List<Coordinate> expected = List.of(
				new Coordinate(0, 0),
				new Coordinate(0, 1),
				new Coordinate(0, 2),
				new Coordinate(0, 3),
				new Coordinate(0, 4),
				new Coordinate(1, 0),
				new Coordinate(1, 1),
				new Coordinate(1, 2),
				new Coordinate(1, 3),
				new Coordinate(1, 4),
				new Coordinate(2, 0),
				new Coordinate(2, 1),
				new Coordinate(2, 3),
				new Coordinate(2, 4),
				new Coordinate(3, 0),
				new Coordinate(3, 1),
				new Coordinate(3, 2),
				new Coordinate(3, 3),
				new Coordinate(3, 4),
				new Coordinate(4, 0),
				new Coordinate(4, 1),
				new Coordinate(4, 2),
				new Coordinate(4, 3),
				new Coordinate(4, 4)
				);
		List<Coordinate> actual = distance.nextFields(new Coordinate(2, 2), new JungleSize(5, 5));
		assertTrue(actual.size() == expected.size() && actual.containsAll(expected) && expected.containsAll(actual));
	}
	
	@Test
	@DisplayName("Next Fields when actual is in the top left with distance 2.")
	void nextFieldsTopLeft() {
		Distance distance = new Distance(2);
		List<Coordinate> expected = List.of(
				new Coordinate(0, 0),
				new Coordinate(0, 1),
				new Coordinate(0, 2),
				new Coordinate(0, 3),
				new Coordinate(1, 0),
				new Coordinate(1, 2),
				new Coordinate(1, 3),
				new Coordinate(2, 0),
				new Coordinate(2, 1),
				new Coordinate(2, 2),
				new Coordinate(2, 3),
				new Coordinate(3, 0),
				new Coordinate(3, 1),
				new Coordinate(3, 2),
				new Coordinate(3, 3)
				);
		List<Coordinate> actual = distance.nextFields(new Coordinate(1, 1), new JungleSize(5, 5));
		assertTrue(actual.size() == expected.size() && actual.containsAll(expected) && expected.containsAll(actual));
	}
	
	@Test
	@DisplayName("Next Fields when actual is in the bottom right with distance 2.")
	void nextFieldsBottomRight() {
		Distance distance = new Distance(2);
		List<Coordinate> expected = List.of(
				new Coordinate(1, 1),
				new Coordinate(1, 2),
				new Coordinate(1, 3),
				new Coordinate(1, 4),
				new Coordinate(2, 1),
				new Coordinate(2, 2),
				new Coordinate(2, 3),
				new Coordinate(2, 4),
				new Coordinate(3, 1),
				new Coordinate(3, 2),
				new Coordinate(3, 4),
				new Coordinate(4, 1),
				new Coordinate(4, 2),
				new Coordinate(4, 3),
				new Coordinate(4, 4)
				);
		List<Coordinate> actual = distance.nextFields(new Coordinate(3, 3), new JungleSize(5, 5));
		assertTrue(actual.size() == expected.size() && actual.containsAll(expected) && expected.containsAll(actual));
	}

}
