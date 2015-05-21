package context.healthinformatics.SequentialDataAnalysis;

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

	private static final int THREE = 3;

	/**
	 * Setup a list of chunks.
	 */
	@Before
	public void before() {
		ArrayList<Chunk> cList = new ArrayList<Chunk>();
		Chunk c1 = new Chunk();
		c1.setLine(1);
		cList.add(c1);
		Chunk c2 = new Chunk();
		c2.setLine(2);
		cList.add(c2);
		Chunk c3 = new Chunk();
		c3.setChunks(cList);
		Chunk c4 = new Chunk();
		c4.setLine(THREE);
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
		c.setComment(3, "hallo");
		System.out.println(cList2);
	}
}
