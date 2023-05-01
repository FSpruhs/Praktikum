package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;

public record SnakeTypeId(String value) {

    public SnakeTypeId {
        if (value == null) {
            throw new InvalidSnakeTypesException("Id is null");
        }

        if (value.charAt(0) != 'A' || !value.substring(1).matches("\\d+")) {
            throw new InvalidSnakeTypesException("Invalid value!");
        }
    }

}
