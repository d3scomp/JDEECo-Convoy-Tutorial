package convoy;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoComponent;
import cz.cuni.mff.d3s.deeco.annotations.DEECoIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;

@DEECoComponent
public class LeaderB extends ComponentKnowledge {
	
	public String name;
	public List<Waypoint> path;
	public Waypoint position;
	
	public LeaderB() {
		path = new LinkedList<Waypoint>();
		path.add(new Waypoint(1, 1));		
		path.add(new Waypoint(1, 2));
		path.add(new Waypoint(1, 3));
		path.add(new Waypoint(2, 3));		
		path.add(new Waypoint(3, 3));
		path.add(new Waypoint(4, 3));
		path.add(new Waypoint(5, 3));		
		path.add(new Waypoint(6, 3));
		path.add(new Waypoint(6, 4));
		path.add(new Waypoint(6, 5));		
		path.add(new Waypoint(6, 6));
		path.add(new Waypoint(6, 7));
		path.add(new Waypoint(6, 8));		
		path.add(new Waypoint(7, 8));
		path.add(new Waypoint(8, 8));
		name = "L2";
		position = new Waypoint(1,0);
	}
	
	@DEECoProcess
	@DEECoPeriodicScheduling(1000)
	public static void moveProcess(
			@DEECoInOut("path") List<Waypoint> path,
			@DEECoIn("name") String name,
			@DEECoInOut("position") Waypoint me
			) {
		
		if (!path.isEmpty() && me.equals(path.get(0))) {
			path.remove(0);
		}
		
		if (!path.isEmpty()) {
			Waypoint next = path.get(0);
			me.x += Integer.signum(next.x - me.x);
			me.y += Integer.signum(next.y - me.y);
		}

		System.out.println("Leader " + name + ": " + me);
	}
}
