package com.techno.broker.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.techno.broker.collection.CustomHashMap;

/**
 * This is the main Service class which manages the Service Instances
 * 
 * @author Prithvish Mukherjee
 *
 */
@Service
@JsonInclude(Include.NON_NULL)
public class HashMapService {

	private CustomHashMap<String, ServiceInstance> serviceMap = new CustomHashMap<String, HashMapService.ServiceInstance>();

	private ServiceInstance[] services;

	public ServiceInstance[] getServices() {
		services = new ServiceInstance[serviceMap.values().size()];
		return serviceMap.values().toArray(services);
	}

	@JsonIgnore
	public CustomHashMap<String, ServiceInstance> getServiceMap() {
		return serviceMap;
	}

	public ServiceInstance findById(String id) {
		return serviceMap.get(id);
	}

	public boolean isExists(ServiceInstance instance) {
		return serviceMap.values().contains(instance);
	}

	public int getNumberOfExistingInstances() {
		return serviceMap.values().size();
	}

	public ServiceInstance create() {
		ServiceInstance instance = new ServiceInstance(UUID.randomUUID().toString());
		serviceMap.put(instance.getId(), instance);
		return instance;
	}

	public void delete(ServiceInstance instance) {
		serviceMap.remove(instance.getId());
	}

	@JsonInclude(Include.NON_NULL)
	public static class ServiceInstance {
		private String name = "cf-hashmap-service";
		private String id;
		private String description = "This is a hashmap service, currently supports only <String, String>";
		private Boolean bindable = true;

		public static class EndPoints {
			private String uri;
			private String method;

			public String getUri() {
				return uri;
			}

			public void setUri(String uri) {
				this.uri = uri;
			}

			public String getMethod() {
				return method;
			}

			public void setMethod(String method) {
				this.method = method;
			}

			public EndPoints(String uri, String method) {
				this.uri = uri;
				this.method = method;
			}

		}

		private EndPoints endPoints[] = new EndPoints[] {
				new EndPoints("/v2/services/{serviceId}/{key}", "GET , DELETE"),
				new EndPoints("/v2/services/{serviceId}/{key}/{value}", "PUT") };

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Boolean getBindable() {
			return bindable;
		}

		public void setBindable(Boolean bindable) {
			this.bindable = bindable;
		}

		public EndPoints[] getEndPoints() {
			return endPoints;
		}

		public void setEndPoints(EndPoints[] endPoints) {
			this.endPoints = endPoints;
		}

		private ServiceInstance(String id) {
			this.id = id;
			for (EndPoints endPoint : endPoints) {
				endPoint.setUri(endPoint.getUri().replace("{serviceId}", id));
			}
		}

	}
}
