package de.fernuni.kurs01584.ss23.users;

import java.util.List;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.domain.ports.in.EvaluateSolutionInPort;
import de.fernuni.kurs01584.ss23.domain.ports.in.ShowJungleInPort;
import de.fernuni.kurs01584.ss23.domain.ports.in.ShowSolutionInPort;
import de.fernuni.kurs01584.ss23.domain.ports.in.SolveInPort;
import de.fernuni.kurs01584.ss23.domain.ports.in.ValidationInPort;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;


public class CLIAdapter {

	private static final Logger log = Logger.getLogger(CLIAdapter.class.getName());
	
	private String procedure;
	private String input;
	private String output;
	private EvaluateSolutionInPort evaluateSolutionInPort;
	private ShowSolutionInPort showSolutionInPort;
	private ShowJungleInPort showJungleInPort;
	private SolveInPort solveInPort;
	private ValidationInPort validationInPort;
	
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
		solveInPort = xmlSnakeHuntInizializer.getSolveInPort();
		validationInPort = xmlSnakeHuntInizializer.getValidationInPort();
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
		// TODO Auto-generated method stub
	}

	private void evaluateSolution() {
		// TODO Auto-generated method stub
	}

	private void validateInstance() {
		List<Fehlertyp> errorTypes = validationInPort.isValid();
		StringBuilder answer = new StringBuilder();
		if (errorTypes.isEmpty()) {
			answer.append("Snake hunt solution is valid.");
		} else {
			answer.append("Snake hunt solution is not valid.\nTotal errors: %s".formatted(errorTypes.size()));
			answer.append("\nFollowing errors found in Solution: ");
			for (Fehlertyp fehlertyp : errorTypes) {
				answer.append("%s ".formatted(fehlertyp.toString()));
			}
		}
		System.out.println(answer);
	}

	private void createInstance() {
		// TODO Auto-generated method stub
	}

	private void solveInstance() {
		// TODO Auto-generated method stub
	}

	public static void main(String[] args) {
		
		CLIAdapter cliAdapter = new CLIAdapter();
		cliAdapter.readCliArgs(args);
		cliAdapter.loadInPorts();
		cliAdapter.runProcedure();

	}
	
}
