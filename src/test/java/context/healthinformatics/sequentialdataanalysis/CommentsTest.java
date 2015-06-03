package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for class Comments.
 */
public class CommentsTest {
	private Comments c;

	/**
	 * The list of chunks.
	 */
	private ArrayList<Chunk> cList2;
	private Chunk c3;
	private Chunk c2;
	private Chunk c4;
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
		c3.setComment("A");
		c4 = new Chunk();
		c4.setLine(FOUR);
		c4.setCode("A");
		cList2 = new ArrayList<Chunk>();
		cList2.add(c3);
		cList2.add(c4);
		c = new Comments(cList2);
	}

	/**
	 * Test if a comment is set by line.
	 */
	@Test
	public void testSetCommentByLine() {
		c.setCommentByLine(THREE, "testComment1");
		assertEquals(c3.getComment(), "testComment1");
		c.setCommentByLine(2, "testComment2");
		assertEquals(c2.getComment(), "testComment2");
	}
	
	/**
	 * Test if a comment is set for a list of chunks.
	 */
	@Test
	public void testsetCommentOfListOfChunks() {
		c.setCommentOfListOfChunks(cList2, "test");
		assertEquals(c3.getComment(), "test");
		assertEquals(c4.getComment(), "test");
	}
	
	/**
	 * Test if a comment is set on chunk with a cetain code.
	 */
	@Test
	public void testsetCommentOnCode() {
		c.setCommentOnCode("test", "A");
		assertEquals(c4.getComment(), "test");
	}
	
	/**
	 * Test if a comment is replaced on chunk with an existing comment.
	 */
	@Test
	public void testsetCommentOnComment() {
		c.setCommentOnComment("B", "A");
		assertEquals(c3.getComment(), "B");
	}

	/**
	 * Try to add a comment to a non existent line.
	 **/
	public void testNonExistentLine() {
		c.setCommentByLine(0, "comment");
	}
}
