package convoy;

import java.util.LinkedList;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
public class LeaderA {
	
	public String name;
	public LinkedList<Waypoint> path;
	public Waypoint position;
	
	public LeaderA() {
		path = new LinkedList<Waypoint>();
		path.add(new Waypoint(8, 7)); path.add(new Waypoint(8, 6)); path.add(new Waypoint(8, 5));
		path.add(new Waypoint(7, 5)); path.add(new Waypoint(6, 5));	path.add(new Waypoint(5, 5));
		path.add(new Waypoint(4, 5)); path.add(new Waypoint(3, 5));	path.add(new Waypoint(2, 5));
		path.add(new Waypoint(1, 5)); path.add(new Waypoint(0, 5));	path.add(new Waypoint(0, 4));
		path.add(new Waypoint(0, 3)); path.add(new Waypoint(0, 2));	path.add(new Waypoint(1, 2));
		path.add(new Waypoint(2, 2)); path.add(new Waypoint(3, 2));	path.add(new Waypoint(4, 2));
		path.add(new Waypoint(5, 2)); path.add(new Waypoint(6, 2));	path.add(new Waypoint(7, 2));
		path.add(new Waypoint(8, 2)); path.add(new Waypoint(9, 2));	path.add(new Waypoint(9, 1));
		path.add(new Waypoint(9, 0));

		name = "L1";
		position = new Waypoint(8,8);
	}
	
	@Process
	@PeriodicScheduling(1000)
	public static void moveProcess(
			@InOut("path") ParamHolder<LinkedList<Waypoint>> path,
			@In("name") String name,
			@InOut("position") ParamHolder<Waypoint> me
			) {
		
		if (!path.value.isEmpty() && me.value.equals(path.value.get(0))) {
			path.value.remove(0);
		}
		
		if (!path.value.isEmpty()) {
			Waypoint next = path.value.get(0);
			me.value.x += Integer.signum(next.x - me.value.x);
			me.value.y += Integer.signum(next.y - me.value.y);
		}

		System.out.println("Leader " + name + ": " + me.value);
	}
}
