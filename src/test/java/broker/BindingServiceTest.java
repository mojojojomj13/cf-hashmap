package broker;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.JsonParser;
import com.techno.broker.Application;
import com.techno.broker.constants.AttribConstants;
import com.techno.broker.controller.utility.ErrorResponseUtil;
import com.techno.broker.exception.ServiceException;
import com.techno.broker.service.BindingService;
import com.techno.broker.service.BindingService.BindingServiceInstance;
import com.techno.broker.service.HashMapService;
import com.techno.broker.service.HashMapService.ServiceInstance;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest("server.port:8090")
public class BindingServiceTest {

	@Autowired
	private BindingService bindingService;

	@Autowired
	private HashMapService service;

	private ServiceInstance instance;
	private BindingServiceInstance bindingInstance;

	@Before
	public void setUp() throws ServiceException {
		instance = service.create();
	}

	@Test
	public void testBindService() {
		try {
			bindingInstance = bindingService.bindService("abcd-1234", instance.getId());
		} catch (ServiceException e) {
		}
		assertNotNull(bindingInstance);
		assertEquals(bindingService.getAllServicesMap().findById(instance.getId()), instance);
		assertTrue(bindingService.getBindings().containsKey(bindingInstance.getBindingServiceId()));
		assertNotNull(bindingService.getBindings().get(bindingInstance.getBindingServiceId()));
		assertEquals(1, bindingService.getBindings().get(bindingInstance.getBindingServiceId()).getServices().size());
	}

	@Test
	public void testNoSuchServiceId() {
		try {
			bindingService.bindService("abcd-1234", "pqrs-4567");
		} catch (ServiceException e) {
			ResponseEntity<?> resp = ErrorResponseUtil.handleException(e);
			boolean hasMessage = new JsonParser().parse(resp.getBody().toString()).getAsJsonObject().has("message");
			if (hasMessage) {
				String message = new JsonParser().parse(resp.getBody().toString()).getAsJsonObject().get("message")
						.getAsString();
				assertEquals(AttribConstants.NO_SUCH_SERVICE_AVAILABLE, message);
			}
		}
	}

	@Test
	public void testUnbindService() {
		assertNotNull(instance);
		testBindService();
		assertTrue(bindingService.getBindings().values().size() > 0);
		assertEquals(1, bindingService.getBindings().values().iterator().next().getServices().size());
		try {
			bindingService.unbindService("abcd-1234", instance.getId());
			assertEquals(1, bindingService.getBindings().values().iterator().next().getServices().size());
			bindingService.unbindService(bindingInstance.getBindingServiceId(), instance.getId());
			assertEquals(0, bindingService.getBindings().values().iterator().next().getServices().size());
			assertTrue(bindingService.getBindings().values().size() > 0);
			assertNotNull(bindingService.getBindings().values().iterator().next());
			bindingService.unbindService(bindingInstance.getBindingServiceId(), instance.getId());
		} catch (ServiceException e) {
			ResponseEntity<?> resp = ErrorResponseUtil.handleException(e);
			boolean hasMessage = new JsonParser().parse(resp.getBody().toString()).getAsJsonObject().has("message");
			if (hasMessage) {
				String message = new JsonParser().parse(resp.getBody().toString()).getAsJsonObject().get("message")
						.getAsString();
				assertEquals(AttribConstants.SERVICE_BOUND_TO_BINDING_SERVICE, message);
			}
		}
	}

	@After
	public void tearDown() {
		service.delete(instance);
		bindingService.getBindings().clear();
		bindingInstance = null;
	}

}
