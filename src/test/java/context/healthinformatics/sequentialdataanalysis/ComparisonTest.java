package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Class to test cOmparison.
 */
public class ComparisonTest {

	/**
	 * Before each method create a arraylist with chunks already.
	 */
	@Before
	public void before() {
		
	}

	/**
	 * Test the set code on a line method.
	 * 
	 * @throws Exception
	 *             maybe the code couldn't be set.
	 */
	@Test
	public void testResolveBOundaries() throws Exception {
		Comparison c = new Comparison();
		assertEquals("Concerned", c.resolveBoundaries("Concerned", "Somewhat Safe"));
	}
}
