package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidNeighboorhoodStructureException;
import de.fernuni.kurs01584.ss23.domain.model.Coordinate;
import de.fernuni.kurs01584.ss23.domain.model.JungleSize;

public class Jump implements NeighborhoodStructure{

	private final int row;
	private final int column;

	public Jump(int row, int column) {
		validateJump(row, column);
		this.row = row;
		this.column = column;
	}

	private void validateJump(int row, int column) {
		validateRowAndColumn(row, column);
		isBothZero(row, column);
	}

	private void validateRowAndColumn(int row, int column) {
		if (row < 0 || column < 0) {
			throw new InvalidNeighboorhoodStructureException("Row and Column cant be negative!");
		}
	}

	private void isBothZero(int row, int column) {
		if (row == 0 && column == 0) {
			throw new InvalidNeighboorhoodStructureException("Row and Column cant both be zero!");
		}
	}

	@Override
	public String toString() {
		return "Jump{" +
				"row=" + row +
				", column=" + column +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jump other = (Jump) obj;
		return column == other.column && row == other.row;
	}

	@Override
	public boolean isNotNeighbour(Coordinate actualCoordinate, Coordinate previousCoordinate) {
		return !((matchesRow(previousCoordinate.row(), actualCoordinate.row())
				&& matchesColumn(previousCoordinate.column(), actualCoordinate.column()))
				|| (matchesRow(previousCoordinate.column(), actualCoordinate.column())
				&& matchesColumn(previousCoordinate.row(), actualCoordinate.row())));
	}

	private boolean matchesColumn(int previous, int actual) {
		return previous + column == actual || previous - column == actual;
	}

	private boolean matchesRow(int previous, int actual) {
		return previous + row == actual || previous - row == actual;
	}

	@Override
	public List<Coordinate> nextFields(Coordinate coordinate, JungleSize jungleSize) {
		if (row == 0 || column == 0) {
			return nextFieldsWithZeroValue(coordinate, jungleSize);
		}
		if (row == column) {
			return nextFieldsWithSameCoordinateValue(coordinate, jungleSize);
		}
		return nextFieldsWithDifferentCoordinateValue(coordinate, jungleSize);

	}

	@Override
	public String getName() {
		return "Jump";
	}

	@Override
	public List<Integer> getParameter() {
		return List.of(row, column);
	}

	private List<Coordinate> nextFieldsWithDifferentCoordinateValue(Coordinate coordinate, JungleSize jungleSize) {
		List<Coordinate> result = new LinkedList<>();
		result.addAll(addFields(coordinate, jungleSize, row, column));
		result.addAll(addFields(coordinate, jungleSize, column, row));
		return result;
	}

	private List<Coordinate> addFields(Coordinate coordinate, JungleSize jungleSize, int firstValue, int secondValue) {
		List<Coordinate> result = new LinkedList<>();
		if (topLeftIsValid(coordinate, firstValue, secondValue)) {
			result.add(new Coordinate(topCoordinate(coordinate, firstValue), leftCoordinate(coordinate, secondValue)));
		}
		if (bottomLeftIsValid(coordinate, jungleSize, firstValue, secondValue)) {
			result.add(new Coordinate(bottomCoordinate(coordinate, firstValue), leftCoordinate(coordinate, secondValue)));
		}
		if (topRightIsValid(coordinate, jungleSize, firstValue, secondValue)) {
			result.add(new Coordinate(topCoordinate(coordinate, firstValue), rightCoordinate(coordinate, secondValue)));
		}
		if (bottomRightIsValid(coordinate, jungleSize, firstValue, secondValue)) {
			result.add(new Coordinate(bottomCoordinate(coordinate, firstValue), rightCoordinate(coordinate, secondValue)));
		}
		return result;
	}

	private boolean bottomRightIsValid(Coordinate coordinate, JungleSize jungleSize, int firstValue, int secondValue) {
		return bottomCoordinate(coordinate, firstValue) <= jungleRows(jungleSize) && rightCoordinate(coordinate, secondValue) <= jungleColumns(jungleSize);
	}

	private boolean topRightIsValid(Coordinate coordinate, JungleSize jungleSize, int firstValue, int secondValue) {
		return topCoordinate(coordinate, firstValue) >= 0 && rightCoordinate(coordinate, secondValue) <= jungleColumns(jungleSize);
	}

	private boolean bottomLeftIsValid(Coordinate coordinate, JungleSize jungleSize, int firstValue, int secondValue) {
		return bottomCoordinate(coordinate, firstValue) <= jungleRows(jungleSize) && leftCoordinate(coordinate, secondValue) >= 0;
	}

	private boolean topLeftIsValid(Coordinate coordinate, int row, int column) {
		return topCoordinate(coordinate, row) >= 0 && leftCoordinate(coordinate, column) >= 0;
	}

	private List<Coordinate> nextFieldsWithSameCoordinateValue(Coordinate coordinate, JungleSize jungleSize) {
		List<Coordinate> result = new LinkedList<>();
		int distance = row;
		if (topLeftIsValid(coordinate, distance)) {
			result.add(new Coordinate(topCoordinate(coordinate, distance), leftCoordinate(coordinate, distance)));
		}
		if (bottomLeftIsValid(coordinate, jungleSize, distance)) {
			result.add(new Coordinate(bottomCoordinate(coordinate, distance), leftCoordinate(coordinate, distance)));
		}
		if (topRightIsValid(coordinate, jungleSize, distance)) {
			result.add(new Coordinate(topCoordinate(coordinate, distance), rightCoordinate(coordinate, distance)));
		}
		if (bottomRightIsValid(coordinate, jungleSize, distance)) {
			result.add(new Coordinate(bottomCoordinate(coordinate, distance), rightCoordinate(coordinate, distance)));
		}
		return result;
	}

	private boolean bottomRightIsValid(Coordinate coordinate, JungleSize jungleSize, int distance) {
		return bottomCoordinate(coordinate, distance) <= jungleRows(jungleSize) && rightCoordinate(coordinate, distance) <= jungleColumns(jungleSize);
	}

	private boolean topRightIsValid(Coordinate coordinate, JungleSize jungleSize, int distance) {
		return topCoordinate(coordinate, distance) >= 0 && rightCoordinate(coordinate, distance) <= jungleColumns(jungleSize);
	}

	private boolean bottomLeftIsValid(Coordinate coordinate, JungleSize jungleSize, int distance) {
		return bottomCoordinate(coordinate, distance) <= jungleRows(jungleSize) && leftCoordinate(coordinate, distance) >= 0;
	}

	private boolean topLeftIsValid(Coordinate coordinate, int distance) {
		return topCoordinate(coordinate, distance) >= 0 && leftCoordinate(coordinate, distance) >= 0;
	}

	private int jungleRows(JungleSize jungleSize) {
		return jungleSize.rows() - 1;
	}

	private int jungleColumns(JungleSize jungleSize) {
		return jungleSize.columns() - 1;
	}

	private List<Coordinate> nextFieldsWithZeroValue(Coordinate coordinate, JungleSize jungleSize) {
		List<Coordinate> result = new LinkedList<>();
		int distance = Math.max(row, column);
		if (topIsValid(coordinate, distance)) {
			result.add(new Coordinate(topCoordinate(coordinate, distance), coordinate.column()));
		}
		if (bottomIsValid(coordinate, jungleSize, distance)) {
			result.add(new Coordinate(bottomCoordinate(coordinate, distance), coordinate.column()));
		}
		if (leftIsValid(coordinate, distance)) {
			result.add(new Coordinate(coordinate.row(), leftCoordinate(coordinate, distance)));
		}
		if (rightIsValid(coordinate, jungleSize, distance)) {
			result.add(new Coordinate(coordinate.row(), rightCoordinate(coordinate, distance)));
		}
		return result;
	}

	private int rightCoordinate(Coordinate coordinate, int distance) {
		return coordinate.column() + distance;
	}

	private int leftCoordinate(Coordinate coordinate, int distance) {
		return coordinate.column() - distance;
	}

	private int bottomCoordinate(Coordinate coordinate, int distance) {
		return coordinate.row() + distance;
	}

	private int topCoordinate(Coordinate coordinate, int distance) {
		return coordinate.row() - distance;
	}


	private boolean rightIsValid(Coordinate coordinate, JungleSize jungleSize, int distance) {
		return rightCoordinate(coordinate, distance) <= jungleColumns(jungleSize);
	}

	private boolean leftIsValid(Coordinate coordinate, int distance) {
		return leftCoordinate(coordinate, distance) >= 0;
	}

	private boolean bottomIsValid(Coordinate coordinate, JungleSize jungleSize, int distance) {
		return bottomCoordinate(coordinate, distance) <= jungleRows(jungleSize);
	}

	private boolean topIsValid(Coordinate coordinate, int distance) {
		return topCoordinate(coordinate, distance) >= 0;
	}

}
