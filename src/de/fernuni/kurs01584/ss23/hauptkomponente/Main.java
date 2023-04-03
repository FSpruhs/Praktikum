package de.fernuni.kurs01584.ss23.hauptkomponente;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {

	private static final Logger log = Logger.getLogger(Main.class.getName());
	public static void main(String[] args) {
		System.out.println("Hi");
		LogManager manager = LogManager.getLogManager();
		try {
		    manager.readConfiguration(new FileInputStream("./res/logging.properties"));
		} catch (IOException e) {
		    log.warning(e.getMessage());
		}
		log.info("Test");

	}

}
