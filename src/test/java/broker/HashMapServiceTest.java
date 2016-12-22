package broker;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techno.broker.Application;
import com.techno.broker.service.HashMapService;
import com.techno.broker.service.HashMapService.ServiceInstance;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest("server.port:8090")
public class HashMapServiceTest {

	@Autowired
	private HashMapService service;

	List<ServiceInstance> serviceIds;

	@Before
	public void setUp() {
		serviceIds = new ArrayList<ServiceInstance>();
		ServiceInstance serviceInstance1 = service.create();
		serviceIds.add(serviceInstance1);
		ServiceInstance serviceInstance2 = service.create();
		serviceIds.add(serviceInstance2);

	}

	@Test
	public void testCreate() {
		assertEquals(2, service.getServices().length);
		ServiceInstance serviceInstance = service.create();
		serviceIds.add(serviceInstance);
		assertEquals(3, service.getServices().length);
	}

	@Test
	public void testGetServices() {
		ServiceInstance[] services = service.getServices();
		assertNotNull(services);
		assertEquals(2, services.length);
	}

	@Test
	public void testIsExists() {
		for (ServiceInstance instance : serviceIds) {
			assertTrue(service.isExists(instance));
		}
	}

	@Test
	public void testFindById() {
		for (ServiceInstance serviceInstance : serviceIds) {
			assertNotNull(service.findById(serviceInstance.getId()));
		}
	}

	@Test
	public void testDelete() {
		assertEquals(2, service.getServices().length);
		service.delete(serviceIds.get(0));
		assertEquals(1, service.getServices().length);

	}

	@Test
	public void testgetNumberOfExistingInstances() {
		assertNotNull(service.getServices());
		assertEquals(2, service.getNumberOfExistingInstances());
		assertEquals(service.getServices().length, service.getNumberOfExistingInstances());
	}

	@After
	public void tearDown() {
		for (ServiceInstance serviceInstance : serviceIds) {
			service.delete(serviceInstance);
		}
	}

}
