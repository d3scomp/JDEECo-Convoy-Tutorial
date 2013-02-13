package convoy;

import java.util.LinkedList;
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
@DEECoPeriodicScheduling(1000)
public class FollowerVisualizerEnsemble extends Ensemble {
	
	@DEECoEnsembleMembership
	public static boolean membership(@DEECoInOut("coord.components") Map<String, BoardObject> components,
			@DEECoIn("member.id") String id,
			@DEECoIn("member.name") String name,
			@DEECoIn("member.destination") Waypoint destination,
			@DEECoIn("member.position") Waypoint position) {	
		return true;
	}

	@DEECoEnsembleMapper
	public static void map(
			@DEECoInOut("coord.components") Map<String, BoardObject> components,
			@DEECoIn("member.id") String id,
			@DEECoIn("member.name") String name,
			@DEECoIn("member.destination") Waypoint destination,
			@DEECoIn("member.position") Waypoint position
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
