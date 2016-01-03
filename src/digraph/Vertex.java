package digraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

/*
 * COMP225 Assignment 2, Semester 1 2014
 * Vertex Class
 * Author: Dr Luke Mathieson
 */

public class Vertex {

	/*
	 * Each vertex has an id, nominally unique and a list
	 * of the vertices it is adjacent to.
	 */
	String id;
	ArrayList<Vertex> neighbours;

	/*
	 * Constructor to create a new vertex with id "id".
	 * Note that there is no set method for the id. This is
	 * the only way to create a vertex with an id.
	 */
	public Vertex(String id) {
		this.id = id;
		neighbours = new ArrayList<Vertex>();
	}

	/*
	 * Adds a neighbour to this vertex.
	 */
	public void addNeighbour(Vertex v) {
		neighbours.add(v);
	}

	/*
	 * Adds a lot of neighbours at once.
	 */
	public void addNeighbours(Collection<Vertex> c) {
		for (Vertex v : c)
			addNeighbour(v);
	}

	/*
	 * Returns true if there is a neighbour with id "id".
	 */
	public boolean adjacent(String id) {
		return findNeighbour(id) != null;
	}

	/*
	 * Returns true if v is a neighbour of the current vertex.
	 */
	public boolean adjacent(Vertex v) {
		return neighbours.contains(v);
	}

	/*
	 * Provides the "natural ordering" for vertices. Mostly
	 * we are interested in using it to determine whether two vertices
	 * are the same or not. As each vertex should have a unique id, we define
	 * "the same" as having the same ids. 
	 */
	public int compareTo(Vertex v) {
		return id.compareTo(v.getID());
	}

	/*
	 * Returns the number of neighbours the vertex has. Note that
	 * this may not be the same as the number of unique neighbours.
	 */
	public int degree() {
		return neighbours.size();
	}

	/*
	 * Given an id, returns the neighbouring vertex with that id, if it
	 * exists, null otherwise.
	 */
	public Vertex findNeighbour(String id) {

		for (Vertex v : neighbours) {
			if (v.getID().compareTo(id) == 0)
				return v;
		}
		return null;
	}

	/*
	 * Returns the id of this vertex.
	 */
	public String getID() {
		return id;
	}

	/*
	 * Returns a ListIterator<Vertex> containing the neighbours
	 * of this vertex. Note that the neighbours may appear several times.
	 */
	public ListIterator<Vertex> neighbours() {
		return neighbours.listIterator();
	}

	/*
	 * Effectively removes an edge to a neighbour, if more than
	 * one exists, it removes the first it finds.
	 */
	public void removeNeighbour(String id) {
		removeNeighbour(findNeighbour(id));
	}

	/*
	 * Effectively removes an edge to a neighbour, if more than
	 * one exists, it removes the first it finds.
	 */
	public void removeNeighbour(Vertex v) {
		neighbours.remove(v);
	}

}
