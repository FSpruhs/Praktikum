package de.fernuni.kurs01584.ss23.application.ports.in;

import de.fernuni.kurs01584.ss23.domain.model.Solution;

import java.io.File;

/**
 * Port to search for a solution to the snake hunt instance.
 */
public interface SolveInPort {

	/**
	 * Search for a solution to the snake hunt instance.
	 */
	void solveSnakeHuntInstance(File output);

}
