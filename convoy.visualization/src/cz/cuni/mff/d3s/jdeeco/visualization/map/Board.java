package cz.cuni.mff.d3s.jdeeco.visualization.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Board {
	private static Board instance = null;

	private BoardController bc;
	private Map<String, ObjectController> objectControllers;
	boolean initiated;
	
	private Thread visualizerThread;
	private IStoppable stoppable;

	private Board() {
		objectControllers = new HashMap<String, ObjectController>();
		bc = null;
		initiated = false;
		visualizerThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Visualizer.main(new String[]{});
			}
		});
		visualizerThread.start();
	}

	public static synchronized Board getInstance() {
		if (instance == null)
			instance = new Board();
		return instance;
	}

	public synchronized void setBoardController(BoardController bc) {
		this.bc = bc;
	}
	
	public void setStoppable(IStoppable stoppable) {
		this.stoppable = stoppable;
	}
	
	public void stop() {
		if (stoppable != null)
			stoppable.stop();
	}
	
	public void setAnimationSpeed(int value) {
		ObjectController.animationSpeed = value;
	}

	public synchronized void updateObjects(Collection<BoardObject> objects) {
		if (bc != null) {
			Set<String> keys = objectControllers.keySet();
			ObjectController oc;
			for (BoardObject bo : objects) {
				if (keys.contains(bo.id)) {
					oc = objectControllers.get(bo.id);
					oc.setPositions(bo.positions, true);
				} else if (bc != null) {
					try {
						oc = bc.addObject(bo);
						objectControllers.put(bo.id, oc);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
