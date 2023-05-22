package de.fernuni.kurs01584.ss23.application.ports.out;

import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.SnakeTypeId;
import de.fernuni.kurs01584.ss23.domain.model.Solution;

import java.io.File;
import java.time.Duration;
import java.util.Map;

/**
 * Port to save the snake hunt instance in a file.
 */
public interface SaveSnakeHuntInstanceOutPort {

    /**
     * Saves the snake hunt in a file. File, junge, snakeTypes and durationInSeconds are required.
     * ActualDuration and solution are optional.
     *
     * @param file The path to the location where the snake hunt instance should be saved.
     * @param jungle the jungle to be saved.
     * @param snakeTypes the snake types to be saved.
     * @param durationInSeconds the target duration to be saved.
     * @param actualDuration the actual duration if exists, otherwise null.
     * @param solution the solution to be saved if exists, otherwise null.
     */
    void save(
            File file,
            Jungle jungle,
            Map<SnakeTypeId, SnakeType> snakeTypes,
            Duration durationInSeconds,
            Duration actualDuration,
            Solution solution
    );
}
