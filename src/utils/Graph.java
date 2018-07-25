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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.errorHandling.ErrorHandling;

/**
 * 
 * <b>Graph</b><p>
 * Implementation based on adjcencies lists.<p>
 *
 * @author Luis Moura (https://github.com/LuisPedroMoura)
 * @version July 2018
 */
public class Graph <V,E> {

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
		private boolean visited = false;
		
		/**
		 * 
		 * Constructor
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
		 * @return visited
		 */
		public boolean isVisited() {
			return visited;
		}
		
		/**
		 * @param b boolean value
		 */
		public void setVisited(boolean b) {
			visited = b;
		}
		
		// FIXME estava no NODE nao consigo já lembrar porquê... nao faz muito sentido. tentar dar a volta era boa idea.
		public void markPathVisited(V vertex) {
			this.visited = true;
			for (ArrayList<Node<V,E>> list : adjList) {
				if (list.get(0).getVertex().equals(vertex)) {
					list.get(0).setVisited(true);
					break;
				}
			}
		}

	}
	
	// End of Internal Class Node
	//---------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// Static Fields
	private static double pathCost = 1.0;
	private static boolean found = false;

	// --------------------------------------------------------------------------
	// Instance Fields
	private Map<V,ArrayList<Node<V,E>>> adjList = new HashMap<>();
	private int size;

	// --------------------------------------------------------------------------
	/**
	 * Constructor
	 */
	public Graph() {}

	
	// --------------------------------------------------------------------------
	// Getters, Setters and Reseters
	
	public Node<V,E> getNode(V vertex){
		for (ArrayList<Node<V,E>> list : adjList) {
			if (list.get(0).getVertex().equals(vertex)) {
				return list.get(0);
			}
		}
		return null;
	}
	
	public Node<V,E> getNode(E edge){
		for (ArrayList<Node<V,E>> list : adjList) {
			for (Node<V,E> node : list) {
				if (node.getEdge().equals(edge)) {
					return node;
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * @return pathE used in getPath()
	 */
	public static double getPathCost() {
		return pathCost;
	}

	/**
	 * @return adjList
	 */
	public List<ArrayList<Node<V,E>>> getAdjList() {
		return adjList;
	}
	
	/**
	 * Resets E used in getPath()
	 */
	static public void resetPathCost() {
		pathCost = 1.0;
		found = false;
	}
	
	
	
	/**
	 * Clears all visited fields from all Nodes
	 * This function should be called every time after using getPath()
	 */
	public void clearVisited() {
		for (ArrayList<Node<V,E>> list : adjList) {
			for (Node<V,E> node : list) {
				node.setVisited(false);
			}
		}
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
	
	/** 
	 * @param vertex
	 * @return List<E> of outgoing Edges for vertex
	 */
	public List<E> getOutgoingEdges(V vertex){
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
	public List<E> getIncomingEdges(V vertex){
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
	 * @param vertex
	 * @param f
	 * @return destination vertex of edge f starting in vertex, null if destination does not exist
	 */
	public V getDest(V startVertex, E edge){
		List<Node<V,E>> nodes = new ArrayList<>();;
		for (Node<V,E> node : adjList.get(startVertex)) {
			if (node.getEdge().equals(edge)) {
				return node.getVertex();
			}
		}
		return null;
	}

	/**
	 * @param vertex
	 * @return the index of the adjacency list to the Node with vertex "vertex"
	 */
	public int getIndexOfNode(V vertex) {
		for (ArrayList<Node> list : adjList) {
			if (list.get(0).getV().equals(vertex)) {
				if (debug) {
					ErrorHandling.printInfo("LIST INDEX : " + adjList.indexOf(list));
				}
				return adjList.indexOf(list);
			}
		}
		return -1;
	}

	/**
	 * @param start vertex of the Node to start path from
	 * @param end vertex of the Node to end path in
	 * @return the cost of traversing the path (as a edge)
	 * @throws Exception if any of the vertexs given is not present in Graph
	 */
	public double getPathCost(V start, V end) {

		int index = getIndexOfNode(start);
		if (index != -1) {
			ArrayList<Node> adj = adjList.get(getIndexOfNode(start));
			found = false;
			for (int i = 1; i < adj.size(); i++) {
				Node node = null;
				if (!found) {
					node = adj.get(i);
					if (debug) {
						ErrorHandling.printInfo("E is: "+ pathE + " node is: " + node.getV().getVName() + " with edge "+node.getE().getE());
						ErrorHandling.printInfo("E is: "+ pathE);
						ErrorHandling.printInfo(node.getV().getVName() + " " + node.getE().getE());
					}
					pathE *= node.getE().getE();
				}

				if (node.getV().equals(end)) {
					found = true;
					//clearVisited();
					return node.getE().getE();
				}
				if (!node.isVisited() && !found) {
					node.markPathVisited(node.getV());
					pathE *= getPathCost(node.getV(),end);
					if (debug) ErrorHandling.printInfo("E is: "+ pathE);
				}
			}
		}
		//clearVisited();
		return pathE;
	}
	
	/**
	 * Same function as previous, boolean return
	 * @param start
	 * @param end
	 * @return
	 */
	public boolean isCompatible(V start, V end) {

		int index = getIndexOfNode(start);
		if (index != -1) {
			ArrayList<Node> adj = adjList.get(getIndexOfNode(start));
			found = false;
			for (int i = 1; i < adj.size(); i++) {
				Node node = null; 
				if (!found) {
					node = adj.get(i);
					if (debug) {
						ErrorHandling.printInfo("GRAPH E is: "+ pathE + " node is: " + node.getV().getVName() + " with edge "+node.getE().getE());
						ErrorHandling.printInfo(node.getV().getVName() + " " + node.getE().getE());
					}
				}

				if (debug) {
					ErrorHandling.printInfo("GRAPH THIS IS NODE: "+ node.getV());
				}
				if (node.getV().equals(end)) {
					if (debug) {
						ErrorHandling.printInfo("FOUND COMPATIBLE TYPE!");
					}
					found = true;
					//clearVisited();
					break;
				}
				if (!node.isVisited() && !found) {
					node.markPathVisited(node.getV());
					isCompatible(node.getV(),end);
					if (debug) {
						ErrorHandling.printInfo("GRAPH E is: "+ pathE);
					}
				}
			}
			return true;
		}
		return false;
	}
	
	

	


	// --------------------------------------------------------------------------
	// Other Methods
	/**
	 * Prints the graph rudimentarily in the console
	 */
	public void printGraph() {
		for (ArrayList<Node> list : adjList) {
			for (Node node : list) {
				System.out.print(" |  " + node.getV().getVName() + " " + node.getE().getE() + " " + node.getE().getIsChildToParent());
			}
		}
	}
}