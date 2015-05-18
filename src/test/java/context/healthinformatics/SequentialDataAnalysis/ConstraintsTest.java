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
		cList.add(new Chunk("c", null));
		cList.add(new Chunk("c", null));
		cList.add(new Chunk("c", null));
		cList.add(new Chunk("d", null));
		ArrayList<Chunk> Clist2 = new ArrayList<Chunk>();
		
		Constraints cs = new Constraints(cList);
	}

}
