package de.fernuni.kurs01584.ss23.application.ports.in;

import java.util.List;

import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;

/**
 * Port that validates the solution of the snake hunt instance.
 */
public interface ValidationInPort {

	/**
	 * Returns a list with errors of the snake hunt instance.
	 *
	 * @return a list of the type 'Fehlertyp'. For each error there is one error in the List.
	 */
	List<Fehlertyp> isValid();

}
