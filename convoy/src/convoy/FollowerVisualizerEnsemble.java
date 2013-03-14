package convoy;

import java.util.LinkedList;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.jdeeco.visualization.map.BoardObject;
import cz.cuni.mff.d3s.jdeeco.visualization.map.Position;


public class FollowerVisualizerEnsemble extends Ensemble {
	
	@Membership
	public static boolean membership(@InOut("coord.components") Map<String, BoardObject> components,
			@In("member.id") String id,
			@In("member.name") String name,
			@In("member.destination") Waypoint destination,
			@In("member.position") Waypoint position) {	
		return true;
	}

	@KnowledgeExchange
	@PeriodicScheduling(150)
	public static void map(
			@InOut("coord.components") Map<String, BoardObject> components,
			@In("member.id") String id,
			@In("member.name") String name,
			@In("member.destination") Waypoint destination,
			@In("member.position") Waypoint position
			) {
		BoardObject bo;
		if (components.keySet().contains(id)) {
			bo = components.get(id);
			bo.positions.clear();
		} else {
			bo = new BoardObject(id, new LinkedList<Position>(), "green", name);
		}
		bo.positions.add(new Position(position.x, position.y));
		bo.positions.add(new Position(destination.x, destination.y));
		components.put(id, bo);
	}
}
