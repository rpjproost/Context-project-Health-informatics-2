package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.Query;
import context.healthinformatics.analyse.SingletonInterpreter;

/**
 * Tests for class Connections.
 */
public class ConnectionsTest {

	private ArrayList<Chunk> chunks;
	private Interpreter i;
	private String note;
	private Chunk c1;
	private Chunk c2;
	private Chunk c3;
	private static final int THREE = 3;

	/**
	 * CodeBefore each method create a arraylist with chunks already.
	 */
	@Before
	public void before() {
		note = "test";
		chunks = new ArrayList<Chunk>();
		c1 = new Chunk();
		c1.setLine(1);
		c1.setCode("CodeA");
		c1.setComment("CommentA");
		chunks.add(c1);
		c2 = new Chunk();
		c2.setComment("CommentB");
		c2.setCode("CodeC");
		c2.setLine(2);
		chunks.add(c2);
		c3 = new Chunk();
		c3.setCode("CodeC");
		c3.setLine(THREE);
		c3.setComment("CommentC");
		chunks.add(c3);
		i = SingletonInterpreter.getInterpreter();
		i.setIntialChunks(chunks);
	}
	
	/**
	 * Clean up interpreter.
	 * @throws Exception if query is not right.
	 */
	@After
	public void after() throws Exception {
		i.setIntialChunks(null);
		i.interpret("undo");
	}

	/**
	 * Increments by 1.
	 * @param i integer.
	 * @return incremented.
	 */
	public int inc(int i) {
		int k = i + 1;
		return k;
	}

	/**
	 * Method which checks if there is no connection made,
	 * if you try to connect to a chunk higher in the table.
	 * @throws Exception e
	 */
	@Test
public void testConnectionOnCodeToComment() throws Exception {
		Connections c = new Connections(note);

	Query query = new Query("Connect(test) code = CodeA to "
				+ "comment = CommentB");
		query.inc();
		c.runCode(query);
	assertEquals(note, c.getResult().get(0).getPointer().get(c2));
	}

	/**
	 * Method which checks if there is a connection made to only,
	 * the first occurence of a destination chunk,
	 * if you try to connect to a chunk higher in the table.
	 * @throws Exception e
	 */
	@Test
	public void testConnectionToFirstOccurence() throws Exception {
		Connections c = new Connections(note);
		Query query = new Query("Connect(test) code = CodeA to code = CodeC");
		query.inc();
		c.runCode(query);
		assertEquals(1, c.getResult().get(0).getPointer().size());
	}

	/**
	 * Checks connection code to code.
	 */
	@Test
	public void testConnectionCodeToCode() {
		Connections c = new Connections(note);
		Query query = new Query("Connect(test) code = CodeA to code = CodeC");
		query.inc();
		c.runCode(query);
		assertEquals(note, c.getResult().get(0).getPointer().get(c2));
	}
	
	/**
	 * Checks connection comment to comment.
	 */
	@Test
	public void testConnectionCommentToComment() {
		Connections c = new Connections(note);
		Query query = new Query("Connect(test) comment = CommentA to comment = CommentB");
		query.inc();
		c.runComment(query);
		assertEquals(note, c.getResult().get(0).getPointer().get(c2));
	}
	
	/**
	 * Checks connection for the sequential property.
	 */
	@Test
	public void testSequentialProperty() {
		Connections c = new Connections(note);
		Query query = new Query("Connect(test) code = CodeC to code = CodeA");
		query.inc();
		c.runCode(query);
		assertEquals(0, c.getResult().get(0).getPointer().size());
	}
	
	/**
	 * Method which checks if there is no connection made,
	 * if you try to connect to a chunk higher in the table.
	 * @throws Exception e
	 */
	@Test
	public void testConnectionNotMade() throws Exception {
		Connections c = new Connections(note);
		Query query = new Query("Connect(test) comment = CommentB to code = CodeA");
		c.runComment(query);
		assertEquals(null, c.getResult().get(1).getPointer().get(c1));
	}
}