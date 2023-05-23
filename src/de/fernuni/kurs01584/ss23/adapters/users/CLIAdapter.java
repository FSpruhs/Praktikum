package de.fernuni.kurs01584.ss23.adapters.users;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.application.*;
import de.fernuni.kurs01584.ss23.application.ports.in.*;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;

/**
 * Adapter for the CLI.
 */
public class CLIAdapter {

	private static final Logger log = Logger.getLogger(CLIAdapter.class.getName());
	private static final char COMMAND_SOLVE = 'l';
	private static final char COMMAND_CREATE = 'e';
	private static final char COMMAND_VALIDATE = 'p';
	private static final char COMMAND_EVALUATE = 'b';
	private static final char COMMAND_SHOW = 'd';
	private static final String ARGUMENT_INPUT = "eingabe";
	private static final String ARGUMENT_OUTPUT = "ausgabe";
	private static final String ARGUMENT_COMMANDS = "ablauf";
	private static final int COMMAND_INDEX = 7;
	private static final int INPUT_INDEX = 8;
	private static final int OUTPUT_INDEX = 8;
	private static final int COMMAND_POSITION = 0;
	private static final int INPUT_POSITION = 1;
	private static final int OUTPUT_POSITION = 2;

	private String procedure;
	private File input;
	private File output;

	private void readCliArgs(String[] args) {
		validateCLIArgs(args);
		readProcedure(args);
		readInput(args);
		readOutput(args);
		validateOutput();
	}

	private void validateOutput() {
		if (isSolveWithoutOutput() || isCreateWithoutOutput()) {
			log.warning("Parameter \"%s\" is required with procedure %s and %s.".formatted(
					ARGUMENT_OUTPUT,
					COMMAND_CREATE,
					COMMAND_SOLVE
			));
			System.exit(0);
		}
	}

	private boolean isCreateWithoutOutput() {
		return procedure.contains(String.valueOf(COMMAND_CREATE)) && outputIsMissing();
	}


	private boolean isSolveWithoutOutput() {
		return procedure.contains(String.valueOf(COMMAND_SOLVE)) && outputIsMissing();
	}

	private boolean outputIsMissing() {
		return output == null;
	}

	private void readOutput(String[] args) {
		if (args.length >= 3) {
			output = new File(args[OUTPUT_POSITION].substring(OUTPUT_INDEX));
			log.info("Output: %s.".formatted(output));
		}
	}

	private void readInput(String[] args) {
		input = new File(args[INPUT_POSITION].substring(INPUT_INDEX));
		log.info("Input: %s.".formatted(input));
	}

	private void readProcedure(String[] args) {
		procedure = args[COMMAND_POSITION].substring(COMMAND_INDEX);
		log.info("Procedure: %s.".formatted(procedure));
	}

	private void validateCLIArgs(String[] args) {
		if (args.length < 2) {
			log.warning("\"%s\" and \"%s\" parameter required.".formatted(ARGUMENT_COMMANDS, ARGUMENT_INPUT));
			System.exit(0);
		}

		if (!args[COMMAND_POSITION].startsWith(ARGUMENT_COMMANDS)) {
			log.warning("Parameter \"%s\" is required.".formatted(ARGUMENT_COMMANDS));
			System.exit(0);
		}

		if (!args[INPUT_POSITION].startsWith(ARGUMENT_INPUT)) {
			log.warning("Parameter \"%s\" is required.".formatted(ARGUMENT_INPUT));
			System.exit(0);
		}
	}

	private void loadSnakeHuntInstance() {
		SnakeHuntInitializer.initialize(input);
	}


	private void runProcedure() {
		for (char command : procedure.toCharArray()) {
			switch (command) {
				case COMMAND_SOLVE -> solveInstance();
				case COMMAND_CREATE -> createInstance();
				case COMMAND_VALIDATE -> validateInstance();
				case COMMAND_EVALUATE -> evaluateSolution();
				case COMMAND_SHOW -> showInstance();
				default -> {
					log.warning("Unknown command %s.".formatted(command));
					System.exit(0);
				}
			}
		}
	}

	private void showInstance() {
		ShowSnakeHuntIntPort showSnakeHuntIntPort = new ShowSnakeHuntUseCase();
		SnakeHuntCLIPrinter snakeHuntCLIPrinter = new SnakeHuntCLIPrinter(
				showSnakeHuntIntPort.showJungle(),
				showSnakeHuntIntPort.showSolution(),
				showSnakeHuntIntPort.showSnakeTypes()
		);
		snakeHuntCLIPrinter.print();
	}

	private void evaluateSolution() {
		EvaluateSolutionInPort evaluateSolutionInPort = new EvaluateSolutionUseCase();
		System.out.printf("Total points of the Solution are: %s%n", evaluateSolutionInPort.evaluateTotalPoints());
	}

	private void validateInstance() {
		ValidationInPort validationInPort = new ValidationUseCase();
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
		CreateSnakeHuntInPort createSnakeHuntInPort = new CreateUseCase();
		createSnakeHuntInPort.create(output);
	}

	private void solveInstance() {
		SolveInPort solveInPort = new SolveUseCase();
		solveInPort.solveSnakeHuntInstance(output);
	}

	/**
	 * Entry point for the CLI. The program can be operated at startup with the following commands:
	 * 'l' solves the snake hunt instance. Requires a path where the output file should be stored.
	 * 'e' creates a new jungle. Requires a path where to save the output file.
	 * 'p' checks the correctness of the snake hunt instance.
	 * 'b' calculates the value of the current solution of the snake hunt instance.
	 * 'd' prints a graphical version of the current snake hunt instance.
	 * The commands are passed when starting the CLI and can be combined arbitrarily.
	 *
	 * @param args Requires as first argument the command 'ablauf=' with a combination of the commands.
	 *                Requires as second 'eingabe=' with the path to the snake hunt instance that should be loaded.
	 *                Can optionally pass the third argument 'ausgabe='.
	 *                This contains the path to the file for which the output should be stored.
	 *                Is mandatory for the commands 'l' and 'e'.
	 */

	public static void main(String[] args) {
		CLIAdapter cliAdapter = new CLIAdapter();
		cliAdapter.readCliArgs(args);
		cliAdapter.loadSnakeHuntInstance();
		cliAdapter.runProcedure();
	}

}
