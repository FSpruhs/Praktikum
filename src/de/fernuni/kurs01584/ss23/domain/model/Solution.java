package de.fernuni.kurs01584.ss23.domain.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Solution {
	
	private int totalPoints;
	private List<Snake> snakes = new LinkedList<>();
	
	public void loadSnakes(List<Snake> snakes) {
		this.snakes = snakes;
		totalPoints = 0;
	}
	
	public List<Snake> getSnakes() {
		return snakes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(snakes);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Solution other = (Solution) obj;
		return Objects.equals(snakes, other.snakes);
	}

	@Override
	public String toString() {
		return "Solution [snakes=" + snakes + "]";
	}

	public void insertSnake(Snake snake) {
		snakes.add(snake);
		
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

}
