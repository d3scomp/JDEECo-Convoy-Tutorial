package convoy;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.ClassDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

/** Launcher for the local deployment.
 * This class provides code that instantiates the necessary jDEECo infrastructure on a single node,
 * deploys components and ensembles of the demo.
 */
public class LauncherLocal {

	public static void main(String[] args) {
		List<Class<?>> components = Arrays.asList(new Class<?>[] {
				LeaderA.class, 
				LeaderB.class, 
				Follower.class,
				Visualizer.class,
		});
		
		List<Class<?>> ensembles = Arrays.asList(new Class<?>[] { 
				ConvoyEnsemble.class,
				LeaderVisualizerEnsemble.class,
				FollowerVisualizerEnsemble.class
		});
		
		KnowledgeManager km = new RepositoryKnowledgeManager(new LocalKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler();
		AbstractDEECoObjectProvider provider = new ClassDEECoObjectProvider(components, ensembles);
		
		final Runtime rt = new Runtime(km, scheduler);
		rt.registerComponentsAndEnsembles(provider);
		rt.startRuntime();
	}

}
