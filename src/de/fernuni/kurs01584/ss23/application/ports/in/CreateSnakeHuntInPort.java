package de.fernuni.kurs01584.ss23.application.ports.in;

import java.time.Duration;

public interface CreateSnakeHuntInPort {

    void create();
    Duration getTargetDuration();
}
