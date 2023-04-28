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

import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;
import de.fernuni.kurs01584.ss23.domain.model.*;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import de.fernuni.kurs01584.ss23.domain.exception.NeighborhoodStructureNotFoundException;
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
				new JungleSize(getJungleRow(), getJungleColumn()),
				readSigns(),
				readJungleFields()
				);
	}
	
	private String readSigns() {
		return getJungle().getAttributeValue("zeichen");
	}

	private int getJungleRow() {
		return Integer.parseInt(getJungle().getAttributeValue("zeilen"));
	}
	
	private int getJungleColumn() {
		return Integer.parseInt(getJungle().getAttributeValue("spalten"));
	}

	private Element getJungle() {
		return root.getChild("Dschungel");
	}

	private List<JungleField> readJungleFields() {
		List<JungleField> result = new ArrayList<>();
		getJungle().getChildren().forEach(field ->
			result.add(readJungleFieldIdInteger(field), readJungleField(field))
		);
		return result;
	}
	
	private int readJungleFieldIdInteger(Element field) {
		return Integer.parseInt(readJungleFieldId(field).substring(1));
	}

	private JungleField readJungleField(Element field) {
		return new JungleField(
				new FieldId(readJungleFieldId(field)),
				readCoordinate(field),
				Integer.parseInt(field.getAttributeValue("verwendbarkeit")),
				Integer.parseInt(field.getAttributeValue("punkte")),
				field.getValue().charAt(0)
				);
	}

	private String readJungleFieldId(Element field) {
		return field.getAttributeValue("id");
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
		readSnakeType().getChildren().forEach(snakeType ->
			result.put(snakeType.getAttributeValue("id"), readSnakeType(snakeType))
		);
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
		if (readNeighborhoodType(neighborhoodStructure).equals("Distanz")) {
			return readDistance(neighborhoodStructure);
		} 
		if (readNeighborhoodType(neighborhoodStructure).equals("Sprung")) {
			return readJump(neighborhoodStructure);
		}
		throw new NeighborhoodStructureNotFoundException(readNeighborhoodType(neighborhoodStructure));
	}

	private String readNeighborhoodType(Element neighborhoodStructure) {
		return neighborhoodStructure.getAttributeValue("typ");
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
		return root.getChild("Schlangen").getChildren().stream().map(this::readSnake).toList();
	}

	private Snake readSnake(Element snake) {
		return new Snake(
				readeSnakeType(snake),
				readSnakeParts(snake),
				getNeighborhoodStructureById(snake)
				);
	}

	private NeighborhoodStructure getNeighborhoodStructureById(Element snake) {
		return readNeighborhoodStructure(readSnakeTypeByType(readeSnakeType(snake)).getChild("Nachbarschaftsstruktur"));
	}

	private List<SnakePart> readSnakeParts(Element snake) {
		List<SnakePart> result = new LinkedList<>();
		int counter = 0;
		for (Element snakePart : snake.getChildren()) {
			result.add(readSnakePart(snakePart, getCharacterBandOfSnakeType(snake).charAt(counter)));
			counter++;
		}
		return result;
	}

	private String getCharacterBandOfSnakeType(Element snake) {
		return readSnakeTypeByType(readeSnakeType(snake)).getChild("Zeichenkette").getValue();
	}

	private String readeSnakeType(Element snake) {
		return snake.getAttributeValue("art");
	}

	private Element readSnakeTypeByType(String type) {
		return readSnakeType()
				.getChildren()
				.stream()
				.filter(snakeType -> readSnakeTypeId(snakeType).equals(type))
				.findFirst()
				.orElseThrow(() -> new InvalidSnakeTypesException(type));
	}

	private String readSnakeTypeId(Element snakeType) {
		return snakeType.getAttributeValue("id");
	}

	private Element readSnakeType() {
		return root.getChild("Schlangenarten");
	}

	private SnakePart readSnakePart(Element snakePart, char character) {
		return new SnakePart(
				new FieldId(readSnakePartField(snakePart)),
				character,
				new Coordinate(
						readSnakePartFieldNumber(snakePart) / getJungleColumn(),
						readSnakePartFieldNumber(snakePart) % getJungleColumn())
				);
	}

	private String readSnakePartField(Element snakePart) {
		return snakePart.getAttributeValue("feld");
	}

	private int readSnakePartFieldNumber(Element snakePart) {
		return Integer.parseInt(readSnakePartField(snakePart).substring(1));
	}
}
