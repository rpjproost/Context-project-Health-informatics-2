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
		chunks.add(c2);
		c3 = new Chunk();
		c3.setCode("CodeC");
		c3.setLine(2);
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
		i.interpret("undo");
		i.setIntialChunks(null);
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

		Query query = new Query("Connect code = CodeA to "
				+ "comment = CommentB");
		query.inc();
		c.runCode(query);
		assertEquals(note, c.getResult().get(0).getPointer().get(c2));
	}

	/**
	 * Checks connection code to code.
	 */
	@Test
	public void testConnectionCodetoCode() {
		Connections c = new Connections(note);
		Query query = new Query("Connect(test) code = CodeA to code = CodeC");
		query.inc();
		c.runCode(query);
		assertEquals(note, c.getResult().get(0).getPointer().get(c3));
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