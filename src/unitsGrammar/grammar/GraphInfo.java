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
	Graph straightfowardPathsGraph = new Graph();
	Graph straightfowardPathsCostsGraph = new Graph();
	Map<Unit, Map<Unit, Double>> pathsTable = new HashMap<>();
	
	protected GraphInfo(Graph graph) {
		this.graph = graph;
		
		if (debug) {
			out.println("---");
			out.print("GRAPHINFO- constructor: printing Graph: " + graph);
			out.println("---");
		}
		
		// have to use deep copy of graph or functions in Graph Class will create concurrent modification exception
		allShortestPaths = getAllShortestPaths(new Graph(graph));
		allStraightFowardPaths = getAllStraightfowardPaths(new Graph(graph));
		
		System.out.println("ALL STRAIGHTFOWARD PATHS\n");
		for (ArrayList<Unit> arr : allStraightFowardPaths) {
			for (Unit unit : arr) {
				System.out.println(unit);
			}
			System.out.println();
		}
		System.out.println("##################\n###################\n##################\n####################\n");
		
		createShortestPathsGraph();
		createStraightfowardPathsGraph();
		createStraightfowardPathsCostsGraph();
		createPathsTable();
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
	protected List<ArrayList<Unit>> getAllStraightFowardPaths() {
		return allStraightFowardPaths;
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
	protected Graph getStraightfowardPathsGraph() {
		return straightfowardPathsGraph;
	}



	/**
	 * @return the straightfowardPathsCostsGraph
	 */
	protected Graph getStraightfowardPathsCostsGraph() {
		return straightfowardPathsCostsGraph;
	}

	

	/**
	 * @return the pathsTable
	 */
	protected Map<Unit, Map<Unit, Double>> getPathsTable() {
		return pathsTable;
	}



	/**
	 * 
	 * @param graph
	 * @return
	 */
	private List<ArrayList<Unit>> getAllStraightfowardPaths(Graph graph) {
		List<ArrayList<Unit>> allPaths = new ArrayList<>();
		List<Unit> vertices = new ArrayList<>();
		for (Unit vertex : graph.getAdjList().keySet()) {
			vertices.add(new Unit(vertex));
		}
		
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
	
	
	private void createPathsTable(){
		double pathCost = 0.0;

		for (int i = 0; i < allStraightFowardPaths.size(); i++) {
			
			List<Unit> list = allStraightFowardPaths.get(i);
			if (pathsTable.get(list.get(0)) == null){
				pathsTable.put(list.get(0), new HashMap<Unit, Double>());
			}
			if (pathsTable.get(list.get(list.size()-1)) == null){
				pathsTable.put(list.get(list.size()-1), new HashMap<Unit, Double>());
			}
			
			for (int j = 0; j < list.size()-1; j++) {
				pathCost *= graph.getEdge(list.get(j), list.get(j+1));
			}
			
			pathsTable.get(list.get(0)).put(list.get(list.size()-1), pathCost);
			pathsTable.get(list.get(list.size()-1)).put(list.get(0), 1/pathCost);
			
			pathCost = 0.0;
		}
	}
	
	
}
























