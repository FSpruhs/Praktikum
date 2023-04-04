package de.fernuni.kurs01584.ss23.domain.model;

import java.lang.reflect.Field;
import java.util.Objects;

public class SnakePart {
	private final String fieldId;
	
	public SnakePart(String fieldId) {
		this.fieldId = fieldId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SnakePart other = (SnakePart) obj;
		return Objects.equals(fieldId, other.fieldId);
	}

	@Override
	public String toString() {
		return "SnakePart [fieldId=" + fieldId + "]";
	}
	
	
}
