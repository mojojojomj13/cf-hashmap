package com.techno.broker.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techno.broker.collection.CustomHashMap;
import com.techno.broker.constants.RequestMappings;
import com.techno.broker.controller.utility.ErrorResponseUtil;
import com.techno.broker.exception.ServiceException;
import com.techno.broker.service.BindingService;
import com.techno.broker.service.BindingService.BindingServiceInstance;

/**
 * This is the Main controller that handles all the service instance calls. Each
 * service instance will have its own Id and needs to be bind with a binding
 * service.
 * 
 * @author Prithvish Mukherjee
 *
 */
@Controller
public class ServiceInstanceController {

	@Autowired
	private BindingService bindingService;

	private CustomHashMap<String, String> myMap = new CustomHashMap<String, String>();

	private void checkBindingStore(String serviceId) throws ServiceException {
		if (bindingService.getBindings().size() == 0)
			throw new ServiceException(
					"NO BINDING SERVICE YET, CREATE A BINDING SERVICE AND BIND THIS SERVICE TO ANY BINDING SERVICE FIRST",
					new Exception(
							"NO BINDING SERVICE YET, CREATE A BINDING SERVICE AND BIND THIS SERVICE TO ANY BINDING SERVICE FIRST"),
					HttpStatus.BAD_REQUEST);
		if (bindingService.getBindings().size() == 1
				&& !((ArrayList<BindingServiceInstance>) bindingService.getBindings().values()).get(0).getServices()
						.containsKey(serviceId)) {
			throw new ServiceException("THIS SERVICE IS NOT BOUND TO THIS BINDING SERVICE YET",
					new Exception("THIS SERVICE IS NOT BOUND TO THIS BINDING SERVICE YET"), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method adds value to a HashMap
	 * 
	 * @param serviceId
	 *            the service id of the service instance that is to be executed
	 * @param key
	 *            the key to be stored in the HashMap
	 * @param value
	 *            the Value to be stored in the HashMap
	 * @return
	 * 
	 * 		A {@link ResponseEntity} that wraps the Map thus created or an
	 *         error message
	 */
	@RequestMapping(value = RequestMappings.HASHMAP_PUTVALUE, method = RequestMethod.PUT)
	public ResponseEntity<?> mapAdd(@PathVariable(value = "serviceId") String serviceId,
			@PathVariable(value = "key") String key, @PathVariable(value = "value") String value) {
		HttpStatus status = HttpStatus.OK;
		String jsonStr = "{}";
		try {
			checkBindingStore(serviceId);
			if (null == bindingService.getAllServicesMap().findById(serviceId))
				throw new ServiceException("NO SUCH SERVICE ID", new Exception("NO SUCH SERVICE ID"),
						HttpStatus.BAD_REQUEST);
			myMap.put(key, value);
			jsonStr = new ObjectMapper().writeValueAsString(myMap);
		} catch (ServiceException e) {
			return ErrorResponseUtil.handleException(e);
		} catch (JsonProcessingException e) {
			return ErrorResponseUtil.handleJsonProcessingException();
		}
		return new ResponseEntity<String>(jsonStr, status);
	}

	/**
	 * This method deletes a value from the HashMap
	 * 
	 * @param serviceId
	 *            the service Id of the service instance that is to be executed
	 * @param key
	 *            the key ,which needs to be deleted from the HashMap
	 * @return
	 * 
	 * 		A {@link ResponseEntity} that wraps the HashMap or an error
	 *         message
	 */
	@RequestMapping(value = RequestMappings.HASHMAP_VALUE, method = RequestMethod.DELETE)
	public ResponseEntity<?> mapDelete(@PathVariable(value = "serviceId") String serviceId,
			@PathVariable(value = "key") String key) {
		HttpStatus status = HttpStatus.OK;
		String jsonStr = "{}";
		try {
			checkBindingStore(serviceId);
			if (null == bindingService.getAllServicesMap().findById(serviceId))
				throw new ServiceException("NO SUCH SERVICE ID", new Exception("NO SUCH SERVICE ID"),
						HttpStatus.BAD_REQUEST);
			myMap.remove(key);
			jsonStr = new ObjectMapper().writeValueAsString(myMap);
		} catch (ServiceException e) {
			return ErrorResponseUtil.handleException(e);
		} catch (JsonProcessingException e) {
			return ErrorResponseUtil.handleJsonProcessingException();
		}
		return new ResponseEntity<String>(jsonStr, status);
	}

	/**
	 * This method retrieves a HashMap value based on the key provided
	 * 
	 * @param serviceId
	 *            the servoce id of the service instance that is to be executed
	 * @param key
	 *            the key , whose value is to be retrieved
	 * @return
	 * 
	 * 		A {@link ResponseEntity} that wraps a hashMap value , if key is
	 *         found, empty if key not found or an error message
	 */
	@RequestMapping(value = RequestMappings.HASHMAP_VALUE, method = RequestMethod.GET)
	public ResponseEntity<?> mapGet(@PathVariable(value = "serviceId") String serviceId,
			@PathVariable(value = "key") String key) {
		HttpStatus status = HttpStatus.OK;
		String jsonStr = "{}";
		try {
			checkBindingStore(serviceId);
			if (null == bindingService.getAllServicesMap().findById(serviceId))
				throw new ServiceException("NO SUCH SERVICE ID", new Exception("NO SUCH SERVICE ID"),
						HttpStatus.BAD_REQUEST);
			jsonStr = null != myMap.get(key) ? "{\"value\":\"" + myMap.get(key) + "\"}" : null;
		} catch (ServiceException e) {
			return ErrorResponseUtil.handleException(e);
		}
		return new ResponseEntity<String>(jsonStr, status);
	}
}
