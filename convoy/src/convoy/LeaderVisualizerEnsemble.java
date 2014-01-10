package convoy;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.jdeeco.visualization.map.BoardObject;
import cz.cuni.mff.d3s.jdeeco.visualization.map.Position;

@Ensemble
@PeriodicScheduling(150)
public class LeaderVisualizerEnsemble {
	
	@Membership
	public static boolean membership(
			@In("coord.components") Map<String, BoardObject> components,
			@In("member.id") String id,
			@In("member.name") String name,
			@In("member.path") List<Waypoint> path,
			@In("member.position") Waypoint position) {	
		return true;
	}

	@KnowledgeExchange
	public static void map(
			@InOut("coord.components") ParamHolder<Map<String, BoardObject>> components,
			@In("member.id") String id,
			@In("member.name") String name,
			@In("member.path") List<Waypoint> path,
			@In("member.position") Waypoint position
			) {
		BoardObject bo;
		if (components.value.keySet().contains(id)) {
			bo = components.value.get(id);
			bo.positions.clear();
		} else {
			bo = new BoardObject(id, new LinkedList<Position>(), "red", name);
		}
		for (Waypoint waypoint : path) {
			bo.positions.add(new Position(waypoint.x, waypoint.y));
		}
		bo.positions.add(0, new Position(position.x, position.y));
		components.value.put(id, bo);
	}
}
