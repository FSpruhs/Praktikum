package de.fernuni.kurs01584.ss23.adapters.users;

import de.fernuni.kurs01584.ss23.application.CreateUseCase;
import de.fernuni.kurs01584.ss23.application.EvaluateSolutionUseCase;
import de.fernuni.kurs01584.ss23.application.SolveUseCase;
import de.fernuni.kurs01584.ss23.application.ValidationUseCase;
import de.fernuni.kurs01584.ss23.application.ports.in.CreateSnakeHuntInPort;
import de.fernuni.kurs01584.ss23.application.ports.in.EvaluateSolutionInPort;
import de.fernuni.kurs01584.ss23.application.ports.in.SolveInPort;
import de.fernuni.kurs01584.ss23.application.ports.in.ValidationInPort;
import de.fernuni.kurs01584.ss23.domain.exception.NoSolutionException;
import de.fernuni.kurs01584.ss23.domain.model.JungleField;
import de.fernuni.kurs01584.ss23.domain.model.SnakeHunt;
import de.fernuni.kurs01584.ss23.domain.model.Solution;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Adapter for the Snake Hunt Api.
 */
public class APIAdapter {

    private static final Logger log = Logger.getLogger(APIAdapter.class.getName());

    /**
     *
     * Constructor for the APIAdapter.
     *
     * @param inputFile Path of the file for the snake hunt instance.
     */
    public APIAdapter(String inputFile) {
        SnakeHuntInitializer.initialize(new File(inputFile));
    }

    /**
     *
     * Search for a solution to the current snake hunt instance.
     *
     * @param xmlAusgabeDatei Path in which the XML file should be saved.
     * @return Returns true if a solution was found, false otherwise.
     */
    public boolean solve(String xmlAusgabeDatei) {
        SolveInPort solveInPort = new SolveUseCase();
        solveInPort.solveSnakeHuntInstance(new File(xmlAusgabeDatei));
        return getSolution() != null;
    }

    private Solution getSolution() {
        return SnakeHunt.getInstance().getSolution();
    }

    /**
     * Returns true if a new jungle was created, otherwise false.
     *
     * @param xmlAusgabeDatei Path in which the XML file should be saved.
     * @return true if snake hunt instance is created, otherwise false.
     */
    public boolean create(String xmlAusgabeDatei) {
        CreateSnakeHuntInPort createSnakeHuntInPort = new CreateUseCase();
        createSnakeHuntInPort.create(new File(xmlAusgabeDatei));
        return getJungleFields() != null;
    }

    private List<JungleField> getJungleFields() {
        return SnakeHunt.getInstance().getJungle().getJungleFields();
    }

    /**
     *
     * Checks if the solution of a snake hunt instance is correct.
     *
     * @return Return a list of SchlangenjagdAPI.Fehlertyp.
     */
    public List<SchlangenjagdAPI.Fehlertyp> validate() {
        ValidationInPort validationInPort = new ValidationUseCase();
        try {
            return validationInPort.isValid();
        } catch (NoSolutionException e) {
            log.info("No solution to validate!");
            return new LinkedList<>();
        }

    }

    /**
     * Calculates the value of the current solution from the current snake hunt instance.
     *
     * @return The current value of the solution of the snake hunt instance.
     */
    public int rate() {
        EvaluateSolutionInPort evaluateSolutionInPort = new EvaluateSolutionUseCase();
        try {
            return evaluateSolutionInPort.evaluateTotalPoints();
        } catch (NoSolutionException e) {
            log.info("No solution to evaluate!");
            return 0;
        }

    }
}
