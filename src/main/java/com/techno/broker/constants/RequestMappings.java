package com.techno.broker.constants;

/**
 * These contain the Request Mapping constants to be used as api URI
 * 
 * @author Prithvish Mukherjee
 *
 */
public class RequestMappings {

	public static final String SERVICE_URL = "/v2/service_instances/{id}";
	public static final String SERVICE_BINDINGS = "/v2/service_instances/{serviceId}/service_bindings/{bindingServiceId}";
	public static final String CATALOG = "/v2/catalog";
	public static final String HASHMAP_VALUE = "/v2/services/{serviceId}/{key}";
	public static final String HASHMAP_PUTVALUE = "/v2/services/{serviceId}/{key}/{value}";

}
