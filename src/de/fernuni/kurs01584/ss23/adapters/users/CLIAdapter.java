package de.fernuni.kurs01584.ss23.adapters.users;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.application.*;
import de.fernuni.kurs01584.ss23.application.ports.in.*;
import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;


public class CLIAdapter {

	private static final Logger log = Logger.getLogger(CLIAdapter.class.getName());

	private String procedure;
	private File input;
	private File output;
	private SnakeHuntInstance snakeHuntInstance;

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

	public void loadSnakeHuntInstance() {
		SnakeHuntInitializer snakeHuntInitializer = new SnakeHuntInitializer(input);
		snakeHuntInstance = snakeHuntInitializer.getSnakeHuntInstance();
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
		ShowSnakeHuntIntPort showSnakeHuntIntPort = new ShowSnakeHuntUseCase(
				snakeHuntInstance.getJungle(),
				snakeHuntInstance.getSolution(),
				snakeHuntInstance.getSnakeTypes()
		);
		SnakeHuntPrinter snakeHuntPrinter = new SnakeHuntPrinter(
				showSnakeHuntIntPort.showJungle(),
				showSnakeHuntIntPort.showSolution(),
				showSnakeHuntIntPort.showSnakeTypes()
		);
		snakeHuntPrinter.print();
	}

	private void evaluateSolution() {
		EvaluateSolutionInPort evaluateSolutionInPort = new EvaluateSolutionUseCase(
				snakeHuntInstance.getSolution(),
				snakeHuntInstance.getJungle(),
				snakeHuntInstance.getSnakeTypes()
		);
		System.out.printf("Total points of the Solution are: %s%n", evaluateSolutionInPort.evaluateTotalPoints());
	}

	private void validateInstance() {
		ValidationInPort validationInPort = new ValidationUseCase(
				snakeHuntInstance.getSolution(),
				snakeHuntInstance.getJungle(),
				snakeHuntInstance.getSnakeTypes()
		);
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
		snakeHuntInstance.setSolution(null);
		CreateSnakeHuntInPort createSnakeHuntInPort = new CreateUseCase(
				snakeHuntInstance.getJungle(),
				snakeHuntInstance.getSnakeTypes(),
				snakeHuntInstance.getTargetDuration()
		);
		createSnakeHuntInPort.create();
		snakeHuntInstance.setTargetDuration(createSnakeHuntInPort.getTargetDuration());
		snakeHuntInstance.save(output);
	}

	private void solveInstance() {
		SolveInPort solveInPort = new SolveUseCase(
				snakeHuntInstance.getJungle(),
				snakeHuntInstance.getTargetDuration(),
				snakeHuntInstance.getSnakeTypes()
		);
		snakeHuntInstance.setSolution(solveInPort.solveSnakeHuntInstance());
		snakeHuntInstance.setActualDuration(solveInPort.getActualDuration());
		snakeHuntInstance.save(output);
	}

	public static void main(String[] args) {
		CLIAdapter cliAdapter = new CLIAdapter();
		cliAdapter.readCliArgs(args);
		cliAdapter.loadSnakeHuntInstance();
		cliAdapter.runProcedure();
	}

}
