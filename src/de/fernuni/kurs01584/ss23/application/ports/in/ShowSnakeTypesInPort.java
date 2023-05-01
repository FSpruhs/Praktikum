package de.fernuni.kurs01584.ss23.application.ports.in;

import de.fernuni.kurs01584.ss23.domain.model.SnakeType;
import de.fernuni.kurs01584.ss23.domain.model.SnakeTypeId;

import java.util.List;

public interface ShowSnakeTypesInPort {

    List<SnakeType> showSnakeTypes();

    SnakeType showSnakeTypesById(SnakeTypeId snakeTypeId);
}
