package de.fernuni.kurs01584.ss23.domain.model;

import java.util.List;

public record JungleDTO(
        int rows,
        int columns,
        String characters,
        List<JungleFieldDTO> jungleFields
) {
}
