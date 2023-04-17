package de.fernuni.kurs01584.ss23.users;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import de.fernuni.kurs01584.ss23.domain.exception.NeighborhoodStructureNotFoundException;
import de.fernuni.kurs01584.ss23.domain.model.Coordinate;
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
			log.info("Loaded input file %s.".formatted(fileName));
		} catch (IOException | JDOMException e) {
			log.warning(e.getMessage());
			System.exit(0);
		}
	}
	
	public Jungle readJungle() {
		return new Jungle(
				getJungleRow(),
				getJungleColumn(),
				readSigns(),
				readJungleFields(getJungleRow(), getJungleColumn())
				);
	}
	
	private String readSigns() {
		return root.getChild("Dschungel").getAttributeValue("zeichen");
	}

	private int getJungleRow() {
		return Integer.parseInt(root.getChild("Dschungel").getAttributeValue("zeilen"));
		
	}
	
	private int getJungleColumn() {
		return Integer.parseInt(root.getChild("Dschungel").getAttributeValue("spalten"));
		
	}

	private List<JungleField> readJungleFields(int row, int column) {
		List<JungleField> result = new ArrayList<JungleField>(); 
		root.getChild("Dschungel").getChildren().forEach(field -> {
			result.add(readJungleFIeldId(field), readJungleField(field));
		});
		return result;
	}
	
	private int readJungleFIeldId(Element field) {
		return Integer.parseInt(field.getAttributeValue("id").substring(1));
	}

	private JungleField readJungleField(Element field) {
		return new JungleField(
				field.getAttributeValue("id"),
				readCoordinate(field),
				Integer.parseInt(field.getAttributeValue("verwendbarkeit")),
				Integer.parseInt(field.getAttributeValue("punkte")),
				field.getValue().charAt(0)
				);
	}
	
	private Coordinate readCoordinate(Element field) {
		return new Coordinate(readJungleFieldRow(field), readJungleFieldColumn(field));
	}

	private int readJungleFieldColumn(Element field) {
		return Integer.parseInt(field.getAttributeValue("spalte"));
	}

	private int readJungleFieldRow(Element field) {
		return Integer.parseInt(field.getAttributeValue("zeile"));
	}

	public Map<String, SnakeType> readSnakeTypes() {
		Map<String, SnakeType> result = new HashMap<>();
		root.getChild("Schlangenarten").getChildren().forEach(snakeType -> {
			result.put(snakeType.getAttributeValue("id"), readSnakeType(snakeType));
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
		String x = "";
		List<Element> snakeTypes = root.getChild("Schlangenarten").getChildren();
		for (Element snakeType : snakeTypes) {
			if (snakeType.getAttributeValue("id").equals(snake.getAttributeValue("art"))) {
				x = snakeType.getChild("Zeichenkette").getValue();
			}
		}
		int counter = 0;
		for (Element snakePart : snake.getChildren()) {
			result.add(readSnakePart(snakePart, counter, x));
			counter++;
		}
		return result;
	}

	private SnakePart readSnakePart(Element snakePart, int i, String x) {
		return new SnakePart(
				snakePart.getAttributeValue("feld"),
				x.charAt(i),
				new Coordinate(
						Integer.parseInt(snakePart.getAttributeValue("feld").substring(1)) / getJungleColumn(),
						Integer.parseInt(snakePart.getAttributeValue("feld").substring(1)) % getJungleColumn())
				);
	}
}
