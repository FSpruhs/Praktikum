package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;

public record Solution (List<Snake> snakes) {

	public void loadSnakes(List<Snake> snakes) {
		this.snakes.clear();
		this.snakes.addAll(snakes);
	}

	public void insertSnake(Snake snake) {
		snakes.add(snake);
	}

	public void removeSnake(Snake snake) {
		snakes.remove(snake);
	}

}
