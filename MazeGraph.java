package graph;

import maze.Juncture;
import maze.Maze;

/** 
 * The MazeGraph is an extension of WeightedGraph. The constructor converts a Maze into a graph.
 */
public class MazeGraph extends WeightedGraph<Juncture> {

	/** 
	 * Construct the MazeGraph using the "maze" contained in the parameter to specify the vertices (Junctures)
	 * and weighted edges.
	 * 
	 * The Maze is a rectangular grid of "junctures", each defined by its X and Y coordinates, using the usual
	 * convention of (0, 0) being the upper left corner.
	 * 
	 * Each juncture in the maze is added as a vertex to this graph.
	 * 
	 * For every pair of adjacent junctures (A and B) which are not blocked by a wall, two edges should be added:  
	 * One from A to B, and another from B to A.  The weight to be used for these edges is provided by the Maze. 
	 * (The Maze methods getMazeWidth and getMazeHeight can be used to determine the number of Junctures in the
	 * maze. The Maze methods called "isWallAbove", "isWallToRight", etc. can be used to detect whether or not there
	 * is a wall between any two adjacent junctures.  The Maze methods called "getWeightAbove", "getWeightToRight",
	 * etc. are used to obtain the weights.)
	 * 
	 * @param maze to be used as the source of information for adding vertices and edges to this MazeGraph.
	 */
	public MazeGraph(Maze maze) {
		for (int width = 0; width < maze.getMazeWidth(); width++) {	// creates vertices for junctures
			for (int height = 0; height < maze.getMazeHeight(); height++) {
				Juncture vertex = new Juncture(width, height);
				addVertex(vertex);
			}
		}
		for (int width = 0; width < maze.getMazeWidth(); width++) {
			for (int height = 0; height < maze.getMazeHeight(); height++) {
				Juncture vertex = new Juncture(width, height);
				if (maze.isWallAbove(vertex) == false) { // checks if wall is not above, adds edge
					Juncture above = new Juncture(width, height - 1);
					int weight = maze.getWeightAbove(vertex);
					addEdge(vertex, above, weight);
				}
				if (maze.isWallBelow(vertex) == false) { // checks if wall is not below, adds edge
					Juncture below = new Juncture(width, height + 1);
					int weight = maze.getWeightBelow(vertex);
					addEdge(vertex, below, weight);
				} 
				if (maze.isWallToLeft(vertex) == false) { // checks if wall is not to the left, adds edge
					Juncture left = new Juncture(width - 1, height);
					int weight = maze.getWeightToLeft(vertex);
					addEdge(vertex, left, weight);
				} 
				if (maze.isWallToRight(vertex) == false) { // checks if wall is not to the right, adds edge
					Juncture right = new Juncture(width + 1, height);
					int weight = maze.getWeightToRight(vertex);
					addEdge(vertex, right, weight);
				}
			}
		}
	}
}
