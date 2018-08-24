/***************************************************************************************
*	Title: PotatoesProject - GraphInfo Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package unitsGrammar.grammar;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>GraphInfo</b><p>
 * 
 * @author Luis Moura (https://github.com/LuisPedroMoura)
 * @version July 2018
 */
public class GraphInfo{
	
	// Static Field (Debug Only)
	private static final boolean debug = false; 
	
	// original Graph to be analyzed
	Graph graph;
	
	// Dijkstra algorithm results in the form of paths with no costs (deep copy of the graph avoids ConcurrentModificationException)
	List<ArrayList<Unit>> allShortestPaths;
	List<ArrayList<Unit>> allMinJumpsPaths;
	
	// Maps with all end to end cost calculated
	Map<Unit, Map<Unit, Double>> allShortestPathCostsTable = new HashMap<>();
	Map<Unit, Map<Unit, Double>> allMinJumpsPathCostsTable = new HashMap<>();
	
	// various adapted graphs
	Graph shortestPathsGraph = new Graph();
	Graph minJumpsPathsGraph = new Graph();
	Graph minJumpsPathCostsGraph = new Graph();
	
	
	protected GraphInfo(Graph graph) {
		this.graph = graph;
		
		if (debug) {
			out.println("---");
			out.print("GRAPHINFO- constructor: printing Graph: " + graph);
			out.println("---");
		}
		
		allShortestPaths = createAllShortestPaths(new Graph(graph));
		allMinJumpsPaths = createAllMinJumpsPaths(new Graph(graph));
		
		createAllShortestPathCostsTable();
		createAllMinJumpsPathCostsTable();
		
		createShortestPathsGraph();
		createMinJumpsPathsGraph();
		createMinJumpsPathsCostsGraph();
	}
	
	
	
	/**
	 * @return the graph
	 */
	protected Graph getGraph() {
		return graph;
	}

	/**
	 * @return the allShortestPaths
	 */
	protected List<ArrayList<Unit>> getAllShortestPaths() {
		return allShortestPaths;
	}

	/**
	 * @return the allStraightFowardPaths
	 */
	protected List<ArrayList<Unit>> getAllMinJumpsPaths() {
		return allMinJumpsPaths;
	}

	/**
	 * @return the shortestPathsGraph
	 */
	protected Graph getShortestPathsGraph() {
		return shortestPathsGraph;
	}

	/**
	 * @return the straightfowardPathsGraph
	 */
	protected Graph getMinJumpsPathsGraph() {
		return minJumpsPathsGraph;
	}

	/**
	 * @return the straightfowardPathsCostsGraph
	 */
	protected Graph getminJumpsPathCostsGraph() {
		return minJumpsPathCostsGraph;
	}
	
	/**
	 * @return the pathsTable
	 */
	protected Map<Unit, Map<Unit, Double>> getAllShortestPathCostsTable() {
		return allShortestPathCostsTable;
	}

	/**
	 * @return the pathsTable
	 */
	protected Map<Unit, Map<Unit, Double>> getAllMinJumpsPathCostsTable() {
		return allMinJumpsPathCostsTable;
	}


	/**
	 * Private methods that uses the Dijkstra Algorithm in Graph Class to create a list of
	 * all the shortest paths in the graph (the paths with smaller edge cost)
	 * @param graph
	 * @return a list of lists containing all the shortest paths
	 */
	private static List<ArrayList<Unit>> createAllShortestPaths(Graph graph) {

		List<ArrayList<Unit>> allPaths = new ArrayList<>();
		List<Unit> vertices = new ArrayList<>();
		for (Unit vertex : graph.getAdjList().keySet()) {
			vertices.add(new Unit(vertex));
		}
		
		for (Unit vertex : vertices) {
			List<ArrayList<Unit>> paths = graph.dijkstraShortestPaths(vertex);
			allPaths.addAll(paths);
		}

		return allPaths;
	}
	
	/**
	 * Private methods that uses the Dijkstra Algorithm with all edges with cost 1.0 in Graph Class
	 * to create a list of all the minimum jumps paths in the graph.
	 * @param graph
	 * @return a list of lists containing all the minimum jumps paths
	 */
	private static List<ArrayList<Unit>> createAllMinJumpsPaths(Graph graph) {
		List<ArrayList<Unit>> allPaths = new ArrayList<>();
		List<Unit> vertices = new ArrayList<>();
		for (Unit vertex : graph.getAdjList().keySet()) {
			vertices.add(new Unit(vertex));
		}
		
		for (Unit vertex : vertices) {
			List<ArrayList<Unit>> paths = graph.dijkstraMinimumJumpsPaths(vertex);
			allPaths.addAll(paths);
		}
		
		return allPaths;
	}
	
	/**
	 * Given all the paths with fewer jumps, this method creates a table with all end to end costs
	 * of traversing those paths
	 */
	private void createAllShortestPathCostsTable(){
		// FIXME this method is wrong!!! is copied from minJumps
		double pathCost = 1.0;
		
		for (Unit vertex : graph.getAdjList().keySet()) {
			
			allMinJumpsPathCostsTable.put(vertex, new HashMap<Unit, Double>());
			Map<Unit, Double> map = allMinJumpsPathCostsTable.get(vertex);
			
			for (Unit ver : graph.getAdjList().keySet()) {
				map.put(ver, Double.POSITIVE_INFINITY);
			}
			
			for (ArrayList<Unit> list : allMinJumpsPaths) {
				if (list.get(0).equals(vertex)) {
					for (int i = 0; i < list.size()-1; i++) {
						pathCost *= graph.getEdge(list.get(i), list.get(i+1));
					}
					map.put(list.get(list.size()-1), pathCost);
					pathCost = 1.0;
				}
			}
		}
	}
	
	
	/**
	 * Given all the paths with fewer jumps, this method creates a table with all end to end costs
	 * of traversing those paths
	 */
	private void createAllMinJumpsPathCostsTable(){
		double pathCost = 1.0;
		
		// find all paths startin from all vertices
		for (Unit vertex : graph.getAdjList().keySet()) {
			
			// create the map for this vertex
			allMinJumpsPathCostsTable.put(vertex, new HashMap<Unit, Double>());
			Map<Unit, Double> map = allMinJumpsPathCostsTable.get(vertex);
			
			// start evrey path at infinity
			for (Unit ver : graph.getAdjList().keySet()) {
				map.put(ver, Double.POSITIVE_INFINITY);
			}
			
			// from allMinJumpsPaths update the existing paths with real value different than infinity
			for (ArrayList<Unit> list : allMinJumpsPaths) {
				if (list.get(0).equals(vertex)) {
					for (int i = 0; i < list.size()-1; i++) {
						pathCost *= graph.getEdge(list.get(i), list.get(i+1));
					}
					map.put(list.get(list.size()-1), pathCost);
					pathCost = 1.0;
				}
			}
			// update loop from vertex to self
			map.put(vertex, graph.getEdge(vertex, vertex));
		}
	}
	
	
	//FIXME still not analised for correction
	private void createShortestPathsGraph(){
		double pathCost = 0.0;
		for (List<Unit> list : allShortestPaths) {
			for (int i = 0; i < list.size()-1; i++) {
				pathCost *= graph.getEdge(list.get(i), list.get(i+1));
			}
			shortestPathsGraph.addEdge(pathCost, list.get(0), list.get(list.size()-1));
			shortestPathsGraph.addEdge(pathCost, list.get(list.size()-1), list.get(0));
			pathCost = 0.0;
		}
	}
	
	//FIXME still not analised for correction
	private void createMinJumpsPathsGraph(){
		double pathCost = 0.0;
		for (List<Unit> list : allMinJumpsPaths) {
			for (int i = 0; i < list.size()-1; i++) {
				pathCost += 1;
			}
			minJumpsPathsGraph.addEdge(pathCost, list.get(0), list.get(list.size()-1));
			minJumpsPathsGraph.addEdge(pathCost, list.get(list.size()-1), list.get(0));
			pathCost = 0.0;
		}
	}
	
	//FIXME still not analised for correction
	private void createMinJumpsPathsCostsGraph(){
		double pathCost = 0.0;
		for (List<Unit> list : allMinJumpsPaths) {
			for (int i = 0; i < list.size()-1; i++) {
				pathCost *= graph.getEdge(list.get(i), list.get(i+1));
			}
			minJumpsPathCostsGraph.addEdge(pathCost, list.get(0), list.get(list.size()-1));
			minJumpsPathCostsGraph.addEdge(pathCost, list.get(list.size()-1), list.get(0));
			pathCost = 0.0;
		}
	}
	
	

	
	
}