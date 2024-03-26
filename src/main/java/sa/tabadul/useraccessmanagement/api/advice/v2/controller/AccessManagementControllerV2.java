package sa.tabadul.useraccessmanagement.api.advice.v2.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sa.tabadul.useraccessmanagement.common.models.request.ResourceServerRequest;
import sa.tabadul.useraccessmanagement.common.models.response.AccessGetResponseV2;
import sa.tabadul.useraccessmanagement.common.models.response.ApiResponse;
import sa.tabadul.useraccessmanagement.service.AccessManagerService;

@RestController
@RequestMapping("/v2/access")
public class AccessManagementControllerV2 {
	
	@Autowired
	private AccessManagerService accessService;
	
	@PostMapping("/access-info")
	public ResponseEntity<ApiResponse<AccessGetResponseV2>> userAccess(@RequestBody ResourceServerRequest request) {

		ApiResponse<AccessGetResponseV2> apiResponse = new ApiResponse<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.accessService.getAllAccess(request));

		return ResponseEntity.ok(apiResponse);
	}
	
	@PostMapping("/access-add")
	public ResponseEntity<ApiResponse<String>> userAccess(@RequestBody AccessGetResponseV2 request,
			@RequestHeader Map<String, String> headers) {

		ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
				this.accessService.createAccess(request,headers.get("authorization")));

		return ResponseEntity.ok(apiResponse);
	}

}
