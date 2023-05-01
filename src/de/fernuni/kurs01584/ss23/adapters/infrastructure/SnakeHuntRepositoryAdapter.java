package de.fernuni.kurs01584.ss23.adapters.infrastructure;

import de.fernuni.kurs01584.ss23.adapters.XMLDictionary;
import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.application.ports.out.SaveSnakeHuntInstanceOutPort;
import org.jdom2.Attribute;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

public class SnakeHuntRepositoryAdapter implements SaveSnakeHuntInstanceOutPort {

    private static final Logger log = Logger.getLogger(SnakeHuntRepositoryAdapter.class.getName());

    @Override
    public void save(File file, Jungle jungle, Map<SnakeTypeId, SnakeType> snakeTypes, Duration durationInSeconds, Solution solution) {
        try {
            XMLOutputter xmlOutputter = new XMLOutputter();
            xmlOutputter.setFormat(Format.getPrettyFormat());
            xmlOutputter.output(snakeHuntInstanceToXML(jungle, snakeTypes, durationInSeconds, solution), new FileWriter(file));
        } catch (IOException e) {
            log.warning(e.getMessage());
        }
    }

    private Document snakeHuntInstanceToXML(Jungle jungle, Map<SnakeTypeId, SnakeType> snakeTypes, Duration durationInSeconds, Solution solution) {
        Document result = createRootElement();
        result.getRootElement().addContent(durationToXML(durationInSeconds));
        result.getRootElement().addContent(jungleToXML(jungle));
        result.getRootElement().addContent(snakeTypesToXML(snakeTypes));
        if (solution != null) {
            result.getRootElement().addContent(solutionToXML(solution));
        }
        return result;
    }

    private Element solutionToXML(Solution solution) {
        Element result = new Element(XMLDictionary.SNAKES);
        for (Snake snake : solution.getSnakes()) {
            result.addContent(snakeToXML(snake));
        }
        return result;
    }

    private Element snakeToXML(Snake snake) {
        Element result = new Element(XMLDictionary.SNAKES);
        result.setAttribute(XMLDictionary.KIND, snake.snakeTypeId().value());
        for (SnakePart snakePart : snake.snakeParts()) {
            result.addContent(snakePartToXML(snakePart));
        }
        return result;
    }

    private Element snakePartToXML(SnakePart snakePart) {
        Element result = new Element(XMLDictionary.SNAKE_PART);
        result.setAttribute(XMLDictionary.FIELD, String.valueOf(snakePart.fieldId().value()));
        return result;
    }

    private Element snakeTypesToXML(Map<SnakeTypeId, SnakeType> snakeTypes) {
        Element result = new Element(XMLDictionary.SNAKE_TYPE);
        for (SnakeType snakeType : snakeTypes.values()) {
            result.addContent(snakeTypeToXML(snakeType));
        }
        return result;
    }

    private Element snakeTypeToXML(SnakeType snakeType) {
        Element result = new Element(XMLDictionary.SNAKE_TYPE);
        result.setAttribute(XMLDictionary.ID, snakeType.snakeTypeId().value());
        result.setAttribute(XMLDictionary.POINTS, String.valueOf(snakeType.snakeValue()));
        result.setAttribute(XMLDictionary.COUNT, String.valueOf(snakeType.count()));
        result.addContent(characterBandToXML(snakeType));
        result.addContent(neighborhoodStructureToXML(snakeType));
        return result;
    }

    private Element characterBandToXML(SnakeType snakeType) {
        Element result = new Element(XMLDictionary.CHARACTER_BAND);
        result.setText(snakeType.characterBand());
        return result;
    }

    private Element neighborhoodStructureToXML(SnakeType snakeType) {
        Element result = new Element(XMLDictionary.NEIGHBORHOOD_STRUCTURE);
        result.setAttribute(XMLDictionary.TYPE, snakeType.neighborhoodStructure().getName().equals("Distance") ? XMLDictionary.DISTANCE : XMLDictionary.JUMP);
        for (Integer parameter : snakeType.neighborhoodStructure().getParameter()) {
            Element parameterXML = new Element(XMLDictionary.PARAMETER);
            parameterXML.setAttribute(XMLDictionary.VALUE, String.valueOf(parameter));
            result.addContent(parameterXML);
        }
        return result;
    }

    private Element jungleToXML(Jungle jungle) {
        Element result = new Element(XMLDictionary.JUNGLE);
        result.setAttribute(XMLDictionary.ROWS, String.valueOf(jungle.getJungleSize().rows()));
        result.setAttribute(XMLDictionary.COLUMNS, String.valueOf(jungle.getJungleSize().columns()));
        result.setAttribute(XMLDictionary.SIGN, jungle.getCharacters());
        for (JungleField jungleField : jungle.getJungleFields()) {
            result.addContent(jungleFieldToXML(jungleField));
        }
        return result;
    }

    private Element jungleFieldToXML(JungleField jungleField) {
        Element result = new Element(XMLDictionary.FIELD);
        result.setAttribute(XMLDictionary.ID, jungleField.getId().value());
        result.setAttribute(XMLDictionary.ROW, String.valueOf(jungleField.getCoordinate().row()));
        result.setAttribute(XMLDictionary.COLUMN, String.valueOf(jungleField.getCoordinate().column()));
        result.setAttribute(XMLDictionary.USABILITY, String.valueOf(jungleField.getUsability()));
        result.setAttribute(XMLDictionary.POINTS, String.valueOf(jungleField.getFieldValue()));
        result.setText(String.valueOf(jungleField.getCharacter()));
        return result;
    }

    private Element durationToXML(Duration durationInSeconds) {
        Element result = new Element(XMLDictionary.TIME);
        result.setAttribute(new Attribute(XMLDictionary.UNIT, "s"));
        Element target = new Element(XMLDictionary.TARGET);
        target.setText(String.valueOf(durationInSeconds.toSeconds()));
        Element delivery = new Element(XMLDictionary.SUBMISSION);
        result.addContent(target);
        result.addContent(delivery);
        return result;
    }

    private Document createRootElement() {
        Element snakeHunt = new Element(XMLDictionary.SNAKE_HUNT);
        Document document = new Document(snakeHunt);
        DocType docType = new DocType(XMLDictionary.SNAKE_HUNT, "schlangenjagd.dtd");
        document.setDocType(docType);
        return document;
    }
}
