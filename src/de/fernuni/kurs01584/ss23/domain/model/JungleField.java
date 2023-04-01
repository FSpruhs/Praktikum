package de.fernuni.kurs01584.ss23.domain.model;

import org.hamcrest.StringDescription;

public class JungleField {
	
	private final String id;
	private final int row;
	private final int column;
	private final int fieldValue;
	private final int usability;
	private final char charachter;
	
	
	public JungleField(String id, int row, int column, int fieldValue, int usability, char charachter) {
		this.id = id;
		this.row = row;
		this.column = column;
		this.fieldValue = fieldValue;
		this.usability = usability;
		this.charachter = charachter;
	}
	
	

}
