package de.fernuni.kurs01584.ss23.domain.exception;

public class NeighborhoodStructureNotFoundException extends RuntimeException{

	public NeighborhoodStructureNotFoundException(String message) {
		super("Neighborhood Structure %s not found!".formatted(message));
	}
	

}
