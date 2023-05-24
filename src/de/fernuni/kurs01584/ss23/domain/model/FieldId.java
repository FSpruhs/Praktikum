package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleFieldException;

/**
 * Value object for the ID of a jungle field. The ID starts with an 'F' followed by a non-negative number.
 *
 * @param value the jungle Field ID.
 */
public record FieldId(String value) {
    public FieldId {
        if (value == null) {
            throw new InvalidJungleFieldException("Id is null");
        }

        if (value.charAt(0) != 'F' || !value.substring(1).matches("\\d+")) {
            throw new InvalidJungleFieldException("Invalid value!");
        }
    }
}
