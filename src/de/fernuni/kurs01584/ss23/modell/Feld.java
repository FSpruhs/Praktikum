package de.fernuni.kurs01584.ss23.modell;

public class Feld {
	private int zeile;
	private int spalte;

	// TODO: (weitere) Attribute

	public Feld(int zeile, int spalte) {
		super();
		if (zeile < 0 || spalte < 0) {
			throw new IllegalArgumentException(
					"Fuer die Klasse 'Feld' duerfen die Attribute 'zeile' und 'spalte' keine negativen Werte annehmen.");
		}
		this.zeile = zeile;
		this.spalte = spalte;
	}

	// TODO: (weitere) Konstruktoren

	public int getZeile() {
		return zeile;
	}

	public int getSpalte() {
		return spalte;
	}

	// TODO: (weitere) Methoden

}
