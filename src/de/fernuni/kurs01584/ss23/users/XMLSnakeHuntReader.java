package de.fernuni.kurs01584.ss23.users;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import de.fernuni.kurs01584.ss23.domain.exception.NeighborhoodStructureNotFoundException;
import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.JungleField;
import de.fernuni.kurs01584.ss23.domain.model.Snake;
import de.fernuni.kurs01584.ss23.domain.model.SnakePart;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.Solution;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.Distance;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.Jump;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

public class XMLSnakeHuntReader {
	
	private static final Logger log = Logger.getLogger(XMLSnakeHuntReader.class.getName());
	private Element root;
	
	public XMLSnakeHuntReader(String fileName) {
		SAXBuilder sax = new SAXBuilder();
		sax.setValidation(true);
		try {
			this.root = sax.build(new File(fileName)).getRootElement();
		} catch (IOException | JDOMException e) {
			log.warning(e.getMessage());
			System.exit(0);
		}
	}
	
	public Jungle readJungle() {
		return new Jungle(
				Integer.parseInt(root.getChild("Dschungel").getAttributeValue("zeilen")),
				Integer.parseInt(root.getChild("Dschungel").getAttributeValue("spalten")),
				root.getChild("Dschungel").getAttributeValue("zeichen"),
				readJungleFields()
				);
	}
	
	private List<JungleField> readJungleFields() {
		List<JungleField> result = new LinkedList<>();
		root.getChild("Dschungel").getChildren().forEach(field -> {
			result.add(readJungleField(field));
		});
		return result;
	}
	
	private JungleField readJungleField(Element field) {
		return new JungleField(
				field.getAttributeValue("id"),
				Integer.parseInt(field.getAttributeValue("zeile")),
				Integer.parseInt(field.getAttributeValue("spalte")),
				Integer.parseInt(field.getAttributeValue("verwendbarkeit")),
				Integer.parseInt(field.getAttributeValue("punkte")),
				field.getValue().charAt(0)
				);
	}
	
	public List<SnakeType> readSnakeTypes() {
		List<SnakeType> result = new LinkedList<>();
		root.getChild("Schlangenarten").getChildren().forEach(snakeType -> {
			result.add(readSnakeType(snakeType))  ;
		});
		return result;
	}
	
	private SnakeType readSnakeType(Element snakeType) {
		return new SnakeType(
				snakeType.getAttributeValue("id"),
				Integer.parseInt(snakeType.getAttributeValue("punkte")),
				Integer.parseInt(snakeType.getAttributeValue("anzahl")),
				snakeType.getChild("Zeichenkette").getValue(),
				readNeighborhoodStructure(snakeType.getChild("Nachbarschaftsstruktur"))
				);
	}
	
	private NeighborhoodStructure readNeighborhoodStructure(Element neighborhoodStructure) {
		if (neighborhoodStructure.getAttributeValue("typ").equals("Distanz")) {
			return readDistance(neighborhoodStructure);
		} 
		if (neighborhoodStructure.getAttributeValue("typ").equals("Sprung")) {
			return readJump(neighborhoodStructure);
		}
		throw new NeighborhoodStructureNotFoundException(neighborhoodStructure.getAttributeValue("typ").toString());
	}
	
	private NeighborhoodStructure readDistance(Element neighborhoodStructure) {
		return new Distance(Integer.parseInt(neighborhoodStructure.getChild("Parameter").getAttributeValue("wert")));
	}

	private NeighborhoodStructure readJump(Element neighborhoodStructure) {
		return new Jump(
				Integer.parseInt(neighborhoodStructure.getChildren().get(0).getAttributeValue("wert")),
				Integer.parseInt(neighborhoodStructure.getChildren().get(1).getAttributeValue("wert"))
				);
	}
	
	public Duration readDurationInSeconds() {
		return Duration.ofSeconds( (long) Float.parseFloat(root.getChild("Zeit").getChild("Vorgabe").getValue()));
	}
	
	public Solution readSolution() {
		if (root.getChild("Schlangen") != null) {
			Solution result = new Solution();
			result.loadSnakes(readSnakes());
			return result;
		}
		return null;
	}

	private List<Snake> readSnakes() {
		List<Snake> result = new LinkedList<>();
		root.getChild("Schlangen").getChildren().forEach(snake -> {
			result.add(readSnake(snake));
		});
		return result;
	}

	private Snake readSnake(Element snake) {
		return new Snake(
				snake.getAttributeValue("art"),
				readSnakeParts(snake)
				);
	}

	private List<SnakePart> readSnakeParts(Element snake) {
		List<SnakePart> result = new LinkedList<>();
		snake.getChildren().forEach(snakePart -> {
			result.add(readSnakePart(snakePart));
		});
		return result;
	}

	private SnakePart readSnakePart(Element snakePart) {
		return new SnakePart(snakePart.getAttributeValue("feld"));
	}
}
