package sa.tabadul.useraccessmanagement.api.advice.v2.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sa.tabadul.useraccessmanagement.common.enums.ExceptionMessage;
import sa.tabadul.useraccessmanagement.common.models.request.Pagination;
import sa.tabadul.useraccessmanagement.common.models.response.ApiResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PaginationResponse;
import sa.tabadul.useraccessmanagement.domain.RoleMaster;
import sa.tabadul.useraccessmanagement.service.RoleMasterService;

@RestController
@RequestMapping("/v2/rolemaster")
public class RoleMasterControllerV2 {
	
	@Autowired
	private RoleMasterService roleMasterService;

	@GetMapping("/stakeholders")
	public ResponseEntity<ApiResponse<List<RoleMaster>>> getRolesByAttribute(@RequestParam String name,
			@RequestParam String value,@RequestParam String appId) {
		ApiResponse<List<RoleMaster>> apiResponse = new ApiResponse<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), roleMasterService.getRolesByAttribute(name, value, appId));
		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping("/role")
	public ResponseEntity<ApiResponse<?>> addv2(@RequestBody RoleMaster roleMaster,
			@RequestHeader Map<String, String> headers) {
		return roleMasterService.addV2(roleMaster, headers.get("authorization"));
	}

	// role pagination v2
	@PostMapping("/roles-list")
	public ResponseEntity<PaginationResponse> rolesListv2(@RequestBody Pagination pagination) {
		return roleMasterService.rolePagination(pagination);
	}

	// role get by id v2
	@GetMapping("/role-id")
	public ResponseEntity<ApiResponse<RoleMaster>> roleByIdv2(@RequestParam String id, @RequestParam String appId,
			@RequestHeader Map<String, String> headers) {

		ApiResponse<RoleMaster> apiResponse = new ApiResponse<RoleMaster>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), roleMasterService.getByRoleId(id, appId));

		return ResponseEntity.ok(apiResponse);
	}

	//update 
	@PutMapping("/role-modify")
	public ResponseEntity<ApiResponse<RoleMaster>> roleDelete(@RequestBody RoleMaster roleMaster,@RequestParam String id,
			@RequestHeader Map<String, String> headers) {

		ApiResponse<RoleMaster> apiResponse = new ApiResponse<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), roleMasterService.update(roleMaster,id,headers.get("authorization")));

		return ResponseEntity.ok(apiResponse);
	}
	
	//delete
	@DeleteMapping("/role-remove")
	public ResponseEntity<ApiResponse<String>> roleDelete(@RequestParam String id,
			@RequestHeader Map<String, String> headers) {

		roleMasterService.delete(id,headers.get("authorization"));
		ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), ExceptionMessage.DATE_DELETED.getValue());

		return ResponseEntity.ok(apiResponse);
	}

}
