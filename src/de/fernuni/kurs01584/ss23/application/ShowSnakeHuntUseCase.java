package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.application.ports.in.ShowSnakeHuntIntPort;
import de.fernuni.kurs01584.ss23.domain.model.*;

import java.util.Map;

/**
 * Use case to show the data of a snake hunt instance.
 */
public class ShowSnakeHuntUseCase implements ShowSnakeHuntIntPort {

    private final SnakeHunt snakeHunt;


    public ShowSnakeHuntUseCase() {
        this.snakeHunt = SnakeHunt.getInstance();
    }

    /**
     * Returns data of the snake types to be shown.
     *
     * @return map withe snake type id and snake types.
     */
    @Override
    public Map<SnakeTypeId, SnakeType>  showSnakeTypes() {
        return snakeHunt.getSnakeTypes();
    }

    /**
     * Returns data of the solution to be shown.
     *
     * @return data of the solution to be shown.
     */
    @Override
    public Solution showSolution() {
        return snakeHunt.getSolution();
    }

    /**
     * Returns data of the jungle to be shown.
     *
     * @return data of the jungle to be shown.
     */
    @Override
    public Jungle showJungle() {
        return snakeHunt.getJungle();
    }
}
