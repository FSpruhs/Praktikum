package de.fernuni.kurs01584.ss23.application.ports.in;

import de.fernuni.kurs01584.ss23.domain.model.Solution;

import java.time.Duration;

public interface SolveInPort {
	Solution solveSnakeHuntInstance();
	Duration getActualDuration();
}
