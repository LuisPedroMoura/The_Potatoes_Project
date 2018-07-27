/***************************************************************************************
*	Title: PotatoesProject - Graph Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Co-author in version 1.0: Pedro Teixeira (https://pedrovt.github.io)
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/
package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.lang.Number;

/**
 * 
 * <b>Graph</b><p>
 * Implementation based on adjcencies lists.<p>
 *
 * @author Luis Moura (https://github.com/LuisPedroMoura)
 * @version July 2018
 */
public class HierarchyDiGraph <V,E> {

	// Static Constant (Debug Only)
	private static final boolean debug = false;

	// --------------------------------------------------------------------------
	// Internal Class Node
	/**
	 * 
	 * <b>Node</b><p>
	 * Internal Class (Structure)
	 * Contains V and InComing Edge.
	 * Visited field is helpful to function getPath() to determine paths between two Nodes in Graph
	 * 
	 * @author Luis Moura (https://github.com/LuisPedroMoura)
	 * @version July 2018
	 */
	public class Node<V,E> {
		
		// attributes
		private V vertex;
		private E edge;		// incoming edge (cost to get to this.vertex)
		private boolean isParent = false;
		
		/**
		 * 
		 * <b>Constructor</b><p>
		 * @param vertex
		 * @param edge
		 */
		public Node(V vertex, E edge) {
			this.vertex = vertex;
			this.edge = edge;
		}
		
		/**
		 * @return the vertex of this Node
		 */
		public V getVertex() {
			return vertex;
		}
		
		/**
		 * @return the edge of this Node
		 */
		public E getEdge() {
			return edge;
		}
		
		/**
		 * @return
		 */
		public boolean isParent() {
			return isParent;
		}
		
		/**
		 * 
		 */
		public void setAsParent() {
			isParent = true;
		}

	}
	
	// End of Internal Class Node
	//---------------------------------------------------------------------------
	

	// --------------------------------------------------------------------------
	// Instance Fields
	protected Function<? super E, ? extends Number> edgeCostFunction;
	private Map<V,ArrayList<Node<V,E>>> adjList = new HashMap<>();
	private int size;

	// --------------------------------------------------------------------------
	/**
	 * <b>Constructor</b><p> creates the graph. Assumes the Type E (the Edge) is or extends Class Number
	 */
	public HierarchyDiGraph() {
		// TODO verify what else can be done to prevent errors and avoid System.exit
		// TODO and verify if this actually works
		this.edgeCostFunction = new Function<E, Number>() {
			
			public Number apply(E edge) {
				Number number = null;
				try {
					number = (Number) edge;
				}
				catch (Exception e) {
					System.out.println("ERROR: default constructor of Graph<V,E> must have Type E that extends Number");
					System.out.println("Use constructor: Graph(Function<E, ? extends Number>) with a function that returns\n"
										+ "the Double value of the Edge cost");
					System.exit(1);
				}
				return number.doubleValue();
			}
		};
	}
	
	/**
	 * <b>Constructor</b> - creates the graph and the specified method of extracting cost from edges 
	 * @param edgeCostFunction the specified method of extracting cost from edges
	 */
	public HierarchyDiGraph(Function<E, ? extends Number> edgeCostFunction) {
		this.edgeCostFunction = edgeCostFunction;
	}

	
	// --------------------------------------------------------------------------
	// Getters, Setters and Reseters
	
	/**
	 * @return edgeCostFunction
	 */
	public Function<? super E, ? extends Number> getEdgeCostFunction(){
		return edgeCostFunction;
	}
	
	/**
	 * @return adjList
	 */
	public Map<V,ArrayList<Node<V,E>>> getAdjList() {
		return adjList;
	}
	
	/**
	 * 
	 * @param edge
	 * @return
	 */
	public double getEdgeCost(E edge) {
		if (edge instanceof Number) {
			return ((Number) edge).doubleValue();
		}
		return edgeCostFunction.apply(edge).doubleValue();
	}
	
	// --------------------------------------------------------------------------
	// Public Methods
	
	/**
	 * @param vertex to be added to the Graph
	 * @return true if vertex is added, false if it already exists
	 */
	public boolean addVertex(V vertex) {
		if (!adjList.containsKey(vertex)) {
			adjList.put(vertex, new ArrayList<>());
			return true;
		}
		return false;
	}
	
	/**
	 * @param edge
	 * @param startVertex the vertex to apply outGoing edge
	 * @param endVertex the vertex to apply inComing edge (in the Node object)
	 * @throws NullPointerException if any param is null
	 * @return true if Edge is added, false if edge already exists
	 */
	public boolean addEdge(E edge, V startVertex, V endVertex) {
		
		this.size++;
		
		if (edge == null || startVertex == null || endVertex == null) {
			throw new NullPointerException();
		}
		
		// add new Vertices to the Graph
		addVertex(startVertex);
		addVertex(endVertex);
		
		// A new Edge between existing connected vertices is not allowed
		for (Node<V,E> node : adjList.get(startVertex)) {	
			if (node.getVertex().equals(endVertex)){
				return false;
			}
		}
		
		// Edge does not exist, so its linked to vertices
		adjList.get(startVertex).add(new Node<V,E>(endVertex, edge));
		
		// create end Node and add to startVertex adjacency list
		Node<V,E> endNode = new Node<V,E>(endVertex, edge);
		adjList.get(startVertex).add(endNode);
		
		return true;
	}
	
	/**
	 * @param vertex
	 * @return true if vertex is in the Graph
	 */
	public boolean containsVertex(V vertex) {
		if(adjList.containsKey(vertex)) {
			return true;
		}
		return false;
	}
	
	/**
	 * checks if the Graph contains the specific Edge that connects the given vertices in the correct direction
	 * @param edge
	 * @param startVertex
	 * @param endVertex
	 * @return
	 */
	public boolean containsEdge(E edge, V startVertex, V endVertex) {
		if (adjList.containsKey(startVertex)) {
			for (Node<V,E> node : adjList.get(startVertex)) {
				if (node.getEdge().equals(edge) && node.getVertex().equals(endVertex)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean containsEdge(V startVertex, V endVertex) {
		if (adjList.containsKey(startVertex)) {
			for (Node<V,E> node : adjList.get(startVertex)) {
				if (node.getVertex().equals(endVertex)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean containsEdge(E edge) {
		for (V key : adjList.keySet()) {
			for (Node<V,E> node : adjList.get(key)) {
				if (node.getEdge().equals(edge)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public E getEdge(V startVertex, V endVertex) {
		if (adjList.containsKey(startVertex)) {
			for (Node<V,E> node : adjList.get(startVertex)) {
				if (node.getVertex().equals(endVertex)){
					return node.getEdge();
				}
			}
		}
		return null;
	}
	
	/** 
	 * @param vertex
	 * @return List<E> of outgoing Edges for vertex
	 */
	public List<E> getVertexOutgoingEdges(V vertex){
		List<E> newList = new ArrayList<>();
		for (Node<V,E> node : adjList.get(vertex)) {
			newList.add(node.getEdge());	
		}
		return newList;
	}
	
	/**
	 * @param vertex
	 * @return List<E> of incoming Edges for vertex
	 */
	public List<E> getVertexIncomingEdges(V vertex){
		List<E> newList = new ArrayList<>();
		for (V key : adjList.keySet()) {
			ArrayList<Node<V,E>> list = adjList.get(key);
			for (Node<V,E> node : list) {
				if (node.getVertex().equals(vertex)) {
					newList.add(node.getEdge());
				}
			}
		}
		return newList;
	}
	
	/**
	 * 
	 * @param vertex
	 * @return
	 */
	public List<V> getVertexOutgoingNeighbors(V vertex){
		List<V> newList = new ArrayList<>();
		for (Node<V,E> node : adjList.get(vertex)) {
			newList.add(node.getVertex());	
		}
		return newList;
	}
	
	/**
	 * 
	 * @param vertex
	 * @return
	 */
	public List<V> getVertexIncomingNeighbors(V vertex){
		List<V> newList = new ArrayList<>();
		for (V key : adjList.keySet()) {
			for (Node<V,E> node : adjList.get(key)) {
				if (node.getVertex().equals(vertex)) {
					newList.add(key);
				}
			}
		}
		return newList;
	}

	/**
	 * @param vertex
	 * @param f
	 * @return destination vertex of edge starting in startVertex, null if destination does not exist
	 */
	public V getDest(V startVertex, E edge){
		for (Node<V,E> node : adjList.get(startVertex)) {
			if (node.getEdge().equals(edge)) {
				return node.getVertex();
			}
		}
		return null;
	}	
	
	/**
	 * 
	 * @param startVertex
	 * @return
	 */
	public List<ArrayList<V>> dijkstraShortestPaths(V startVertex) {
		
		Map<V, Double> totalCosts = new HashMap<>(); 	// stores the minimum cost from startVertex to all other vertices
		Map<V,V> prevVertex = new HashMap<>();			// stores the connections that build the minimum Cost Tree
		Map<V, Double> minPath = new HashMap<>();		// improvised Priority Queue, easier to use
		Set<V> visited = new HashSet<>();				// keeps track of visited vertices
		
		// initialize with startVertex
		totalCosts.put(startVertex, 0.0);
		minPath.put(startVertex, 0.0);
		
		// initialize the cost to all vertices as infinity
		for (V vertex : getAdjList().keySet()) {
			if (vertex != startVertex) {
				totalCosts.put(vertex, Double.POSITIVE_INFINITY);
			}
		}
		
		// Dijkstra algorithm, runs while there are cheaper paths to be explored
		while (!minPath.isEmpty()) {
			
			// Find the next Vertex to be analyzed by finding the minimum Path so far
			double minPathsmallestValue = minPath.get(0);
			V newSmallest = null;
			for (V vertex : minPath.keySet()) {
				if(minPath.get(vertex) <= minPathsmallestValue) {
					newSmallest = vertex;
				}
			}
			// once found, removes path that will be processed and adds vertex as visited
			minPath.remove(newSmallest);
			visited.add(newSmallest);
			
			// search for neighbors and update paths costs
			List<V> neighbors = getVertexOutgoingNeighbors(newSmallest);
			for (V neighbor : neighbors) {
				// if already visited, no update necessary
				if (!visited.contains(neighbor)) {
					// calculate path cost
					double altPathCost = totalCosts.get(newSmallest) + getEdgeCost(getEdge(newSmallest, neighbor));
					// if calculated path cost is cheaper than previous calculation, replace and store information
					if (altPathCost < totalCosts.get(neighbor)) {
						// update total cost to get to this vertex (now is cheaper)
						totalCosts.put(neighbor, altPathCost);
						// update the previous vertex to this one that made the path cheaper
						prevVertex.put(neighbor, newSmallest);
						// store the new minimum path to later processing
						minPath.put(neighbor, altPathCost);
					}
				}
			}	
		}
		
		// shortest paths are calculated but not organized, connection between vertices that form the minimum Cost Tree
		// are calculated but not ordered. The following steps stores a List of Lists of all the minimum cost paths
		// starting in startVertex to all other vertices.
		List<ArrayList<V>> shortestPaths = new ArrayList<>();
		
		Set<V> vertexSet = getAdjList().keySet();
		vertexSet.remove(startVertex);
		
		// creates paths in reverse other (starting with endVertex to startVertex)
		for (V vertex : vertexSet) {
			ArrayList<V> path = new ArrayList<>();
			path.add(vertex);
			while(vertex != startVertex) {
				V next = prevVertex.get(vertex);
				path.add(next);
				vertex = next;
			}
			Collections.reverse(path);
			shortestPaths.add(path);
		}

		return shortestPaths;
	}
	
	/**
	 * Same as dijkstraShortestPaths() but all paths are considered as having cost 1
	 * @param startVertex
	 * @return
	 */
	public List<ArrayList<V>> dijkstraStraightFowardPaths(V startVertex) {
		
		Map<V, Double> totalCosts = new HashMap<>(); 	// stores the minimum cost from startVertex to all other vertices
		Map<V,V> prevVertex = new HashMap<>();			// stores the connections that build the minimum Cost Tree
		Map<V, Double> minPath = new HashMap<>();		// improvised Priority Queue, easier to use
		Set<V> visited = new HashSet<>();				// keeps track of visited vertices
		
		// initialize with startVertex
		totalCosts.put(startVertex, 0.0);
		minPath.put(startVertex, 0.0);
		
		// initialize the cost to all vertices as infinity
		for (V vertex : getAdjList().keySet()) {
			if (vertex != startVertex) {
				totalCosts.put(vertex, Double.POSITIVE_INFINITY);
			}
		}
		
		// Dijkstra algorithm, runs while there are cheaper paths to be explored
		while (!minPath.isEmpty()) {
			
			// Find the next Vertex to be analyzed by finding the minimum Path so far
			double minPathsmallestValue = minPath.get(0);
			V newSmallest = null;
			for (V vertex : minPath.keySet()) {
				if(minPath.get(vertex) <= minPathsmallestValue) {
					newSmallest = vertex;
				}
			}
			// once found, removes path that will be processed and adds vertex as visited
			minPath.remove(newSmallest);
			visited.add(newSmallest);
			
			// search for neighbors and update paths costs
			List<V> neighbors = getVertexOutgoingNeighbors(newSmallest);
			for (V neighbor : neighbors) {
				// if already visited, no update necessary
				if (!visited.contains(neighbor)) {
					// calculate path cost
					double altPathCost = totalCosts.get(newSmallest) + 1.0;
					// if calculated path cost is cheaper than previous calculation, replace and store information
					if (altPathCost < totalCosts.get(neighbor)) {
						// update total cost to get to this vertex (now is cheaper)
						totalCosts.put(neighbor, altPathCost);
						// update the previous vertex to this one that made the path cheaper
						prevVertex.put(neighbor, newSmallest);
						// store the new minimum path to later processing
						minPath.put(neighbor, altPathCost);
					}
				}
			}	
		}
		
		// shortest paths are calculated but not organized, connection between vertices that form the minimum Cost Tree
		// are calculated but not ordered. The following steps stores a List of Lists of all the minimum cost paths
		// starting in startVertex to all other vertices.
		List<ArrayList<V>> shortestPaths = new ArrayList<>();
		
		Set<V> vertexSet = getAdjList().keySet();
		vertexSet.remove(startVertex);
		
		// creates paths in reverse other (starting with endVertex to startVertex)
		for (V vertex : vertexSet) {
			ArrayList<V> path = new ArrayList<>();
			path.add(vertex);
			while(vertex != startVertex) {
				V next = prevVertex.get(vertex);
				path.add(next);
				vertex = next;
			}
			Collections.reverse(path);
			shortestPaths.add(path);
		}

		return shortestPaths;
	}
	

	// --------------------------------------------------------------------------
	// Other Methods

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (V key : adjList.keySet()) {
			for (Node<V,E> node : adjList.get(key)) {
				str.append(node.getVertex() + " " + node.getEdge() + "  |  ");
			}
			str.append("\n");
		}
		return str.toString();
	}
}