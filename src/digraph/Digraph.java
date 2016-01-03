package digraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

/*
 * COMP225 Assignment 2, Semester 1 2014
 * Directed Graph Class
 * Author: Dr Luke Mathieson
 */

public class Digraph {
	/*
	 * The vertices of the graph, each vertex stores its
	 * own neighbours.
	 */
	ArrayList<Vertex> vertices;

	/*
	 * Default constructor. Initialises an empty graph.
	 */
	public Digraph() {
		vertices = new ArrayList<Vertex>();
	}

	/*
	 * Testing constructor that takes a file and reads in
	 * a pre-existing graph. Not important for the assignment.
	 */
	public Digraph(String filename) {
		createGraph(filename);
	}
	
	/*
	 * Adds an directed edge (an arc) between the vertex
	 * represented by "u_id" to the vertex represented by
	 * "v_id". Returns true if the operation was successful,
	 * false otherwise.
	 */
	public boolean addEdge(String u_id, String v_id) {

		if (!hasVertex(u_id) || !hasVertex(v_id))
			return false;

		getVertex(u_id).addNeighbour(getVertex(v_id));

		return true;

	}

	/*
	 * Adds an arc from u to v. Returns a boolean indicating
	 * whether the operation was successful.
	 */
	public boolean addEdge(Vertex u, Vertex v) {

		if (!hasVertex(u) || !hasVertex(v))
			return false;

		u.addNeighbour(v);

		return true;
	}

	/*
	 * Adds a vertex to the graph with id "id", if that
	 * id is not already used. Returns a boolean indicating
	 * success.
	 */
	public boolean addVertex(String id) {

		if (!hasVertex(id)) {
			vertices.add(new Vertex(id));
			return true;
		}

		return false;
	}

	/*
	 * Adds the Vertex v to the graph if it does not
	 * already contain that vertex (q.v. Vertex.compareTo()).
	 * Returns a boolean indicating success.
	 */
	public boolean addVertex(Vertex v) {
		if (!hasVertex(v)) {
			vertices.add(v);
			return true;
		}

		return false;
	}

	/*
	 * Returns true if both vertices are in the graph
	 * and there is an arc from the first to the second.
	 */
	public boolean adjacent(String u_id, String v_id) {

		if (!hasVertex(u_id))
			return false;

		return getVertex(u_id).adjacent(v_id);
	}

	/*
	 * Returns true if both vertices are in the graph
	 * and there is an arc from the first to the second.
	 */
	public boolean adjacent(Vertex u, Vertex v) {

		if (!hasVertex(u))
			return false;

		return u.adjacent(v);
	}

	/*
	 * Reads a graph in from a file. You can don't need to
	 * worry about this for the assingment.
	 */
	public void createGraph(String filename) {

		vertices = new ArrayList<Vertex>();

		try {

			BufferedReader reader = new BufferedReader(new FileReader(filename));

			ArrayList<String[]> lines = new ArrayList<String[]>();

			while (reader.ready()) {
				lines.add(reader.readLine().split("\\s+"));
			}

			reader.close();

			for (String[] s : lines) {
				boolean result = addVertex(s[0]);
				if (!result)
					System.err.println("Vertex " + s[0] + " already exists.");
			}

			for (String[] s : lines) {

				for (int i = 0; i < s.length; i++) {
					boolean result = addEdge(s[0], s[i]);
					if (!result)
						System.err.println("Cannot add edge (" + s[0] + ","
								+ s[i] + ")");
				}

			}

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
	}

	/*
	 * Returns the Vertex object with id "id" if it exists.
	 * Returns null otherwise.
	 */
	public Vertex getVertex(String id) {

		for (Vertex v : vertices)
			if (v.getID().compareTo(id) == 0)
				return v;

		return null;
	}

	/*
	 * Returns true if a vertex with id "id" exists in the graph,
	 * false otherwise.
	 */
	public boolean hasVertex(String id) {
		for (Vertex v : vertices)
			if (v.getID().compareTo(id) == 0)
				return true;

		return false;
	}

	/*
	 * Returns true if v is in the graph, false otherwise.
	 */
	public boolean hasVertex(Vertex v) {
		return vertices.contains(v);
	}

	/*
	 * Returns the number of edges (arcs) in the graph.
	 */
	public int numEdges() {
		int sum = 0;
		for (Vertex v : vertices)
			sum += v.degree();

		return sum;
	}

	/*
	 * Removes an edge, if it exists.
	 */
	public void removeEdge(String u_id, String v_id) {
		removeEdge(getVertex(u_id), getVertex(v_id));
	}

	/*
	 * Removes an edge, if it exists.
	 */
	public void removeEdge(Vertex u, Vertex v) {
		if (u != null)
			u.removeNeighbour(v);
	}

	/*
	 * Removes a vertex from the graph, if it exists.
	 */
	public void removeVertex(String id) {
		removeVertex(getVertex(id));
	}

	/*
	 * Removes a vertex from the graph, if it exists.
	 */
	public void removeVertex(Vertex v) {
		for (Vertex u : vertices) {
			if (u.adjacent(v))
				u.removeNeighbour(v);
		}

		vertices.remove(v);
	}

	/*
	 * Returns the number of vertices in the graph.
	 */
	public int size() {
		return vertices.size();
	}

	/*
	 * Gives a string representation of the graph as
	 * an adjacency list. The vertices appear in the order
	 * they happen to be in in the iterators.
	 */
	public String toString() {

		String output = new String();

		for (Vertex v : vertices) {
			output += v.getID() + ": ";
			ListIterator<Vertex> n = v.neighbours();
			while (n.hasNext()) {
				output += n.next().getID() + " ";
			}
			output += "\n";
		}

		return output;

	}

	/*
	 * Returns a ListIterator<Vertex> containing the
	 * vertices in the graph.
	 */
	public ListIterator<Vertex> vertices() {
		return vertices.listIterator();
	}

}