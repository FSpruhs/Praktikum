package de.fernuni.kurs01584.ss23.hauptkomponente;

import de.fernuni.kurs01584.ss23.adapters.users.APIAdapter;

import java.util.List;

public class Schlangenjagd implements SchlangenjagdAPI {


	@Override
	public boolean loeseProbleminstanz(String xmlEingabeDatei, String xmlAusgabeDatei) {
		APIAdapter apiAdapter = new APIAdapter(xmlEingabeDatei);
		return apiAdapter.solve(xmlAusgabeDatei);
	}

	@Override
	public boolean erzeugeProbleminstanz(String xmlEingabeDatei, String xmlAusgabeDatei) {
		APIAdapter apiAdapter = new APIAdapter(xmlEingabeDatei);
		return apiAdapter.create(xmlAusgabeDatei);
	}

	@Override
	public List<Fehlertyp> pruefeLoesung(String xmlEingabeDatei) {
		APIAdapter apiAdapter = new APIAdapter(xmlEingabeDatei);
		return apiAdapter.validate();
	}

	@Override
	public int bewerteLoesung(String xmlEingabeDatei) {
		APIAdapter apiAdapter = new APIAdapter(xmlEingabeDatei);
		return apiAdapter.rate();
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
