package graph;

import java.util.*;

/**
 * This class represents a general "directed graph", which could be used for any purpose.  The graph is viewed as a
 * collection of vertices, which are sometimes connected by weighted, directed edges.
 * 
 * This graph will never store duplicate vertices.
 * 
 * The weights will always be non-negative integers.
 * 
 * The WeightedGraph will be capable of performing three algorithms:
 * Depth-First-Search, Breadth-First-Search, and Djikatra's.
 * 
 * The Weighted Graph will maintain a collection of "GraphAlgorithmObservers", which will be notified during the
 * performance of the graph algorithms to update the observers on how the algorithms are progressing.
 */
public class WeightedGraph<V> {

	// instance variables
	/**
	 * A Map with the keys as vertices and the values as another map representing the adjacencies and weights of the
	 * edges connecting the vertices and adjacencies is used to implement the weighted, directed graph.
	 */

	private Map<V, Map<V, Integer>> weightedGraph;

	/**
	 * Collection of observers. The method "addObserver" is called to populate this collection. The graph algorithms 
	 * (DFS, BFS, and Dijkstra) will notify these observers to let them know how the algorithms are progressing. 
	 */
	private Collection<GraphAlgorithmObserver<V>> observerList;

	// constructor
	/** Initializes the data structures to "empty", including the collection of GraphAlgorithmObservers
	 * (observerList).
	 */
	public WeightedGraph() {
		weightedGraph = new HashMap<>();
		observerList = new ArrayList<>();
	}

	// methods
	/** Adds a GraphAlgorithmObserver to the collection maintained by this graph (observerList).
	 * 
	 * @param observer
	 */
	public void addObserver(GraphAlgorithmObserver<V> observer) {
		observerList.add(observer);
	}

	/** Adds a vertex to the graph.  If the vertex is already in the graph, throws an IllegalArgumentException.
	 * 
	 * @param vertex vertex to be added to the graph
	 * @throws IllegalArgumentException if the vertex is already in the graph
	 */
	public void addVertex(V vertex) {
		if (containsVertex(vertex)) {
			throw new IllegalArgumentException();
		} else {
			Map<V, Integer> set = new HashMap<V, Integer>();
			weightedGraph.put(vertex, set);
		}
	}

	/** Searches for a given vertex.
	 * 
	 * @param vertex the vertex we are looking for
	 * @return true if the vertex is in the graph, false otherwise.
	 */
	public boolean containsVertex(V vertex) {
		return weightedGraph.containsKey(vertex);
	}

	/** 
	 * Adds an edge from one vertex of the graph to another, with the weight specified.
	 * 
	 * The two vertices must already be present in the graph.
	 * 
	 * This method throws an IllegalArgumentExeption in three
	 * cases:
	 * 1. The "from" vertex is not already in the graph.
	 * 2. The "to" vertex is not already in the graph.
	 * 3. The weight is less than 0.
	 * 
	 * @param from the vertex the edge leads from
	 * @param to the vertex the edge leads to
	 * @param weight the (non-negative) weight of this edge
	 * @throws IllegalArgumentException when either vertex is not in the graph, or the weight is negative.
	 */
	public void addEdge(V from, V to, Integer weight) {
		if (containsVertex(from) == false || containsVertex(to) == false || weight < 0) {
			throw new IllegalArgumentException();
		} else {
			Map<V, Integer> values = weightedGraph.get(from);
			values.put(to, weight);
			weightedGraph.put(from, values);
		}
	}

	/** 
	 * Returns the weight of the edge connecting one vertex to another. Returns null if the edge does not exist.
	 * 
	 * Throws an IllegalArgumentException if either of the vertices specified are not in the graph.
	 * 
	 * @param from vertex where edge begins
	 * @param to vertex where edge terminates
	 * @return weight of the edge, or null if there is no edge connecting these vertices
	 * @throws IllegalArgumentException if either of the vertices specified are not in the graph.
	 */
	public Integer getWeight(V from, V to) {
		if (containsVertex(from) == false || containsVertex(to) == false) {
			throw new IllegalArgumentException();
		} else {
			return weightedGraph.get(from).get(to);
		}
	}

	/** 
	 * This method performs a Breadth-First-Search on the graph. The search begins at the "start" vertex and concludes
	 * once the "end" vertex has been reached.
	 * 
	 * Before the search begins, this method will goes through the collection of Observers, calling notifyBFSHasBegun
	 * on each one.
	 * 
	 * Just after a particular vertex is visited, this method goes through the collection of observers, calling
	 * notifyVisit on each one (passing in the vertex being visited as the argument.)
	 * 
	 * After the "end" vertex has been visited, this method goes through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method terminates immediately, without processing further 
	 * vertices. 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex is visited
	 */
	public void DoBFS(V start, V end) {
		for (GraphAlgorithmObserver<V> observer : observerList) { // begin search
			observer.notifyBFSHasBegun();
		}
		Set<V> visitedSet = new HashSet<V>();
		Queue<V> queue = new LinkedList<V>();
		queue.add(start);
		while (queue.isEmpty() == false) {
			V first = queue.poll();
			if (visitedSet.contains(first) == false) {
				for (GraphAlgorithmObserver<V> observer : observerList) { // visits the vertex
					observer.notifyVisit(first);
				}
				if (first.equals(end)) { // determines whether end has been reached yet
					for (GraphAlgorithmObserver<V> observer : observerList) {
						observer.notifySearchIsOver();
					}
					return;
				}
				visitedSet.add(first);
				for (V adjacency : weightedGraph.get(first).keySet()) {
					if (visitedSet.contains(adjacency) == false) {
						queue.add(adjacency); // adds adjacency to queue
					}
				}
			}
		}
	}

	/** 
	 * This method will perform a Depth-First-Search on the graph. The search begins at the "start" vertex and
	 * concludes once the "end" vertex has been reached.
	 * 
	 * Before the search begins, this method goes through the collection of Observers, calling notifyDFSHasBegun on
	 * each one.
	 * 
	 * Just after a particular vertex is visited, this method goes through the collection of observers, calling
	 * notifyVisit on each one (passing in the vertex being visited as the argument.)
	 * 
	 * After the "end" vertex has been visited, this method goes through the collection of observers, calling 
	 * notifySearchIsOver on each one, after which the method terminates immediately, without visiting further 
	 * vertices.
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex is visited
	 */
	public void DoDFS(V start, V end) {
		for (GraphAlgorithmObserver<V> observer : observerList) { // begin search
			observer.notifyDFSHasBegun();
		}
		Set<V> visitedSet = new HashSet<V>();
		Stack<V> stack = new Stack<V>();
		stack.add(start);
		while (stack.isEmpty() == false) {
			V first = stack.pop();
			if (visitedSet.contains(first) == false) {
				for (GraphAlgorithmObserver<V> observer : observerList) { // visits the vertex
					observer.notifyVisit(first);
				}
				if (first.equals(end)) { // determines whether end has been reached yet
					for (GraphAlgorithmObserver<V> observer : observerList) {
						observer.notifySearchIsOver();
					}
					return;
				}
				visitedSet.add(first);
				for (V adjacency : weightedGraph.get(first).keySet()) {
					if (visitedSet.contains(adjacency) == false) {
						stack.add(adjacency); // adds adjacency to queue
					}
				}
			}
		}
	}

	/** 
	 * Performs Dijkstra's algorithm, beginning at the "start" vertex.
	 * 
	 * The algorithm DOES NOT terminate when the "end" vertex is reached.  It will continue until EVERY vertex in the
	 * graph has been added to the finished set.
	 * 
	 * Before the algorithm begins, this method goes through the collection of Observers, calling
	 * notifyDijkstraHasBegun on each Observer.
	 * 
	 * Each time a vertex is added to the "finished set", this method goes through the collection of Observers, calling 
	 * notifyDijkstraVertexFinished on each one (passing the vertex that was just added to the finished set as the
	 * first argument, and the optimal "cost" of the path leading to that vertex as the second argument.)
	 * 
	 * After all of the vertices have been added to the finished set, the algorithm will calculate the "least cost"
	 * path of vertices leading from the starting vertex to the ending vertex. Next, it will go through the collection 
	 * of observers, calling notifyDijkstraIsOver on each one, passing in as the argument the "lowest cost" sequence of 
	 * vertices that leads from start to end (I.e. the first vertex in the list will be the "start" vertex, and the
	 * last vertex in the list will be the "end" vertex.)
	 * 
	 * @param start vertex where algorithm will start
	 * @param end special vertex used as the end of the path reported to observers via the notifyDijkstraIsOver method.
	 */
	public void DoDijsktra(V start, V end) {
		for (GraphAlgorithmObserver<V> observer : observerList) { // begins dijsktras
			observer.notifyDijkstraHasBegun();
		}
		Set<V> finishedSet = new HashSet<V>();
		Map<V, V> pred = new HashMap<>();
		Map<V, Integer> cost = new HashMap<>();
		for (V vertex : weightedGraph.keySet()) { // sets pred to null and cost to infinity
			pred.put(vertex, null);
			cost.put(vertex, Integer.MAX_VALUE);
		}
		cost.put(start, 0);
		while (finishedSet.containsAll(weightedGraph.keySet()) == false) {
			V lowestVertex = null;
			Integer lowestCost = null;
			for (V vertex : weightedGraph.keySet()) { // finds lowest cost vertex
				if (finishedSet.contains(vertex) == false) {
					if (lowestCost == null || cost.get(vertex) < lowestCost) {
						lowestCost = cost.get(vertex);
						lowestVertex = vertex;
					}
				}
			}
			finishedSet.add(lowestVertex);
			for (GraphAlgorithmObserver<V> observer : observerList) { // vertex is added
				observer.notifyDijkstraVertexFinished(lowestVertex, lowestCost);
			}
			for (V adjacency : weightedGraph.get(lowestVertex).keySet()) { // finds lowest weight of adjacencies
				if (finishedSet.contains(adjacency) == false) {
					if (cost.get(lowestVertex) + getWeight(lowestVertex, adjacency) < cost.get(adjacency)) {
						cost.put(adjacency, cost.get(lowestVertex) + getWeight(lowestVertex, adjacency));
						pred.put(adjacency, lowestVertex);
					}
				}
			}
		}
		ArrayList<V> smallestPath = new ArrayList<>();
		V curr = end;
		while (curr.equals(start) == false) { // finds smallest path from start to end
			smallestPath.add(curr);
			curr = pred.get(curr);
		}
		smallestPath.add(start);
		Collections.reverse(smallestPath);
		for (GraphAlgorithmObserver<V> observer : observerList) { // dijkstra is over
			observer.notifyDijkstraIsOver(smallestPath);
		}
	}
}
