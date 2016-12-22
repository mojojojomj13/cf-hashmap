package com.techno.broker.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.techno.broker.collection.CustomHashMap;
import com.techno.broker.constants.AttribConstants;
import com.techno.broker.exception.ServiceException;
import com.techno.broker.service.HashMapService.ServiceInstance;

/**
 * This is the main class that manages the Service Binding/Unbinding and its
 * instances
 * 
 * @author Prithvish Mukherjee
 *
 */
@Service
public class BindingService {

	private CustomHashMap<String, BindingServiceInstance> bindings = new CustomHashMap<String, BindingServiceInstance>();

	@Autowired
	private HashMapService allServicesMap;

	public CustomHashMap<String, BindingServiceInstance> getBindings() {
		return bindings;
	}

	public HashMapService getAllServicesMap() {
		return allServicesMap;
	}

	/**
	 * This method binds the Service to a binding Service.
	 * 
	 * @param bindingServiceId
	 *            the id of the binding service to which the service is to be
	 *            bound
	 * @param serviceId
	 *            the id of the service which is to be bound
	 * @return A {@link BindingService} that represents the binding service id
	 *         and a List of services that are bound to that binding service id
	 * @throws ServiceException
	 *             may throw a {@link ServiceException} in case of any service
	 *             layer error
	 */
	public BindingServiceInstance bindService(String bindingServiceId, String serviceId) throws ServiceException {
		if (null == allServicesMap.findById(serviceId))
			throw new ServiceException(AttribConstants.NO_SUCH_SERVICE_AVAILABLE,
					new Exception(AttribConstants.NO_SUCH_SERVICE_AVAILABLE), HttpStatus.NOT_FOUND);
		if (null != allServicesMap.getServiceMap().get(serviceId)
				&& !allServicesMap.getServiceMap().get(serviceId).getBindable())
			throw new ServiceException(AttribConstants.SERVICE_CANNOT_BE_BOUND,
					new Exception(AttribConstants.SERVICE_CANNOT_BE_BOUND), HttpStatus.NOT_FOUND);

		CustomHashMap<String, ServiceInstance> services = new CustomHashMap<String, HashMapService.ServiceInstance>();
		BindingServiceInstance bindingServiceInstance = null;
		if (bindings.size() == 0) {
			bindingServiceId = UUID.randomUUID().toString();
			if (null != this.allServicesMap.findById(serviceId))
				services.put(serviceId, this.allServicesMap.findById(serviceId));
			bindingServiceInstance = new BindingServiceInstance(bindingServiceId, services);
			bindings.put(bindingServiceId, bindingServiceInstance);
		}
		if (bindings.size() == 1 && null != bindings.get(bindingServiceId) && null != allServicesMap.findById(serviceId)
				&& !bindings.get(bindingServiceId).getServices().containsKey(serviceId)) {
			bindingServiceInstance = bindings.get(bindingServiceId);
			bindings.get(bindingServiceId).getServices().put(serviceId, allServicesMap.findById(serviceId));
		}
		return bindings.get(bindingServiceId);
	}

	/**
	 * This method unbinds the Service to a binding Service.
	 * 
	 * @param bindingServiceId
	 *            the id of the binding service from which the service is to be
	 *            unbound
	 * @param serviceId
	 *            the id of the service which is to be unbound
	 * @return A {@link BindingService} that represents the binding service id
	 *         and a List of services that are currently bounded to that binding
	 *         service id
	 * @throws ServiceException
	 *             may throw a {@link ServiceException} in case of any service
	 *             layer error
	 */
	public BindingServiceInstance unbindService(String bindingServiceId, String serviceId) throws ServiceException {
		if (null == allServicesMap.findById(serviceId))
			throw new ServiceException(AttribConstants.NO_SUCH_SERVICE_AVAILABLE,
					new Exception(AttribConstants.NO_SUCH_SERVICE_AVAILABLE), HttpStatus.NOT_FOUND);
		if (bindings.containsKey(bindingServiceId)
				&& !bindings.get(bindingServiceId).getServices().containsKey(serviceId))
			throw new ServiceException(AttribConstants.SERVICE_BOUND_TO_BINDING_SERVICE,
					new Exception(AttribConstants.SERVICE_BOUND_TO_BINDING_SERVICE), HttpStatus.BAD_REQUEST);
		if (bindings.containsKey(bindingServiceId)
				&& bindings.get(bindingServiceId).getServices().containsKey(serviceId)) {
			bindings.get(bindingServiceId).getServices().remove(serviceId);
		}
		return bindings.get(bindingServiceId);
	}

	/**
	 * The main class that represents the structure of the Binding service
	 * instance and its Services
	 * 
	 * @author Prithvish Mukherjee
	 *
	 */
	@JsonInclude(Include.NON_NULL)
	public static class BindingServiceInstance {
		private String bindingServiceId;

		private String message;

		private CustomHashMap<String, ServiceInstance> services = new CustomHashMap<String, HashMapService.ServiceInstance>();

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getBindingServiceId() {
			return bindingServiceId;
		}

		public void setBindingServiceId(String bindingServiceId) {
			this.bindingServiceId = bindingServiceId;
		}

		public CustomHashMap<String, ServiceInstance> getServices() {
			return services;
		}

		public void setServices(CustomHashMap<String, ServiceInstance> services) {
			this.services = services;
		}

		public BindingServiceInstance(String bindingServiceId, CustomHashMap<String, ServiceInstance> services) {
			this.bindingServiceId = bindingServiceId;
			this.services = services;
		}

		public BindingServiceInstance() {
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((bindingServiceId == null) ? 0 : bindingServiceId.hashCode());
			result = prime * result + ((message == null) ? 0 : message.hashCode());
			result = prime * result + ((services == null) ? 0 : services.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BindingServiceInstance other = (BindingServiceInstance) obj;
			if (bindingServiceId == null) {
				if (other.bindingServiceId != null)
					return false;
			} else if (!bindingServiceId.equals(other.bindingServiceId))
				return false;
			if (message == null) {
				if (other.message != null)
					return false;
			} else if (!message.equals(other.message))
				return false;
			if (services == null) {
				if (other.services != null)
					return false;
			} else if (!services.equals(other.services))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "{\"bindingServiceId\":" + (null != bindingServiceId ? "\"" + bindingServiceId + "\"" : null)
					+ ", \"message\":" + (null != message ? "\"" + message + "\"" : null) + ", \"services\":"
					+ (null != services ? "\"" + services + "\"" : null) + "}";
		}

	}
}
