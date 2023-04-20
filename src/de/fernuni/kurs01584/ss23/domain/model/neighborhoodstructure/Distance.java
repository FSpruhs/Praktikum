package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidNeighboorhoodStructureException;
import de.fernuni.kurs01584.ss23.domain.model.Coordinate;

public class Distance implements NeighborhoodStructure {
	
	
	private final int fieldRange;

	public Distance(int distance) {
		
		if (distance <= 0) {
			throw new InvalidNeighboorhoodStructureException("Distance must be greater than 0!");
		}
		
		this.fieldRange = distance;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Distance distance = (Distance) o;
		return fieldRange == distance.fieldRange;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldRange);
	}

	@Override
	public boolean isNotNeighbour(Coordinate actualCoordinate, Coordinate previousCoordinate) {
		if (actualCoordinate.row() == previousCoordinate.row() && actualCoordinate.column() == previousCoordinate.column()) {
			return true;
		}
		if (actualCoordinate.row() > previousCoordinate.row() + fieldRange || actualCoordinate.row() < previousCoordinate.row() - fieldRange) {
			return true;
		}
		return actualCoordinate.column() > previousCoordinate.column() + fieldRange || actualCoordinate.column() < previousCoordinate.column() - fieldRange;
	}

	@Override
	public List<Coordinate> nextFields(Coordinate coordinate, int rows, int columns) {
		List<Coordinate> result = new LinkedList<>();

		int startPosRow = Math.max(coordinate.row() - fieldRange, 0);
		int startPosColumn = Math.max(coordinate.column() - fieldRange, 0);
		int endPosRow = Math.min(coordinate.row() + fieldRange, rows - 1);
		int endPosColumn = Math.min(coordinate.column() + fieldRange, columns - 1);

		for (int rowNum = startPosRow; rowNum <= endPosRow; rowNum++) {
			for (int colNum = startPosColumn; colNum <= endPosColumn; colNum++) {
				if (rowNum != coordinate.row() || colNum != coordinate.column()) {
					result.add(new Coordinate(rowNum, colNum));
				}
			}
		}
		return result;
	}
}
