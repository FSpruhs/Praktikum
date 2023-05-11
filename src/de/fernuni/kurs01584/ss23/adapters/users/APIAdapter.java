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

public class APIAdapter {

    private final SnakeHuntInstance snakeHuntInstance;

    public APIAdapter(String file) {
        SnakeHuntInitializer snakeHuntInitializer = new SnakeHuntInitializer(new File(file));
        this.snakeHuntInstance = snakeHuntInitializer.getSnakeHuntInstance();

    }

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

    public List<SchlangenjagdAPI.Fehlertyp> validate() {
        ValidationInPort validationInPort = new ValidationUseCase(
                snakeHuntInstance.getSolution(),
                snakeHuntInstance.getJungle(),
                snakeHuntInstance.getSnakeTypes()
        );
        return validationInPort.isValid();
    }

    public int rate() {
        EvaluateSolutionInPort evaluateSolutionInPort = new EvaluateSolutionUseCase(
                snakeHuntInstance.getSolution(),
                snakeHuntInstance.getJungle(),
                snakeHuntInstance.getSnakeTypes()
        );
        return evaluateSolutionInPort.evaluateTotalPoints();
    }
}
