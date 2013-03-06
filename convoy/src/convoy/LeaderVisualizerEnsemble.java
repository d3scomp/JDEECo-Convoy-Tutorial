package convoy;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsemble;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMapper;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMembership;
import cz.cuni.mff.d3s.deeco.annotations.DEECoIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoInOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.jdeeco.visualization.map.BoardObject;
import cz.cuni.mff.d3s.jdeeco.visualization.map.Position;

@DEECoEnsemble
@DEECoPeriodicScheduling(150)
public class LeaderVisualizerEnsemble extends Ensemble {
	
	@DEECoEnsembleMembership
	public static boolean membership(
			@DEECoIn("coord.components") Map<String, BoardObject> components,
			@DEECoIn("member.id") String id,
			@DEECoIn("member.name") String name,
			@DEECoIn("member.path") List<Waypoint> path,
			@DEECoIn("member.position") Waypoint position) {	
		return true;
	}

	@DEECoEnsembleMapper
	public static void map(
			@DEECoInOut("coord.components") Map<String, BoardObject> components,
			@DEECoIn("member.id") String id,
			@DEECoIn("member.name") String name,
			@DEECoIn("member.path") List<Waypoint> path,
			@DEECoIn("member.position") Waypoint position
			) {
		BoardObject bo;
		if (components.keySet().contains(id)) {
			bo = components.get(id);
			bo.positions.clear();
		} else {
			bo = new BoardObject(id, new LinkedList<Position>(), "red", name);
		}
		for (Waypoint waypoint : path) {
			bo.positions.add(new Position(waypoint.x, waypoint.y));
		}
		bo.positions.add(0, new Position(position.x, position.y));
		components.put(id, bo);
	}
}
