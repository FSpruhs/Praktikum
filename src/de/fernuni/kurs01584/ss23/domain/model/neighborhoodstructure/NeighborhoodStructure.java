package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

import java.util.List;

import de.fernuni.kurs01584.ss23.domain.model.Coordinate;
import de.fernuni.kurs01584.ss23.domain.model.JungleSize;

/**
 * The neighborhood structure sets the rules in which the snakes can spread across the jungle.
 */
public interface NeighborhoodStructure {

	/**
	 * Checks whether the current coordinate can follow on the previous coordinate.
	 * The rules of the neighborhood structure are used for the check.
	 *
	 * @param actualCoordinate  the actual coordinate.
	 * @param previousCoordinate the previous coordinate.
	 * @return Returns false if the current coordinate can follow the previous coordinate, otherwise true.
	 */
	boolean isNotNeighbour(Coordinate actualCoordinate, Coordinate previousCoordinate);

	/**
	 * Gets a field in a jungle and returns a list with all fields that can be reached with the neighborhood structure.
	 *
	 * @param coordinate The coordinate that serves as the starting point.
	 * @param jungleSize The dimensions of the jungle.
	 * @return A list with all coordinates that can be reached from the starting point.
	 */
	List<Coordinate> nextFields(Coordinate coordinate, JungleSize jungleSize);

	/**
	 * Returns the name of the neighborhood structure.
	 *
	 * @return the name of the neighborhood structure
	 */
	String getName();

	/**
	 * Returns a list of the parameters of the neighborhood structure.
	 * The parameters determine how the nearest neighbors are determined.
	 *
	 * @return a list of the parameters of the neighborhood structure
	 */
	List<Integer> getParameter();

}
