package de.fernuni.kurs01584.ss23.adapters.users;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.fernuni.kurs01584.ss23.adapters.SnakeHuntXML;
import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;
import de.fernuni.kurs01584.ss23.domain.model.*;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import de.fernuni.kurs01584.ss23.domain.exception.NeighborhoodStructureNotFoundException;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.Distance;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.Jump;
import de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure.NeighborhoodStructure;

/**
 * Read the data from a snake hunts xml file.
 */
public class XMLSnakeHuntReader {
	
	private static final Logger log = Logger.getLogger(XMLSnakeHuntReader.class.getName());
	private static final int MINUTES_TO_SECOND_FACTOR = 60;
	private static final int HOURS_TO_SECOND_FACTOR = 3600;
	private static final int DAYS_TO_SECOND_FACTOR = 86400;
	private Element root;

	/**
	 * Constructor for the XML Snake Hunt Reader.
	 *
	 * @param file The path to the XML file to be read.
	 */
	public XMLSnakeHuntReader(File file) {
		SAXBuilder sax = new SAXBuilder();
		sax.setValidation(true);
		try {
			this.root = sax.build(file).getRootElement();
			log.info("Loaded input file %s.".formatted(file.getName()));
		} catch (IOException | JDOMException e) {
			log.warning(e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Reads out the jungle of XML the snake hunt file.
	 *
	 * @return the read jungle.
	 */
	public Jungle readJungle() {
		return new Jungle(
				new JungleSize(getJungleRow(), getJungleColumn()),
				readSigns(),
				readJungleFields()
				);
	}
	
	private String readSigns() {
		return getJungle().getAttributeValue(SnakeHuntXML.SIGN);
	}

	private int getJungleRow() {
		return Integer.parseInt(getJungle().getAttributeValue(SnakeHuntXML.ROWS));
	}
	
	private int getJungleColumn() {
		return Integer.parseInt(getJungle().getAttributeValue(SnakeHuntXML.COLUMNS));
	}

	private Element getJungle() {
		return root.getChild(SnakeHuntXML.JUNGLE);
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
				Integer.parseInt(field.getAttributeValue(SnakeHuntXML.POINTS)),
				Integer.parseInt(field.getAttributeValue(SnakeHuntXML.USABILITY)),
				field.getValue().charAt(0),
				new LinkedList<>()
				);
	}

	private String readJungleFieldId(Element field) {
		return field.getAttributeValue(SnakeHuntXML.ID);
	}
	
	private Coordinate readCoordinate(Element field) {
		return new Coordinate(readJungleFieldRow(field), readJungleFieldColumn(field));
	}

	private int readJungleFieldColumn(Element field) {
		return Integer.parseInt(field.getAttributeValue(SnakeHuntXML.COLUMN));
	}

	private int readJungleFieldRow(Element field) {
		return Integer.parseInt(field.getAttributeValue(SnakeHuntXML.ROW));
	}

	/**
	 * Reads out the snake types of the XML snake hunt file.
	 *
	 * @return the read snake types as a map of snake type id and snake type.
	 */
	public Map<SnakeTypeId, SnakeType> readSnakeTypes() {
		Map<SnakeTypeId, SnakeType> result = new HashMap<>();
		readSnakeType().getChildren().forEach(snakeType ->
			result.put(new SnakeTypeId(snakeType.getAttributeValue(SnakeHuntXML.ID)), readSnakeType(snakeType))
		);
		return result;
	}
	
	private SnakeType readSnakeType(Element snakeType) {
		return new SnakeType(
				new SnakeTypeId(snakeType.getAttributeValue(SnakeHuntXML.ID)),
				Integer.parseInt(snakeType.getAttributeValue(SnakeHuntXML.POINTS)),
				Integer.parseInt(snakeType.getAttributeValue(SnakeHuntXML.COUNT)),
				snakeType.getChild(SnakeHuntXML.CHARACTER_BAND).getValue(),
				readNeighborhoodStructure(snakeType.getChild(SnakeHuntXML.NEIGHBORHOOD_STRUCTURE))
				);
	}
	
	private NeighborhoodStructure readNeighborhoodStructure(Element neighborhoodStructure) {
		if (readNeighborhoodType(neighborhoodStructure).equals(SnakeHuntXML.DISTANCE)) {
			return readDistance(neighborhoodStructure);
		} 
		if (readNeighborhoodType(neighborhoodStructure).equals(SnakeHuntXML.JUMP)) {
			return readJump(neighborhoodStructure);
		}
		throw new NeighborhoodStructureNotFoundException(readNeighborhoodType(neighborhoodStructure));
	}

	private String readNeighborhoodType(Element neighborhoodStructure) {
		return neighborhoodStructure.getAttributeValue(SnakeHuntXML.TYPE);
	}
	
	private NeighborhoodStructure readDistance(Element neighborhoodStructure) {
		return new Distance(Integer.parseInt(neighborhoodStructure.getChild(SnakeHuntXML.PARAMETER).getAttributeValue(SnakeHuntXML.VALUE)));
	}

	private NeighborhoodStructure readJump(Element neighborhoodStructure) {
		return new Jump(
				Integer.parseInt(neighborhoodStructure.getChildren().get(0).getAttributeValue(SnakeHuntXML.VALUE)),
				Integer.parseInt(neighborhoodStructure.getChildren().get(1).getAttributeValue(SnakeHuntXML.VALUE))
				);
	}

	/**
	 * Reads out the target time in seconds of the XML snake hunt file.
	 *
	 * @return the read target time.
	 */
	public Duration readDuration() {
		String timeUnit = root.getChild(SnakeHuntXML.TIME).getAttributeValue(SnakeHuntXML.UNIT);
		switch (timeUnit) {
			case SnakeHuntXML.MILLI_SECONDS -> {
				return Duration.ofMillis(getDuration());
			}
			case SnakeHuntXML.SECONDS -> {
				return Duration.ofSeconds(getDuration());
			}
			case SnakeHuntXML.MINUTES -> {
				return Duration.ofSeconds(getDurationMinutesToSeconds());
			}
			case SnakeHuntXML.HOURS -> {
				return Duration.ofSeconds(getDurationHoursToSeconds());
			}
			case SnakeHuntXML.DAYS -> {
				return Duration.ofSeconds(getDurationDaysToSeconds());
			}
			default -> {
				return Duration.ofSeconds(30);
			}
		}
	}

	private long getDuration() {
		return (long) Double.parseDouble(getDurationValue());
	}

	private long getDurationMinutesToSeconds() {
		return (long) (Double.parseDouble(getDurationValue()) * MINUTES_TO_SECOND_FACTOR);
	}

	private long getDurationHoursToSeconds() {
		return (long) (Double.parseDouble(getDurationValue()) * HOURS_TO_SECOND_FACTOR);
	}

	private long getDurationDaysToSeconds() {
		return (long) (Double.parseDouble(getDurationValue()) * DAYS_TO_SECOND_FACTOR);
	}

	private String getDurationValue() {
		return root.getChild(SnakeHuntXML.TIME).getChild(SnakeHuntXML.TARGET).getValue();
	}

	/**
	 * Reads out the target time in seconds of the XML snake hunt file.
	 *
	 * @return the read solution if it exists, otherwise null.
	 */
	public Solution readSolution() {
		if (root.getChild(SnakeHuntXML.SNAKES) != null) {
			Solution result = new Solution(new LinkedList<>());
			result.loadSnakes(readSnakes());
			return result;
		}
		return null;
	}

	private List<Snake> readSnakes() {
		return root.getChild(SnakeHuntXML.SNAKES).getChildren().stream().map(this::readSnake).toList();
	}

	private Snake readSnake(Element snake) {
		return new Snake(
				new SnakeTypeId(readeSnakeType(snake)),
				readSnakeParts(snake),
				getNeighborhoodStructureById(snake)
				);
	}

	private NeighborhoodStructure getNeighborhoodStructureById(Element snake) {
		return readNeighborhoodStructure(readSnakeTypeByType(readeSnakeType(snake)).getChild(SnakeHuntXML.NEIGHBORHOOD_STRUCTURE));
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
		return readSnakeTypeByType(readeSnakeType(snake)).getChild(SnakeHuntXML.CHARACTER_BAND).getValue();
	}

	private String readeSnakeType(Element snake) {
		return snake.getAttributeValue(SnakeHuntXML.KIND);
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
		return snakeType.getAttributeValue(SnakeHuntXML.ID);
	}

	private Element readSnakeType() {
		return root.getChild(SnakeHuntXML.SNAKE_TYPES);
	}

	private SnakePart readSnakePart(Element snakePart, char character) {
		return new SnakePart(
				new FieldId(readSnakePartField(snakePart)),
				character,
				new Coordinate(
						readRow(snakePart),
						readColumn(snakePart))
				);
	}

	private int readColumn(Element snakePart) {
		return readSnakePartFieldNumber(snakePart) % getJungleColumn();
	}

	private int readRow(Element snakePart) {
		return readSnakePartFieldNumber(snakePart) / getJungleColumn();
	}

	private String readSnakePartField(Element snakePart) {
		return snakePart.getAttributeValue(SnakeHuntXML.FIELD_LOWER);
	}

	private int readSnakePartFieldNumber(Element snakePart) {
		return Integer.parseInt(readSnakePartField(snakePart).substring(1));
	}
}
