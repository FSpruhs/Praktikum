package de.fernuni.kurs01584.ss23.application.ports.in;

import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.SnakeTypeId;
import de.fernuni.kurs01584.ss23.domain.model.Solution;

import java.util.Map;

public interface ShowSnakeHuntIntPort {

    Jungle showJungle();
    Map<SnakeTypeId, SnakeType> showSnakeTypes();
    Solution showSolution();
}
