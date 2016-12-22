package com.techno.broker.controller.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.techno.broker.exception.ServiceException;

/**
 * This is a Utility class to help the Controllers deal with different errors
 * into a Json equivalent
 * 
 * @author Prithvish Mukherjee
 *
 */
public class ErrorResponseUtil {
	/**
	 * This method handles any kind of exception that is thrown in a controller.
	 * Wraps the exception as a Json and then wraps it as a
	 * {@link ResponseEntity}. It will remove attributes like stackTrace and
	 * throwable
	 * 
	 * @param e
	 *            The {@link Exception} that is to be wrapped
	 * @return The {@link ResponseEntity} that wraps the Error
	 *         Object(transformed as {@link JsonObject} first)
	 * 
	 */
	public static ResponseEntity<?> handleException(Exception e) {
		HttpStatus status = null;
		String jsonStr = null;
		try {
			jsonStr = new ObjectMapper().writeValueAsString(e);
			JsonObject obj = (JsonObject) new JsonParser().parse(jsonStr);
			obj.remove("stackTrace");
			obj.remove("throwable");
			jsonStr = obj.toString();
			status = ((ServiceException) e).getStatus();
		} catch (JsonProcessingException ex) {
			jsonStr = "{\"message\":\"Some Error with the Service\", \"code\": "
					+ HttpStatus.SERVICE_UNAVAILABLE.value() + "}";
			status = HttpStatus.SERVICE_UNAVAILABLE;
		}
		return new ResponseEntity<String>(jsonStr, status);
	}

	/**
	 * This method handles any {@link JsonProcessingException} and wraps the
	 * Object as a Json
	 * 
	 * @return
	 * 
	 * 		the wrapped JsonObject in a {@link ResponseEntity}
	 */
	public static ResponseEntity<?> handleJsonProcessingException() {
		String jsonStr = "{\"message\":\"Some Error with the App Service\", \"code\": "
				+ HttpStatus.SERVICE_UNAVAILABLE.value() + "}";
		HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
		return new ResponseEntity<String>(jsonStr, status);
	}
}
