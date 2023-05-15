package de.fernuni.kurs01584.ss23.e2e;

import de.fernuni.kurs01584.ss23.adapters.users.CLIAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CLITest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void evaluate() {
        CLIAdapter.main(new String[] {"ablauf=b", "eingabe=res/sj_p1_loesung.xml"});
        assertEquals("Total points of the Solution are: 8", outputStreamCaptor.toString().trim());
    }

    @Test
    void validateWithoutErrors() {
        CLIAdapter.main(new String[] {"ablauf=p", "eingabe=res/sj_p1_loesung.xml"});
        assertEquals("Snake hunt solution is valid.", outputStreamCaptor.toString().trim());
    }

    @Test
    void validateWithAllErrors() {
        String expected = """
                Snake hunt solution is not valid.
                Total errors: 4
                Following errors found in Solution: GLIEDER VERWENDUNG ZUORDNUNG NACHBARSCHAFT""";
        CLIAdapter.main(new String[] {"ablauf=p", "eingabe=res/sj_p1_errors.xml"});
        assertEquals(expected, outputStreamCaptor.toString().trim());
    }

    @Test
    void show() {
        String expected = """
                -------------------------- Jungle data --------------------------
                Rows: 2
                Columns: 2
                Character band: ABCDEFGHIJKLMNOPQRSTUVWXYZ
                                
                -------------------------- Snake Types --------------------------
                Snake Type: A0
                Character band: FERNUNI
                Neighborhood Structure: Distance
                Value: 1
                Count: 1
                                
                -------------------------- Jungle Fields --------------------------
                (Value, Character, Usability)
                                
                +-------+-------+-------+-------+
                |       |       |       |       |
                | 1 [1mF[0m 1 | 1 [1mE[0m 1 | 1 [1mR[0m 1 | 1 [1mN[0m 1 |
                |       |       |       |       |
                +-------+-------+-------+-------+
                |       |       |       |       |
                | 1 [1mX[0m 1 | 1 [1mI[0m 1 | 1 [1mN[0m 1 | 1 [1mU[0m 1 |
                |       |       |       |       |
                +-------+-------+-------+-------+
                                
                -------------------------- Solution --------------------------
                SnakeType: A0
                Character band: FERNUNI
                Neighborhood Structure: Distance
                Snake Length: 7
                Snakeparts: (0, F, 0) -> (0, E, 1) -> (0, R, 2) -> (0, N, 3) -> (1, U, 3) -> (1, N, 2) -> (1, I, 1)
                 1  2  3  4\s
                 .  7  6  5""";
        CLIAdapter.main(new String[] {"ablauf=d", "eingabe=res/sj_p1_loesung.xml"});
        assertEquals(expected, outputStreamCaptor.toString().trim());
    }

    @Test
    void create() {
        String expected = """
                -------------------------- Jungle data --------------------------
                Rows: 2
                Columns: 2
                Character band: ABCDEFGHIJKLMNOPQRSTUVWXYZ
                                
                -------------------------- Snake Types --------------------------
                Snake Type: A0
                Character band: FERNUNI
                Neighborhood Structure: Distance
                Value: 1
                Count: 1
                                
                -------------------------- Jungle Fields --------------------------
                (Value, Character, Usability)
                                
                +-------+-------+-------+-------+
                |       |       |       |       |
                | 1 [1mQ[0m 1 | 1 [1mR[0m 1 | 1 [1mU[0m 1 | 1 [1mI[0m 1 |
                |       |       |       |       |
                +-------+-------+-------+-------+
                |       |       |       |       |
                | 1 [1mE[0m 1 | 1 [1mF[0m 1 | 1 [1mN[0m 1 | 1 [1mN[0m 1 |
                |       |       |       |       |
                +-------+-------+-------+-------+""";
        File output = new File("res/e2e.xml");
        output.delete();
        CLIAdapter.main(new String[] {"ablauf=ed", "eingabe=res/sj_p1_unvollstaendig.xml", "ausgabe=res/e2e.xml"});
        assertEquals(expected, outputStreamCaptor.toString().trim());
        assertTrue(output.exists());
    }

    @Test
    void solve() {
        String expected = """
                -------------------------- Jungle data --------------------------
                Rows: 2
                Columns: 2
                Character band: ABCDEFGHIJKLMNOPQRSTUVWXYZ
                               
                -------------------------- Snake Types --------------------------
                Snake Type: A0
                Character band: FERNUNI
                Neighborhood Structure: Distance
                Value: 1
                Count: 1
                               
                -------------------------- Jungle Fields --------------------------
                (Value, Character, Usability)
                               
                +-------+-------+-------+-------+
                |       |       |       |       |
                | 1 [1mF[0m 1 | 1 [1mE[0m 1 | 1 [1mR[0m 1 | 1 [1mN[0m 1 |
                |       |       |       |       |
                +-------+-------+-------+-------+
                |       |       |       |       |
                | 1 [1mX[0m 1 | 1 [1mI[0m 1 | 1 [1mN[0m 1 | 1 [1mU[0m 1 |
                |       |       |       |       |
                +-------+-------+-------+-------+
                               
                -------------------------- Solution --------------------------
                SnakeType: A0
                Character band: FERNUNI
                Neighborhood Structure: Distance
                Snake Length: 7
                Snakeparts: (0, F, 0) -> (0, E, 1) -> (0, R, 2) -> (0, N, 3) -> (1, U, 3) -> (1, N, 2) -> (1, I, 1)
                 1  2  3  4\s
                 .  7  6  5""";
        File output = new File("res/e2e.xml");
        output.delete();
        CLIAdapter.main(new String[] {"ablauf=ld", "eingabe=res/sj_p1_probleminstanz.xml", "ausgabe=res/e2e.xml"});
        assertEquals(expected, outputStreamCaptor.toString().trim());
        assertTrue(output.exists());
    }

    @Test
    void createSolveShowValidateEvaluate() {
        String expected = """
                -------------------------- Jungle data --------------------------
                Rows: 2
                Columns: 2
                Character band: ABCDEFGHIJKLMNOPQRSTUVWXYZ
                                
                -------------------------- Snake Types --------------------------
                Snake Type: A0
                Character band: FERNUNI
                Neighborhood Structure: Distance
                Value: 1
                Count: 1
                                
                -------------------------- Jungle Fields --------------------------
                (Value, Character, Usability)
                                
                +-------+-------+-------+-------+
                |       |       |       |       |
                | 1 [1mQ[0m 1 | 1 [1mR[0m 1 | 1 [1mU[0m 1 | 1 [1mI[0m 1 |
                |       |       |       |       |
                +-------+-------+-------+-------+
                |       |       |       |       |
                | 1 [1mE[0m 1 | 1 [1mF[0m 1 | 1 [1mN[0m 1 | 1 [1mN[0m 1 |
                |       |       |       |       |
                +-------+-------+-------+-------+
                                
                -------------------------- Solution --------------------------
                SnakeType: A0
                Character band: FERNUNI
                Neighborhood Structure: Distance
                Snake Length: 7
                Snakeparts: (1, F, 1) -> (1, E, 0) -> (0, R, 1) -> (1, N, 2) -> (0, U, 2) -> (1, N, 3) -> (0, I, 3)
                 .  3  5  7\s
                 2  1  4  6\s
                                
                Snake hunt solution is valid.
                Total points of the Solution are: 8""";
        File output = new File("res/e2e.xml");
        output.delete();
        CLIAdapter.main(new String[] {"ablauf=eldpb", "eingabe=res/sj_p1_unvollstaendig.xml", "ausgabe=res/e2e.xml"});
        assertEquals(expected, outputStreamCaptor.toString().trim());
        assertTrue(output.exists());
    }



}
