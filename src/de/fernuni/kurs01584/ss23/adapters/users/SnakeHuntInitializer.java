package de.fernuni.kurs01584.ss23.adapters.users;

import java.io.File;
import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.adapters.infrastructure.SnakeHuntRepositoryAdapter;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidDataException;
import de.fernuni.kurs01584.ss23.domain.model.*;

/**
 * Initializer of a snake hunt instance.
 */
class SnakeHuntInitializer {
	
	private static final Logger log = Logger.getLogger(SnakeHuntInitializer.class.getName());

	private SnakeHuntInitializer(){}

	public static void initialize(File xmlFilePath) {
		XMLSnakeHuntReader xmlSnakeHuntReader = new XMLSnakeHuntReader(xmlFilePath); 
		try {
			Duration duration = xmlSnakeHuntReader.readDurationInSeconds();
			log.info("Duration initialized: %s".formatted(duration.toNanos()));
			Jungle jungle = xmlSnakeHuntReader.readJungle();
			log.info("Jungle initialized: %s".formatted(jungle));
			Map<SnakeTypeId, SnakeType> snakeTypes = xmlSnakeHuntReader.readSnakeTypes();
			log.info("Snake Types initialized: %s".formatted(snakeTypes));
			Solution solution = xmlSnakeHuntReader.readSolution();
			log.info("Solution is: %s".formatted(solution));
			if (solution == null) {
				SnakeHunt.createInstance(jungle, snakeTypes, duration, new SnakeHuntRepositoryAdapter());
			} else {
				SnakeHunt.createInstance(jungle, snakeTypes, duration, solution, new SnakeHuntRepositoryAdapter());
			}
		} catch (InvalidDataException e) {
			log.warning(e.getMessage());
			System.exit(0);
		}
	}

}

