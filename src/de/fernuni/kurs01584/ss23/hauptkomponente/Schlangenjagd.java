package de.fernuni.kurs01584.ss23.hauptkomponente;

import de.fernuni.kurs01584.ss23.adapters.users.APIAdapter;

import java.util.List;

public class Schlangenjagd implements SchlangenjagdAPI {
	private final APIAdapter apiAdapter = new APIAdapter();

	@Override
	public boolean loeseProbleminstanz(String xmlEingabeDatei, String xmlAusgabeDatei) {
		return apiAdapter.solve(xmlEingabeDatei, xmlAusgabeDatei);
	}

	@Override
	public boolean erzeugeProbleminstanz(String xmlEingabeDatei, String xmlAusgabeDatei) {
		return apiAdapter.create(xmlEingabeDatei, xmlAusgabeDatei);
	}

	@Override
	public List<Fehlertyp> pruefeLoesung(String xmlEingabeDatei) {
		return apiAdapter.validate(xmlEingabeDatei);
	}

	@Override
	public int bewerteLoesung(String xmlEingabeDatei) {
		return apiAdapter.rate(xmlEingabeDatei);
	}

	@Override
	public String getName() {
		return "Fabian Spruhs";
	}

	@Override
	public String getMatrikelnummer() {
		return "3568695";
	}

	@Override
	public String getEmail() {
		return "fabian@spruhs.com";
	}
}
