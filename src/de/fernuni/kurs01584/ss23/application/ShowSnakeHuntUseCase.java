package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.application.ports.in.ShowSnakeHuntIntPort;
import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.SnakeTypeId;
import de.fernuni.kurs01584.ss23.domain.model.Solution;

import java.util.Map;

public class ShowSnakeHuntUseCase implements ShowSnakeHuntIntPort {

    private final Jungle jungle;
    private final Solution solution;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;

    public ShowSnakeHuntUseCase(Jungle jungle, Solution solution, Map<SnakeTypeId, SnakeType> snakeTypes) {
        this.jungle = jungle;
        this.solution = solution;
        this.snakeTypes = snakeTypes;
    }

    @Override
    public Map<SnakeTypeId, SnakeType>  showSnakeTypes() {
        return snakeTypes;
    }

    @Override
    public Solution showSolution() {
        return solution;
    }

    @Override
    public Jungle showJungle() {
        return jungle;
    }
}
