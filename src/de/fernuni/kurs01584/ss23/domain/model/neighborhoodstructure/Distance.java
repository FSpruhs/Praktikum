package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidNeighboorhoodStructureException;
import de.fernuni.kurs01584.ss23.domain.model.Coordinate;

public class Distance implements NeighborhoodStructure {
	
	
	private final int distance;

	public Distance(int distance) {
		
		if (distance <= 0) {
			throw new InvalidNeighboorhoodStructureException("Distance must be greater than 0!");
		}
		
		this.distance = distance;
	}

	@Override
	public int hashCode() {
		return Objects.hash(distance);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Distance other = (Distance) obj;
		return distance == other.distance;
	}

	@Override
	public boolean isNotNeighbour(Coordinate actualCoordinate, Coordinate previousCoordinate) {
		if (actualCoordinate.row() == previousCoordinate.row() && actualCoordinate.column() == previousCoordinate.column()) {
			return true;
		}
		if (actualCoordinate.row() > previousCoordinate.row() + distance || actualCoordinate.row() < previousCoordinate.row() - distance) {
			return true;
		}
		if (actualCoordinate.column() > previousCoordinate.column() + distance || actualCoordinate.column() < previousCoordinate.column() - distance) {
			return true;
		}
		return false;
	}

	@Override
	public List<Coordinate> nextFields(Coordinate coordinate, int rows, int columns) {
		List<Coordinate> result = new LinkedList<>();
		
        int startPosRow = (coordinate.row() - distance < 0) ? 0 : coordinate.row() - distance;
        int startPosColumn = (coordinate.column() - distance < 0) ? 0 : coordinate.column() - distance;
        int endPosRow = (coordinate.row() + distance > rows - 1) ? rows - 1: coordinate.row() + distance;
        int endPosColumn = (coordinate.column() + distance > columns - 1) ? columns - 1  : coordinate.column() + distance;
        
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
