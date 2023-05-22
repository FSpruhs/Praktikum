package de.fernuni.kurs01584.ss23.application.ports.in;

/**
 * Port to evaluate the value of the snake hunt solution.
 */
public interface EvaluateSolutionInPort {

	/**
	 * Calculates the value of the snake hunt solution.
	 *
	 * @return the value of the snake hunt solution. Default 0.
	 */
	int evaluateTotalPoints();
}
