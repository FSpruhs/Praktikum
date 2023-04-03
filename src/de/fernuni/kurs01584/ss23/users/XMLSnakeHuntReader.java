package de.fernuni.kurs01584.ss23.users;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.XMLConstants;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.JDOMParseException;
import org.jdom2.input.SAXBuilder;

import de.fernuni.kurs01584.ss23.domain.model.SnakePart;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.hauptkomponente.Main;

public class XMLSnakeHuntReader {
	
	private static final Logger log = Logger.getLogger(XMLSnakeHuntReader.class.getName());
	
	private void readFile(String fileName) {
		
		SAXBuilder sax = new SAXBuilder();
		sax.setValidation(true);
		try {

			Element root = sax.build(new File(fileName)).getRootElement();
			
			log.info(root.getChild("Zeit").getAttributeValue("einheit").toString());
			log.info(root.getChild("Zeit").getChild("Vorgabe").getValue().toString());
			
			log.info(root.getChild("Dschungel").getAttributeValue("zeilen"));
			log.info(root.getChild("Dschungel").getAttributeValue("spalten"));
			log.info(root.getChild("Dschungel").getAttributeValue("zeichen"));

			
			root.getChild("Dschungel").getChildren().forEach(field -> {
				log.info(field.getAttributeValue("id"));
				log.info(field.getAttributeValue("zeile"));
				log.info(field.getAttributeValue("spalte"));
				log.info(field.getAttributeValue("verwendbarkeit"));
				log.info(field.getAttributeValue("punkte"));
				log.info(field.getValue());
			});
			
			root.getChild("Schlangenarten").getChildren().forEach(snakeType -> {
				log.info(snakeType.getAttributeValue("id"));
				log.info(snakeType.getAttributeValue("punkte"));
				log.info(snakeType.getAttributeValue("anzahl"));
				log.info(snakeType.getChild("Zeichenkette").getValue());
				log.info(snakeType.getChild("Nachbarschaftsstruktur").getAttributeValue("typ"));
				if (snakeType.getChild("Nachbarschaftsstruktur").getAttributeValue("typ").equals("Distanz")) {
					log.info(snakeType.getChild("Nachbarschaftsstruktur").getChild("Parameter").getAttributeValue("wert"));
				}
				if (snakeType.getChild("Nachbarschaftsstruktur").getAttributeValue("typ").equals("Sprung")) {
					log.info(snakeType.getChild("Nachbarschaftsstruktur").getChildren().get(0).getAttributeValue("wert"));
					log.info(snakeType.getChild("Nachbarschaftsstruktur").getChildren().get(1).getAttributeValue("wert"));
				}
			});;
			
			if (root.getChild("Schlangen") != null) {
				root.getChild("Schlangen").getChildren().forEach(snake -> {
					log.info(snake.getAttributeValue("art"));
					snake.getChildren().forEach(snakePart -> {
						log.info(snakePart.getAttributeValue("feld"));
					});
				});
			}
			
		} catch (IOException | JDOMException e) {
			log.warning(e.getMessage());
			System.exit(0);
		}
		
		
	}
	
	public static void main(String[] args) {
		XMLSnakeHuntReader xmlSnakeHuntReader = new XMLSnakeHuntReader();
		xmlSnakeHuntReader.readFile("./res/sj_p5_loesung.xml");
		
	}
	
	

}
