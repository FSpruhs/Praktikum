package de.fernuni.kurs01584.ss23.application.ports.in;

import java.io.File;

/**
 * Port to create a new snake hunt instance.
 */
public interface CreateSnakeHuntInPort {

    /**
     * Creates a new snake hunt instance.
     */
    void create(File output);

}
