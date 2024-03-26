package sa.tabadul.useraccessmanagement.api.advice.v2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sa.tabadul.useraccessmanagement.common.models.response.ResponseEnvelope;
import sa.tabadul.useraccessmanagement.domain.ResourceServerResource;
import sa.tabadul.useraccessmanagement.service.ServerResourceService;

@RestController
@RequestMapping("/server-resources")
public class ServerResourceController {

	@Autowired
	ServerResourceService serverResourceService;
	
	@PostMapping("/save-server-resources")
	public ResponseEntity<ResponseEnvelope<?>> saveUser(@RequestBody ResourceServerResource resource){
		return serverResourceService.save(resource);
	}

	@GetMapping("/all-moduleV2-details")
	public ResponseEntity<ResponseEnvelope<?>> getAllModule(@RequestParam("type") String type) {
		return serverResourceService.getAllModules(type);
	}

}
