package sa.tabadul.useraccessmanagement.api.advice.v1.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sa.tabadul.useraccessmanagement.common.models.request.UserAccessRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserUpdateRequest;
import sa.tabadul.useraccessmanagement.common.models.response.AccessPages;
import sa.tabadul.useraccessmanagement.common.models.response.AccessResponse;
import sa.tabadul.useraccessmanagement.common.models.response.ApiResponse;
import sa.tabadul.useraccessmanagement.domain.AccessHeader;
import sa.tabadul.useraccessmanagement.service.AccessManagerService;

@Validated
@RestController
@RequestMapping(value = "/access")
public class AccessManagementContoller {

	@Autowired
	AccessManagerService accessService;

	// To view access based on stake holder and module id
	@PostMapping(value = "/access-info", consumes = "application/json")
	public ResponseEntity<ApiResponse<List<AccessResponse>>> userAccess(@RequestBody UserAccessRequest request,
			@RequestHeader Map<String, String> headers) {

		ApiResponse<List<AccessResponse>> apiResponse = new ApiResponse<List<AccessResponse>>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.accessService.ViewUserAccess(request));

		return ResponseEntity.ok(apiResponse);
	}

	// To list all the access given to stake holder
	@GetMapping(value = "/stakeholder")
	public ResponseEntity<ApiResponse<List<AccessResponse>>> giveRes(
			@NotNull @RequestParam(value = "stakeholdertype") Integer stakeholdertype,
			@RequestParam(required = false, value = "subrole") Integer subrole,
			@NotNull @RequestParam(value = "appid") Integer appid, @RequestHeader Map<String, String> headers) {

		ApiResponse<List<AccessResponse>> apiResponse = new ApiResponse<List<AccessResponse>>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(),
				this.accessService.ViewAccessByshType(stakeholdertype, subrole, appid));

		return ResponseEntity.ok(apiResponse);

	}

	// To update permissions to activities if activity present else will create new
	@PutMapping(value = "/access-user", consumes = "application/json")
	public ResponseEntity<ApiResponse<String>> updateAccess(@RequestBody @Valid UserUpdateRequest req,
			@RequestHeader Map<String, String> headers) {

		ApiResponse<String> apiResponse = new ApiResponse<String>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.accessService.updateUserAccess(req));

		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping(value = "/access-headers")
	public ResponseEntity<ApiResponse<List<AccessHeader>>> accessHeader(@RequestHeader Map<String, String> headers) {

		ApiResponse<List<AccessHeader>> apiResponse = new ApiResponse<List<AccessHeader>>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.accessService.accessHeader());

		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping(value = "/access-pages")
	public ResponseEntity<ApiResponse<List<AccessPages>>> accessPages(@RequestParam Integer stakeholdeCategoryRID,
			@RequestHeader Map<String, String> headers) {

		ApiResponse<List<AccessPages>> apiResponse = new ApiResponse<List<AccessPages>>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.accessService.accessPages(stakeholdeCategoryRID));

		return ResponseEntity.ok(apiResponse);

	}

}
