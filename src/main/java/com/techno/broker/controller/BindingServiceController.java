package com.techno.broker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techno.broker.constants.RequestMappings;
import com.techno.broker.controller.utility.ErrorResponseUtil;
import com.techno.broker.exception.ServiceException;
import com.techno.broker.service.BindingService;
import com.techno.broker.service.BindingService.BindingServiceInstance;

/**
 * This is the main controller that takes care of the Binding Services
 * 
 * @author Prithvish Mukherjee
 *
 */
@Controller
public class BindingServiceController {

	@Autowired
	private BindingService service;

	/**
	 * This method returns all the available services
	 * 
	 * @return
	 * 
	 * 		A {@link ResponseEntity} which wraps the response object which
	 *         could be a list of services or error message in case of any error
	 */
	@RequestMapping(value = RequestMappings.CATALOG, method = RequestMethod.GET)
	public ResponseEntity<?> getCatalog() {
		HttpStatus status = HttpStatus.OK;
		String jsonStr = "{}";
		try {
			jsonStr = service.getAllServicesMap() != null
					? new ObjectMapper().writeValueAsString(service.getAllServicesMap()) : jsonStr;
		} catch (JsonProcessingException e) {
			return ErrorResponseUtil.handleJsonProcessingException();
		}
		return new ResponseEntity<String>(jsonStr, status);
	}

	/**
	 * This method is used for binding a service
	 * 
	 * @param bindingServiceId
	 *            the id for this binding service
	 * @param serviceId
	 *            the id of the service that we want to bind to this
	 *            bindingService
	 * @return
	 * 
	 * 		A {@link ResponseEntity} which wraps the response object which
	 *         could be a list of services or error message in case of any error
	 * 
	 */
	@RequestMapping(value = RequestMappings.SERVICE_BINDINGS, method = RequestMethod.PUT)
	public ResponseEntity<?> bindService(@PathVariable(value = "bindingServiceId") String bindingServiceId,
			@PathVariable(value = "serviceId") String serviceId) {
		HttpStatus status = HttpStatus.OK;
		String jsonStr = "{}";
		try {
			BindingServiceInstance m = null;
			if (service.getBindings().size() == 0) {
				m = service.bindService(bindingServiceId, serviceId);
				if (null == m)
					throw new ServiceException("NO SUCH BINDING SERVICE ID FOUND",
							new Exception("NO SUCH BINDING SERVICE ID FOUND"), HttpStatus.NOT_FOUND);
			} else {
				m = service.getBindings().values().iterator().next();
				m = service.bindService(m.getBindingServiceId(), serviceId);
			}
			jsonStr = new ObjectMapper().writeValueAsString(m);
		} catch (ServiceException e) {
			return ErrorResponseUtil.handleException(e);
		} catch (JsonProcessingException e) {
			return ErrorResponseUtil.handleJsonProcessingException();
		}
		return new ResponseEntity<String>(jsonStr, status);
	}

	/**
	 * This method is used to unbind the service from the binding service
	 * 
	 * @param bindingServiceId
	 *            the id for the binding service
	 * @param serviceId
	 *            the id for the service that we want to unbind from this
	 *            binding service
	 * @return
	 * 
	 * 		A {@link ResponseEntity} which wraps the response object which
	 *         could be a list of services or error message in case of any error
	 */
	@RequestMapping(value = RequestMappings.SERVICE_BINDINGS, method = RequestMethod.DELETE)
	public ResponseEntity<?> unbindService(@PathVariable(value = "bindingServiceId") String bindingServiceId,
			@PathVariable(value = "serviceId") String serviceId) {
		HttpStatus status = HttpStatus.OK;
		String jsonStr = "{}";
		try {
			BindingServiceInstance m = service.unbindService(bindingServiceId, serviceId);
			if (null == m)
				throw new ServiceException("NO SUCH BINDING SERVICE ID FOUND",
						new Exception("NO SUCH BINDING SERVICE ID FOUND"), HttpStatus.NOT_FOUND);
			m.setMessage("Binding service " + bindingServiceId + " Unbound successfully");
			jsonStr = new ObjectMapper().writeValueAsString(m);
		} catch (ServiceException e) {
			return ErrorResponseUtil.handleException(e);
		} catch (JsonProcessingException e) {
			return ErrorResponseUtil.handleJsonProcessingException();
		}
		return new ResponseEntity<String>(jsonStr, status);
	}

}
