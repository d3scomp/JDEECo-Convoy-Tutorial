package convoy;

import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;

/** Represents a position in a 2D plane.
 */
public class Waypoint extends Knowledge {
	public Waypoint() {	
	}
	
	public Waypoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int x, y;
	
	@Override
	public boolean equals(Object that) {
		if (that instanceof Waypoint) {
			Waypoint thatWaypoint = (Waypoint)that;
			return thatWaypoint.x == x && thatWaypoint.y == y;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}