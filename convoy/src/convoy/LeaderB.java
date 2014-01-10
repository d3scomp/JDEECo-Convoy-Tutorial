package convoy;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

@Component
public class LeaderB {
	
	public String name;
	public List<Waypoint> path;
	public Waypoint position;
	
	public LeaderB() {
		path = new LinkedList<Waypoint>();
		path.add(new Waypoint(1, 1)); path.add(new Waypoint(1, 2)); path.add(new Waypoint(1, 3));
		path.add(new Waypoint(2, 3)); path.add(new Waypoint(3, 3)); path.add(new Waypoint(4, 3));
		path.add(new Waypoint(5, 3)); path.add(new Waypoint(6, 3)); path.add(new Waypoint(6, 4));
		path.add(new Waypoint(6, 5)); path.add(new Waypoint(6, 6)); path.add(new Waypoint(6, 7));
		path.add(new Waypoint(6, 8)); path.add(new Waypoint(7, 8)); path.add(new Waypoint(8, 8));
		
		name = "L2";
		position = new Waypoint(1,0);
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
