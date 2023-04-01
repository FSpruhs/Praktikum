package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;

import javax.sql.RowSet;

public class Jungle {
	
	private final int rows;
	private final int columns;
	private final String characters;
	private final List<JungleField> jungleFields;
	
	public Jungle(int rows, int columns, String characters, List<JungleField> jungleFields) {
		this.rows = rows;
		this.columns = columns;
		this.characters = characters;
		this.jungleFields = jungleFields;
	}
	
	

}
