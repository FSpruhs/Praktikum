package de.fernuni.kurs01584.ss23.domain.model.neighborhoodstructure;

import java.util.Objects;

public class Distance implements NeighborhoodStructure {
	
	
	private final int distance;

	public Distance(int distance) {
		this.distance = distance;
	}

	@Override
	public int hashCode() {
		return Objects.hash(distance);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Distance other = (Distance) obj;
		return distance == other.distance;
	}
	
	

}
