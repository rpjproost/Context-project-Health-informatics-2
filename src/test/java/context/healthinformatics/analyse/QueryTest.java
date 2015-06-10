package context.healthinformatics.analyse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * class with Query tests.
 */
public class QueryTest {
	

	/**
	 * split a normal line test.
	 */
	@Test
	public void splitNormalLineTest() {
		Query q = new Query("filter code where = A");
		assertEquals("filter", q.part());
		assertEquals("code", q.next());
		assertEquals("where", q.next());
		assertEquals("=", q.next());
		assertEquals("A", q.next());
	}
	/**
	 * split a line with a parameter.
	 */
	@Test
	public void splitParameterLineTest() {
		Query q = new Query("chunk(B) code where = A");
		assertEquals("chunk", q.part());
		assertEquals("code", q.next());
		assertEquals("where", q.next());
		assertEquals("=", q.next());
		assertEquals("A", q.next());
		assertEquals(q.getParameter(), "B");
	}
	
	/**
	 * checks if the regex works as intended.
	 */
	@Test
	public void checkForCommandTest() {
		Query q = new Query();
		assertTrue(q.checkForCommand("true"));
		assertFalse(q.checkForCommand("this should be false"));
	}
	
	/**
	 * split a line with a parameter of mulitple words.
	 */
	@Test
	public void multiWordParameterTest() {
		Query q = new Query("chunk(this is a comment) coment where = A");
		assertEquals("this is a comment", q.getParameter());
	}
	
	/**
	 * brackets not are not a parameter.
	 */
	@Test
	public void bracketsNotAParameterTest() {
		Query q = new Query("filter code where = test(A)");
		assertEquals("", q.getParameter());
	}
	
	/**
	 * test if spaces are ignored.
	 */
	@Test
	public void tooManySpacesTest() {
		Query q = new Query("This is             it");
		assertEquals("is", q.next());
		assertEquals("it", q.next());
	}
	
	/**
	 * has Next test.
	 */
	@Test
	public void hasNextTest() {
		Query q = new Query("this is a test");
		q.inc(2);
		assertEquals("a", q.part());
		assertTrue(q.hasNext());
		q.inc();
		assertFalse(q.hasNext());
	}
	
	
}
