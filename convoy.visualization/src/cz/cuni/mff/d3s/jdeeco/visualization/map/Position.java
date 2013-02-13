package cz.cuni.mff.d3s.jdeeco.visualization.map;

import java.io.Serializable;

public class Position implements Serializable {
	public Integer x = 0;
	public Integer y = 0;

	public Position() {}
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean equals(Object o) {
		return o != null && o instanceof Position && this.x == ((Position) o).x
				&& this.y == ((Position) o).y;
	}
}
