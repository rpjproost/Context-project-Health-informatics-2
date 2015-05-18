package context.healthinformatics.SequentialDataAnalysis;


import java.util.ArrayList;

import org.junit.Test;


/**
 * Test for class Constraints.
 */
public class ConstraintsTest {
	
	/**
	 * Test for code constraint.
	 */
	@Test
	public void testCodeConstraints() {
		ArrayList<Chunk> cList = new ArrayList<Chunk>();
		Chunk c = new Chunk();
		c.setCode("c");
		cList.add(c);
		cList.add(c);
		cList.add(c);
		c.setCode("d");
		cList.add(c);
		ArrayList<Chunk> clist2 = new ArrayList<Chunk>();
		clist2.add(c);
		c.setChunks(cList);
		clist2.add(c);
		
		Constraints cs = new Constraints(clist2);
		ArrayList<Chunk> res = cs.containsCode("c", cs.getChunks());
		System.out.println(res);
	}

}
