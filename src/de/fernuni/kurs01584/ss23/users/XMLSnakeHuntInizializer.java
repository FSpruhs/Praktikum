package de.fernuni.kurs01584.ss23.users;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidDataException;
import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.SnakeHuntInstance;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.Solution;
import de.fernuni.kurs01584.ss23.domain.ports.in.*;

public class XMLSnakeHuntInizializer {
	
	private static final Logger log = Logger.getLogger(XMLSnakeHuntInizializer.class.getName());
	
	private final String xmlFilePath;
	private SnakeHuntInstance snakeHuntInstance;
	
	public XMLSnakeHuntInizializer(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
		inizializeSnakeHuntInstance();
	}

	private void inizializeSnakeHuntInstance() {
		XMLSnakeHuntReader xmlSnakeHuntReader = new XMLSnakeHuntReader(xmlFilePath); 
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
	
	public EvaluateSolutionInPort getEvaluateSolutionInPort() {
		return snakeHuntInstance;
	}
	
	public ShowJungleInPort getShowJungleInPort() {
		return snakeHuntInstance;
	}
	
	public ShowSolutionInPort getShowSolutionInPort() {
		return snakeHuntInstance;
	}
	
	public SolveInPort getSolveInPort() {
		return snakeHuntInstance;
	}
	
	public ValidationInPort getValidationInPort() {
		return snakeHuntInstance;
	}

	public ShowSnakeTypesInPort getShowSnakeTypeInPort() {
		return snakeHuntInstance;
	}
}

