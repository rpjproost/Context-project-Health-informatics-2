package context.healthinformatics.writer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

/**
 * Class to test the ReadWriteAnalyseFilter.
 */
public class ReadWriteAnalyseFilterTest {
	/**
	 * Test the read function.
	 */
	@Test
	public void testReadFilter() {
		ReadWriteAnalyseFilter rwaf = new ReadWriteAnalyseFilter(
				"src/test/data/savedFilters/(default).txt");
		try {
			assertEquals(rwaf.readFilter().size(), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
