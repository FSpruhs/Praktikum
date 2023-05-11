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

public class CreateUseCase implements CreateSnakeHuntInPort {

    private static final Logger log = Logger.getLogger(CreateUseCase.class.getName());

    private final Jungle jungle;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;
    private Duration targetDuration;

    public CreateUseCase(Jungle jungle, Map<SnakeTypeId, SnakeType> snakeTypes, Duration targetDuration) {
        this.jungle = jungle;
        this.snakeTypes = snakeTypes;
        this.targetDuration = targetDuration;
    }

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
        long begin = System.nanoTime();
        solveForTimeMeasurement();
        this. targetDuration = Duration.ofNanos((System.nanoTime() - begin) * 2);
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

    @Override
    public Duration getTargetDuration() {
        return targetDuration;
    }

}
