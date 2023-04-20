package de.fernuni.kurs01584.ss23.domain.model;

import de.fernuni.kurs01584.ss23.domain.exception.InvalidJungleFieldException;

public record FieldId(String id) {
    public FieldId {
        if (id == null) {
            throw new InvalidJungleFieldException("Id is null");
        }

        if (id.charAt(0) != 'F' || !id.substring(1).matches("\\d+")) {
            throw new InvalidJungleFieldException("Invalid id!");
        }
    }
}
