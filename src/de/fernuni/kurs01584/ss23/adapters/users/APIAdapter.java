package de.fernuni.kurs01584.ss23.adapters.users;

import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI;

import java.io.File;
import java.util.List;

public class APIAdapter {

    private SnakeHuntInitializer snakeHuntInitializer;

    public boolean solve(String xmlEingabeDatei, String xmlAusgabeDatei) {
        snakeHuntInitializer = new SnakeHuntInitializer(new File(xmlEingabeDatei));
        return snakeHuntInitializer.getSolveInPort().solveSnakeHuntInstance(new File(xmlAusgabeDatei));
    }

    public boolean create(String xmlEingabeDatei, String xmlAusgabeDatei) {
        snakeHuntInitializer = new SnakeHuntInitializer(new File(xmlEingabeDatei));
        return snakeHuntInitializer.getCreateSnakeHuntInPort().create(new File(xmlAusgabeDatei));
    }

    public List<SchlangenjagdAPI.Fehlertyp> validate(String xmlEingabeDatei) {
        snakeHuntInitializer = new SnakeHuntInitializer(new File(xmlEingabeDatei));
        return snakeHuntInitializer.getValidationInPort().isValid();
    }

    public int rate(String xmlEingabeDatei) {
        snakeHuntInitializer = new SnakeHuntInitializer(new File(xmlEingabeDatei));
        return snakeHuntInitializer.getEvaluateSolutionInPort().evaluateTotalPoints();
    }
}
