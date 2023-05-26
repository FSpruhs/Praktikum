package de.fernuni.kurs01584.ss23.application.algorithm;

import de.fernuni.kurs01584.ss23.domain.model.Solution;

/**
 * Looking for a solution to a snake hunt.
 * The goal is to find a solution with the highest value.
 * There is a time limit for the search.
 * If this time limit is exceeded, the search is aborted.
 * If the search is aborted, the solution with the highest value found up to that point is returned.
 */
public interface SnakeHuntAlgorithm {

    /**
     * Creates a new solution for the snakes hunts.
     *
     * @return the new solution.
     */
    Solution solveSnakeHuntInstance();

}
