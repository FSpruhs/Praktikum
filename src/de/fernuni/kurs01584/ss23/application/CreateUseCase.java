package de.fernuni.kurs01584.ss23.application;

import de.fernuni.kurs01584.ss23.application.algorithm.DoubleRecursionAlgorithm;
import de.fernuni.kurs01584.ss23.application.algorithm.SnakeHuntAlgorithm;
import de.fernuni.kurs01584.ss23.application.junglegenerator.JungleGenerator;
import de.fernuni.kurs01584.ss23.application.junglegenerator.SimpleJungleGenerator;
import de.fernuni.kurs01584.ss23.application.ports.in.CreateSnakeHuntInPort;
import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.SnakeTypeId;

import java.time.Duration;
import java.util.Map;
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

    private final Jungle jungle;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;
    private Duration targetDuration;

    /**
     * Constructor for the create use case.
     *
     * @param jungle jungle with the jungle data.
     * @param snakeTypes map with the snake type id and the snake type data.
     * @param targetDuration target duration to solve the snake hunt instance.
     */
    public CreateUseCase(
            Jungle jungle,
            Map<SnakeTypeId, SnakeType> snakeTypes,
            Duration targetDuration
    ) {
        this.jungle = jungle;
        this.snakeTypes = snakeTypes;
        this.targetDuration = targetDuration;
    }

    /**
     * Creates a new jungle with a new target duration.
     */
    @Override
    public void create() {
        jungle.removeJungleFields();
        JungleGenerator jungleGenerator = new SimpleJungleGenerator(jungle, snakeTypes);
        log.info("Start create jungle");
        jungleGenerator.generate();
        log.info("New jungle created");
        setDuration();
    }

    private void setDuration() {
        long begin = actualTime();
        solveForTimeMeasurement();
        this.targetDuration = Duration.ofNanos((actualTime() - begin) * EXTRA_TIME_MULTIPLIER);
    }

    private long actualTime() {
        return System.nanoTime();
    }

    private void solveForTimeMeasurement() {
        if (jungle.getJungleFields().isEmpty()) {
            log.info("Can not solve jungle, because jungle is empty.");
        }
        SnakeHuntAlgorithm snakeHuntAlgorithm = new DoubleRecursionAlgorithm(
                jungle,
                snakeTypes,
                targetDuration
        );
        snakeHuntAlgorithm.solveSnakeHuntInstance();
    }

    /**
     * Get the target duration.
     *
     * @return the target duration.
     */
    @Override
    public Duration getTargetDuration() {
        return targetDuration;
    }

}
