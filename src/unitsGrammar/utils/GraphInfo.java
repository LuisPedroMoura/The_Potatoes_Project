/***************************************************************************************
*	Title: PotatoesProject - GraphInfo Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package unitsGrammar.utils;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <b>GraphInfo</b><p>
 * 
 * @author Luis Moura (https://github.com/LuisPedroMoura)
 * @version July 2018
 */
public class GraphInfo{
	
	// Static Field (Debug Only)
	private static final boolean debug = false; 
	
	Graph graph;
	List<ArrayList<Unit>> allShortestPaths;
	List<ArrayList<Unit>> allStraightFowardPaths;
	Graph shortestPathsGraph = new Graph();
	Graph straightfowardPathsGraph = new Graph();;
	Graph straightfowardPathsCostsGraph = new Graph();
	
	public GraphInfo(Graph graph) {
		this.graph = graph;
		
		if (debug) {
			out.println("---");
			out.print("GRAPHINFO- constructor: printing Graph: " + graph);
			out.println("---");
		}
		
		// have to use deep copy of graph or functions in Graph Class will create concurrent modification exception
		allShortestPaths = getAllShortestPaths(new Graph(graph));
		allStraightFowardPaths = getAllStraightfowardPaths(new Graph(graph));
		createShortestPathsGraph();
		createStraightfowardPathsGraph();
		createStraightfowardPathsCostsGraph();
	}
	
	
	
	/**
	 * @return the graph
	 */
	public Graph getGraph() {
		return graph;
	}



	/**
	 * @return the allShortestPaths
	 */
	public List<ArrayList<Unit>> getAllShortestPaths() {
		return allShortestPaths;
	}



	/**
	 * @return the allStraightFowardPaths
	 */
	public List<ArrayList<Unit>> getAllStraightFowardPaths() {
		return allStraightFowardPaths;
	}



	/**
	 * @return the shortestPathsGraph
	 */
	public Graph getShortestPathsGraph() {
		return shortestPathsGraph;
	}



	/**
	 * @return the straightfowardPathsGraph
	 */
	public Graph getStraightfowardPathsGraph() {
		return straightfowardPathsGraph;
	}



	/**
	 * @return the straightfowardPathsCostsGraph
	 */
	public Graph getStraightfowardPathsCostsGraph() {
		return straightfowardPathsCostsGraph;
	}



	/**
	 * 
	 * @param graph
	 * @return
	 */
	private List<ArrayList<Unit>> getAllStraightfowardPaths(Graph graph) {
		List<ArrayList<Unit>> allPaths = new ArrayList<>();
		Set<Unit> vertices = graph.getAdjList().keySet();
		for (Unit vertex : vertices) {
			List<ArrayList<Unit>> paths = graph.dijkstraStraightFowardPaths(vertex);
			allPaths.addAll(paths);
		}
		return allPaths;
	}
	
	/**
	 * 
	 * @param graph
	 * @return
	 */
	private List<ArrayList<Unit>> getAllShortestPaths(Graph graph) {
		List<ArrayList<Unit>> allPaths = new ArrayList<>();
		Set<Unit> vertices = graph.getAdjList().keySet();
		for (Unit vertex : vertices) {
			List<ArrayList<Unit>> paths = graph.dijkstraShortestPaths(vertex);
			allPaths.addAll(paths);
		}
		return allPaths;
	}
	
	
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
	
	private void createStraightfowardPathsGraph(){
		double pathCost = 0.0;
		for (List<Unit> list : allStraightFowardPaths) {
			for (int i = 0; i < list.size()-1; i++) {
				pathCost += 1;
			}
			straightfowardPathsGraph.addEdge(pathCost, list.get(0), list.get(list.size()-1));
			straightfowardPathsGraph.addEdge(pathCost, list.get(list.size()-1), list.get(0));
			pathCost = 0.0;
		}
	}
	
	
	private void createStraightfowardPathsCostsGraph(){
		double pathCost = 0.0;
		for (List<Unit> list : allStraightFowardPaths) {
			for (int i = 0; i < list.size()-1; i++) {
				pathCost *= graph.getEdge(list.get(i), list.get(i+1));
			}
			straightfowardPathsCostsGraph.addEdge(pathCost, list.get(0), list.get(list.size()-1));
			straightfowardPathsCostsGraph.addEdge(pathCost, list.get(list.size()-1), list.get(0));
			pathCost = 0.0;
		}
	}
	
	
}
























