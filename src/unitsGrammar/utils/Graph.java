/***************************************************************************************
*	Title: PotatoesProject - Graph Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Co-author in version 1.0: Pedro Teixeira (https://pedrovt.github.io)
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/
package unitsGrammar.utils;

import static java.lang.System.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * <b>Graph</b><p>
 * Implementation based on adjacencies lists.<p>
 *
 * @author Luis Moura (https://github.com/LuisPedroMoura)
 * @version July 2018
 */
public class Graph {

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
	public class Node {
		
		// attributes
		private Unit vertex;
		private Double edge;		// incoming edge (cost to get to this.vertex)
		private boolean isParent = false;
		
		/**
		 * 
		 * <b>Constructor</b><p>
		 * @param vertex
		 * @param edge
		 */
		protected Node(Unit vertex, Double edge) {
			this.vertex = vertex;
			this.edge = edge;
		}
		
		protected Node(Node n) {
			this.vertex = new Unit(n.getVertex());
			this.edge = n.getEdge();
		}
		
		/**
		 * @return the vertex of this Node
		 */
		protected Unit getVertex() {
			return vertex;
		}
		
		/**
		 * @return the edge of this Node
		 */
		protected Double getEdge() {
			return edge;
		}
		
		/**
		 * @return
		 */
		protected boolean isParent() {
			return isParent;
		}
		
		/**
		 * 
		 */
		protected void setAsParent() {
			isParent = true;
		}

	}
	
	// End of Internal Class Node
	//---------------------------------------------------------------------------
	

	// --------------------------------------------------------------------------
	// Instance Fields
	private Map<Unit,List<Node>> adjList = new HashMap<>();
	private int size;

	// --------------------------------------------------------------------------
	/**
	 * <b>Constructor</b><p> creates the graph. Assumes the Unit E (the Edge) is or extends Class Number
	 */
	public Graph() {}
	
	public Graph(Graph graph) {
		this.size = graph.getSize();
		Set<Unit> keys = graph.getAdjList().keySet();
		for (Unit key : keys) {
			List<Node> newList = new ArrayList<>();
			List<Node> list = graph.getAdjList().get(key);
			for (Node n : list) {
				newList.add(new Node(n));
			}
			this.adjList.put(new Unit(key), newList);
		}
	}
	
	// --------------------------------------------------------------------------
	// Getters, Setters and Resetters
	

	/**
	 * @return adjList
	 */
	public Map<Unit, List<Node>> getAdjList() {
		return adjList;
	}
	
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}	
	
	// --------------------------------------------------------------------------
	// Public Methods
	
	/**
	 * @param vertex to be added to the Graph
	 * @return true if vertex is added, false if it already exists
	 */
	public boolean addVertex(Unit vertex) {
		if (!adjList.containsKey(vertex)) {
			adjList.put(vertex, new ArrayList<>());
			
			if (debug) {
				out.println("\n---");
				out.println("HIERARCHYDIGRAPH - ADDED VERTEX: " + vertex);
				printGraph();
				out.print("---");
			}
			
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
	public boolean addEdge(Double edge, Unit startVertex, Unit endVertex) {
		
		this.size++;
		
		if (edge == null || startVertex == null || endVertex == null) {
			throw new NullPointerException();
		}
		
		// add new Vertices to the Graph
		addVertex(startVertex);
		addVertex(endVertex);
		
		// A new Edge between existing connected vertices is not allowed
		for (Node node : adjList.get(startVertex)) {	
			if (node.getVertex().equals(endVertex)){
				return false;
			}
		}
		
		// Edge does not exist, so its linked to vertices
		//adjList.get(startVertex).add(new Node(endVertex, edge));
		
		// create end Node and add to startVertex adjacency list
		Node endNode = new Node(endVertex, edge);
		adjList.get(startVertex).add(endNode);
		
		if (debug) {
			out.println("\n---");
			out.println("HIERARCHYDIGRAPH - ADDED EDGE: " + startVertex + " -> " + edge + " -> " + endVertex);
			printGraph();
			out.println("---");
		}
		
		return true;
	}
	
	/**
	 * @param vertex
	 * @return true if vertex is in the Graph
	 */
	public boolean containsVertex(Unit vertex) {
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
	public boolean containsEdge(Double edge, Unit startVertex, Unit endVertex) {
		if (adjList.containsKey(startVertex)) {
			for (Node node : adjList.get(startVertex)) {
				if (node.getEdge().equals(edge) && node.getVertex().equals(endVertex)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean containsEdge(Unit startVertex, Unit endVertex) {
		if (adjList.containsKey(startVertex)) {
			for (Node node : adjList.get(startVertex)) {
				if (node.getVertex().equals(endVertex)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean containsEdge(Double edge) {
		for (Unit key : adjList.keySet()) {
			for (Node node : adjList.get(key)) {
				if (node.getEdge().equals(edge)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Double getEdge(Unit startVertex, Unit endVertex) {
		if (adjList.containsKey(startVertex)) {
			for (Node node : adjList.get(startVertex)) {
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
	public List<Double> getVertexOutgoingEdges(Unit vertex){
		List<Double> newList = new ArrayList<>();
		for (Node node : adjList.get(vertex)) {
			newList.add(node.getEdge());	
		}
		return newList;
	}
	
	/**
	 * @param vertex
	 * @return List<E> of incoming Edges for vertex
	 */
	public List<Double> getVertexIncomingEdges(Unit vertex){
		List<Double> newList = new ArrayList<>();
		for (Unit key : adjList.keySet()) {
			List<Node> list = adjList.get(key);
			for (Node node : list) {
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
	public List<Unit> getVertexOutgoingNeighbors(Unit vertex){
		List<Unit> newList = new ArrayList<>();
		
		if (debug) {
			out.println("---");
			out.println("HIERARCHYDIGRAPH - getVertexOutgoingNeighbors(): for vertex: " + vertex);
			printGraph();
			out.print("---");
		}
		
		for (Node node : adjList.get(vertex)) {
			newList.add(node.getVertex());	
		}
		return newList;
	}
	
	/**
	 * 
	 * @param vertex
	 * @return
	 */
	public List<Unit> getVertexIncomingNeighbors(Unit vertex){
		List<Unit> newList = new ArrayList<>();
		for (Unit key : adjList.keySet()) {
			for (Node node : adjList.get(key)) {
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
	public Unit getDest(Unit startVertex, Double edge){
		for (Node node : adjList.get(startVertex)) {
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
	public List<ArrayList<Unit>> dijkstraShortestPaths(Unit startVertex) {
		
		Map<Unit, Double> totalCosts = new HashMap<>(); 	// stores the minimum cost from startVertex to all other vertices
		Map<Unit,Unit> prevVertex = new HashMap<>();			// stores the connections that build the minimum Cost Tree
		Map<Unit, Double> minPath = new HashMap<>();		// improvised Priority Queue, easier to use
		Set<Unit> visited = new HashSet<>();				// keeps track of visited vertices
		
		// initialize with startVertex
		totalCosts.put(startVertex, 0.0);
		minPath.put(startVertex, 0.0);
		
		// initialize the cost to all vertices as infinity
		for (Unit vertex : getAdjList().keySet()) {
			if (vertex != startVertex) {
				totalCosts.put(vertex, Double.POSITIVE_INFINITY);
			}
		}
		
		// Dijkstra algorithm, runs while there are cheaper paths to be explored
		while (!minPath.isEmpty()) {
			
			// Find the next Vertex to be analyzed by finding the minimum Path so far
			Unit newSmallest = (Unit) minPath.keySet().toArray()[0];
			double minPathsmallestValue = minPath.get(newSmallest);
			
			for (Unit vertex : minPath.keySet()) {
				if(minPath.get(vertex) <= minPathsmallestValue) {
					newSmallest = vertex;
				}
			}
			// once found, removes path that will be processed and adds vertex as visited
			minPath.remove(newSmallest);
			visited.add(newSmallest);
			
			// search for neighbors and update paths costs
			List<Unit> neighbors = getVertexOutgoingNeighbors(newSmallest);
			for (Unit neighbor : neighbors) {
				// if already visited, no update necessary
				if (!visited.contains(neighbor)) {
					// calculate path cost
					double altPathCost = totalCosts.get(newSmallest) + getEdge(newSmallest, neighbor);
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
		List<ArrayList<Unit>> shortestPaths = new ArrayList<>();
		
		Set<Unit> vertexSet = getAdjList().keySet();
		vertexSet.remove(startVertex);
		
		// creates paths in reverse other (starting with endVertex to startVertex)
		for (Unit vertex : vertexSet) {
			ArrayList<Unit> path = new ArrayList<>();
			path.add(vertex);
			while(vertex != startVertex) {
				Unit next = prevVertex.get(vertex);
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
	public List<ArrayList<Unit>> dijkstraStraightFowardPaths(Unit startVertex) {
		
		Map<Unit, Double> totalCosts = new HashMap<>(); 	// stores the minimum cost from startVertex to all other vertices
		Map<Unit,Unit> prevVertex = new HashMap<>();			// stores the connections that build the minimum Cost Tree
		Map<Unit, Double> minPath = new HashMap<>();		// improvised Priority Queue, easier to use
		Set<Unit> visited = new HashSet<>();				// keeps track of visited vertices
		
		// initialize with startVertex
		totalCosts.put(startVertex, 0.0);
		minPath.put(startVertex, 0.0);
		
		// initialize the cost to all vertices as infinity
		for (Unit vertex : getAdjList().keySet()) {
			if (vertex != startVertex) {
				totalCosts.put(vertex, Double.POSITIVE_INFINITY);
			}
		}
		
		// Dijkstra algorithm, runs while there are cheaper paths to be explored
		while (!minPath.isEmpty()) {
			
			// Find the next Vertex to be analyzed by finding the minimum Path so far
			Unit newSmallest = (Unit) minPath.keySet().toArray()[0];
			double minPathsmallestValue = minPath.get(newSmallest);

			for (Unit vertex : minPath.keySet()) {
				if(minPath.get(vertex) <= minPathsmallestValue) {
					newSmallest = vertex;
				}
			}
			// once found, removes path that will be processed and adds vertex as visited
			minPath.remove(newSmallest);
			visited.add(newSmallest);
			
			// search for neighbors and update paths costs
			List<Unit> neighbors = getVertexOutgoingNeighbors(newSmallest);
			for (Unit neighbor : neighbors) {
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
		List<ArrayList<Unit>> shortestPaths = new ArrayList<>();
		
		Set<Unit> vertexSet = getAdjList().keySet();
		vertexSet.remove(startVertex);
		
		// creates paths in reverse other (starting with endVertex to startVertex)
		for (Unit vertex : vertexSet) {
			ArrayList<Unit> path = new ArrayList<>();
			path.add(vertex);
			while(vertex != startVertex) {
				Unit next = prevVertex.get(vertex);
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
	
	public void printGraph() {
		System.out.print(this);
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (Unit key : adjList.keySet()) {
			str.append(key + " ->");
			for (Node node : adjList.get(key)) {
				str.append("\n\t" + node.getVertex() + " " + node.getEdge() + "  |  ");
			}
			str.append("\n");
		}
		return str.toString();
	}
}