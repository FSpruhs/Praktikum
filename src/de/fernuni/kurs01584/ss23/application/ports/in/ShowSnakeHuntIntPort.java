package de.fernuni.kurs01584.ss23.application.ports.in;

import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.SnakeTypeId;
import de.fernuni.kurs01584.ss23.domain.model.Solution;

import java.util.Map;

/**
 * Port to show the data of the snake hunt instance.
 */
public interface ShowSnakeHuntIntPort {

    /**
     * Shows the values of the jungle.
     *
     * @return the jungle with all data.
     */
    Jungle showJungle();

    /**
     * Shows the values of the snake types.
     *
     * @return a map of the snake types with snake type id and snake type.
     */
    Map<SnakeTypeId, SnakeType> showSnakeTypes();

    /**
     * Shows the values of the solution.
     *
     * @return the solution with all data.
     */
    Solution showSolution();
}
