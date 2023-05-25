package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidSnakeTypesException;

/**
 * Value object that represents a snake type id.
 *
 * @param value The snakes type id. The id must not be null.
 *              The id must start with an 'A' followed by a non-negative number.
 */
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
