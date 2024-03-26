package sa.tabadul.useraccessmanagement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import sa.tabadul.useraccessmanagement.common.configs.PropertiesConfig;
import sa.tabadul.useraccessmanagement.common.models.response.ResponseEnvelope;
import sa.tabadul.useraccessmanagement.domain.ResourceServerResource;
import sa.tabadul.useraccessmanagement.repository.ResourceServerResourceRepository;

@Service
public class ServerResourceService {

	@Autowired
	ResourceServerResourceRepository serverResourceRepository;
	
	@Autowired
	PropertiesConfig propertiesConfig;

	ResponseEnvelope<?> apiResponse = null;

	public ResponseEntity<ResponseEnvelope<?>> save(ResourceServerResource serverResource) {
		apiResponse = new ResponseEnvelope<>(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(),
				serverResourceRepository.save(serverResource));
		return ResponseEntity.ok(apiResponse);
	}

	public ResponseEntity<ResponseEnvelope<?>> getAllModules(String type) {

		List<ResourceServerResource> serverResources = serverResourceRepository.findByServerIdAndModuleType(propertiesConfig.getResourceServerId(),type);
		List<Map<String, Object>> serverResourcesResponse = new ArrayList<>();
		for (ResourceServerResource resource : serverResources) {
			Map<String, Object> result = new HashMap<>();
			result.put("id", resource.getId());
			result.put("name", resource.getName());
			result.put("type", resource.getType());
			result.put("displayName", resource.getDisplayName());
			serverResourcesResponse.add(result);

		}

		apiResponse = new ResponseEnvelope<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
				serverResourcesResponse);
		return ResponseEntity.ok(apiResponse);
	}

}
