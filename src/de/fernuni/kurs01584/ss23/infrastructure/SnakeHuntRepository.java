package de.fernuni.kurs01584.ss23.infrastructure;

import de.fernuni.kurs01584.ss23.domain.model.*;
import de.fernuni.kurs01584.ss23.domain.ports.out.SaveSnakeHuntInstanceOutPort;
import de.fernuni.kurs01584.ss23.users.XMLSnakeHuntInizializer;
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

public class SnakeHuntRepository implements SaveSnakeHuntInstanceOutPort {

    private static final Logger log = Logger.getLogger(SnakeHuntRepository.class.getName());

    @Override
    public void save(File file, Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds, Solution solution) {
        Element snakeHunt = new Element("Schlangenjagd");
        Document document = new Document(snakeHunt);

        DocType docType = new DocType("Schlangenjagd", "schlangenjagd.dtd");
        document.setDocType(docType);

        Element duration = new Element("Zeit");
        duration.setAttribute(new Attribute("einheit", "s"));
        Element target = new Element("Vorgabe");
        target.setText(String.valueOf(durationInSeconds.toSeconds()));
        Element delivery = new Element("Abgabe");
        duration.addContent(target);
        duration.addContent(delivery);
        document.getRootElement().addContent(duration);

        Element jungleXML = new Element("Dschungel");
        jungleXML.setAttribute("zeilen", String.valueOf(jungle.getJungleSize().rows()));
        jungleXML.setAttribute("spalten", String.valueOf(jungle.getJungleSize().columns()));
        jungleXML.setAttribute("zeichen", jungle.getCharacters());
        for (JungleField jungleField : jungle.getJungleFields()) {
            Element jungleFieldXML = new Element("Feld");
            jungleFieldXML.setAttribute("id", jungleField.getId());
            jungleFieldXML.setAttribute("zeile", String.valueOf(jungleField.getCoordinate().row()));
            jungleFieldXML.setAttribute("spalte", String.valueOf(jungleField.getCoordinate().column()));
            jungleFieldXML.setAttribute("verwendbarkeit", String.valueOf(jungleField.getUsability()));
            jungleFieldXML.setAttribute("punkte", String.valueOf(jungleField.getFieldValue()));
            jungleFieldXML.setText(String.valueOf(jungleField.getCharacter()));
            jungleXML.addContent(jungleFieldXML);
        }
        document.getRootElement().addContent(jungleXML);

        Element snakeTypesXML = new Element("Schlangenarten");
        for (SnakeType snakeType : snakeTypes.values()) {
            Element snakeTypeXML = new Element("Schlangenart");
            snakeTypeXML.setAttribute("id", snakeType.getId());
            snakeTypeXML.setAttribute("punkte", String.valueOf(snakeType.getSnakeValue()));
            snakeTypeXML.setAttribute("anzahl", String.valueOf(snakeType.getCount()));
            Element characterBandXMl = new Element("Zeichenkette");
            characterBandXMl.setText(snakeType.getCharacterBand());
            snakeTypeXML.addContent(characterBandXMl);
            Element neighborhoodStructureXML = new Element("Nachbarschaftsstruktur");
            neighborhoodStructureXML.setAttribute("typ", snakeType.getNeighborhoodStructure().getName().equals("Distance") ? "Distanz" : "Sprung");
            for (Integer parameter : snakeType.getNeighborhoodStructure().getParameter()) {
                Element parameterXML = new Element("Parameter");
                parameterXML.setAttribute("wert", String.valueOf(parameter));
                neighborhoodStructureXML.addContent(parameterXML);
            }
            snakeTypeXML.addContent(neighborhoodStructureXML);
            snakeTypesXML.addContent(snakeTypeXML);
        }
        document.getRootElement().addContent(snakeTypesXML);

        if (solution != null) {
            Element snakesXML = new Element("Schlangen");
            for (Snake snake : solution.getSnakes()) {
                Element snakeXML = new Element("Schlange");
                snakeXML.setAttribute("art", snake.getSnakeTypeId());
                for (SnakePart snakePart : snake.getSnakeParts()) {
                    Element snakePartXML = new Element("Schlangenglied");
                    snakePartXML.setAttribute("feld", String.valueOf(snakePart.fieldId().id()));
                    snakeXML.addContent(snakePartXML);
                }
                snakesXML.addContent(snakeXML);
            }
            document.getRootElement().addContent(snakesXML);
        }






        try {
            FileWriter fileWriter = new FileWriter(file);
            XMLOutputter xmlOutputter = new XMLOutputter();
            xmlOutputter.setFormat(Format.getPrettyFormat());
            xmlOutputter.output(document, fileWriter);
        } catch (IOException e) {
            log.warning(e.getMessage());
            e.printStackTrace();
        }
    }
}
