package de.fernuni.kurs01584.ss23.domain.exception;

public class InvalidDataException extends RuntimeException{
	public InvalidDataException(String message) {
		super(message);
	}
}
