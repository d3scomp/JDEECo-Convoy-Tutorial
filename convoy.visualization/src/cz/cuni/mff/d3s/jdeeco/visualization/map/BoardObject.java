package cz.cuni.mff.d3s.jdeeco.visualization.map;

import java.io.Serializable;
import java.util.List;

public class BoardObject implements Serializable {
	public String id;
	public String label;
	public List<Position> positions;
	public String color;
	
	public BoardObject() {}
	
	public BoardObject(String id, List<Position> positions, String color, String label) {
		this.id = id;
		this.positions = positions;
		this.color = color;
		this.label = label;
	}
}
