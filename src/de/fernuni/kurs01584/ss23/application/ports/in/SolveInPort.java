package de.fernuni.kurs01584.ss23.application.ports.in;

import de.fernuni.kurs01584.ss23.domain.model.Solution;

import java.time.Duration;

/**
 * Port to search for a solution to the snake hunt instance.
 */
public interface SolveInPort {

	/**
	 * Search for a solution to the snake hunt instance.
	 *
	 * @return the found solution.
	 */
	Solution solveSnakeHuntInstance();

	/**
	 * Returns the duration in which the solution was found.
	 *
	 * @return the duration in which the solution was found
	 */
	Duration getActualDuration();
}
