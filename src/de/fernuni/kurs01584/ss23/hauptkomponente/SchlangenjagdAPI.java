package de.fernuni.kurs01584.ss23.hauptkomponente;

import java.util.List;

public interface SchlangenjagdAPI {
	/**
	 * Liest die vorgegebene Eingabedatei mit einer vollstaendigen Probleminstanz
	 * und startet das Loesungverfahren fuer die Schlangensuche. Die gefundene
	 * Loesung wird zusammen mit der Probleminstanz in der Ausgabedatei gespeichert.
	 * 
	 * @param xmlEingabeDatei Dateipfad zu einer XML-Datei mit der Probleminstanz,
	 *                        die geloest werden soll.
	 * @param xmlAusgabeDatei Dateipfad zu einer XML-Datei fuer die Probleminstanz
	 *                        und die erzeugte Loesung.
	 * @return <tt>true</tt>, wenn mindestens eine Schlange gefunden wurde,
	 *         ansonsten <tt>false</tt>. Beim Auftreten eines Fehlers wird ebenfalls
	 *         <tt>false</tt> zurückgegeben.
	 */
	public boolean loeseProbleminstanz(String xmlEingabeDatei, String xmlAusgabeDatei);

	/**
	 * Liest die Vorgegebene Eingabedatei mit einer (moeglicherweise
	 * unvollstaendigen) Probleminstanz und erzeugt eine neue Probleminstanz auf
	 * Basis der gegebenen Parameter. Die erzeugte Probleminstanz wird in der
	 * vorgegebenen Ausgabedatei gespeichert.
	 * 
	 * @param xmlEingabeDatei Dateipfad zu einer XML-Datei mit Parametern fuer eine
	 *                        Probleminstanz, die erzeugt werden soll.
	 * @param xmlAusgabeDatei Dateipfad zu einer XML-Datei fuer die erzeugte
	 *                        Probleminstanz.
	 * @return <tt>true</tt>, bei Erfolg, ansonsten <tt>false</tt>. Beim Auftreten
	 *         eines Fehlers wird ebenfalls <tt>false</tt> zurückgegeben.
	 */
	public boolean erzeugeProbleminstanz(String xmlEingabeDatei, String xmlAusgabeDatei);

	/**
	 * Moegliche Fehlertypen fuer eine Loesung einer Probleminstanz.
	 * <ul>
	 * <li><tt>GLIEDER</tt>: Eine Schlange besteht nicht aus der richtigen Anzahl
	 * von Schlangengliedern.</li>
	 * <li><tt>ZUORDNUNG</tt>: Ein Schlangenglied ist einem Feld mit einem falschen
	 * Zeichen zugeordnet.</li>
	 * <li><tt>VERWENDUNG</tt>: Ein Schlangenglied ist einem bereits maximal
	 * verwendeten Feld zugeordnet.</li>
	 * <li><tt>NACHBARSCHAFT</tt>: Ein Schlangenglied befindet sich nicht in der
	 * Nachbarschaft des jeweils vorherigen Schlangengliedes.</li>
	 * </ul>
	 */
	public enum Fehlertyp {
		GLIEDER, ZUORDNUNG, VERWENDUNG, NACHBARSCHAFT
	}

	/**
	 * Liest die Probleminstanz und Loesung aus der gegebenen Datei ein und
	 * ueberprueft die Loesung auf Zulaessingkeit. Dabei werden sowohl die Art als
	 * auch die Haeufigkeit der verletzten Bedingungen ermittelt.
	 * 
	 * @param xmlEingabeDatei Dateipfad zu einer XML-Datei mit einer Probleminstanz
	 *                        und der zugehoerigen Loesung.
	 * @return Liste der gefundenen Einzelfehler. Beim Auftreten eines Fehlers wird
	 *         eine leere Liste zurueckgegeben.
	 */
	public List<Fehlertyp> pruefeLoesung(String xmlEingabeDatei);

	/**
	 * Liest die Probleminstanz und Loesung aus der gegebenen Datei ein und
	 * berechnet die erreichte Punktzahl. Die Berechnung erfolgt unabhaengig von der
	 * Zulaessigkeit der Loesung. Punkte werden fuer jede gefundene Schlange einer
	 * Schlangenart und fuer jedes von einem Schlangenglied verwendete Feld (fuer
	 * jede Verwendung) vergeben.
	 * 
	 * @param xmlEingabeDatei Dateipfad zu einer XML-Datei mit einer Probleminstanz
	 *                        und Loesung.
	 * @return Erreichte Gesamtpunktzahl. Beim Auftreten eines Fehlers wird der Wert
	 *         <tt>0</tt> zurueckgegeben.
	 */
	public int bewerteLoesung(String xmlEingabeDatei);

	/**
	 * @return Ihr vollstaendiger Name.
	 */
	public String getName();

	/**
	 * @return Ihre Matrikelnummer.
	 */
	public String getMatrikelnummer();

	/**
	 * @return Ihre E-Mail Adresse.
	 */
	public String getEmail();
}
