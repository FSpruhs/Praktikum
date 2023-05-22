package de.fernuni.kurs01584.ss23.application.ports.in;

import java.time.Duration;

/**
 * Port to create a new snake hunt instance.
 */
public interface CreateSnakeHuntInPort {

    /**
     * Creates a new snake hunt instance.
     */
    void create();

    /**
     * Returns the target duration in which the snake hunt should be solved.
     *
     * @return the target duration.
     */
    Duration getTargetDuration();
}
