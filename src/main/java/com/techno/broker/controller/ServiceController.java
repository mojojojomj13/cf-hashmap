package com.techno.broker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.techno.broker.constants.RequestMappings;
import com.techno.broker.controller.utility.ErrorResponseUtil;
import com.techno.broker.service.HashMapService;

/**
 * This is the main controller for managing the Services
 * 
 * @author Prithvish Mukherjee
 *
 */
@Controller
@RequestMapping(RequestMappings.SERVICE_URL)
public class ServiceController {

	@Autowired
	private HashMapService service;

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceController.class);

	private JsonObject defaultJson() {
		JsonObject obj = new JsonObject();
		obj.addProperty("status", "success");
		obj.addProperty("code", HttpStatus.OK.value());
		return obj;
	}

	/**
	 * This method creates or updates a Service
	 * 
	 * @param id
	 *            the Id of the service
	 * @return A {@link ResponseEntity} that wraps the response which could be
	 *         the service definition or error
	 * 
	 * @see HashMapService.ServiceInstance for more details
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<?> update(@PathVariable String id) {
		HttpStatus status = HttpStatus.OK;
		String jsonStr = "{}";
		try {
			HashMapService.ServiceInstance instance = service.findById(id);
			if (!service.isExists(instance)) {
				instance = service.create();
				status = HttpStatus.CREATED;
			}
			jsonStr = new ObjectMapper().writeValueAsString(instance);
		} catch (JsonProcessingException e) {
			ErrorResponseUtil.handleJsonProcessingException();
		}
		return new ResponseEntity<String>(jsonStr, status);
	}

	/**
	 * This method is used to get the service information of a service
	 * 
	 * @param id
	 *            the id of the service whose information is required
	 * 
	 * @return A {@link ResponseEntity} that wraps the response which could be
	 *         the service definition or error
	 * 
	 * @see HashMapService.ServiceInstance for more details
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> getServiceInstance(@PathVariable String id) {
		String jsonStr = "{\"message\":\"no Such service\", \"code\":" + HttpStatus.NOT_FOUND.value()
				+ ", \"status\": \"" + HttpStatus.NOT_FOUND.getReasonPhrase() + "\"}";
		HttpStatus status = HttpStatus.NOT_FOUND;
		try {
			HashMapService.ServiceInstance instance = service.findById(id);
			if (service.isExists(instance)) {
				jsonStr = new ObjectMapper().writeValueAsString(instance);
				status = HttpStatus.OK;
			}
		} catch (JsonProcessingException e) {
			ErrorResponseUtil.handleJsonProcessingException();
		}
		return new ResponseEntity<String>(jsonStr, status);
	}

	/**
	 * This method is used to delete a service
	 * 
	 * @param id
	 *            the id of the service that is to be deleted
	 * @return
	 * 
	 * 		A {@link ResponseEntity} that wraps a response that contains
	 *         either a success with a message or an error
	 */
	@RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> delete(@PathVariable String id) {
		String jsonStr = "{\"message\":\"no Such service\", \"code\":" + HttpStatus.NOT_FOUND.value()
				+ ", \"status\": \"" + HttpStatus.NOT_FOUND.getReasonPhrase() + "\"}";
		HttpStatus status = HttpStatus.NOT_FOUND;
		try {
			HashMapService.ServiceInstance instance = service.findById(id);
			if (service.isExists(instance)) {
				service.delete(instance);
				LOGGER.info("Service " + id + " deleted successfully ");
				JsonObject obj = defaultJson();
				obj.addProperty("message", "Service deleted successfully");
				jsonStr = obj.toString();
				status = HttpStatus.OK;
			}
		} catch (Exception e) {
			return ErrorResponseUtil.handleException(e);
		}
		return new ResponseEntity<String>(jsonStr, status);
	}

}
