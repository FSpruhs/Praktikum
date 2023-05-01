package de.fernuni.kurs01584.ss23.users;

import java.io.File;
import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidDataException;
import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.domain.ports.in.*;
import de.fernuni.kurs01584.ss23.infrastructure.SnakeHuntRepositoryAdapter;

public class XMLSnakeHuntInizializer {
	
	private static final Logger log = Logger.getLogger(XMLSnakeHuntInizializer.class.getName());
	
	private final File xmlFilePath;
	private SnakeHuntInstance snakeHuntInstance;
	
	public XMLSnakeHuntInizializer(File xmlFilePath) {
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
			Map<SnakeTypeId, SnakeType> snakeTypes = xmlSnakeHuntReader.readSnakeTypes();
			log.info("Snake Types inizialized: %s".formatted(snakeTypes));
			Solution solution = xmlSnakeHuntReader.readSolution();
			log.info("Solution is: %s".formatted(solution));
			if (solution == null) {
				snakeHuntInstance = new SnakeHuntInstance(jungle, snakeTypes, duration, new SnakeHuntRepositoryAdapter());
			} else {
				snakeHuntInstance = new SnakeHuntInstance(jungle, snakeTypes, duration, solution, new SnakeHuntRepositoryAdapter());
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

