package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.application.algorithm.DoubleRecursionAlgorithm;
import de.fernuni.kurs01584.ss23.application.algorithm.SnakeHuntAlgorithm;
import de.fernuni.kurs01584.ss23.application.junglegenerator.JungleGenerator;
import de.fernuni.kurs01584.ss23.application.junglegenerator.SimpleJungleGenerator;
import de.fernuni.kurs01584.ss23.application.ports.in.CreateSnakeHuntInPort;
import de.fernuni.kurs01584.ss23.domain.model.SnakeHunt;

import java.io.File;
import java.time.Duration;
import java.util.logging.Logger;

/**
 * Use case to create a new snake hunt instance. The use case deletes the actual solution, the actual jungle,
 * the actual duration and the target duration. Then it creates a new jungle with all snake types in it.
 * Each new jungle field has the field value 1 and the usability 1.
 * For the new jungle a new target duration will be calculated.
 */
public class CreateUseCase implements CreateSnakeHuntInPort {

    private static final Logger log = Logger.getLogger(CreateUseCase.class.getName());
    private static final int EXTRA_TIME_MULTIPLIER = 2;

    private final SnakeHunt snakeHunt;

    public CreateUseCase() {
        this.snakeHunt = SnakeHunt.getInstance();
    }

    /**
     * Creates a new jungle with a new target duration.
     */
    @Override
    public void create(File output) {
        snakeHunt.setSolution(null);
        snakeHunt.getJungle().removeJungleFields();
        JungleGenerator jungleGenerator = new SimpleJungleGenerator(snakeHunt.getJungle(), snakeHunt.getSnakeTypes());
        log.info("Start create jungle");
        jungleGenerator.generate();
        log.info("New jungle created");
        setDuration();
        snakeHunt.save(output);
    }

    private void setDuration() {
        long begin = actualTime();
        solveForTimeMeasurement();
        snakeHunt.setTargetDuration(Duration.ofNanos((actualTime() - begin) * EXTRA_TIME_MULTIPLIER));
    }

    private long actualTime() {
        return System.nanoTime();
    }

    private void solveForTimeMeasurement() {
        if (isJungleFieldsEmpty()) {
            log.info("Can not solve jungle, because jungle is empty.");
        }
        SnakeHuntAlgorithm snakeHuntAlgorithm = new DoubleRecursionAlgorithm(
                snakeHunt.getJungle(),
                snakeHunt.getSnakeTypes(),
                snakeHunt.getTargetDuration()
        );
        snakeHuntAlgorithm.solveSnakeHuntInstance();
    }

    private boolean isJungleFieldsEmpty() {
        return snakeHunt.getJungle().getJungleFields().isEmpty();
    }
}
