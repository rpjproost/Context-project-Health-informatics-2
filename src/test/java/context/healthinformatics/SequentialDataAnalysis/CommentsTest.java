package context.healthinformatics.SequentialDataAnalysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for class Comments.
 */
public class CommentsTest {
	/**
	 * The constrains class.
	 */
	private Comments c;

	/**
	 * The list of chunks.
	 */
	private ArrayList<Chunk> cList2;
	private Chunk c3;
	private Chunk c2;
	private static final int THREE = 3;
	private static final int FOUR = 4;

	/**
	 * Setup a list of chunks.
	 */
	@Before
	public void before() {
		ArrayList<Chunk> cList = new ArrayList<Chunk>();
		Chunk c1 = new Chunk();
		c1.setLine(1);
		cList.add(c1);
		c2 = new Chunk();
		c2.setLine(2);
		cList.add(c2);
		c3 = new Chunk();
		c3.setChunks(cList);
		c3.setLine(THREE);
		Chunk c4 = new Chunk();
		c4.setLine(FOUR);
		cList2 = new ArrayList<Chunk>();
		cList2.add(c3);
		cList2.add(c4);
		c = new Comments(cList2);
	}

	/**
	 * Test is a comment is set.
	 */
	@Test
	public void testSetComment() {
		c.setComment(THREE, "testComment1");
		assertEquals(c3.getComment(), "testComment1");
		c.setComment(2, "testComment2");
		assertEquals(c2.getComment(), "testComment2");
	}

	/**
	 * Try to add a comment to a non existent line.
	 **/
	public void testNonExistentLine() {
		c.setComment(0, "comment");
	}
}
