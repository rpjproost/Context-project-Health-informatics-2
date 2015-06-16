package context.healthinformatics.kreatininestatus;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * class for testing advise.
 */
@RunWith(Parameterized.class)
public class AdviseTest {

	private static KreatinineStatus safe = new SafeStatus();
	private static KreatinineStatus res = new ReasonablySafeStatus();
	private static KreatinineStatus mild = new MildConcernStatus();
	private static KreatinineStatus conc = new ConcernStatus();
	private static KreatinineStatus nul = new NullStatus();
	
	private static String niets = "nothing extra";
	private static String herhalen = "measure tomorrow";
	private static String contact = "contact hospital";
	private static String advice = "follow doctor's advice";
	private static String nulString = "null, not measured";
	
	private KreatinineStatus today;
	private KreatinineStatus yesterday;
	private String expected;
	
	/**
	 * Constructor for test.
	 * @param firstArg today.
	 * @param secondArg yesterday.
	 * @param expected expected advice.
	 */
	public AdviseTest(KreatinineStatus firstArg, 
			KreatinineStatus secondArg, String expected) {
	     today = firstArg;
	     yesterday = secondArg;
	     this.expected = expected;
		  
	}
	
	/**
	 * all parameters.
	 * @return all parameters.
	 */
	 @Parameterized.Parameters
	  public static Collection<Object[]> testData() {
	      return Arrays.asList(new Object[][] {
	         { safe, nul, nulString},
	         { safe, conc, advice },
	         { safe, mild, niets },
	         { safe, res, niets },
	         { safe, safe, niets },
	         { res, nul, nulString},
	         { res, conc, advice },
	         { res, mild, herhalen },
	         { res, res, niets },
	         { res, safe, niets },
	         { mild, nul, nulString},
	         { mild, conc, advice },
	         { mild, mild, contact },
	         { mild, res, herhalen },
	         { mild, safe, herhalen },
	         { conc, nul, nulString},
	         { conc, conc, advice },
	         { conc, mild, contact },
	         { conc, res, contact },
	         { conc, safe, contact },
	         { nul, nul, nulString},
	         { nul, conc, nulString },
	         { nul, mild, nulString },
	         { nul, res, nulString },
	         { nul, safe, nulString },
	        
	      });
	   }

	 /**
		 * actual test.
		 */
		@Test
		public void statustest() {
			String temp = today.getAdvice(yesterday);
			if (expected != null && !expected.toString().equals(temp)) {
				System.out.println(today + ", " + yesterday);
				System.out.println("expected: " + expected + " actual: " + temp);
			}
			assertEquals(expected, temp);
		}
}
