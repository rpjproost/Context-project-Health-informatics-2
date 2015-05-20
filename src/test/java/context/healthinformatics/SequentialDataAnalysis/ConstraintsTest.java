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
		Chunk c1 = new Chunk(1);
		c1.setCode("c");
		cList.add(c1);
		Chunk c2 = new Chunk(2);
		c2.setCode("c");
		cList.add(c2);
		Chunk c3 = new Chunk(3);
		c3.setCode("c");
		c3.setChunks(cList);
		Chunk c4 = new Chunk(4);
		c4.setCode("d");
		ArrayList<Chunk> cList2 = new ArrayList<Chunk>();
		cList2.add(c3);
		cList2.add(c4);
		

		Constraints cs = new Constraints(cList2);
		System.out.println(cs.getChunks());
		ArrayList<Chunk> res = cs.containsCode("c", cs.getChunks());

	}

}
