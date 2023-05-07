package de.fernuni.kurs01584.ss23.application.ports.out;

import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.SnakeTypeId;
import de.fernuni.kurs01584.ss23.domain.model.Solution;

import java.io.File;
import java.time.Duration;
import java.util.Map;

public interface SaveSnakeHuntInstanceOutPort {
    void save(File file, Jungle jungle, Map<SnakeTypeId, SnakeType> snakeTypes, Duration durationInSeconds, Duration actualDuration, Solution solution);
}
