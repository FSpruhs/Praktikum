package de.fernuni.kurs01584.ss23.application.ports.in;

import de.fernuni.kurs01584.ss23.domain.model.*;

import java.util.List;
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
     * @return a list of the snake types.
     */
    List<SnakeTypeDTO> showSnakeTypes();

    /**
     * Shows the values of the solution.
     *
     * @return the solution with all data.
     */
    List<SnakeDTO> showSolutionSnakes();
}
