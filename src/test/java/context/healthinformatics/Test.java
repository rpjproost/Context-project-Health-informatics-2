package context.healthinformatics;

import static org.junit.Assert.assertEquals;

public class Test {
	
	private Main main;

	@org.junit.Test
	public void test() {
		main = new Main();
		assertEquals(main.getX(), 1);
	}

}
