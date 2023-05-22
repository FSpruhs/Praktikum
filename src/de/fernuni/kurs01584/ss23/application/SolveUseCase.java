package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.application.algorithm.DoubleRecursionAlgorithm;
import de.fernuni.kurs01584.ss23.application.algorithm.SnakeHuntAlgorithm;
import de.fernuni.kurs01584.ss23.application.ports.in.SolveInPort;
import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.SnakeTypeId;
import de.fernuni.kurs01584.ss23.domain.model.Solution;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Use case to solve a snake hunt instance.
 */
public class SolveUseCase implements SolveInPort {

    private static final Logger log = Logger.getLogger(SolveUseCase.class.getName());
    private final Jungle jungle;
    private final Duration targetDuration;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;
    private Duration actualDuration;

    /**
     * Constructor of the solve use case.
     *
     * @param jungle jungle in which to search for the snakes.
     * @param targetDuration target duration to solve the snake hunt instance.
     * @param snakeTypes map of snake types to find.
     */
    public SolveUseCase(
            Jungle jungle,
            Duration targetDuration,
            Map<SnakeTypeId, SnakeType> snakeTypes
    ) {
        this.jungle = jungle;
        this.targetDuration = targetDuration;
        this.snakeTypes = snakeTypes;
    }

    /**
     * Searches for the solution of the snakes hunt instance with the highest value in the target time.
     *
     * @return the found solution.
     */
    @Override
    public Solution solveSnakeHuntInstance() {
        checkJungleFields();
        SnakeHuntAlgorithm snakeHuntAlgorithm = new DoubleRecursionAlgorithm(
                jungle,
                snakeTypes,
                targetDuration
        );
        long start = actualTime();
        Solution solution = snakeHuntAlgorithm.solveSnakeHuntInstance();
        this.actualDuration = Duration.ofNanos(actualTime() - start);
        return solution;
    }

    private void checkJungleFields() {
        if (jungle.getJungleFields().isEmpty()) {
            log.warning("Can not solve jungle, because jungle is empty.");
            throw new NoSolutionException();
        }
    }

    private long actualTime() {
        return System.nanoTime();
    }

    /**
     * Returns the duration in which the solution was found.
     *
     * @return the duration in which the solution was found.
     */
    @Override
    public Duration getActualDuration() {
        return actualDuration;
    }

}
