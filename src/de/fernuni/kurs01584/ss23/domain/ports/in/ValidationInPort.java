package de.fernuni.kurs01584.ss23.domain.ports.in;

import java.util.List;

import de.fernuni.kurs01584.ss23.hauptkomponente.SchlangenjagdAPI.Fehlertyp;

public interface ValidationInPort {
	
	List<Fehlertyp> isValid();

}
