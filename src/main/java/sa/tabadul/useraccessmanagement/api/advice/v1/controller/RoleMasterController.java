package sa.tabadul.useraccessmanagement.api.advice.v1.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.validation.Valid;

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
import sa.tabadul.useraccessmanagement.common.models.request.RoleMasterFilter;
import sa.tabadul.useraccessmanagement.common.models.response.ApiResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PaginationResponse;
import sa.tabadul.useraccessmanagement.common.models.response.RoleTypeResponse;
import sa.tabadul.useraccessmanagement.domain.RoleMaster;
import sa.tabadul.useraccessmanagement.service.RoleMasterService;

@RestController
@RequestMapping("/rolemaster")
public class RoleMasterController {

	@Autowired
	private RoleMasterService roleMasterService;

	// add
	@PostMapping("/role")
	public ResponseEntity<ApiResponse<RoleMaster>> role(@Valid @RequestBody RoleMaster r,
			@RequestHeader Map<String, String> headers) {

		ApiResponse<RoleMaster> apiResponse = new ApiResponse<RoleMaster>(HttpStatus.CREATED.value(),
				HttpStatus.CREATED.getReasonPhrase(), roleMasterService.add(r));

		return ResponseEntity.ok(apiResponse);
	}

	// get all based on appId
	@GetMapping("/roles")
	public ResponseEntity<ApiResponse<List<RoleMaster>>> roles(@RequestParam Integer appid,
			@RequestHeader Map<String, String> headers) {

		ApiResponse<List<RoleMaster>> apiResponse = new ApiResponse<List<RoleMaster>>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), roleMasterService.get(appid));

		return ResponseEntity.ok(apiResponse);
	}

	// get by id
	@GetMapping("/role-id")
	public ResponseEntity<ApiResponse<RoleMaster>> roleById(@RequestParam Integer id, @RequestParam Integer appid,
			@RequestHeader Map<String, String> headers) {

		ApiResponse<RoleMaster> apiResponse = new ApiResponse<RoleMaster>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), roleMasterService.getBy(id, appid));

		return ResponseEntity.ok(apiResponse);
	}

	// update
	@PutMapping("/role-modify")
	public ResponseEntity<ApiResponse<RoleMaster>> roleUpdate(@RequestParam Integer id,
			@Valid @RequestBody RoleMaster rolemaster, @RequestHeader Map<String, String> headers) {

		ApiResponse<RoleMaster> apiResponse = new ApiResponse<RoleMaster>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), roleMasterService.updateBy(id, rolemaster));

		return ResponseEntity.ok(apiResponse);
	}

	// delete
	@DeleteMapping("/role-remove")
	public ResponseEntity<ApiResponse<String>> roleDelete(@RequestParam Integer id,
			@RequestHeader Map<String, String> headers) {

		roleMasterService.deleteBy(id);
		ApiResponse<String> apiResponse = new ApiResponse<String>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), ExceptionMessage.DATE_DELETED.getValue());

		return ResponseEntity.ok(apiResponse);
	}

	// pagination
	@PostMapping("/roles-list")
	public ResponseEntity<PaginationResponse> rolesList(@RequestBody Pagination p, @RequestParam Integer appid,
			@RequestHeader Map<String, String> headers) {

		List<RoleMaster> role1 = roleMasterService.getAllBy(p, appid);
		long recordsTotal = roleMasterService.getTotalRecord(appid);
		long recordsFiltered = roleMasterService.getFilteredRecord(p.getSearch(), appid);
		PaginationResponse response = new PaginationResponse(recordsTotal, recordsFiltered, role1);

		return ResponseEntity.ok(response);
	}

	// filter
	@PostMapping("/roles-filter")
	public ResponseEntity<ApiResponse<List<RoleMaster>>> rolesFilter(@RequestBody RoleMasterFilter f,
			@RequestParam Integer appid, @RequestHeader Map<String, String> headers) {

		List<RoleMaster> c1 = roleMasterService.getAllData(f, appid);
		ApiResponse<List<RoleMaster>> apiResponse = new ApiResponse<List<RoleMaster>>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), c1);

		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping("/role-modules")
	public ResponseEntity<ApiResponse<List<Map<String, ?>>>> modulesList(@RequestHeader Map<String, String> headers) {

		ApiResponse<List<Map<String, ?>>> apiResponse = new ApiResponse<List<Map<String, ?>>>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.roleMasterService.getallModules());

		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping("/stakeholders")
	public ResponseEntity<ApiResponse<List<RoleTypeResponse>>> stakeHoldersById(@RequestParam Integer appid,
			@RequestParam @Nullable String rolltype, @RequestHeader Map<String, String> headers) {

		ApiResponse<List<RoleTypeResponse>> apiResponse = new ApiResponse<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.roleMasterService.getStkateholderById(appid, rolltype));

		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping("/all-roles")
	public ResponseEntity<ApiResponse<List<RoleMaster>>> getRoles() {
		ApiResponse<List<RoleMaster>> apiResponse = new ApiResponse<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), roleMasterService.allRoles());
		return ResponseEntity.ok(apiResponse);
	}

}
