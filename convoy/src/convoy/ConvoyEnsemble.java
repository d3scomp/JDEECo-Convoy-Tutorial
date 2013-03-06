package convoy;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsemble;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMapper;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMembership;
import cz.cuni.mff.d3s.deeco.annotations.DEECoIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoOut;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

@DEECoEnsemble
@DEECoPeriodicScheduling(200)
public class ConvoyEnsemble extends Ensemble {

	@DEECoEnsembleMembership
	public static boolean membership(
			@DEECoIn("member.position") Waypoint fPosition,
			@DEECoIn("member.destination") Waypoint fDestination,
			@DEECoIn("coord.position") Waypoint lPosition,
			@DEECoIn("coord.path") List<Waypoint> lPath) {
		
		return 
			!fPosition.equals(fDestination) &&
			(Math.abs(lPosition.x - fPosition.x) + Math.abs(lPosition.y - fPosition.y)) <= 2 &&
			lPath.contains(fDestination);
	}

	@DEECoEnsembleMapper
	public static void map(
			@DEECoOut("member.leaderPosition") OutWrapper<Waypoint> fLeaderPosition,
			@DEECoIn("coord.position") Waypoint lPosition) {
		
		fLeaderPosition.item = lPosition;
	}
}
