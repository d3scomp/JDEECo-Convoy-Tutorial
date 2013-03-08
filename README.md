JDEECo Convoy Tutorial
======================

This project contains the source code of the **Convoy example** implementation in the **jDEECo framework**.
jDEECo is a Java implementation of the DEECo domponent system (the source code and documentation of jDEECo is available at https://github.com/d3scomp/JDEECo).

###Example Scenario Description
There are two robots named "Leader" and "Follower". The leader moves along some arbitrary path (composed by the list of intermediate positions, which reflect fields on some abstract map, being a grid). The follower remains idle until the leader heading through Follower's destination passes next to it (Leader crosses Follower's current position). When that happens, the follower starts to move following the leader.

###Outline
1. [Requirements](#requirements)
2. [Usage](#usage)
2. [Code snippets](#code-snippets)
3. [Code description](#code-description)



## Requirements
To build and run the example from command line, the following software has to be available on your system:
* Java SDK >= 1.7.0 (http://java.com/en/)
* one of the following:
	* Apache Ant (http://ant.apache.org/), version 1.8.2 was used when writing the build scripts
	* Eclipse for Java developers

## Usage
1. Download the repository as a zip package and unpack it.
2. Build the example by executing `ant` in the root folder. 
Alternatively, you can import the `convoy` project into Eclipse and build it via Eclipse.
3. Run the example by executing `ant run` in the root folder.
Alternatively, after importing the `convoy` project into Eclipse, run the `LauncherLocal` class "as Java Application".

## Code snippets

### Launcher
```java

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

```

### Leader
```java
package convoy;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.knowledge.Component;

@DEECoComponent
public class LeaderA extends Component {
	
	public String name;
	public List<Waypoint> path;
	public Waypoint position;
	
	public LeaderA() {
		path = new LinkedList<Waypoint>();
		path.add(new Waypoint(8, 7)); path.add(new Waypoint(8, 6)); path.add(new Waypoint(8, 5));
		path.add(new Waypoint(7, 5)); path.add(new Waypoint(6, 5));	path.add(new Waypoint(5, 5));
		path.add(new Waypoint(4, 5)); path.add(new Waypoint(3, 5));	path.add(new Waypoint(2, 5));
		path.add(new Waypoint(1, 5)); path.add(new Waypoint(0, 5));	path.add(new Waypoint(0, 4));
		path.add(new Waypoint(0, 3)); path.add(new Waypoint(0, 2));	path.add(new Waypoint(1, 2));
		path.add(new Waypoint(2, 2)); path.add(new Waypoint(3, 2));	path.add(new Waypoint(4, 2));
		path.add(new Waypoint(5, 2)); path.add(new Waypoint(6, 2));	path.add(new Waypoint(7, 2));
		path.add(new Waypoint(8, 2)); path.add(new Waypoint(9, 2));	path.add(new Waypoint(9, 1));
		path.add(new Waypoint(9, 0));

		name = "L1";
		position = new Waypoint(8,8);
	}
	
	@Process
	@PeriodicScheduling(1000)
	public static void moveProcess(
			@InOut("path") List<Waypoint> path,
			@In("name") String name,
			@InOut("position") Waypoint me
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
###Follower
```java
package convoy;

import cz.cuni.mff.d3s.deeco.annotations.DEECoComponent;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.knowledge.Component;

@DEECoComponent
public class Follower extends Component {

	public String name = "F";
	public Waypoint position = new Waypoint(1, 4);
	public Waypoint destination = new Waypoint(7, 2);
	public Waypoint leaderPosition;
	
	@Process
	@PeriodicScheduling(1000)
	public static void followProcess(
		@InOut("position") Waypoint me,
		@In("destination") Waypoint destination, 
		@In("name") String name, 
		@In("leaderPosition") Waypoint leader 
		) {
		
		if (!destination.equals(me) && leader != null) {
			me.x += Integer.signum(leader.x - me.x);
			me.y += Integer.signum(leader.y - me.y);
		}

		System.out.println("Follower " + name + ": me = " + me + " leader = " + leader);
	}
}
```
### Convoy ensemble
```java
package convoy;

import java.util.List;

import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsemble;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

@DEECoEnsemble
@PeriodicScheduling(200)
public class ConvoyEnsemble extends Ensemble {

	@Membership
	public static boolean membership(
			@In("member.position") Waypoint fPosition,
			@In("member.destination") Waypoint fDestination,
			@In("coord.position") Waypoint lPosition,
			@In("coord.path") List<Waypoint> lPath) {
		
		return 
			!fPosition.equals(fDestination) &&
			(Math.abs(lPosition.x - fPosition.x) + Math.abs(lPosition.y - fPosition.y)) <= 2 &&
			lPath.contains(fDestination);
	}

	@KnowledgeExchange
	public static void map(
			@Out("member.leaderPosition") OutWrapper<Waypoint> fLeaderPosition,
			@In("coord.position") Waypoint lPosition) {
		
		fLeaderPosition.item = lPosition;
	}
}
```
##Code description
### Launcher

The `LauncherLocal` class main method instantiates the DEECo runtime, which automatically starts its operation - classes are "interpreted" and appropriate processes are started.

### Robot Follower and robot Leader

Basically there are two robot roles "Leader" and "Follower", which have their own Java class definitions.
Each of those classes extends the `Component` type, which describes knowledge of a particular component (i.e. top-level node in the tree knowledge structure). Additionally they are marked with the `@DEECoComponent` annotation, allowing the system to properly identify component classes. In this case both components put additional **public** fields to their definitions to describe their knowledge.
Apart from it, each component is expected to have at least one process method which executes relying on component data. Such method is tagged with `@Process` annotation and can take  a random number of properly tagged parameters. Allowed annotations for the process method parameters are:
`@In`, `@Out` and `@InOut`. Parameters marked as an input will be retrieved from the knowledge but never stored back after the execution. Those marked as an output, will be first instantiated (using non-parameterized constructors for the class) and pass to the method before its execution. When the execution completes, they are stored in the knowledge. Parameters, annotated as both input and output, are first retrieved from the knowledge and then (after the method execution) stored back to it. Each of those annotations contains the `value` attribute, which specifies the parameter name together with the knowledge nesting path. To explain it, let's consider the following parameter:
`@InOut("position") OutWrapper<Integer> me`. For this parameter the `value` attribute is the"position", which means that the required (as it is also an input parameter) knowledge value will be searched in the knowledge structure under the node: `Follower.position`. The "Follower" at the beginning denotes the particular knowledge id. As it was mentioned before, it is possible to use nesting paths in a parameter annotation attribute.
The last but not least important thing regarding the process method parameters are the outputs. Very often it is necessary to change value of an object, which type is immutable (for example `Integer`). As such, JDEECo provides the `OutWrapper` generic class, which allows for modification of immutable types, and storing those changes in the knowledge repository.

Moreover, both component processes have additional annotation, above the process method header, called `@PeriodicScheduling`.
As the name indicates, this annotation allows for describing process method execution scheme, which in this case is periodic with time interval (milliseconds) set in the annotation attribute.

### Convoy ensemble

To let the follower robot to follow the leader we need to establish an ensemble between those robots.
In jDEECo ensembles are defined as the first class objects.
Similarly to the component class, it is tagged with the annotation `@DEECoEnsemble` and it extends the `Ensemble` class. The `ConvoyEnsemble` is executed periodically, which is expressed by additional annotation before the class header - `@PeriodicScheduling`.
In the ensemble definition there are two important methods: the ensemble membership function and the knowledge exchange function. To properly identify them, annotations (`@Membership` and `@KnowledgeExchange` respectively) are used, before the method headers. In both cases, a method can accept a random number of parameters, but in case of the membership checking function those parameters can be of input type only. Additionally in the `ConvoyEnsemble`, the method tagged with `@Membership` annotation returns a boolean value indicating when the ensemble can be established. As it is depicted in the code above, method parameters are also tagged. The rules from the process method parameters apply here as well and the only difference is the additional distinguishment between coordinator and member parameters. To find out more details about what it means, please refer to the DEECo component model description (Ensemble describing part).

* * *

An important thing to mention is that, the parameters for both process methods and ensemble methods are retrieved using duck typing. As such it is possible to define your own structures (extending the `Knowledge` class), with fields which are actually needed for those method execution.
