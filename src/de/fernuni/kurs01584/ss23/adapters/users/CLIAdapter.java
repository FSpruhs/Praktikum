package de.fernuni.kurs01584.ss23.adapters.users;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.application.ports.in.*;
import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;


public class CLIAdapter {

	private static final Logger log = Logger.getLogger(CLIAdapter.class.getName());
	private static final String SEPARATOR = "-------------------------- %s --------------------------%n";

	private String procedure;
	private File input;
	private File output;
	private EvaluateSolutionInPort evaluateSolutionInPort;
	private ShowSnakeHuntIntPort showSnakeHuntIntPort;
	private SolveInPort solveInPort;
	private ValidationInPort validationInPort;
	private CreateSnakeHuntInPort createSnakeHuntInPort;

	private void readCliArgs(String[] args) {
		validateCLIArgs(args);
		readProcedure(args);
		readInput(args);
		readOutput(args);
		validateOutput();
	}

	private void validateOutput() {
		if (isSolveWithoutOutput() || isCreateWithoutOutput()) {
			log.warning("Parameter \"ausgabe\" is required with procedure l and e.");
			System.exit(0);
		}
	}

	private boolean isCreateWithoutOutput() {
		return procedure.contains("e") && output == null;
	}

	private boolean isSolveWithoutOutput() {
		return procedure.contains("l") && output == null;
	}

	private void readOutput(String[] args) {
		if (args.length >= 3) {
			output = new File(args[2].substring(8));
			log.info("Output: %s.".formatted(output));
		}
	}

	private void readInput(String[] args) {
		input = new File(args[1].substring(8));
		log.info("Input: %s.".formatted(input));
	}

	private void readProcedure(String[] args) {
		procedure = args[0].substring(7);
		log.info("Procedure: %s.".formatted(procedure));
	}

	private void validateCLIArgs(String[] args) {
		if (args.length < 2) {
			log.warning("\"ablauf\" and \"eingabe\" parameter required.");
			System.exit(0);
		}

		if (!args[0].startsWith("ablauf")) {
			log.warning("Parameter \"ablauf\" is required.");
			System.exit(0);
		}

		if (!args[1].startsWith("eingabe")) {
			log.warning("Parameter \"eingabe\" is required.");
			System.exit(0);
		}
	}

	public void loadInPorts() {
		SnakeHuntInitializer snakeHuntInizializer = new SnakeHuntInitializer(input);
		evaluateSolutionInPort = snakeHuntInizializer.getEvaluateSolutionInPort();
		showSnakeHuntIntPort = snakeHuntInizializer.getShowSnakeHuntInPort();
		solveInPort = snakeHuntInizializer.getSolveInPort();
		validationInPort = snakeHuntInizializer.getValidationInPort();
		createSnakeHuntInPort = snakeHuntInizializer.getCreateSnakeHuntInPort();
	}


	private void runProcedure() {
		for (char command : procedure.toCharArray()) {
			switch (command) {
				case 'l' -> solveInstance();
				case 'e' -> createInstance();
				case 'p' -> validateInstance();
				case 'b' -> evaluateSolution();
				case 'd' -> showInstance();
				default -> {
					log.warning("Unknown command %s.".formatted(command));
					System.exit(0);
				}
			}
		}

	}

	private void showInstance() {
		printJungleData(showSnakeHuntIntPort.showJungle());
		printSnakeTypes();
		printJungleFields(showSnakeHuntIntPort.showJungle());
		if (showSnakeHuntIntPort.showSolution() != null) {
			printSolution(showSnakeHuntIntPort.showJungle().getJungleSize());
		}
	}

	private void printSolution(JungleSize jungleSize) {
		System.out.printf(SEPARATOR, "Solution");
		for (Snake snake : showSnakeHuntIntPort.showSolution().getSnakes()) {
			printSolutionData(snake);
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

	private void printSolutionData(Snake snake) {
		System.out.printf("SnakeType: %s%n", snake.snakeTypeId().value());
		System.out.printf("Character band: %s%n", showSnakeHuntIntPort.showSnakeTypesById(snake.snakeTypeId()).characterBand());
		System.out.printf("Neighborhood Structure: %s%n", showSnakeHuntIntPort.showSnakeTypesById(snake.snakeTypeId()).neighborhoodStructure().getName());
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
			System.out.printf("| %s \033[1m%s\033[0m %s ", jungleField.getFieldValue(), jungleField.getCharacter(), jungleField.getUsability());
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
		for (SnakeType snakeType : showSnakeHuntIntPort.showSnakeTypes()) {
			System.out.printf("Snake Type: %s%n", snakeType.snakeTypeId());
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

	private void evaluateSolution() {
		System.out.printf("Total points of the Solution are: %s%n", evaluateSolutionInPort.evaluateTotalPoints());
	}

	private void validateInstance() {
		List<Fehlertyp> errorTypes = validationInPort.isValid();
		StringBuilder result = new StringBuilder();
		if (errorTypes.isEmpty()) {
			result.append("Snake hunt solution is valid.");
		} else {
			createErrorMessage(errorTypes, result);
		}
		System.out.println(result);
	}

	private void createErrorMessage(List<Fehlertyp> errorTypes, StringBuilder result) {
		result.append("Snake hunt solution is not valid.%nTotal errors: %s".formatted(errorTypes.size()));
		result.append("\nFollowing errors found in Solution: ");
		for (Fehlertyp fehlertyp : errorTypes) {
			result.append("%s ".formatted(fehlertyp.toString()));
		}
	}

	private void createInstance() {
		createSnakeHuntInPort.create(output);
	}

	private void solveInstance() {
		solveInPort.solveSnakeHuntInstance(output);
	}

	public static void main(String[] args) {

		CLIAdapter cliAdapter = new CLIAdapter();
		cliAdapter.readCliArgs(args);
		cliAdapter.loadInPorts();
		cliAdapter.runProcedure();

	}

}
