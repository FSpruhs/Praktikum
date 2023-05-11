package de.fernuni.kurs01584.ss23.adapters.users;

import de.fernuni.kurs01584.ss23.domain.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SnakeHuntPrinter {

    private static final String SEPARATOR = "-------------------------- %s --------------------------%n";

    private final Solution solution;
    private final Jungle jungle;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;

    public SnakeHuntPrinter(Jungle jungle, Solution solution, Map<SnakeTypeId, SnakeType> snakeTypes) {
        this.solution = solution;
        this.jungle = jungle;
        this.snakeTypes = snakeTypes;
    }

    public void print() {
        printJungleData(jungle);
        printSnakeTypes(snakeTypes.values().stream().toList());
        printJungleFields(jungle);
        if (solution != null) {
            printSolution(jungle.getJungleSize(), solution, snakeTypes);
        }
    }

    private void printSolution(JungleSize jungleSize, Solution solution, Map<SnakeTypeId, SnakeType> snakeTypes) {
        System.out.printf(SEPARATOR, "Solution");
        for (Snake snake : solution.snakes()) {
            printSolutionData(snake, snakeTypes);
            String[][] solutionGrid = initializeSolutionGrid(jungleSize);
            printSnakeParts(snake, solutionGrid);
            printSolutionGrid(jungleSize, solutionGrid);
        }
    }

    private void printSnakeParts(Snake snake, String[][] solutionGrid) {
        System.out.print("Snakeparts: ");
        int counter = 1;
        for (SnakePart snakePart : snake.snakeParts()) {
            printSnakePart(snake, counter, snakePart);
            solutionGrid[snakePart.coordinate().row()][snakePart.coordinate().column()] = counter < 10 ? " " + counter + " " :" " + counter;
            counter++;
        }
        System.out.println();
    }

    private void printSnakePart(Snake snake, int counter, SnakePart snakePart) {
        System.out.printf("(%s, %s, %s)", snakePart.coordinate().row(), snakePart.character(), snakePart.coordinate().column());
        if (counter != snake.snakeParts().size()) {
            System.out.print(" -> ");
        }
        if (counter % 10 == 0) {
            System.out.println();
        }
    }

    private void printSolutionGrid(JungleSize jungleSize, String[][] solutionGrid) {
        for (int row = 0; row < jungleSize.rows() ; row++) {
            for (int column = 0; column < jungleSize.columns() ; column++) {
                System.out.print(solutionGrid[row][column]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private String[][] initializeSolutionGrid(JungleSize jungleSize) {
        String[][] result = new String[jungleSize.rows()][jungleSize.columns()];
        Arrays.stream(result).forEach(row -> Arrays.fill(row, " . "));
        return result;
    }

    private void printSolutionData(Snake snake, Map<SnakeTypeId, SnakeType> snakeTypes) {
        System.out.printf("SnakeType: %s%n", snake.snakeTypeId().value());
        System.out.printf("Character band: %s%n", snakeTypes.get(snake.snakeTypeId()).characterBand());
        System.out.printf("Neighborhood Structure: %s%n", snakeTypes.get(snake.snakeTypeId()).neighborhoodStructure().getName());
        System.out.printf("Snake Length: %s%n", snake.snakeParts().size());
    }

    private void printJungleFields(Jungle jungle) {
        System.out.printf(SEPARATOR, "Jungle Fields");
        System.out.println("(Value, Character, Usability)\n");
        printGrid(jungle);
    }

    private void printGrid(Jungle jungle) {
        for (int row = 0; row < jungle.getJungleSize().rows() ; row++) {
            printGridTop(jungle.getJungleSize().columns());
            printGridValue(jungle, row);
            printGridBottom(jungle.getJungleSize().columns());
        }
        System.out.print("+-------".repeat(jungle.getJungleSize().columns()));
        System.out.println("+\n");
    }

    private void printGridValue(Jungle jungle, int row) {
        for (int column = 0; column < jungle.getJungleSize().columns(); column++) {
            JungleField jungleField = jungle.getJungleField(new Coordinate(row, column));
            System.out.printf("| %s \033[1m%s\033[0m %s ", jungleField.fieldValue(), jungleField.character(), jungleField.getUsability());
        }
    }

    private void printGridBottom(int columns) {
        System.out.println("|");
        System.out.print("|       ".repeat(columns));
        System.out.println("|");
    }

    private void printGridTop(int columns) {
        System.out.print("+-------".repeat(columns));
        System.out.println("+");
        System.out.print("|       ".repeat(columns));
        System.out.println("|");
    }

    private void printSnakeTypes(List<SnakeType> snakeTypes) {
        System.out.printf(SEPARATOR, "Snake Types");
        for (SnakeType snakeType : snakeTypes) {
            System.out.printf("Snake Type: %s%n", snakeType.snakeTypeId().value());
            System.out.printf("Character band: %s%n", snakeType.characterBand());
            System.out.printf("Neighborhood Structure: %s%n", snakeType.neighborhoodStructure().getName());
            System.out.printf("Value: %s%n", snakeType.snakeValue());
            System.out.printf("Count: %s%n%n", snakeType.count());
        }
    }

    private void printJungleData(Jungle jungle) {
        System.out.printf(SEPARATOR, "Jungle data");
        System.out.printf("Rows: %s%n", jungle.getJungleSize().rows());
        System.out.printf("Columns: %s%n", jungle.getJungleSize().rows());
        System.out.printf("Character band: %s%n%n", jungle.getCharacters());
    }
}
