package de.fernuni.kurs01584.ss23.users;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.domain.ports.in.*;
import de.fernuni.kurs01584.ss23.domain.ports.out.SaveSnakeHuntInstanceOutPort;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;


public class CLIAdapter {

	private static final Logger log = Logger.getLogger(CLIAdapter.class.getName());

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

		procedure = args[0].substring(7);
		input = args[1].substring(8);
		log.info("Procedure: %s.".formatted(procedure));
		log.info("Input: %s.".formatted(input));
		if (args.length >= 3) {
			output = args[2].substring(8);
			log.info("Output: %s.".formatted(output));
		}
		if ((procedure.contains("l") && output == null) || (procedure.contains("e") && output == null) ) {
			log.warning("Parameter \"ausgabe\" is required with procedure l and e.");
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
		Jungle jungle = showJungleInPort.showJungle();
		JungleSize jungleSize = jungle.getJungleSize();
		System.out.println("------------------------Jungle data------------------------");
		System.out.printf("Rows: %s%n", jungleSize.rows());
		System.out.printf("Columns: %s%n", jungleSize.columns());
		System.out.printf("Character band: %s%n", jungle.getCharacters());
		System.out.println();
		System.out.println("------------------------Snake Types------------------------");
		for (SnakeType snakeType : showSnakeTypesInPort.showSnakeTypes()) {
			System.out.printf("Snake Type: %s%n", snakeType.getId());
			System.out.printf("Character band: %s%n", snakeType.getCharacterBand());
			System.out.printf("Neighborhood Structure: %s%n", snakeType.getNeighborhoodStructure().getName());
			System.out.printf("Value: %s%n", snakeType.getSnakeValue());
			System.out.printf("Count: %s%n", snakeType.getCount());
			System.out.println();
		}
		System.out.println("------------------------Jungle Fields------------------------");
		System.out.println("(Value, Character, Usability)\n");
		for (int i = 0; i < jungleSize.rows() ; i++) {
			System.out.print("+-------".repeat(jungleSize.columns()));
			System.out.println("+");
			System.out.print("|       ".repeat(jungleSize.columns()));
			System.out.println("|");
			for (int j = 0; j < jungleSize.columns(); j++) {
				JungleField jungleField = jungle.getJungleField(new Coordinate(i, j));
				System.out.printf("| %s \033[1m%s\033[0m %s ", jungleField.getFieldValue(), jungleField.getCharacter(), jungleField.getUsability());
			}
			System.out.println("|");
			System.out.print("|       ".repeat(jungleSize.columns()));
			System.out.println("|");
		}
		System.out.print("+-------".repeat(jungleSize.columns()));
		System.out.println("+");
		if (showSolutionInPort.showSolution() != null) {
			System.out.println("\n------------------------Solution------------------------");
			for (Snake snake : showSolutionInPort.showSolution().getSnakes()) {
				String[][] result = new String[jungleSize.rows()][jungleSize.columns()];
				Arrays.stream(result).forEach(row -> Arrays.fill(row, " . "));
				System.out.printf("SnakeType: %s%n", snake.getSnakeTypeId());
				System.out.printf("Character band: %s%n", showSnakeTypesInPort.showSnakeTypesById(snake.getSnakeTypeId()).getCharacterBand());
				System.out.printf("Neighborhood Structure: %s%n", showSnakeTypesInPort.showSnakeTypesById(snake.getSnakeTypeId()).getNeighborhoodStructure().getName());
				System.out.printf("Snake Length: %s%n", snake.getSnakeParts().size());
				int counter = 1;
				System.out.print("Snakeparts: ");
				for (SnakePart snakePart : snake.getSnakeParts()) {
					System.out.printf("(%s, %s, %s)", snakePart.coordinate().row(), snakePart.character(), snakePart.coordinate().column());
					if (counter != snake.getSnakeParts().size()) {
						System.out.print(" -> ");
					}
					if (counter % 10 == 0) {
						System.out.println();
					}
					result[snakePart.coordinate().row()][snakePart.coordinate().column()] = counter < 10 ? " " + counter + " " :" " + counter;
					counter++;
				}
				System.out.printf("\nSnake Length: %s%n", snake.getSnakeParts().size());
				for (int i = 0; i < jungleSize.rows() ; i++) {
					for (int j = 0; j < jungleSize.columns() ; j++) {
						System.out.print(result[i][j]);
					}
					System.out.println();
				}
				System.out.println();
			}

		}
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
