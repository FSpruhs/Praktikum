package de.fernuni.kurs01584.ss23.adapters.infrastructure;

import de.fernuni.kurs01584.ss23.adapters.SnakeHuntXML;
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

/**
 * Adapter to save all data of a snake hunt instance in one file in XML format.
 */

public class SnakeHuntRepositoryAdapter implements SaveSnakeHuntInstanceOutPort {

    private static final Logger log = Logger.getLogger(SnakeHuntRepositoryAdapter.class.getName());

    /**
     * Saves all passed data of the snake hunt instance into the passed file in xml format.
     *
     * @param file The file path where the instance will be saved.
     * @param jungle The jungle to be saved.
     * @param snakeTypes The snake types to be saved.
     * @param targetDuration The target time in which the snake hunt must be solved.
     * @param actualDuration The time when the snake hunt was actually solved.
     * @param solution The solution to be saved.
     */
    @Override
    public void save(
            File file,
            Jungle jungle,
            Map<SnakeTypeId, SnakeType> snakeTypes,
            Duration targetDuration,
            Duration actualDuration,
            Solution solution
    ) {
        try {
            XMLOutputter xmlOutputter = new XMLOutputter();
            xmlOutputter.setFormat(Format.getPrettyFormat());
            xmlOutputter.output(snakeHuntInstanceToXML(
                    jungle,
                    snakeTypes,
                    targetDuration,
                    actualDuration,
                    solution
            ), new FileWriter(file));
            log.info("Saved in %s".formatted(file));
        } catch (IOException e) {
            log.warning(e.getMessage());
        }
    }

    private Document snakeHuntInstanceToXML(
            Jungle jungle,
            Map<SnakeTypeId, SnakeType> snakeTypes,
            Duration targetDuration,
            Duration actualDuration,
            Solution solution
    ) {
        Document result = createRootElement();
        result.getRootElement().addContent(durationToXML(targetDuration, actualDuration));
        result.getRootElement().addContent(jungleToXML(jungle));
        result.getRootElement().addContent(snakeTypesToXML(snakeTypes));
        if (solution != null) {
            result.getRootElement().addContent(solutionToXML(solution));
        }
        return result;
    }

    private Element solutionToXML(Solution solution) {
        Element result = new Element(SnakeHuntXML.SNAKES);
        for (Snake snake : solution.snakes()) {
            result.addContent(snakeToXML(snake));
        }
        return result;
    }

    private Element snakeToXML(Snake snake) {
        Element result = new Element(SnakeHuntXML.SNAKE);
        result.setAttribute(SnakeHuntXML.KIND, snake.snakeTypeId().value());
        for (SnakePart snakePart : snake.snakeParts()) {
            result.addContent(snakePartToXML(snakePart));
        }
        return result;
    }

    private Element snakePartToXML(SnakePart snakePart) {
        Element result = new Element(SnakeHuntXML.SNAKE_PART);
        result.setAttribute(SnakeHuntXML.FIELD_LOWER, String.valueOf(snakePart.fieldId().value()));
        return result;
    }

    private Element snakeTypesToXML(Map<SnakeTypeId, SnakeType> snakeTypes) {
        Element result = new Element(SnakeHuntXML.SNAKE_TYPES);
        for (SnakeType snakeType : snakeTypes.values()) {
            result.addContent(snakeTypeToXML(snakeType));
        }
        return result;
    }

    private Element snakeTypeToXML(SnakeType snakeType) {
        Element result = new Element(SnakeHuntXML.SNAKE_TYPE);
        result.setAttribute(SnakeHuntXML.ID, snakeType.snakeTypeId().value());
        result.setAttribute(SnakeHuntXML.POINTS, String.valueOf(snakeType.snakeValue()));
        result.setAttribute(SnakeHuntXML.COUNT, String.valueOf(snakeType.count()));
        result.addContent(characterBandToXML(snakeType));
        result.addContent(neighborhoodStructureToXML(snakeType));
        return result;
    }

    private Element characterBandToXML(SnakeType snakeType) {
        Element result = new Element(SnakeHuntXML.CHARACTER_BAND);
        result.setText(snakeType.characterBand());
        return result;
    }

    private Element neighborhoodStructureToXML(SnakeType snakeType) {
        Element result = new Element(SnakeHuntXML.NEIGHBORHOOD_STRUCTURE);
        result.setAttribute(
                SnakeHuntXML.TYPE,
                snakeType.neighborhoodStructure().getName().equals("Distance") ? SnakeHuntXML.DISTANCE : SnakeHuntXML.JUMP);
        for (Integer parameter : snakeType.neighborhoodStructure().getParameter()) {
            Element parameterXML = new Element(SnakeHuntXML.PARAMETER);
            parameterXML.setAttribute(SnakeHuntXML.VALUE, String.valueOf(parameter));
            result.addContent(parameterXML);
        }
        return result;
    }

    private Element jungleToXML(Jungle jungle) {
        Element result = new Element(SnakeHuntXML.JUNGLE);
        result.setAttribute(SnakeHuntXML.ROWS, String.valueOf(jungle.getJungleSize().rows()));
        result.setAttribute(SnakeHuntXML.COLUMNS, String.valueOf(jungle.getJungleSize().columns()));
        result.setAttribute(SnakeHuntXML.SIGN, jungle.getCharacters());
        for (JungleField jungleField : jungle.getJungleFields()) {
            result.addContent(jungleFieldToXML(jungleField));
        }
        return result;
    }

    private Element jungleFieldToXML(JungleField jungleField) {
        Element result = new Element(SnakeHuntXML.FIELD_UPPER);
        result.setAttribute(SnakeHuntXML.ID, jungleField.fieldId().value());
        result.setAttribute(SnakeHuntXML.ROW, String.valueOf(jungleField.coordinate().row()));
        result.setAttribute(SnakeHuntXML.COLUMN, String.valueOf(jungleField.coordinate().column()));
        result.setAttribute(SnakeHuntXML.USABILITY, String.valueOf(jungleField.getUsability()));
        result.setAttribute(SnakeHuntXML.POINTS, String.valueOf(jungleField.fieldValue()));
        result.setText(String.valueOf(jungleField.character()));
        return result;
    }

    private Element durationToXML(Duration targetDuration, Duration actualDuration) {
        Element result = new Element(SnakeHuntXML.TIME);
        result.setAttribute(new Attribute(SnakeHuntXML.UNIT, "s"));
        targetToXML(targetDuration, result);
        deliveryToXML(actualDuration, result);
        return result;
    }

    private void deliveryToXML(Duration actualDuration, Element result) {
        Element delivery = new Element(SnakeHuntXML.SUBMISSION);
        if (actualDuration != null) {
            delivery.setText(String.valueOf(Double.valueOf(actualDuration.toNanos()) / 1_000_000_000));
        }
        result.addContent(delivery);
    }

    private void targetToXML(Duration targetDuration, Element result) {
        Element target = new Element(SnakeHuntXML.TARGET);
        target.setText(String.valueOf(targetDuration.toSeconds() == 0 ? 1: targetDuration.toSeconds()));
        result.addContent(target);
    }

    private Document createRootElement() {
        Element snakeHunt = new Element(SnakeHuntXML.SNAKE_HUNT);
        Document document = new Document(snakeHunt);
        DocType docType = new DocType(SnakeHuntXML.SNAKE_HUNT, "schlangenjagd.dtd");
        document.setDocType(docType);
        return document;
    }
}
