package broker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ HashMapServiceTest.class, CustomHashMapTest.class, BindingServiceTest.class })
public class SuiteTest {

	@Test
	public void temp() {
		assertEquals("A", "A");
	}
}
