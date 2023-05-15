package de.fernuni.kurs01584.ss23.e2e;

import de.fernuni.kurs01584.ss23.hauptkomponente.Schlangenjagd;
import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiTest {

    @Test
    void evaluate() {
        Schlangenjagd snakeHunt = new Schlangenjagd();
        assertEquals(8, snakeHunt.bewerteLoesung("res/sj_p1_loesung.xml"));
    }

    @Test
    void validateWithoutErrors() {
        Schlangenjagd snakeHunt = new Schlangenjagd();
        assertEquals(List.of(), snakeHunt.pruefeLoesung("res/sj_p1_loesung.xml"));
    }

    @Test
    void validateWithErrors() {
        Schlangenjagd snakeHunt = new Schlangenjagd();
        assertEquals(List.of(
                SchlangenjagdAPI.Fehlertyp.GLIEDER,
                SchlangenjagdAPI.Fehlertyp.VERWENDUNG,
                SchlangenjagdAPI.Fehlertyp.ZUORDNUNG,
                SchlangenjagdAPI.Fehlertyp.NACHBARSCHAFT
        ), snakeHunt.pruefeLoesung("res/sj_p1_errors.xml"));
    }

    @Test
    void create() {
        File output = new File("res/e2e.xml");
        output.delete();
        Schlangenjagd snakeHunt = new Schlangenjagd();
        assertTrue(snakeHunt.erzeugeProbleminstanz("res/sj_p1_unvollstaendig.xml", "res/e2e.xml"));
        assertTrue(output.exists());
    }

    @Test
    void solve() {
        File output = new File("res/e2e.xml");
        output.delete();
        Schlangenjagd snakeHunt = new Schlangenjagd();
        assertTrue(snakeHunt.loeseProbleminstanz("res/sj_p1_probleminstanz.xml", "res/e2e.xml"));
        assertTrue(output.exists());
    }

}
