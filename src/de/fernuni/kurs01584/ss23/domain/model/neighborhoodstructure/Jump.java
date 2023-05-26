package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidNeighboorhoodStructureException;
import de.fernuni.kurs01584.ss23.domain.model.Coordinate;
import de.fernuni.kurs01584.ss23.domain.model.JungleSize;


/**
 * Jump is a neighborhood structure designed to find the nearest possible neighbor from the current position.
 * First, the fields in the first parameter are counted vertically or horizontally.
 * If the first move was vertical, the next move must be horizontal and vice versa.
 * In the second move, the number of fields from the second parameter is counted accordingly.
 * All fields that can be reached in this way are possible neighboring fields to the current field.
 */
public class Jump implements NeighborhoodStructure{

	private final int fristMove;
	private final int secondMove;

	/**
	 * Constructor of jump.
	 *
	 * @param firstMove The first distance needed to find the next neighbor.
	 * @param secondMove The second distance needed to find the next neighbor.
	 */
	public Jump(int firstMove, int secondMove) {
		validateJump(firstMove, secondMove);
		this.fristMove = firstMove;
		this.secondMove = secondMove;
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
				"row=" + fristMove +
				", column=" + secondMove +
				'}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(secondMove, fristMove);
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
		return secondMove == other.secondMove && fristMove == other.fristMove;
	}

	/**
	 * Checks whether the current coordinate can follow on the previous coordinate.
	 * The jump rules are used for the check.
	 *
	 * @param actualCoordinate  the actual coordinate.
	 * @param previousCoordinate the previous coordinate.
	 * @return Returns false if the current coordinate can follow the previous coordinate, otherwise true.
	 */
	@Override
	public boolean isNotNeighbour(Coordinate actualCoordinate, Coordinate previousCoordinate) {
		return !((matchesRow(previousCoordinate.row(), actualCoordinate.row())
				&& matchesColumn(previousCoordinate.column(), actualCoordinate.column()))
				|| (matchesRow(previousCoordinate.column(), actualCoordinate.column())
				&& matchesColumn(previousCoordinate.row(), actualCoordinate.row())));
	}

	private boolean matchesColumn(int previous, int actual) {
		return previous + secondMove == actual || previous - secondMove == actual;
	}

	private boolean matchesRow(int previous, int actual) {
		return previous + fristMove == actual || previous - fristMove == actual;
	}

	/**
	 * Gets a field in a jungle and returns a list with all fields that can be reached with jump.
	 *
	 * @param coordinate The coordinate that serves as the starting point.
	 * @param jungleSize The dimensions of the jungle.
	 * @return A list with all coordinates that can be reached from the starting point.
	 */
	@Override
	public List<Coordinate> nextFields(Coordinate coordinate, JungleSize jungleSize) {
		if (fristMove == 0 || secondMove == 0) {
			return nextFieldsWithZeroValue(coordinate, jungleSize);
		}
		if (fristMove == secondMove) {
			return nextFieldsWithSameCoordinateValue(coordinate, jungleSize);
		}
		return nextFieldsWithDifferentCoordinateValue(coordinate, jungleSize);

	}

	/**
	 * Returns the name of jump.
	 *
	 * @return the name of jump.
	 */
	@Override
	public String getName() {
		return "Jump";
	}

	/**
	 * Returns a list of the parameters of jump.
	 *
	 * @return a list of the parameters of jump.
	 */
	@Override
	public List<Integer> getParameter() {
		return List.of(fristMove, secondMove);
	}

	private List<Coordinate> nextFieldsWithDifferentCoordinateValue(Coordinate coordinate, JungleSize jungleSize) {
		List<Coordinate> result = new LinkedList<>();
		result.addAll(addFields(coordinate, jungleSize, fristMove, secondMove));
		result.addAll(addFields(coordinate, jungleSize, secondMove, fristMove));
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
		int distance = fristMove;
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
		int distance = Math.max(fristMove, secondMove);
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
