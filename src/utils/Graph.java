package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Variable</b><p>
 * To be used on the general purpose language<p>
 * For example, an instruction like {@code distance x = (distance) 5} will create an instance of this object with Type {@code distance}
 * (if the type exists in the Types table) and value {@code 5}.<p>
 * @author Inês Justo (84804), Luis Pedro Moura (83808), Maria João Lavoura (84681), Pedro Teixeira (84715)
 * @version May-June 2018
 */
public class Graph {
	
	/**
	 * 
	 * <b>Node</b><p>
	 * Internal Class (Structure)
	 * Contains Type and InComing Edge (Factor).
	 * Visited field is helpful to function getPath() to determine paths between two Nodes in Graph
	 * 
	 * @author Ines Justo (84804), Luis Pedro Moura (83808), Maria Joao Lavoura (84681), Pedro Teixeira (84715)
	 * @version May-June 2018
	 */
	public class Node {

		private Type type;
		private Factor factor;
		private boolean visited = false;

		public Node(Type type, Factor factor) {
			super();
			this.type = type;
			this.factor = factor;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		public Factor getFactor() {
			return factor;
		}

		public void setFactor(Factor factor) {
			this.factor = factor;
		}

		public boolean isVisited() {
			return visited;
		}

		public void setVisited(boolean b) {
			visited = b;
		}

		public void markPathVisited(Type Type) {
			this.visited = true;
			for (ArrayList<Node> list : adjList) {
				if (list.get(0).getType().equals(type)) {
					list.get(0).setVisited(true);
					break;
				}
			}
		}
		
	}
	
	// END OF INTERNAL CLASS


	// Fields
	private List<ArrayList<Node>> adjList = new ArrayList<>();
	private int size;
	private int visitedCount;

	// Constructor
	public Graph() {}


	// Getters, Setters and Reseters
	/**
	 * Resets Factor used in getPath()
	 */
	static public void resetFactor() {
		pathFactor = 1.0;
		found = false;
	}

	/**
	 * @return pathFactor used in getPath()
	 */
	static public double getPathFactor() {
		return pathFactor;
	}
	
	public List<ArrayList<Node>> getAdjList() {
		return adjList;
	}
	

	/**
	 * Clears all visited fields from all Nodes
	 * This function should be called everytime after using getPath()
	 */
	public void clearVisited() {
		for (ArrayList<Node> list : adjList) {
			for (Node node : list) {
				node.setVisited(false);
			}
		}
	}
	
	public List<Factor> getOutEdges(Type type){
		List<Factor> newList = new ArrayList<>();
		for (ArrayList<Node> list : adjList) {
			if (list.get(0).getType().equals(type)) {
				for (Node node : list) {
					if(!node.getType().equals(type))
						newList.add(node.getFactor());
				}
				return newList;
			}
		}
		return null;
	}
	
	public Type getDest(Type type, Factor f){
		List<Node> nodes = new ArrayList<>();;
		for (ArrayList<Node> list : adjList) {
			if (list.get(0).getType().equals(type)) {
				for (Node node : list) {
					if(!node.getType().equals(type))
						nodes.add(node);
				}
			}
		}
		for (Node node : nodes) {
			if (node.getFactor().equals(f)) {
				return node.getType();
			}
		}
		return null;
	}

	/**
	 * Prints the graph rudimentarily in the console
	 */
	public void printGraph() {
		for (ArrayList<Node> list : adjList) {
			for (Node node : list) {
				System.out.print(" |  " + node.getType().getTypeName() + " " + node.getFactor().getFactor() + " " + node.getFactor().getIsChildToParent());
			}
			System.out.println();
		}
	}

	/**
	 * @param type
	 * @return true if type belongs to any Node, false if is not present in any Node of the Graph
	 */
	public boolean containsVertex(Type type) {
		for (ArrayList<Node> list : adjList) {
			if (list.get(0).getType().equals(type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param factor cost of the Edge and isChildToParent relation
	 * @param start type of the Node to apply outGoing Edge (Factor)
	 * @param end type of the Node to apply inComing Edge (Factor)
	 * @return true if Edge is added, false if any of the types is not present in the Graph
	 */
	public boolean addEdge(Factor factor, Type start, Type end) {

		if (factor == null || start == null || end == null) {
			return false;
		}

		// creates start node with Factor 1 to himself
		Node nodeStart = new Node(start, new Factor(1.0, false));

		// creates end node with given Factor
		Node nodeEnd = new Node(end, new Factor (1/factor.getFactor(), !factor.getIsChildToParent()));

		boolean foundStart = false;
		for (ArrayList<Node> list : adjList) {
			if (list.get(0).getType().equals(start)) {
				list.add(nodeEnd);
				foundStart = true;
				size += 1;
				return true;
			}
		}
		if (!foundStart) {
			ArrayList<Node> newList = new ArrayList<>();
			newList.add(nodeStart);
			newList.add(nodeEnd);
			adjList.add(newList);
			size += 2;
			return true;
		}


		// invert nodes to maintain Factor convertion logic
		nodeEnd = new Node(end, new Factor(1.0, false));
		Factor newFactor = new Factor (1/factor.getFactor(), false);
		nodeStart = new Node(start, newFactor);

		boolean foundEnd = false;
		for (ArrayList<Node> list : adjList) {
			if (list.get(0).getType().equals(end)) {
				list.add(nodeStart);
				foundEnd = true;
				size += 1;
				return true;
			}
		}
		if (!foundEnd) {
			ArrayList<Node> newList = new ArrayList<>();
			newList.add(nodeEnd);
			newList.add(nodeStart);
			adjList.add(newList);
			size += 2;
			return true;
		}

		return false;

	}

	/**
	 * @param type
	 * @return the index of the adjacency list to the Node with type "type"
	 */
	public int getIndexOfNode(Type type) {
		for (ArrayList<Node> list : adjList) {
			if (list.get(0).getType().equals(type)) {
				System.err.println("LIST INDEX : " + adjList.indexOf(list));
				return adjList.indexOf(list);
			}
		}
		return -1;
	}

	private static double pathFactor = 1.0;
	private static boolean found = false;
	/**
	 * 
	 * @param start type of the Node to start path from
	 * @param end type of the Node to end path in
	 * @return the cost of traversing the path (as a factor)
	 * @throws Exception if any of the types given is not present in Graph
	 */
	public double getPathCost(Type start, Type end) {

		int index = getIndexOfNode(start);
		if (index != -1) {
			ArrayList<Node> adj = adjList.get(getIndexOfNode(start));
			found = false;
			for (int i = 1; i < adj.size(); i++) {
				Node node = null;
				if (!found) {
					node = adj.get(i);
					System.err.println("Factor is: "+ pathFactor + " node is: " + node.getType().getTypeName() + " with factor "+node.getFactor().getFactor());
					pathFactor *= node.getFactor().getFactor();
					System.err.println("Factor is: "+ pathFactor);
					System.err.println(node.getType().getTypeName() + " " + node.getFactor().getFactor());
				}

				if (node.getType().equals(end)) {
					found = true;
					//clearVisited();
					return node.getFactor().getFactor();
				}
				if (!node.isVisited() && !found) {
					node.markPathVisited(node.getType());
					pathFactor *= getPathCost(node.getType(),end);
					System.err.println("Factor is: "+ pathFactor);
				}
			}
		}
		//clearVisited();
		return pathFactor;
	}
	
	/**
	 * Same function as previous, boolean return

	 * @param start
	 * @param end
	 * @return
	 */
	public boolean isCompatible(Type start, Type end) {

		int index = getIndexOfNode(start);
		if (index != -1) {
			ArrayList<Node> adj = adjList.get(getIndexOfNode(start));
			found = false;
			for (int i = 1; i < adj.size(); i++) {
				Node node = null; //FIXME its this null here!
				if (!found) {
					node = adj.get(i);
					System.err.println("GRAPH Factor is: "+ pathFactor + " node is: " + node.getType().getTypeName() + " with factor "+node.getFactor().getFactor());
					System.err.println(node.getType().getTypeName() + " " + node.getFactor().getFactor());
				}
				// FIXME ???
				System.out.println("GRAPH THIS IS NODE: "+ node.getType());
				if (node.getType().equals(end)) {
					System.out.println("!!!!!!!!!!!!!!!  FOUND COMPATIBLE TYPE !!!!!!!!!!!!!!!!");
					found = true;
					//clearVisited();
					break;
				}
				if (!node.isVisited() && !found) {
					node.markPathVisited(node.getType());
					isCompatible(node.getType(),end);
					System.err.println("GRAPH Factor is: "+ pathFactor);
				}
			}
			return true;
		}
		return false;
	}


	/**
	 * @return
	 */
	

}