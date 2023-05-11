package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.adapters.users.CLIAdapter;
import de.fernuni.kurs01584.ss23.application.algorithm.DoubleRecursionAlgorithm;
import de.fernuni.kurs01584.ss23.application.algorithm.SnakeHuntAlgorithm;
import de.fernuni.kurs01584.ss23.application.ports.in.SolveInPort;
import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.SnakeTypeId;
import de.fernuni.kurs01584.ss23.domain.model.Solution;

import java.io.File;
import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

public class SolveUseCase implements SolveInPort {

    private static final Logger log = Logger.getLogger(SolveUseCase.class.getName());
    private final Jungle jungle;
    private final Duration targetDuration;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;
    private Duration actualDuration;

    public SolveUseCase(Jungle jungle, Duration targetDuration, Map<SnakeTypeId, SnakeType> snakeTypes) {
        this.jungle = jungle;
        this.targetDuration = targetDuration;
        this.snakeTypes = snakeTypes;
    }

    @Override
    public Solution solveSnakeHuntInstance() {
        if (jungle.getJungleFields().isEmpty()) {
            log.warning("Can not solve jungle, because jungle is empty.");
            throw new NoSolutionException();
        }
        SnakeHuntAlgorithm snakeHuntAlgorithm = new DoubleRecursionAlgorithm(jungle, snakeTypes, targetDuration);
        long start = System.nanoTime();
        Solution solution = snakeHuntAlgorithm.solveSnakeHuntInstance();
        this.actualDuration = Duration.ofNanos(System.nanoTime() - start);
        return solution;
    }

    @Override
    public Duration getActualDuration() {
        return actualDuration;
    }

}
