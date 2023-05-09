package de.fernuni.kurs01584.ss23.adapters.users;

import java.io.File;
import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.adapters.infrastructure.SnakeHuntRepositoryAdapter;
import de.fernuni.kurs01584.ss23.application.ports.in.*;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidDataException;
import de.fernuni.kurs01584.ss23.domain.model.*;

class SnakeHuntInitializer {
	
	private static final Logger log = Logger.getLogger(SnakeHuntInitializer.class.getName());
	
	private final File xmlFilePath;
	private SnakeHuntInstance snakeHuntInstance;
	
	public SnakeHuntInitializer(File xmlFilePath) {
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
	
	public ShowSnakeHuntIntPort getShowSnakeHuntInPort() {
		return snakeHuntInstance;
	}
	
	public SolveInPort getSolveInPort() {
		return snakeHuntInstance;
	}
	
	public ValidationInPort getValidationInPort() {
		return snakeHuntInstance;
	}

	public CreateSnakeHuntInPort getCreateSnakeHuntInPort() {
		return snakeHuntInstance;
	}


}

