package de.fernuni.kurs01584.ss23.hauptkomponente;

import java.util.List;

public class Schlangenjagd implements SchlangenjagdAPI {
	// TODO: Implementierung von Schnittstelle und Programm-Einstieg

	@Override
	public boolean loeseProbleminstanz(String xmlEingabeDatei, String xmlAusgabeDatei) {
		// TODO Implementierung der API-Methode zur Loesung von Probleminstanzen.
		return false;
	}

	@Override
	public boolean erzeugeProbleminstanz(String xmlEingabeDatei, String xmlAusgabeDatei) {
		// TODO Implementierung der API-Methode zur Erzeugung von Probleminstanzen.
		return false;
	}

	@Override
	public List<Fehlertyp> pruefeLoesung(String xmlEingabeDatei) {
		// TODO Implementierung der API-Methode zur Pruefung von Loesungen.
		return null;
	}

	@Override
	public int bewerteLoesung(String xmlEingabeDatei) {
		// TODO Implementierung der API-Methode zur Bewertung von Loesungen.
		return 0;
	}

	@Override
	public String getName() {
		// TODO Implementierung der API-Methode zur Rueckgabe Ihres vollstaenigen Namens.
		return null;
	}

	@Override
	public String getMatrikelnummer() {
		// TODO Implementierung der API-Methode zur Rueckgabe Ihrer Matrikelnummer.
		return null;
	}

	@Override
	public String getEmail() {
		// TODO Implementierung der API-Methode zur Rueckgabe Ihrer E-Mail Adresse.
		return null;
	}
}
