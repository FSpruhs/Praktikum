package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidNeighboorhoodStructureException;
import de.fernuni.kurs01584.ss23.domain.model.Coordinate;

public class Jump implements NeighborhoodStructure{
	
	private final int row;
	private final int column;
	
	public Jump(int row, int column) {
		
		if (row < 0 || column < 0) {
			throw new InvalidNeighboorhoodStructureException("Row and Column cant be negative!");
		}
		
		if (row == 0 && column == 0) {
			throw new InvalidNeighboorhoodStructureException("Row and Column cant both be zero!");
		}
		
		this.row = row;
		this.column = column;
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
		if (previousCoordinate.row() + row == actualCoordinate.row() && previousCoordinate.column() + column == actualCoordinate.column()) {
			return true;
		}
		if (previousCoordinate.row() + row == actualCoordinate.row() && previousCoordinate.column() - column == actualCoordinate.column()) {
			return true;
		}
		if (previousCoordinate.row() - row == actualCoordinate.row() && previousCoordinate.column() + column == actualCoordinate.column()) {
			return true;
		}
		if (previousCoordinate.row() - row == actualCoordinate.row() && previousCoordinate.column() - column == actualCoordinate.column()) {
			return true;
		}
		if (previousCoordinate.row() + column == actualCoordinate.row() && previousCoordinate.column() + row == actualCoordinate.column()) {
			return true;
		}
		if (previousCoordinate.row() + column == actualCoordinate.row() && previousCoordinate.column() - row == actualCoordinate.column()) {
			return true;
		}
		if (previousCoordinate.row() - column == actualCoordinate.row() && previousCoordinate.column() + row == actualCoordinate.column()) {
			return true;
		}
		return previousCoordinate.row() - column == actualCoordinate.row() && previousCoordinate.column() - row == actualCoordinate.column();
	}

	@Override
	public List<Coordinate> nextFields(Coordinate coordinate, int rows, int columns) {
		List<Coordinate> result = new LinkedList<>();
		if (row == 0 || column == 0) {
			int distance = Math.max(row, column);
			if (coordinate.row() - distance >= 0) {
				result.add(new Coordinate(coordinate.row() - distance, coordinate.column()));
			}
			if (coordinate.row() + distance <= rows - 1) {
				result.add(new Coordinate(coordinate.row() + distance, coordinate.column()));
			}
			if (coordinate.column() - distance >= 0) {
				result.add(new Coordinate(coordinate.row(), coordinate.column() - distance));
			}
			if (coordinate.column() + distance <= columns - 1) {
				result.add(new Coordinate(coordinate.row(), coordinate.column() + distance));
			}
			return result;
		}
		
		if (row == column) {
			int distance = row;
			if (coordinate.row() - distance >= 0 && coordinate.column() - distance >= 0) {
				result.add(new Coordinate(coordinate.row() - distance, coordinate.column() - distance));
			}
			if (coordinate.row() + distance <= rows - 1 && coordinate.column() - distance >= 0) {
				result.add(new Coordinate(coordinate.row() + distance, coordinate.column() - distance));
			}
			if (coordinate.row() - distance >= 0 && coordinate.column() + distance <= columns - 1) {
				result.add(new Coordinate(coordinate.row() - distance, coordinate.column() + distance));
			}
			if (coordinate.row() + distance <= rows - 1 && coordinate.column() + distance <= columns - 1) {
				result.add(new Coordinate(coordinate.row() + distance, coordinate.column() + distance));
			}
			return result;

		}
		if (coordinate.row() - row >= 0 && coordinate.column() - column >= 0) {
			result.add(new Coordinate(coordinate.row() - row, coordinate.column() - column));
		}
		if (coordinate.row() + row <= rows - 1 && coordinate.column() - column >= 0) {
			result.add(new Coordinate(coordinate.row() + row, coordinate.column() - column));
		}
		if (coordinate.row() - row >= 0 && coordinate.column() + column <= columns - 1) {
			result.add(new Coordinate(coordinate.row() - row, coordinate.column() + column));
		}
		if (coordinate.row() + row <= rows - 1 && coordinate.column() + column <= columns - 1) {
			result.add(new Coordinate(coordinate.row() + row, coordinate.column() + column));
		}
		if (coordinate.row() - column >= 0 && coordinate.column() - row >= 0) {
			result.add(new Coordinate(coordinate.row() - column, coordinate.column() - row));
		}
		if (coordinate.row() + column <= rows - 1 && coordinate.column() - row >= 0) {
			result.add(new Coordinate(coordinate.row() + column, coordinate.column() - row));
		}
		if (coordinate.row() - column >= 0 && coordinate.column() + row <= columns - 1) {
			result.add(new Coordinate(coordinate.row() - column, coordinate.column() + row));
		}
		if (coordinate.row() + column <= rows - 1 && coordinate.column() + row <= columns - 1) {
			result.add(new Coordinate(coordinate.row() + column, coordinate.column() + row));
		}
		
		return result;
	}

}
