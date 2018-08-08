/***************************************************************************************
*	Title: PotatoesProject - GraphInfo Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package typesGrammar.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <b>GraphInfo</b><p>
 * 
 * @author Luis Moura (https://github.com/LuisPedroMoura)
 * @version July 2018
 */
public class GraphInfo<V,E> extends HierarchyDiGraph<V,E>{
	
	// Static Field (Debug Only)
	private static final boolean debug = true; 
	
	HierarchyDiGraph<V,E> graph;
	List<ArrayList<V>> allShortestPaths;
	List<ArrayList<V>> allStraightFowardPaths;
	HierarchyDiGraph<V,Double> shortestPathsGraph;
	HierarchyDiGraph<V,Double> straightfowardPathsGraph;
	HierarchyDiGraph<V,Double> straightfowardPathsCostsGraph;
	
	public GraphInfo(HierarchyDiGraph<V,E> graph) {
		this.graph = graph;		if (debug) System.out.print(graph);
		allShortestPaths = getAllShortestPaths(graph);
		allStraightFowardPaths = getAllStraightfowardPaths(graph);
		createShortestPathsGraph();
		createStraightfowardPathsGraph();
		createStraightfowardPathsCostsGraph();
	}
	
	
	
	/**
	 * @return the graph
	 */
	public HierarchyDiGraph<V, E> getGraph() {
		return graph;
	}



	/**
	 * @return the allShortestPaths
	 */
	public List<ArrayList<V>> getAllShortestPaths() {
		return allShortestPaths;
	}



	/**
	 * @return the allStraightFowardPaths
	 */
	public List<ArrayList<V>> getAllStraightFowardPaths() {
		return allStraightFowardPaths;
	}



	/**
	 * @return the shortestPathsGraph
	 */
	public HierarchyDiGraph<V, Double> getShortestPathsGraph() {
		return shortestPathsGraph;
	}



	/**
	 * @return the straightfowardPathsGraph
	 */
	public HierarchyDiGraph<V, Double> getStraightfowardPathsGraph() {
		return straightfowardPathsGraph;
	}



	/**
	 * @return the straightfowardPathsCostsGraph
	 */
	public HierarchyDiGraph<V, Double> getStraightfowardPathsCostsGraph() {
		return straightfowardPathsCostsGraph;
	}



	/**
	 * 
	 * @param graph
	 * @return
	 */
	private List<ArrayList<V>> getAllStraightfowardPaths(HierarchyDiGraph<V,E> graph) {
		List<ArrayList<V>> allPaths = new ArrayList<>();
		Set<V> vertices = graph.getAdjList().keySet();
		for (V vertex : vertices) {
			List<ArrayList<V>> paths = dijkstraStraightFowardPaths(vertex);
			allPaths.addAll(paths);
		}
		return allPaths;
	}
	
	/**
	 * 
	 * @param graph
	 * @return
	 */
	private List<ArrayList<V>> getAllShortestPaths(HierarchyDiGraph<V,E> graph) {
		List<ArrayList<V>> allPaths = new ArrayList<>();
		Set<V> vertices = graph.getAdjList().keySet();
		for (V vertex : vertices) {
			List<ArrayList<V>> paths = dijkstraShortestPaths(vertex);
			allPaths.addAll(paths);
		}
		return allPaths;
	}
	
	
	private void createShortestPathsGraph(){
		double pathCost = 0.0;
		for (List<V> list : allShortestPaths) {
			for (int i = 0; i < list.size()-1; i++) {
				pathCost *= graph.getEdgeCost(graph.getEdge(list.get(i), list.get(i+1)));
			}
			shortestPathsGraph.addEdge(pathCost, list.get(0), list.get(list.size()-1));
			shortestPathsGraph.addEdge(pathCost, list.get(list.size()-1), list.get(0));
			pathCost = 0.0;
		}
	}
	
	private void createStraightfowardPathsGraph(){
		double pathCost = 0.0;
		for (List<V> list : allStraightFowardPaths) {
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
		for (List<V> list : allStraightFowardPaths) {
			for (int i = 0; i < list.size()-1; i++) {
				pathCost *= graph.getEdgeCost(graph.getEdge(list.get(i), list.get(i+1)));
			}
			straightfowardPathsCostsGraph.addEdge(pathCost, list.get(0), list.get(list.size()-1));
			straightfowardPathsCostsGraph.addEdge(pathCost, list.get(list.size()-1), list.get(0));
			pathCost = 0.0;
		}
	}
	
	
}
























