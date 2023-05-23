package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.application.ports.in.ValidationInPort;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;
import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI;

import java.util.LinkedList;
import java.util.List;

/**
 * Use case to validate the solution of the snake hunt instance.
 */
public class ValidationUseCase implements ValidationInPort {

    private final SnakeHunt snakeHunt;

    public ValidationUseCase() {
        this.snakeHunt = SnakeHunt.getInstance();
    }


    /**
     * Validates the solution and returns a list of the type 'Fehlertyp'.
     * For each error type found there is one entry in the list.
     *
     * @return list of error types.
     */
    @Override
    public List<SchlangenjagdAPI.Fehlertyp> isValid() {
        List<SchlangenjagdAPI.Fehlertyp> result = new LinkedList<>();
        solutionNullCheck();
        for (Snake snake : getSnakes()) {
            findSnakeErrors(result, snake);
        }
        snakeHunt.getJungle().removeAllSnakeParts();
        return result;
    }

    private List<Snake> getSnakes() {
        return snakeHunt.getSolution().snakes();
    }

    private void findSnakeErrors(List<SchlangenjagdAPI.Fehlertyp> result, Snake snake) {
        findLengthError(result, snake);
        SnakePart previousSnakePart = null;
        for (SnakePart snakePart : snake.snakeParts()) {
            snakeHunt.getJungle().placeSnakePart(snakePart);
            findUsageError(result, snakePart);
            findAllocationError(result, snakePart);
            findNeighborhoodError(result, snakePart, previousSnakePart, getSnakeTypeById(snake.snakeTypeId()));
            previousSnakePart = snakePart;
        }
    }

    private SnakeType getSnakeTypeById(SnakeTypeId snakeTypeId) {
        return snakeHunt.getSnakeTypes().get(snakeTypeId);
    }

    private void solutionNullCheck() {
        if (snakeHunt.getSolution() == null) {
            throw new NoSolutionException();
        }
    }

    private SnakeType getSnakeType(SnakeTypeId snakeTypeId) {
        if (getSnakeTypeById(snakeTypeId) == null) {
            throw new InvalidSnakeTypesException("Snake Type with value %s does not exist!".formatted(snakeTypeId));
        }
        return getSnakeTypeById(snakeTypeId);
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
        if (getJungleFieldSign(snakePart) != snakePart.character()) {
            result.add(SchlangenjagdAPI.Fehlertyp.ZUORDNUNG);
        }

    }

    private char getJungleFieldSign(SnakePart snakePart) {
        return snakeHunt.getJungle().getJungleFieldSign(snakePart.coordinate());
    }

    private void findUsageError(List<SchlangenjagdAPI.Fehlertyp> result, SnakePart snakePart) {
        if (getJungleFieldUsability(snakePart) < 0) {
            result.add(SchlangenjagdAPI.Fehlertyp.VERWENDUNG);
        }
    }

    private int getJungleFieldUsability(SnakePart snakePart) {
        return snakeHunt.getJungle().getJungleFieldUsability(snakePart.coordinate());
    }
}
