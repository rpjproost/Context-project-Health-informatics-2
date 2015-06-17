package context.healthinformatics.kreatininestatus;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for all statuses if there is a re-measure needed.
 */
@RunWith(Parameterized.class)
public class ReMeasureTest {
	
	private static KreatinineStatus safe = new SafeStatus();
	private static KreatinineStatus res = new ReasonablySafeStatus();
	private static KreatinineStatus mild = new MildConcernStatus();
	private static KreatinineStatus conc = new ConcernStatus();
	private static KreatinineStatus nul = new NullStatus();
	
	private static String niets = "NULL";
	private static String yes = "Yes";
	private static String no = "No";
	
	private KreatinineStatus status;
	private String expected;
	
	/**
	 * Constructor for test.
	 * @param firstArg today.
	 * @param expected expected advice.
	 */
	public ReMeasureTest(KreatinineStatus firstArg, String expected) {
	     status = firstArg;
	     this.expected = expected;
		  
	}
	
	/**
	 * all parameters.
	 * @return all parameters.
	 */
	 @Parameters
	  public static Collection<Object[]> testData() {
	      return Arrays.asList(new Object[][] {
	         { safe, no},
	         { res, no},
	         { mild, yes},
	         { conc, yes},
	         { nul, niets},
	        
	      });
	   }

	  /**
		 * actual test.
		 */
		@Test
		public void statustest() {
			String temp = status.needToReMeasure();
			assertEquals(expected, temp);
		}

}
