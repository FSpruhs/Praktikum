package de.fernuni.kurs01584.ss23.users;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.XMLConstants;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import de.fernuni.kurs01584.ss23.hauptkomponente.Main;

public class XMLSnakeHuntReader {
	
	private static final Logger log = Logger.getLogger(Main.class.getName());
	
	private void readFile(String fileName) {
		
		try {
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(new File(fileName));
			log.info(doc.toString());
			
			
		} catch (IOException | JDOMException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		XMLSnakeHuntReader xmlSnakeHuntReader = new XMLSnakeHuntReader();
		xmlSnakeHuntReader.readFile("./res/sj_p1_unvollstaendig.xml");
		
	}
	
	

}
