package convoy;

import cz.cuni.mff.d3s.deeco.annotations.DEECoComponent;
import cz.cuni.mff.d3s.deeco.annotations.DEECoIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;

@DEECoComponent
public class Follower extends ComponentKnowledge {

	public String name = "F";
	public Waypoint position = new Waypoint(1, 4);
	public Waypoint destination = new Waypoint(7, 2);
	public Waypoint leaderPosition;
	
	@DEECoProcess
	@DEECoPeriodicScheduling(1000)
	public static void followProcess(
		@DEECoInOut("position") Waypoint me,
		@DEECoIn("destination") Waypoint destination, 
		@DEECoIn("name") String name, 
		@DEECoIn("leaderPosition") Waypoint leader 
		) {
		
		if (!destination.equals(me) && leader != null) {
			me.x += Integer.signum(leader.x - me.x);
			me.y += Integer.signum(leader.y - me.y);
		}

		System.out.println("Follower " + name + ": me = " + me + " leader = " + leader);
	}
}
