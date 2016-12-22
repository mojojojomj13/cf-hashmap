package broker;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techno.broker.Application;
import com.techno.broker.collection.CustomEntry;
import com.techno.broker.collection.CustomHashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest("server.port:8090")
public class CustomHashMapTest {

	private static final String VAL3 = "sadadsad";
	private static final String VAL2 = "sadasd";
	private static final String VAL1 = "abcd";
	private CustomHashMap<Integer, String> map;

	@Before
	public void setUp() {
		map = new CustomHashMap<Integer, String>();
	}

	@Test
	public void testPut() {
		map.put(1, VAL1);
		assertTrue(!map.isEmpty());
		assertEquals(1, map.size());
		map.put(2, VAL2);
		assertEquals(2, map.size());
		map.put(3, VAL3);
		assertEquals(3, map.size());
	}

	@Test
	public void testGet() {
		map.put(1, VAL1);
		assertTrue(!map.isEmpty());
		assertEquals(1, map.size());
		assertNotNull(map.get(1));
		assertEquals(VAL1, map.get(1));
	}

	@Test
	public void testRemove() {
		testPut();
		String oldVal = map.remove(1);
		assertEquals(VAL1, oldVal);
		assertFalse(map.containsKey(1));
		assertEquals(2, map.size());
	}

	@Test
	public void testSize() {
		testPut();
		assertTrue(!map.isEmpty());
		assertEquals(3, map.size());
	}

	@Test
	public void testClear() {
		testPut();
		map.clear();
		assertTrue(map.isEmpty());
		assertEquals(0, map.size());
	}

	@Test
	public void testContainsKey() {
		testPut();
		assertTrue(map.containsKey(1));
		assertTrue(map.containsKey(2));
		assertTrue(map.containsKey(3));
		assertFalse(map.containsKey(10));
		assertFalse(map.containsKey(11));
	}

	@Test
	public void testEntrySet() {
		testPut();
		assertNotNull(map);
		CustomEntry<Integer, String> entry = map.entrySet().iterator().next();
		assertNotNull(entry);
		assertNotNull(entry.getKey());
		assertEquals(entry.getKey().getClass(), Integer.class);
		if (entry.getValue() != null)
			assertEquals(entry.getValue().getClass(), String.class);
		else
			assertNull(entry.getValue());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(map.isEmpty());
		map.put(1, VAL1);
		assertFalse(map.isEmpty());
	}

	@Test
	public void testKeySet() {
		testPut();
		assertEquals(3, map.keySet().size());
		assertTrue(map.keySet().contains(1));
		assertTrue(map.keySet().contains(2));
		assertTrue(map.keySet().contains(3));
	}

	@Test
	public void testValues() {
		testPut();
		assertEquals(3, map.values().size());
		assertTrue(map.values().contains(VAL1));
		assertTrue(map.values().contains(VAL2));
		assertTrue(map.values().contains(VAL3));
	}

	@After
	public void tearDown() {
		map = null;
	}

}
