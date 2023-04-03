package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

public class SnakeType {
	
	private final String id;
	private final int snakeValue;
	private final int count;
	private final String characterband;
	private final NeighborhoodStructure neighborhoodStructure;
	
	public SnakeType(String id, int snakeValue, int count, String characterband,
			NeighborhoodStructure neighborhoodStructure) {
		this.id = id;
		this.snakeValue = snakeValue;
		this.count = count;
		this.characterband = characterband;
		this.neighborhoodStructure = neighborhoodStructure;
	}
	
	

}
