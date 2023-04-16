package de.fernuni.kurs01584.ss23.users;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidDataException;
import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.SnakeHuntInstance;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.Solution;

public class CLIAdapter {

	private static final Logger log = Logger.getLogger(CLIAdapter.class.getName());
	
	private String procedure;
	private String input;
	private String output;
	private SnakeHuntInstance snakeHuntInstance;
	
	private void readCliArgs(String[] args) {
		if (!args[0].substring(0, 6).equals("ablauf")) {
			log.warning("Parameter \"ablauf\" is requierd.");
			System.exit(0);
		}
		
		if (!args[1].substring(0, 7).equals("eingabe")) {
			log.warning("Parameter \"eingabe\" is requierd.");
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
			log.warning("Parameter \"ausgabe\" is requierd with prosedure l and e.");
			System.exit(0);
		}
	}
	
	private void inizializeSnakeHuntInstance() {
		XMLSnakeHuntReader xmlSnakeHuntReader = new XMLSnakeHuntReader(input); 
		try {
			Duration duration = xmlSnakeHuntReader.readDurationInSeconds();
			log.info("Duration inizialized: %s".formatted(duration.toNanos()));
			Jungle jungle = xmlSnakeHuntReader.readJungle();
			log.info("Jungle inizialized: %s".formatted(jungle));
			Map<String, SnakeType> snakeTypes = xmlSnakeHuntReader.readSnakeTypes();
			log.info("Snake Types inizialized: %s".formatted(snakeTypes));
			Solution solution = xmlSnakeHuntReader.readSolution();
			log.info("Solution is: %s".formatted(solution));
			if (solution == null) {
				snakeHuntInstance = new SnakeHuntInstance(jungle, snakeTypes, duration);
			} else {
				snakeHuntInstance = new SnakeHuntInstance(jungle, snakeTypes, duration, solution);
			}
		} catch (InvalidDataException e) {
			log.warning(e.getMessage());
			System.exit(0);
		}
	}
	
	private void runProcedure() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		
		CLIAdapter cliAdapter = new CLIAdapter();
		cliAdapter.readCliArgs(args);
		cliAdapter.inizializeSnakeHuntInstance();
		cliAdapter.runProcedure();
	

	}


	
}
