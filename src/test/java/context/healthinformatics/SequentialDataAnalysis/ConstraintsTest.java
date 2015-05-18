package context.healthinformatics.SequentialDataAnalysis;


import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


/**
 * Test for class Constraints.
 */
public class ConstraintsTest {
	
	@Before
	public void before() {
		
	}
	
	/**
	 * Test for code constraint.
	 */
	@Test
	public void testCodeConstraints() {
		ArrayList<Chunk> cList = new ArrayList<Chunk>();
		Chunk c1 = new Chunk();
		c1.setComment("comment");
		c1.setCode("c");
		cList.add(c1);
		Chunk c2 = new Chunk();
		c2.setComment("comment");
		c2.setCode("c");
		cList.add(c2);

		
		ArrayList<Chunk> clist2 = new ArrayList<Chunk>();
		clist2.add(c1);
		Chunk c3 = new Chunk();
		c3.setChunks(cList);
		c3.setComment("comment");
		clist2.add(c3);
		
		Constraints cs = new Constraints(clist2);
		System.out.println(cs.getChunks());
		ArrayList<Chunk> res = cs.containsCode("c", cs.getChunks());

	}

}
