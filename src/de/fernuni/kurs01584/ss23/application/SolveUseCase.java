package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.application.algorithm.DoubleRecursionAlgorithm;
import de.fernuni.kurs01584.ss23.application.algorithm.SnakeHuntAlgorithm;
import de.fernuni.kurs01584.ss23.application.ports.in.SolveInPort;
import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.domain.model.*;

import java.io.File;
import java.time.Duration;
import java.util.logging.Logger;

/**
 * Use case to solve a snake hunt instance.
 */
public class SolveUseCase implements SolveInPort {

    private static final Logger log = Logger.getLogger(SolveUseCase.class.getName());
    private final SnakeHunt snakeHunt;
    public SolveUseCase() {
        this.snakeHunt = SnakeHunt.getInstance();
    }

    /**
     * Searches for the solution of the snakes hunt instance with the highest value in the target time.
     *
     */
    @Override
    public void solveSnakeHuntInstance(File output) {
        checkJungleFields();
        SnakeHuntAlgorithm snakeHuntAlgorithm = new DoubleRecursionAlgorithm(
                snakeHunt.getJungle(),
                snakeHunt.getSnakeTypes(),
                snakeHunt.getTargetDuration()
        );
        long start = actualTime();
        snakeHunt.setSolution(snakeHuntAlgorithm.solveSnakeHuntInstance());
        snakeHunt.setActualDuration(Duration.ofNanos(actualTime() - start));
        snakeHunt.save(output);
    }

    private void checkJungleFields() {
        if (isJungleFieldsEmpty()) {
            log.warning("Can not solve jungle, because jungle is empty.");
            throw new NoSolutionException();
        }
    }

    private boolean isJungleFieldsEmpty() {
        return snakeHunt.getJungle().getJungleFields().isEmpty();
    }

    private long actualTime() {
        return System.nanoTime();
    }

}
