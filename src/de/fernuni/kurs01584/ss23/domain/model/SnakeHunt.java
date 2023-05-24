package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.application.ports.out.SaveSnakeHuntInstanceOutPort;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleException;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;

import java.io.File;
import java.time.Duration;
import java.util.Map;

/**
 * The central snake hunts instance. This instance aggregates all the necessary values for a snake hunt.
 */
public class SnakeHunt {

    private static SnakeHunt instance;
    private final Jungle jungle;
    private final Map<SnakeTypeId, SnakeType> snakeTypes;
    private final SaveSnakeHuntInstanceOutPort repository;
    private Duration targetDuration;
    private Duration actualDuration;
    private Solution solution;

    private SnakeHunt(
            Jungle jungle,
            Map<SnakeTypeId, SnakeType> snakeTypes,
            Duration targetDuration,
            Solution solution,
            SaveSnakeHuntInstanceOutPort saveSnakeHuntInstanceOutPort) {
        this.jungle = jungle;
        this.snakeTypes = snakeTypes;
        this.targetDuration = targetDuration;
        this.solution = solution;
        this.repository = saveSnakeHuntInstanceOutPort;
        validateSnakeHuntInstance();
    }

    private SnakeHunt(
            Jungle jungle,
            Map<SnakeTypeId, SnakeType> snakeTypes,
            Duration targetDuration,
            SaveSnakeHuntInstanceOutPort saveSnakeHuntInstanceOutPort) {
        this.jungle = jungle;
        this.snakeTypes = snakeTypes;
        this.targetDuration = targetDuration;
        this.repository = saveSnakeHuntInstanceOutPort;
        validateSnakeHuntInstance();
    }

    /**
     * Created a new snake hunt instance with a given solution.
     *
     * @param jungle The jungle of a snakes hunt instance.
     * @param snakeTypes A map of snake type id and snake types of a snakes hunt instance.
     * @param targetDuration The target duration in which the snake hunt should be solved.
     * @param solution A concrete solution of the snake hunt with the associated jungle and snake types.
     * @param repository A repository with which the snake hunt can be persisted.
     */
    public static void createInstance(
            Jungle jungle,
            Map<SnakeTypeId, SnakeType> snakeTypes,
            Duration targetDuration,
            Solution solution,
            SaveSnakeHuntInstanceOutPort repository) {
        instance = new SnakeHunt(jungle, snakeTypes, targetDuration, solution, repository);
    }

    /**
     * Created a new snake hunt instance without a solution.
     *
     * @param jungle The jungle of a snakes hunt instance. Jungle cant be null.
     * @param snakeTypes A map of snake type id and snake types of a snakes hunt instance. Snake types cant be null or empty.
     * @param targetDuration The target duration in which the snake hunt should be solved. If the target duration
     *                       is not present, the default value is 30 seconds.
     * @param repository A repository with which the snake hunt can be persisted.
     */
    public static void createInstance(
            Jungle jungle,
            Map<SnakeTypeId, SnakeType> snakeTypes,
            Duration targetDuration,
            SaveSnakeHuntInstanceOutPort repository) {
        instance = new SnakeHunt(jungle, snakeTypes, targetDuration, repository);
    }

    /**
     * Returns the concrete snake hunts instance.
     *
     * @return the concrete snake hunts instance.
     */
    public static SnakeHunt getInstance() {
        return instance;
    }

    private void validateSnakeHuntInstance() {
        validateJungle();
        validateSnakeTypes();
        setDefaultDuration();
    }

    private void setDefaultDuration() {
        if (targetDuration == null) {
            this.targetDuration = Duration.ofSeconds(30);
        }
    }

    private void validateSnakeTypes() {
        if (snakeTypes == null || snakeTypes.isEmpty()) {
            throw new InvalidSnakeTypesException("Snake Types is Null!");
        }
    }

    private void validateJungle() {
        if (jungle == null) {
            throw new InvalidJungleException("Jungle is Null!");
        }
    }

    /**
     * Saves the current data of the snake chases instance with the repository.
     *
     * @param file The path to the file where the data of the snake hunt instance should be saved.
     */
    public void save(File file) {
        repository.save(
                file,
                jungle,
                snakeTypes,
                targetDuration,
                actualDuration,
                solution
        );
    }


    /**
     * Returns the current solution.
     *
     * @return the current solution.
     */
    public Solution getSolution() {
        return solution;
    }

    /**
     * Returns the current solution.
     *
     * @return the current jungle.
     */
    public Jungle getJungle() {
        return jungle;
    }

    /**
     * Returns the current snake types.
     *
     * @return a map of snake type id and snake types.
     */
    public Map<SnakeTypeId, SnakeType> getSnakeTypes() {
        return snakeTypes;
    }

    /**
     * Adds a new solution to the snake hunt instance.
     *
     * @param solution the solution to be added.
     */
    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    /**
     * Sets the duration in which the snake hunt instance was solved.
     *
     * @param actualDuration the duration in which the snake hunt instance was solved.
     */
    public void setActualDuration(Duration actualDuration) {
        this.actualDuration = actualDuration;
    }

    /**
     * Returns the actual duration in which the snake hunt instance was solved.
     *
     * @return the actual duration in which the snake hunt instance was solved.
     */
    public Duration getTargetDuration() {
        return targetDuration;
    }

    /**
     * Sets the duration in which the snake hunt instance should be solved.
     *
     * @param targetDuration the duration in which the snake hunt instance should be solved.
     */
    public void setTargetDuration(Duration targetDuration) {
        this.targetDuration = targetDuration;
    }
}
