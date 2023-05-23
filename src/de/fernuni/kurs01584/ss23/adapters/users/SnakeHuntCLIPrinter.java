package de.fernuni.kurs01584.ss23.adapters.users;

import de.fernuni.kurs01584.ss23.domain.model.*;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Creates an optical version of the snake chase instance and outputs it to the CLI.
 */

public class SnakeHuntCLIPrinter {

    private static final String SEPARATOR = "-------------------------- %s --------------------------%n";

    private final List<SnakeDTO> snakes;
    private final Jungle jungle;
    private final List<SnakeTypeDTO> snakeTypes;

    /**
     * Constructor for a snake hunt CLI printer.
     *
     * @param jungle the jungle to be printed.
     * @param snakes the list of snakes of the solution to be printed.
     * @param snakeTypes the snake types to be printed.
     */
    public SnakeHuntCLIPrinter(Jungle jungle, List<SnakeDTO> snakes, List<SnakeTypeDTO> snakeTypes) {
        this.snakes = snakes;
        this.jungle = jungle;
        this.snakeTypes = snakeTypes;
    }

    /**
     * Generates the output of the passed jungle, solution and snake types on the CLI
     */
    public void print() {
        printJungleData();
        printSnakeTypes();
        printJungleFields();
        if (!snakes.isEmpty()) {
            printSolution(jungle.getJungleSize());
        }
    }

    private void printSolution(JungleSize jungleSize) {
        System.out.printf(SEPARATOR, "Solution");
        for (SnakeDTO snake : snakes) {
            printSolutionData(snake);
            String[][] solutionGrid = initializeSolutionGrid(jungleSize);
            printSnakeParts(snake, solutionGrid);
            printSolutionGrid(jungleSize, solutionGrid);
        }
    }

    private void printSnakeParts(SnakeDTO snake, String[][] solutionGrid) {
        System.out.print("Snakeparts: ");
        int counter = 1;
        for (SnakePartDTO snakePart : snake.snakeParts()) {
            printSnakePart(snake, counter, snakePart);
            solutionGrid[snakePart.row()][snakePart.column()] = counter < 10 ? " " + counter + " " :" " + counter;
            counter++;
        }
        System.out.println();
    }

    private void printSnakePart(SnakeDTO snake, int counter, SnakePartDTO snakePart) {
        System.out.printf("(%s, %s, %s)", snakePart.row(), snakePart.character(), snakePart.column());
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

    private void printSolutionData(SnakeDTO snake) {
        System.out.printf("SnakeType: %s%n", snake.snakeTypeId());
        System.out.printf("Character band: %s%n", getSnakeTypeById(snake.snakeTypeId()).characterBand());
        System.out.printf("Neighborhood Structure: %s%n", getSnakeTypeById(snake.snakeTypeId()).neighborhoodStructure());
        System.out.printf("Snake Length: %s%n", snake.snakeParts().size());
    }

    private SnakeTypeDTO getSnakeTypeById(String snakeTypeId) {
        return snakeTypes.stream()
                .filter(snakeTypeDTO -> snakeTypeDTO.snakeTypeId().equals(snakeTypeId))
                .findFirst()
                .orElseThrow();
    }

    private void printJungleFields() {
        System.out.printf(SEPARATOR, "Jungle Fields");
        System.out.println("(Value, Character, Usability)\n");
        printGrid();
    }

    private void printGrid() {
        for (int row = 0; row < jungle.getJungleSize().rows() ; row++) {
            printGridTop(jungle.getJungleSize().columns());
            printGridValue(row);
            printGridBottom(jungle.getJungleSize().columns());
        }
        System.out.print("+-------".repeat(jungle.getJungleSize().columns()));
        System.out.println("+\n");
    }

    private void printGridValue(int row) {
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

    private void printSnakeTypes() {
        System.out.printf(SEPARATOR, "Snake Types");
        for (SnakeTypeDTO snakeType : snakeTypes) {
            System.out.printf("Snake Type: %s%n", snakeType.snakeTypeId());
            System.out.printf("Character band: %s%n", snakeType.characterBand());
            System.out.printf("Neighborhood Structure: %s%n", snakeType.neighborhoodStructure());
            System.out.printf("Value: %s%n", snakeType.snakeValue());
            System.out.printf("Count: %s%n%n", snakeType.count());
        }
    }

    private void printJungleData() {
        System.out.printf(SEPARATOR, "Jungle data");
        System.out.printf("Rows: %s%n", jungle.getJungleSize().rows());
        System.out.printf("Columns: %s%n", jungle.getJungleSize().columns());
        System.out.printf("Character band: %s%n%n", jungle.getCharacters());
    }
}
