package de.fernuni.kurs01584.ss23.domain.model;

public record JungleFieldDTO(
        String fieldId,
        int row,
        int column,
        int fieldValue,
        int usability,
        char character
) {
}
