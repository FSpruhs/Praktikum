package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidNeighboorhoodStructureException;
import de.fernuni.kurs01584.ss23.domain.model.Coordinate;
import de.fernuni.kurs01584.ss23.domain.model.JungleSize;

public class Distance implements NeighborhoodStructure {


	private final int fieldRange;

	public Distance(int fieldRange) {
		this.fieldRange = fieldRange;
		validateDistance(fieldRange);
	}

	private void validateDistance(int fieldRange) {
		if (fieldRange <= 0) {
			throw new InvalidNeighboorhoodStructureException("Field Range must be greater than 0!");
		}
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
		return isSameCoordinate(actualCoordinate, previousCoordinate) ||
				isCoordinateOutOfRange(actualCoordinate.row(), previousCoordinate.row()) ||
				isCoordinateOutOfRange(actualCoordinate.column(), previousCoordinate.column());
	}

	public boolean isSameCoordinate(Coordinate actualCoordinate, Coordinate previousCoordinate) {
		return actualCoordinate.row() == previousCoordinate.row() && actualCoordinate.column() == previousCoordinate.column();
	}

	public boolean isCoordinateOutOfRange(int actualCoordinate, int previousCoordinate) {
		return istToBig(actualCoordinate, previousCoordinate) || isToSmall(actualCoordinate, previousCoordinate);
	}

	private boolean isToSmall(int actualCoordinate, int previousCoordinate) {
		return actualCoordinate < previousCoordinate - fieldRange;
	}

	private boolean istToBig(int actualCoordinate, int previousCoordinate) {
		return actualCoordinate > previousCoordinate + fieldRange;
	}

	@Override
	public List<Coordinate> nextFields(Coordinate coordinate, JungleSize jungleSize) {
		List<Coordinate> result = new LinkedList<>();
		for (int rowNum = getStartPosRow(coordinate); rowNum <= getEndPosRow(coordinate, jungleSize.rows()); rowNum++) {
			for (int colNum = getStartPosColumn(coordinate); colNum <= getEndPosColumn(coordinate, jungleSize.columns()); colNum++) {
				checkCoordinate(coordinate, result, rowNum, colNum);
			}
		}
		return result;
	}

	@Override
	public String getName() {
		return "Distance";
	}

	private void checkCoordinate(Coordinate coordinate, List<Coordinate> result, int rowNum, int colNum) {
		if (isNext(coordinate, rowNum, colNum)) {
			result.add(new Coordinate(rowNum, colNum));
		}
	}

	private boolean isNext(Coordinate coordinate, int rowNum, int colNum) {
		return rowNum != coordinate.row() || colNum != coordinate.column();
	}

	private int getEndPosColumn(Coordinate coordinate, int columns) {
		return Math.min(coordinate.column() + fieldRange, columns - 1);
	}

	private int getEndPosRow(Coordinate coordinate, int rows) {
		return Math.min(coordinate.row() + fieldRange, rows - 1);
	}

	private int getStartPosColumn(Coordinate coordinate) {
		return Math.max(coordinate.column() - fieldRange, 0);
	}

	private int getStartPosRow(Coordinate coordinate) {
		return Math.max(coordinate.row() - fieldRange, 0);
	}
}
