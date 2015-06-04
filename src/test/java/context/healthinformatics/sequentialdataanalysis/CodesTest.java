package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the Codes class.
 */
public class CodesTest {

	private ArrayList<Chunk> chunks;

	/**
	 * Before each method create a arraylist with chunks already.
	 */
	@Before
	public void before() {
		chunks = new ArrayList<Chunk>();
		Chunk c1 = new Chunk();
		c1.setComment("CommentA");
		c1.setLine(1);
		chunks.add(c1);
		Chunk c2 = new Chunk();
		c2.setComment("CommentB");
		chunks.add(c2);
		Chunk c3 = new Chunk();
		c3.setCode("old");
		chunks.add(c3);
	}

	/**
	 * Test the set code on a line method.
	 * 
	 * @throws Exception
	 *             maybe the code couldn't be set.
	 */
	@Test
	public void testSetCodeOfLine() throws Exception {
		Codes codes = new Codes("A");
		codes.setChunks(chunks);
		codes.setCodeOfLine(1, "A");
		assertEquals("A", chunks.get(0).getCode());
	}

	/**
	 * Test if the exception will be thrown if the line doesn't exists.
	 * 
	 */
	@Test
	public void testSetCodeOfLineException() {
		Codes codes = new Codes("test");
		codes.setChunks(chunks);
		codes.setCodeOfLine(-1, "test");
	}

	/**
	 * Test if you can set the codes on a chunk of chunks.
	 */
	@Test
	public void testSetCodeOfChunks() {
		ArrayList<Chunk> combined = new ArrayList<Chunk>();
		Chunk combi = new Chunk();
		combi.setChunks(chunks);
		combined.add(combi);

		Codes codes = new Codes("A");
		codes.setChunks(combined);
		codes.setCodeOfChunks(0, "A");
		assertEquals("A", combined.get(0).getCode());
	}
	
	/**
	 * Test if you can set the codes on every chunk in a list of chunks.
	 */
	@Test
	public void testSetCodeOfListOfChunks() {
		Codes codes = new Codes("test");
		codes.setChunks(chunks);
		codes.setCodeOfListOfChunks(chunks, "test");
		assertEquals("test", chunks.get(0).getCode());
		assertEquals("test", chunks.get(1).getCode());
	}
	
	/**
	 * Test if the chunk with comment "CommentB" will have its code set to the correct code.
	 */
	@Test
	public void testSetCodeOnComment() {
		Codes codes = new Codes("test");
		codes.setChunks(chunks);
		codes.setCodeOnComment("CommentB");
		assertEquals("", chunks.get(0).getCode());
		assertEquals("test", chunks.get(1).getCode());
	}
	
	/**
	 * Test if the chunk with code "old" will have its code set to the "new" code.
	 */
	@Test
	public void testSetCodeOnCode() {
		Codes codes = new Codes("new");
		codes.setChunks(chunks);
		codes.setCodeOnCode("old");
		assertEquals("new", chunks.get(2).getCode());
	}
	
	/**
	 * Test if the correct chunk is returned by line number.
	 * @throws Exception e
	 */
	@Test
	public void testGetChunkByLine() throws Exception {
		Codes codes = new Codes("CommentA");
		codes.setChunks(chunks);
		assertEquals("CommentA", codes.getChunkByLine(1, chunks).getComment());
	}

	/**
	 * Test the exception handling of coding the chunk of chunks.
	 */
	@Test(expected = Exception.class)
	public void testSetCodeOfChunksException() {
		ArrayList<Chunk> combined = new ArrayList<Chunk>();
		Codes codes = new Codes("test");
		codes.setChunks(combined);
		codes.setCodeOfChunks(0, "test");
	}
}
