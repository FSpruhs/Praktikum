package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.application.ports.in.ValidationInPort;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;
import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ValidationUseCase implements ValidationInPort {

    private final Solution solution;
    private final Jungle jungle;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;

    public ValidationUseCase(Solution solution, Jungle jungle, Map<SnakeTypeId, SnakeType> snakeTypes) {
        this.solution = solution;
        this.jungle = jungle;
        this.snakeTypes = snakeTypes;
    }

    @Override
    public List<SchlangenjagdAPI.Fehlertyp> isValid() {
        List<SchlangenjagdAPI.Fehlertyp> result = new LinkedList<>();
        solutionNullCheck();
        for (Snake snake : solution.snakes()) {
            findSnakeErrors(result, snake);
        }
        jungle.removeAllSnakeParts();
        return result;
    }

    private void findSnakeErrors(List<SchlangenjagdAPI.Fehlertyp> result, Snake snake) {
        findLengthError(result, snake);
        SnakePart previousSnakePart = null;
        for (SnakePart snakePart : snake.snakeParts()) {
            jungle.placeSnakePart(snakePart);
            findUsageError(result, snakePart);
            findAllocationError(result, snakePart);
            findNeighborhoodError(result, snakePart, previousSnakePart, snakeTypes.get(snake.snakeTypeId()));
            previousSnakePart = snakePart;
        }
    }

    private void solutionNullCheck() {
        if (solution == null) {
            throw new NoSolutionException();
        }
    }

    private SnakeType getSnakeType(SnakeTypeId snakeTypeId) {
        if (snakeTypes.get(snakeTypeId) == null) {
            throw new InvalidSnakeTypesException("Snake Type with value %s does not exist!".formatted(snakeTypeId));
        }
        return snakeTypes.get(snakeTypeId);
    }

    private void findLengthError(List<SchlangenjagdAPI.Fehlertyp> result, Snake snake) {
        if (getSnakeType(snake.snakeTypeId()).getSnakeLength() != snake.getLength()) {
            result.add(SchlangenjagdAPI.Fehlertyp.GLIEDER);
        }
    }

    private void findNeighborhoodError(List<SchlangenjagdAPI.Fehlertyp> result, SnakePart snakePart, SnakePart previousSnakePart, SnakeType snakeType) {
        if (previousSnakePart != null && snakeType.isNotSuccessor(snakePart, previousSnakePart)) {
            result.add(SchlangenjagdAPI.Fehlertyp.NACHBARSCHAFT);
        }
    }

    private void findAllocationError(List<SchlangenjagdAPI.Fehlertyp> result, SnakePart snakePart) {
        if (jungle.getJungleFieldSign(snakePart.coordinate()) != snakePart.character()) {
            result.add(SchlangenjagdAPI.Fehlertyp.ZUORDNUNG);
        }

    }

    private void findUsageError(List<SchlangenjagdAPI.Fehlertyp> result, SnakePart snakePart) {
        if (jungle.getJungleFieldUsability(snakePart.coordinate()) < 0) {
            result.add(SchlangenjagdAPI.Fehlertyp.VERWENDUNG);
        }
    }
}
