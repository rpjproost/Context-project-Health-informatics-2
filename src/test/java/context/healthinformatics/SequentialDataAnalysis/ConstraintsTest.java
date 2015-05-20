package context.healthinformatics.SequentialDataAnalysis;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for class Constraints.
 */
public class ConstraintsTest {

	private ArrayList<Chunk> cList2;
	private Constraints cs;

	/**
	 * Init list of chunks.
	 */
	@Before
	public void before() {
		ArrayList<Chunk> cList = new ArrayList<Chunk>();
		Chunk c1 = new Chunk();
		c1.setCode("c");
		c1.setComment("testcomment");
		cList.add(c1);
		Chunk c2 = new Chunk();
		c2.setCode("c");
		c2.setComment("testcomment");
		cList.add(c2);
		Chunk c3 = new Chunk();
		c3.setCode("d");
		c3.setComment("testcomment2");
		c3.setChunks(cList);
		Chunk c4 = new Chunk();
		c4.setCode("d");
		c4.setComment("testcomment2");
		cList2 = new ArrayList<Chunk>();
		cList2.add(c3);
		cList2.add(c4);
		cs = new Constraints(cList2);
	}

	/**
	 * Test for code constraint.
	 */
	@Test
	public void testhasCodeConstraints() {
		ArrayList<Chunk> res = cs.hasCode("d", cs.getChunks(),
				new ArrayList<Chunk>());
		System.out.println("ReS: " + res);
	}

	/**
	 * Test for comment equals.
	 */
	@Test
	public void testEqualsCommentConstraints() {
		ArrayList<Chunk> resC = cs.equalsComment("testcomment", cs.getChunks(),
				new ArrayList<Chunk>());
		System.out.println("ReS: " + resC);
	}

	/**
	 * Test for contains comment.
	 */
	@Test
	public void testContainsCommentConstraints() {
		ArrayList<Chunk> resCcontain = cs.containsComment("com",
				cs.getChunks(), new ArrayList<Chunk>());
		System.out.println("ReS: " + resCcontain);
	}

	/**
	 * 
	 */
	@Test
	public void testValueConstraint() {

	}

}
