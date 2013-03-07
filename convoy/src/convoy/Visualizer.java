package convoy;

import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.DEECoComponent;
import cz.cuni.mff.d3s.deeco.annotations.DEECoIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.jdeeco.visualization.map.Board;
import cz.cuni.mff.d3s.jdeeco.visualization.map.BoardObject;
import cz.cuni.mff.d3s.jdeeco.visualization.map.IStoppable;

@DEECoComponent
public class Visualizer extends ComponentKnowledge {

	public Map<String, BoardObject> components = new HashMap<String, BoardObject>();
	
	public Visualizer() {
		Board bInstance = Board.getInstance();
		bInstance.setAnimationSpeed(80);
		bInstance.setStoppable(new IStoppable() {
			@Override
			public void stop() {
				Runtime.getDefaultRuntime().stopRuntime();
			}
		});
	}
	
	@DEECoProcess
	@DEECoPeriodicScheduling(900)
	public static void process(@DEECoIn("components") Map<String, BoardObject> components) {
		Board.getInstance().updateObjects(components.values());
	}
}
