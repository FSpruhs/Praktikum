package de.fernuni.kurs01584.ss23.infrastructure;

import de.fernuni.kurs01584.ss23.domain.model.Jungle;
import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.Solution;
import de.fernuni.kurs01584.ss23.domain.ports.out.SaveSnakeHuntInstanceOutPort;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import javax.print.Doc;
import java.io.File;
import java.time.Duration;
import java.util.Map;

public class SnakeHuntRepository implements SaveSnakeHuntInstanceOutPort {
    @Override
    public void save(File file, Jungle jungle, Map<String, SnakeType> snakeTypes, Duration durationInSeconds, Solution solution) {
        Element snakeHunt = new Element("Schnlangensuche");
        Document document = new Document(snakeHunt);

        Element duration = new Element("Zeit");
        duration.setAttribute(new Attribute("einheit", "ns"));
        Element target = new Element("Vorgabe");
        target.setText(String.valueOf(durationInSeconds.toNanos()));
        Element delivery = new Element("Abgabe");
        duration.addContent(target);
        duration.addContent(delivery);
    }
}
