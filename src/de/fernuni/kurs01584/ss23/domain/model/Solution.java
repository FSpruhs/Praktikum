package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;

/**
 * Value object that represents the solution to a snake hunt.
 *
 * @param snakes A list of snakes found during snake hunting.
 */
public record Solution (List<Snake> snakes) {

	/**
	 * Clears the list of snakes and adds the snakes to the passed list.
	 *
	 * @param snakes the list of snakes to be added.
	 */
	public void loadSnakes(List<Snake> snakes) {
		this.snakes.clear();
		this.snakes.addAll(snakes);
	}

	/**
	 * Adds a snake to the list of snakes.
	 *
	 * @param snake the snake to be added.
	 */
	public void insertSnake(Snake snake) {
		snakes.add(snake);
	}

	/**
	 * Removes a snake from the list of snakes.
	 *
	 * @param snake the snake to be removed.
	 */
	public void removeSnake(Snake snake) {
		snakes.remove(snake);
	}

}
