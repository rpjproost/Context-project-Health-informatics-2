package context.healthinformatics.writer;

import java.io.IOException;

import org.junit.Test;

public class ReadWriteAnalyseFilterTest {
	@Test
	public void testReadFilter() {
		ReadWriteAnalyseFilter rwaf = new ReadWriteAnalyseFilter(
				"src/main/data/savedFilters/(default).txt");
		try {
			rwaf.readFilter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
