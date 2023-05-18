package de.fernuni.kurs01584.ss23.adapters.users;

import de.fernuni.kurs01584.ss23.application.CreateUseCase;
import de.fernuni.kurs01584.ss23.application.EvaluateSolutionUseCase;
import de.fernuni.kurs01584.ss23.application.SolveUseCase;
import de.fernuni.kurs01584.ss23.application.ValidationUseCase;
import de.fernuni.kurs01584.ss23.application.ports.in.CreateSnakeHuntInPort;
import de.fernuni.kurs01584.ss23.application.ports.in.EvaluateSolutionInPort;
import de.fernuni.kurs01584.ss23.application.ports.in.SolveInPort;
import de.fernuni.kurs01584.ss23.application.ports.in.ValidationInPort;
import de.fernuni.kurs01584.ss23.domain.model.SnakeHuntInstance;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI;

import java.io.File;
import java.util.List;

/**
 * Adapter for the Snake Hunt Api.
 */
public class APIAdapter {

    private final SnakeHuntInstance snakeHuntInstance;

    /**
     *
     * Constructor for the APIAdapter.
     *
     * @param inputFile Path of the file for the snake hunt instance.
     */
    public APIAdapter(String inputFile) {
        SnakeHuntInitializer snakeHuntInitializer = new SnakeHuntInitializer(new File(inputFile));
        this.snakeHuntInstance = snakeHuntInitializer.getSnakeHuntInstance();
    }

    /**
     *
     * Search for a solution to the current snake hunt instance.
     *
     * @param xmlAusgabeDatei Path in which the XML file should be saved.
     * @return Returns true if a solution was found, false otherwise.
     */

    public boolean solve(String xmlAusgabeDatei) {
        SolveInPort solveInPort = new SolveUseCase(
                snakeHuntInstance.getJungle(),
                snakeHuntInstance.getTargetDuration(),
                snakeHuntInstance.getSnakeTypes()
        );
        snakeHuntInstance.setSolution(solveInPort.solveSnakeHuntInstance());
        snakeHuntInstance.setActualDuration(solveInPort.getActualDuration());
        snakeHuntInstance.save(new File(xmlAusgabeDatei));
        return snakeHuntInstance.getSolution() != null;
    }

    /**
     * Returns true if a new jungle was created, otherwise false.
     *
     * @param xmlAusgabeDatei Path in which the XML file should be saved.
     * @return
     */

    public boolean create(String xmlAusgabeDatei) {
        snakeHuntInstance.setSolution(null);
        CreateSnakeHuntInPort createSnakeHuntInPort = new CreateUseCase(
                snakeHuntInstance.getJungle(),
                snakeHuntInstance.getSnakeTypes(),
                snakeHuntInstance.getTargetDuration()
        );
        createSnakeHuntInPort.create();
        snakeHuntInstance.setTargetDuration(createSnakeHuntInPort.getTargetDuration());
        snakeHuntInstance.save(new File(xmlAusgabeDatei));
        return snakeHuntInstance.getJungle().getJungleFields() != null;
    }

    /**
     *
     * Checks if the solution of a snake hunt instance is correct.
     *
     * @return Return a list of SchlangenjagdAPI.Fehlertyp.
     */

    public List<SchlangenjagdAPI.Fehlertyp> validate() {
        ValidationInPort validationInPort = new ValidationUseCase(
                snakeHuntInstance.getSolution(),
                snakeHuntInstance.getJungle(),
                snakeHuntInstance.getSnakeTypes()
        );
        return validationInPort.isValid();
    }

    /**
     * Calculates the value of the current solution from the current snake hunt instance.
     *
     * @return The current value of the solution of the snake hunt instance.
     */

    public int rate() {
        EvaluateSolutionInPort evaluateSolutionInPort = new EvaluateSolutionUseCase(
                snakeHuntInstance.getSolution(),
                snakeHuntInstance.getJungle(),
                snakeHuntInstance.getSnakeTypes()
        );
        return evaluateSolutionInPort.evaluateTotalPoints();
    }
}
