JDEECo-Convoy-Tutorial
======================

This project contains and explains the source code  of the Convoy example implementation in the jDEECo framework (the source code and documentation is available at [https://github.com/d3scomp/JDEECo]).

**Example Scenario Description**:

There are two robots named "Leader" and "Follower". The leader moves along some arbitrary path (composed by the list of intermediate positions, which reflect fields on some abstract map, being a grid). The follower remains idle until the leader heading through Follower's destination passes next to it (Leader crosses Follower's current position). When that happens, the follower starts to move following the leader.

### Code snippets

* **Launcher**:
```java

public class LauncherLocal {

  public static void main(String[] args) {
		List<Class<?>> components = Arrays.asList(new Class<?>[] {
				Leader.class, 
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
		Board.getInstance().setStoppable(new IStoppable() {
			
			@Override
			public void stop() {
				
				rt.stopRuntime();
				
			}
		});

		rt.registerComponentsAndEnsembles(provider);
		rt.startRuntime();
	}

}
```

* **Leader**:
```java
@DEECoComponent
public class Leader extends ComponentKnowledge {
	
	public String name;
	public List<Waypoint> path = new LinkedList<Waypoint>();
	public Waypoint position;
	
	@DEECoInitialize
	public static List<Leader> getInitKnowledge() {
		return LeaderFactory.getLeaders();		
	}
	
	@DEECoProcess
	@DEECoPeriodicScheduling(3000)
	public static void moveProcess(
			@DEECoInOut("path") List<Waypoint> path,
			@DEECoIn("name") String name,
			@DEECoInOut("position") Waypoint me
			) {
		
		if (!path.isEmpty() && me.equals(path.get(0))) {
			path.remove(0);
		}
		
		if (!path.isEmpty()) {
			Waypoint next = path.get(0);
			me.x += Integer.signum(next.x - me.x);
			me.y += Integer.signum(next.y - me.y);
		}

		System.out.println("Leader " + name + ": " + me);
	}
}
```
* **Follower**:
```java
@DEECoComponent
public class Follower extends ComponentKnowledge {

	public String name = "F";
	public Waypoint position = new Waypoint(1, 4);
	public Waypoint destination = new Waypoint(7, 2);
	public Waypoint leaderPosition;
	
	@DEECoProcess
	@DEECoPeriodicScheduling(2000)
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
```
* **Convoy ensemble**:
```java
@DEECoEnsemble
@DEECoPeriodicScheduling(800)
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
```

### Launcher

The `Launcher` class main method instantiates the DEECo runtime, which automatically starts its operation - classes are "interpreted" and appropriate processes are started.

### Robot Follower and robot Leader

Basically there are two robot roles "Leader" and "Follower", which have their own Java class definitions.
Each of those classes extends the ComponentKnowledge type, which describes knowledge of a particular component (i.e. top-level node in the tree knowledge structure). Additionally they are marked with the `@DEECoComponent` annotation, allowing the system to properly identify component classes. In this case both components put additional **public** fields to their definitions to describe their knowledge.
Apart from it, each component is expected to have at least one process method which executes relying on component data. Such method is tagged with `@DEECoProcess` annotation and can take  a random number of properly tagged parameters. Allowed annotations for the process method parameters are:
`@DEECoIn`, `@DEECoOut` and `@DEECoInOut`. Parameters marked as an input will be retrieved from the knowledge but never stored back after the execution. Those marked as an output, will be first instantiated (using non-parameterized constructors for the class) and pass to the method before its execution. When the execution completes, they are stored in the knowledge. Parameters, annotated as both input and output, are first retrieved from the knowledge and then (after the method execution) stored back to it. Each of those annotations contains the `value` attribute, which specifies the parameter name together with the knowledge nesting path. To explain it, let's consider the following parameter:
`@DEECoInOut("position") OutWrapper<Integer> me`. For this parameter the `value` attribute is the"position", which means that the required (as it is also an input parameter) knowledge value will be searched in the knowledge structure under the node: `Follower.position`. The "Follower" at the beginning denotes the particular knowledge id. As it was mentioned before, it is possible to use nesting paths in a parameter annotation attribute.
The last but not least important thing regarding the process method parameters are the outputs. Very often it is necessary to change value of an object, which type is immutable (for example `Integer`). As such, JDEECo provides the `OutWrapper` generic class, which allows for modification of immutable types, and storing those changes in the knowledge repository.

Moreover, both component processes have additional annotation, above the process method header, called `@DEECoPeriodicScheduling`.
As the name indicates, this annotation allows for describing process method execution scheme, which in this case is periodic with time interval (milliseconds) set in the annotation attribute.

### Convoy ensemble

To let the follower robot to follow the leader we need to establish an ensemble between those robots.
In jDEECo ensembles are defined as the first class objects.
Similarly to the component class, it is tagged with the annotation `@DEECoEnsemble` and it extends the `Ensemble` class. The `ConvoyEnsemble` is executed periodically, which is expressed by additional annotation before the class header - `@DEECoPeriodicScheduling`.
In the ensemble definition there are two important methods: the ensemble membership checking function and the mapping function. To properly identify them, annotations (`@DEECoEnsembleMembership` and `@DEECoEnsembleMapper` respectively) are used, before the method headers. In both cases, a method can accept a random number of parameters, but in case of the membership checking function those parameters can be of input type only. Additionally in the `ConvoyEnsemble`, the method tagged with `@DEECoEnsembleMembership` annotation returns a boolean value indicating when the ensemble can be established. As it is depicted in the code above, method parameters are also tagged. The rules from the process method parameters apply here as well and the only difference is the additional distinguishment between coordinator and member parameters. To find out more details about what it means, please refer to the DEECo component model description (Ensemble describing part).

* * *

An important thing to mention is that, the parameters for both process methods and ensemble methods are retrieved using duck typing. As such it is possible to define your own structures (extending the `Knowledge` class), with fields which are actually needed for those method execution.
