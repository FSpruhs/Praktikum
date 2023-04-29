package de.fernuni.kurs01584.ss23.users;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.domain.ports.in.*;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;


public class CLIAdapter {

	private static final Logger log = Logger.getLogger(CLIAdapter.class.getName());
	private static final String SEPARATOR = "-------------------------- %s --------------------------%n";

	private String procedure;
	private String input;
	private String output;
	private EvaluateSolutionInPort evaluateSolutionInPort;
	private ShowSolutionInPort showSolutionInPort;
	private ShowSnakeTypesInPort showSnakeTypesInPort;
	private ShowJungleInPort showJungleInPort;
	private SolveInPort solveInPort;
	private ValidationInPort validationInPort;
	private SaveSnakeHuntInstanceInPort saveSnakeHuntInstanceInPort;

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
			output = args[2].substring(8);
			log.info("Output: %s.".formatted(output));
		}
	}

	private void readInput(String[] args) {
		input = args[1].substring(8);
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
		XMLSnakeHuntInizializer xmlSnakeHuntInizializer = new XMLSnakeHuntInizializer(input);
		evaluateSolutionInPort = xmlSnakeHuntInizializer.getEvaluateSolutionInPort();
		showSolutionInPort = xmlSnakeHuntInizializer.getShowSolutionInPort();
		showJungleInPort = xmlSnakeHuntInizializer.getShowJungleInPort();
		showSnakeTypesInPort = xmlSnakeHuntInizializer.getShowSnakeTypeInPort();
		solveInPort = xmlSnakeHuntInizializer.getSolveInPort();
		validationInPort = xmlSnakeHuntInizializer.getValidationInPort();
		saveSnakeHuntInstanceInPort = xmlSnakeHuntInizializer.getSaveSnakeHuntInstanceInPort();
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
		printJungleData(showJungleInPort.showJungle());
		printSnakeTypes();
		printJungleFields(showJungleInPort.showJungle());
		if (showSolutionInPort.showSolution() != null) {
			printSolution(showJungleInPort.showJungle().getJungleSize());
		}
	}

	private void printSolution(JungleSize jungleSize) {
		System.out.printf(SEPARATOR, "Solution");
		for (Snake snake : showSolutionInPort.showSolution().getSnakes()) {
			printSolutionData(snake);
			String[][] solutionGrid = initializeSolutionGrid(jungleSize);
			printSnakeParts(snake, solutionGrid);
			printSolutionGrid(jungleSize, solutionGrid);
		}
	}

	private void printSnakeParts(Snake snake, String[][] solutionGrid) {
		System.out.print("Snakeparts: ");
		int counter = 1;
		for (SnakePart snakePart : snake.getSnakeParts()) {
			printSnakePart(snake, counter, snakePart);
			solutionGrid[snakePart.coordinate().row()][snakePart.coordinate().column()] = counter < 10 ? " " + counter + " " :" " + counter;
			counter++;
		}
		System.out.println();
	}

	private void printSnakePart(Snake snake, int counter, SnakePart snakePart) {
		System.out.printf("(%s, %s, %s)", snakePart.coordinate().row(), snakePart.character(), snakePart.coordinate().column());
		if (counter != snake.getSnakeParts().size()) {
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
		System.out.printf("SnakeType: %s%n", snake.getSnakeTypeId());
		System.out.printf("Character band: %s%n", showSnakeTypesInPort.showSnakeTypesById(snake.getSnakeTypeId()).getCharacterBand());
		System.out.printf("Neighborhood Structure: %s%n", showSnakeTypesInPort.showSnakeTypesById(snake.getSnakeTypeId()).getNeighborhoodStructure().getName());
		System.out.printf("Snake Length: %s%n", snake.getSnakeParts().size());
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
		for (SnakeType snakeType : showSnakeTypesInPort.showSnakeTypes()) {
			System.out.printf("Snake Type: %s%n", snakeType.getId());
			System.out.printf("Character band: %s%n", snakeType.getCharacterBand());
			System.out.printf("Neighborhood Structure: %s%n", snakeType.getNeighborhoodStructure().getName());
			System.out.printf("Value: %s%n", snakeType.getSnakeValue());
			System.out.printf("Count: %s%n%n", snakeType.getCount());
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
		// TODO Auto-generated method stub
	}

	private void solveInstance() {
		saveSnakeHuntInstanceInPort.save(new File(output));
	}

	public static void main(String[] args) {

		CLIAdapter cliAdapter = new CLIAdapter();
		cliAdapter.readCliArgs(args);
		cliAdapter.loadInPorts();
		cliAdapter.runProcedure();

	}

}
