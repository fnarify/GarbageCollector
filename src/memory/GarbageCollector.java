package memory;

// Bao-lim Smith
// 43277047

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Stack;

import digraph.Digraph;
import digraph.Vertex;

/*
 * COMP225 Assignment 2, Semester 1 2014
 * Garbage Collector Class Template
 * Author: Dr Luke Mathieson
 */
public class GarbageCollector {

	/*
	 * Basic data members that represent the heap (in the memory sense,
	 * not data structure sense), the call stack and a HashMap to be
	 * used to mark memory objects for the mark(), sweep() and isMarked()
	 * methods. 
	 */
	Digraph heapMemory;
	Deque<Vertex> stackMemory;
	HashMap<String, Vertex> markedObjects;

	/*
	 * Default constructor. Don't forget to set things up.
	 */
	public GarbageCollector() {
		heapMemory = new Digraph();
		stackMemory = new LinkedList<Vertex>();
		markedObjects = new HashMap<String, Vertex>();
	}

	/*
	 * Method adds a new object to the heap,
	 * assuming it's not already there.
	 * PRE: A valid string to be the id for the object.
	 * POST: Either a new object in heap memory, if the id is new,
	 * or no change otherwise.
	 */
	public void addHeapObject(String id) 
	{
		if (!heapMemory.hasVertex(id))
			heapMemory.addVertex(id);
	}
	/*
	 * Add a reference from one object to another.
	 * The objects could be in either part of memory.
	 * If one or neither exist, nothing happens.
	 * PRE: Two valid strings, representing the ids of the objects
	 * between which the reference is to be created.
	 * POST: The object corresponding to "fromId" should have a new reference
	 * (duplicates allowed) to the object corresponding to "toId".
	 * If one or neither exist, the method fails silently.
	 */
	public void addReference(String fromId, String toId) 
	{
		// In Heap
		if (isInHeapMemory(fromId))
		{
			// Heap -> Heap. 
			if (isInHeapMemory(toId))
				heapMemory.addEdge(fromId, toId);	
			// Heap -> Stack. 
			if (isInStackMemory(toId))
				heapMemory.getVertex(fromId).addNeighbour(getStackObject(toId));
		}
		
		// In Stack. 
		if (isInStackMemory(fromId))
		{
			// Stack -> Heap.
			if (isInHeapMemory(toId))
				getStackObject(fromId).addNeighbour(heapMemory.getVertex(toId));
			// Stack -> Stack. 
			if (isInStackMemory(toId))
				getStackObject(fromId).addNeighbour(getStackObject(toId));
		}
	}
	
	public Vertex getStackObject(String id)
	{
		Vertex stackObject = null;
		int stackSize = stackUse();
		
		// Checks the entire stackMemory to see if id is within it
		// By taking the front and moving it to the back.
		if (stackSize > 0)
		{
			for (int i = 0; i < stackSize; i++)
			{
				Vertex temp = stackMemory.pop();

				// In stackMemory.
				if (id == temp.getID())
					stackObject = temp;

				stackMemory.push(temp);
			}
		}
		return stackObject;
	}

	/*
	 * Add a new object to the stack with id "id".
	 * As with the heap, it should not already be there.
	 * PRE: A valid string "id".
	 * POST: Either a new object on the top of the stack with id "id",
	 * if no object with "id" already exists, otherwise no change.
	 */
	public void addStackObject(String id) 
	{
		if (!stackMemory.contains(id))
			stackMemory.add(new Vertex(id));
	}

	/*
	 * Performs the basic mark and sweep garbage collection routine.
	 * PRE: none.
	 * POST: The heap should not contain any object that cannot be reached
	 * from the stack by following references.
	 */
	public void collectGarbage() 
	{
		// Mark the objects that are referenced.
		mark();
		// Remove all objects that are not linked by references.
		sweep();
	}
	/*
	 * Helper method that returns the number of objects in the heap.
	 */
	public int heapUse() 
	{
		if (heapMemory != null)
			return heapMemory.size();
		else
			return 0;
	}

	/*
	 * Helper method that returns true if the heap contains
	 * an object with id "id", false otherwise.
	 */
	public boolean isInHeapMemory(String id) 
	{
		if (heapUse() > 0)
			return heapMemory.hasVertex(id);
		else
			return false;
	}

	/*
	 * Helper method that returns true if memory contains
	 * an object with id "id", false otherwise.
	 */
	public boolean isInMemory(String id) 
	{
		if (isInHeapMemory(id) == true || isInStackMemory(id) == true)
			return true;
		else
			return false;
	}

	/*
	 * Helper method that returns true if the stack contains
	 * an object with id "id", false otherwise.
	 */
	public boolean isInStackMemory(String id) 
	{	
		boolean isInStackMemory = false;
		int stackSize = stackUse();
		
		// Checks the entire stackMemory to see if id is within it
		// By taking the front and moving it to the back.
		if (stackSize > 0)
		{
			for (int i = 0; i < stackSize; i++)
			{
				Vertex temp = stackMemory.pop();

				// In stackMemory.
				if (id == temp.getID())
					isInStackMemory = true;

				stackMemory.push(temp);
			}
		}
		return isInStackMemory;
	}

	/*
	 * Helper method that returns true if there is
	 * an object with id "id" that is also marked, false otherwise.
	 */
	public boolean isMarked(String id) 
	{
		boolean isMarked = false;
		
		if (markedObjects.containsKey(id))
			isMarked = true;
		else
			isMarked = false;
		
		return isMarked;
	}

	/*
	 * Method to implement the mark phase of the mark and sweep algorithm.
	 * Performs a traversal of the memory reachable from the stack, marking
	 * the objects it reaches. The class member "markedObjects" should be
	 * useful here.
	 * PRE: none.
	 * POST: Every object reachable by following references from the stack should
	 * be marked, all other objects should not be marked.
	 * 
	 */
	public void mark() 
	{	
		// Empties the markedObjects hash map, so it can be re-used correctly. 
		markedObjects.clear();
		
		// Run only if the stack/heap is not empty. 
		if (stackUse() > 0 || heapUse() > 0)
		{
			// Creates an ArrayList that contains all vertices in the graph. 
			ListIterator<Vertex> heapIterator = heapMemory.vertices();
			ArrayList<String> vertices = new ArrayList<String>();

			while (heapIterator.hasNext())
				vertices.add(heapIterator.next().getID());

			// Stack of the vertices in the graph that should be marked. 
			Stack<Vertex> unprocessed = new Stack<Vertex>();

			// Queue of stackMemory objects
			Queue<Vertex> objectQueue = new LinkedList<Vertex>();
			objectQueue = takeStackObjects(objectQueue);
			
			// Runs until all elements in the stackMemory are checked for references. 
			while (!objectQueue.isEmpty())
			{
				Vertex obj1 = objectQueue.poll();

				// Adds all elements adjacent to obj1 to the stack.
				hasReference(obj1, unprocessed, vertices);

				// If the stack or heap has a reference to an object in the heap. 
				while (!unprocessed.isEmpty())
				{
					obj1 = unprocessed.pop();

					if (isMarked(obj1.getID()) == false)
					{
						// Mark the object
						markedObjects.put(obj1.getID(), heapMemory.getVertex(obj1.getID()));

						// Checks if there is a reference to the next object in the heap.
						for (int i = 0; i < vertices.size(); i++)
						{
							Vertex obj2 = heapMemory.getVertex(vertices.get(i));

							// If there is an edge between the two objects, and they aren't the same object. 
							if (heapMemory.adjacent(obj1.getID(), obj2.getID()) == true && obj1.getID() != vertices.get(i))
								unprocessed.push(obj2);
						}
					}
				}
			}
		}
	}
	
	// If there's a reference from an object in the stack to one in the heap, or heap -> heap. 
	public void hasReference(Vertex obj, Stack<Vertex> unprocessed, ArrayList<String> vertices)
	{
		// Checks through all vertices within the heap for one that has a reference. 
		for (int i = 0; i < vertices.size(); i++)
		{
			String tempId = vertices.get(i);
			
			// Has an edge, so add it to the stack. 
			if (obj.adjacent(tempId))
				unprocessed.add(heapMemory.getVertex(tempId));
		}
	}

	// Moves all the elements of the stack into a queue.
	public Queue<Vertex> takeStackObjects(Queue<Vertex> queue)
	{
		int stackSize = stackUse();
		// iterates through stack, taking all elements. Does not change order. 
		for (int i = 0; i < stackSize; i++)
		{
			Vertex temp = stackMemory.pop();

			// Adds vertex to queue.
			queue.add(temp);
			stackMemory.push(temp);
		}
		return queue;
	}

	/*
	 * Helper method that returns the number of
	 * objects in memory.
	 */
	public int memUse() 
	{
		return heapUse() + stackUse();
	}
	/*
	 * Removes the object with id "id" from memory if it exists.
	 * PRE: A valid string "id".
	 * POST: Memory either has one less object, where that object has id "id",
	 * otherwise no change.
	 */
	public void removeObject(String id) 
	{
		if (isInMemory(id) == true)
		{
			// In heap, so remove it. 
			if (isInHeapMemory(id))
				heapMemory.removeVertex(id);
			
			// In stack, so remove it. 
			if (isInStackMemory(id))
			{
				// Searches for the item to be removed, retains order. 
				for (int i = 0; i < stackUse(); i++)
				{
					Vertex temp = stackMemory.pop();
					
					// Item not to be removed, so re-add it to the end of stackMemory.
					if (temp.getID() != id)
						stackMemory.add(temp);
				}
			}
		}
	}

	/*
	 * Removes a reference between the objects corresponding to
	 * "fromId" and "toId" if the objects exists and such a reference
	 * exists. If some or all parts do not exist, the method fails silently.
	 * PRE: Two valid strings, representing the ids of the objects
	 * between which the reference is to be created.
	 * POST: The object corresponding to "fromId" should have one less
	 * reference to the object corresponding to "toId".
	 * If one or neither exist, or the reference doesn't exist,
	 * the method fails silently.
	 */
	public void removeReference(String fromId, String toId) 
	{
        // In Stack. 
		if (isInStackMemory(fromId))
		{
			// Stack -> Heap.
			if (isInHeapMemory(toId))
			{
				Vertex tempStack = getStackObject(fromId);

				// Has a reference between the two objects. 
				if (tempStack.adjacent(heapMemory.getVertex(toId)) == true)
					tempStack.removeNeighbour(heapMemory.getVertex(toId));
			}
			// Stack -> Stack. 
			if (isInStackMemory(toId))
				getStackObject(fromId).removeNeighbour(getStackObject(toId));
		}
		
		// In heap
		if (isInHeapMemory(fromId))
		{
			Vertex heapStack = heapMemory.getVertex(fromId);

			// Heap -> Stack
			if (isInStackMemory(toId))
			{
				Vertex tempStack = getStackObject(toId);
				
				// Has a reference between the two objects. 
				if (heapStack.adjacent(tempStack) == true)
					heapStack.removeNeighbour(tempStack);
			}
			// Heap -> Heap. 
			if (isInHeapMemory(toId))
				if (heapMemory.adjacent(fromId, toId) == true)
					heapMemory.removeEdge(heapStack, heapMemory.getVertex(toId));	
		}
	}

	/*
	 * Helper method that returns the number of
	 * objects in the stack.
	 */
	public int stackUse() 
	{
		if (stackMemory != null)
			return stackMemory.size();
		else 
			return 0;
	}

	/*
	 * Implements the sweep phase of the mark and sweep algorithm.
	 * Scans memory and removes anything that is not marked.
	 * PRE: Memory has passed through the mark phase of the algorithm.
	 * POST: Memory contains no unmarked objects.
	 */
	public void sweep() 
	{
		// Nothing can be marked. 
		if (markedObjects.isEmpty() || stackUse() == 0)
			heapMemory = new Digraph();
		// At least have something in the heap. 
		if (heapUse() > 0)
		{
			// All vertices in the graph.
			ListIterator<Vertex> heapIterator = heapMemory.vertices();
			ArrayList<String> vertices = new ArrayList<String>();

			while (heapIterator.hasNext())
				vertices.add(heapIterator.next().getID());

			// If the vertex has not been marked, then remove it. 
			for (int i = 0; i < vertices.size(); i++)
				if (markedObjects.containsKey(vertices.get(i)) == false)
					heapMemory.removeVertex(vertices.get(i));
		}
	}
	
}