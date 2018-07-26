/***************************************************************************************
*	Title: PotatoesProject - GraphInfo Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <b>GraphInfo</b><p>
 * 
 * @author Luis Moura (https://github.com/LuisPedroMoura)
 * @version July 2018
 */
public class GraphInfo<V,E> extends Graph<V,E>{
	
	Graph<V,E> graph;
	List<ArrayList<V>> allShortestPaths;
	List<ArrayList<V>> allStraightFowardPaths;
	Graph<V,Double> shortestPathsGraph;
	Graph<V,Double> straightfowardPathsGraph;
	Graph<V,Double> straightfowardPathsCostsGraph;
	
	GraphInfo(Graph<V,E> graph) {
		this.graph = graph;
		allShortestPaths = getAllShortestPaths(graph);
		allStraightFowardPaths = getAllStraightfowardPaths(graph);
		createShortestPathsGraph();
		createStraightfowardPathsGraph();
		createStraightfowardPathsCostsGraph();
	}
	
	/**
	 * 
	 * @param graph
	 * @return
	 */
	private List<ArrayList<V>> getAllStraightfowardPaths(Graph<V,E> graph) {
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
	private List<ArrayList<V>> getAllShortestPaths(Graph<V,E> graph) {
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
























