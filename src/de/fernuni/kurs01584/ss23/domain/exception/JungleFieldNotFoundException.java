package de.fernuni.kurs01584.ss23.domain.exception;

public class JungleFieldNotFoundException extends RuntimeException{

	public JungleFieldNotFoundException(String fieldId) {
		super("Jungle Field with id %s not found!".formatted(fieldId));
	}

}
