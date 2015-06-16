package context.healthinformatics.kreatininestatus;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
/**
 * tests for status generation (second part of algorithm).
 */
@RunWith(Parameterized.class)
public class StatusTest {
	
	private static KreatinineStatus safe = new SafeStatus();
	private static KreatinineStatus res = new ReasonablySafeStatus();
	private static KreatinineStatus mild = new MildConcernStatus();
	private static KreatinineStatus conc = new ConcernStatus();
	private static KreatinineStatus nul = new NullStatus();
	
	private KreatinineStatus first;
	private KreatinineStatus second;
	private KreatinineStatus expected;
	
	/**
	 * constructor for test.
	 * @param firstArg first argument.
	 * @param secondArg second argument.
	 * @param expected expected answer.
	 */
	public StatusTest(KreatinineStatus firstArg, 
			KreatinineStatus secondArg, KreatinineStatus expected) {
		     first = firstArg;
		     second = secondArg;
		     this.expected = expected;
		  
		   }

	/**
	 * all parameters.
	 * @return all parameters.
	 */
	 @Parameterized.Parameters
	  public static Collection<Object[]> testData() {
	      return Arrays.asList(new Object[][] {
	         { safe, nul, safe},
	         { safe, conc, safe },
	         { safe, mild, safe },
	         { safe, res, safe },
	         { safe, safe, safe },
	         { res, nul, res},
	         { res, conc, res },
	         { res, mild, res },
	         { res, res, res },
	         { res, safe, res },
	         { mild, nul, nul},
	         { mild, conc, conc },
	         { mild, mild, mild },
	         { mild, res, res },
	         { mild, safe, safe },
	         { conc, nul, nul},
	         { conc, conc, conc },
	         { conc, mild, conc },
	         { conc, res, mild },
	         { conc, safe, res },
	         { nul, nul, nul},
	         { nul, conc, nul },
	         { nul, mild, nul },
	         { nul, res, nul },
	         { nul, safe, nul },
	        
	      });
	   }
	 
	/**
	 * actual test.
	 */
	@Test
	public void statustest() {
		KreatinineStatus temp = first.getStatus(second);
		if (!expected.toString().equals(temp.toString())) {
			System.out.println(first + " " + second + " " + expected);
		}
		assertEquals(expected.toString(), temp.toString());
	}

}
