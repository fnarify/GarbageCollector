package tests;

import static org.junit.Assert.*;
import memory.GarbageCollector;

import org.junit.Test;

public class GarbageCollectorTest {

	@Test
	public void basicTest() {
		
		GarbageCollector gc = new GarbageCollector();
		
		String id = "testObject";
		
		gc.addHeapObject(id);
		
		assertEquals(1, gc.memUse());
		
		assertFalse(gc.isMarked(id));
		
		assertTrue(gc.isInHeapMemory(id));
		
		assertFalse(gc.isInStackMemory(id));
		
		assertTrue(gc.isInMemory(id));
		
		gc.mark();
		
		assertFalse(gc.isMarked(id));
		
		gc.sweep();
		
		assertFalse(gc.isInMemory(id));
		
		assertEquals(0, gc.memUse());
		
	}
	
	@Test
	public void basicTest2() {
		
		GarbageCollector gc = new GarbageCollector();
		
		String id = "testObject";
		String id2 = "testObject2";
		String id3 = "testObject3";

		
		gc.addHeapObject(id);
		gc.addHeapObject(id2);
		gc.addHeapObject(id3);
		
		gc.addStackObject(id);
		gc.addStackObject(id2);
		
		gc.addReference(id, id2);
		
		assertEquals(5, gc.memUse());
		
		assertTrue(gc.isInHeapMemory(id));
		
		assertTrue(gc.isInStackMemory(id));
		
		assertTrue(gc.isInMemory(id));
		
		gc.mark();
				
		gc.sweep();

		assertEquals(gc.memUse(), 3);	
		
		assertFalse(gc.isInHeapMemory(id3));
	}
	
	@Test
	public void basicTest3() {
		
		GarbageCollector gc = new GarbageCollector();
		
		String id = "testObject";
		String id2 = "testObject2";
		String id3 = "testObject3";
		String id4 = "testObject4";
		String id5 = "testObject4";

		gc.addHeapObject(id2);
		gc.addHeapObject(id3);
		gc.addHeapObject(id4);
		gc.addHeapObject(id5);
		
		gc.addStackObject(id);
		
		gc.addReference(id, id2);
		gc.addReference(id, id3);
		gc.addReference(id, id4);
		
		gc.mark();
				
		gc.sweep();

		assertEquals(gc.memUse(), 4);		
	}

	@Test
	public void basicTest4() {
		
		GarbageCollector gc = new GarbageCollector();
		
		String id = "testObject";
		String id2 = "testObject2";
		String id3 = "testObject3";
		String id4 = "testObject4";
		String id5 = "testObject4";

		gc.addHeapObject(id2);
		gc.addHeapObject(id3);
		gc.addHeapObject(id4);
		gc.addHeapObject(id5);
		
		gc.addStackObject(id);
		
		gc.addReference(id, id2);
		gc.addReference(id2, id3);
		gc.addReference(id2, id4);
		
		gc.mark();
				
		gc.sweep();

		assertEquals(gc.memUse(), 4);		
	}
	
	@Test
	public void basicTest5() {
		
		GarbageCollector gc = new GarbageCollector();
		
		String id = "testObject";
		String id2 = "testObject2";
		String id3 = "testObject3";
		String id4 = "testObject4";
		String id5 = "testObject4";

		gc.addHeapObject(id2);
		gc.addHeapObject(id3);
		gc.addHeapObject(id4);
		gc.addHeapObject(id5);
		
		gc.addStackObject(id);
		gc.addStackObject(id2);
		gc.addStackObject(id3);
		gc.addStackObject(id4);
		
		gc.addReference(id, id2);
		gc.addReference(id2, id3);
		gc.addReference(id2, id4);
		
		gc.mark();
				
		gc.sweep();
		
		assertEquals(gc.memUse(), 7);		

		gc.removeReference(id2, id4);
		gc.removeReference(id2, id4);
		gc.removeReference(id2, id4);

		gc.mark();
		
		gc.sweep();
		
		assertEquals(gc.memUse(), 6);		
	}
	
	@Test
	public void Test2() {
		
		GarbageCollector gc = new GarbageCollector();
		
		String id = "testObject";
		String id2 = "testObject2";
		String id3 = "testObject3";
		
		gc.addHeapObject(id);
		gc.addHeapObject(id3);
		gc.addStackObject(id2);
		
		gc.addReference(id2, id);
		gc.mark();
		gc.sweep();
		
		assertEquals(2, gc.memUse());
		
		gc.removeReference(id2, id);
		gc.mark();
		gc.sweep();
		
		assertEquals(1, gc.memUse());
	}
}
