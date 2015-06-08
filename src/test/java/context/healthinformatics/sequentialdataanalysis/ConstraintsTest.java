package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for class Constraints.
 */
public class ConstraintsTest {

	/**
	 * The constrains class.
	 */
	private Constraints cs;

	/**
	 * The list of chunks.
	 */
	private ArrayList<Chunk> cList2;

	private static final int THREE = 3;

	/**
	 * Init list of chunks.
	 */
	@Before
	public void before() {
		ArrayList<Chunk> cList = new ArrayList<Chunk>();
		Chunk c1 = new Chunk();
		c1.setCode("c");
		c1.setComment("testcomment");
		c1.setLine(1);
		cList.add(c1);
		Chunk c2 = new Chunk();
		c2.setCode("c");
		c2.setComment("testcomment");
		c2.setLine(2);
		cList.add(c2);
		Chunk c3 = new Chunk();
		c3.setCode("d");
		c3.setComment("testcomment2");
		c3.setChunks(cList);
		Chunk c4 = new Chunk();
		c4.setCode("d");
		c4.setComment("testcomment2");
		c4.setLine(THREE);
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
		cs.hasCode("d", cs.getChunks(), new ArrayList<Chunk>());
	}

	/**
	 * Test for comment equals.
	 */
	@Test
	public void testEqualsCommentConstraints() {
		cs.equalsComment("testcomment", cs.getChunks(), new ArrayList<Chunk>());
	}

	/**
	 * Test for contains comment.
	 */
	@Test
	public void testContainsCommentConstraints() {
		cs.containsComment("com", cs.getChunks(), new ArrayList<Chunk>());
	}

	/**
	 * Test getColumnName.
	 */
	@Test
	public void testGetColumnName() {
		assertEquals(new Constraints(cList2, "value").getColumnName(), "value");
	}
}
