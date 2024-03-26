package sa.tabadul.useraccessmanagement.api.advice.v1.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import sa.tabadul.useraccessmanagement.common.filters.Filter;
import sa.tabadul.useraccessmanagement.common.models.request.ApproveRejectRequest;
import sa.tabadul.useraccessmanagement.common.models.request.InvoiceCreateRequest;
import sa.tabadul.useraccessmanagement.common.models.request.LicenceRequest;
import sa.tabadul.useraccessmanagement.common.models.request.LicenseApproveRejectRequest;
import sa.tabadul.useraccessmanagement.common.models.request.LicenseFilter;
import sa.tabadul.useraccessmanagement.common.models.request.OrganizationApproveReject;
import sa.tabadul.useraccessmanagement.common.models.request.Pagination;
import sa.tabadul.useraccessmanagement.common.models.request.PortPermitFilter;
import sa.tabadul.useraccessmanagement.common.models.request.PortPermitListRequest;
import sa.tabadul.useraccessmanagement.common.models.request.ResetPasswordRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserByIdsRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserListRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserPcRequset;
import sa.tabadul.useraccessmanagement.common.models.request.UserPortPermitFilter;
import sa.tabadul.useraccessmanagement.common.models.request.UserRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserValidateRequest;
import sa.tabadul.useraccessmanagement.common.models.response.ApiResponse;
import sa.tabadul.useraccessmanagement.common.models.response.CreateInvoiceReturn;
import sa.tabadul.useraccessmanagement.common.models.response.LicenseApplicationResponse;
import sa.tabadul.useraccessmanagement.common.models.response.LicenseApproveRejectResponse;
import sa.tabadul.useraccessmanagement.common.models.response.LicenseCrnViewResponse;
import sa.tabadul.useraccessmanagement.common.models.response.OrganizationUserCreationResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PaginationResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PortPermitResponseDTO;
import sa.tabadul.useraccessmanagement.common.models.response.ResponseEnvelope;
import sa.tabadul.useraccessmanagement.common.models.response.ReturnDetails;
import sa.tabadul.useraccessmanagement.common.models.response.UserAccessResponse;
import sa.tabadul.useraccessmanagement.common.models.response.UserBranchDetailsResponse;
import sa.tabadul.useraccessmanagement.common.models.response.UserBranchViewResponse;
import sa.tabadul.useraccessmanagement.common.models.response.UserListResponse;
import sa.tabadul.useraccessmanagement.common.models.response.UserOrganisationListResponse;
import sa.tabadul.useraccessmanagement.common.models.response.UserPcListResponse;
import sa.tabadul.useraccessmanagement.common.models.response.UsersListResponse;
import sa.tabadul.useraccessmanagement.domain.LicenseOrganization;
import sa.tabadul.useraccessmanagement.domain.PortMaster;
import sa.tabadul.useraccessmanagement.domain.PortPermitApplication;
import sa.tabadul.useraccessmanagement.domain.UserBranchRegistration;
import sa.tabadul.useraccessmanagement.domain.UserOrganization;
import sa.tabadul.useraccessmanagement.domain.Users;
import sa.tabadul.useraccessmanagement.service.FasahService;
import sa.tabadul.useraccessmanagement.service.LicenseService;
import sa.tabadul.useraccessmanagement.service.UserBranchService;
import sa.tabadul.useraccessmanagement.service.UserManagementService;
import sa.tabadul.useraccessmanagement.service.UserOrganizationService;
import sa.tabadul.useraccessmanagement.service.UserPortPermitService;

@RestController
@RequestMapping(value = "/users")

public class UserManagementController {

	@Autowired
	private UserManagementService serviceImplementation;

	@Autowired
	private UserOrganizationService userOrganizationService;

	@Autowired
	private UserPortPermitService userPortPermitService;

	@Autowired
	private UserBranchService userBranchService;

	@Autowired
	private LicenseService licenseService;

	@Autowired
	private FasahService fasahService;

	// To display list of all port admins
	@PostMapping(value = "/port-admins")
	public PaginationResponse portadmin(@RequestBody Pagination<Filter> pageRequest,
			@RequestHeader Map<String, String> headers) {

		PaginationResponse resp = this.serviceImplementation.getAllportAdmin(pageRequest);

		return resp;
	}

	@GetMapping(value = "/user")
	public ResponseEntity<ApiResponse<Users>> userbyuserid(@NotNull @RequestParam String userid,
			@RequestHeader Map<String, String> headers) {

		ApiResponse<Users> apiResponse = new ApiResponse<Users>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
				this.serviceImplementation.getbyIAMid(userid));

		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping(value = "/new-user")
	public ResponseEntity<ApiResponse<String>> savePortadmin(@RequestBody Users user,
			@RequestHeader Map<String, String> headers) {

		ApiResponse<String> apiResponse = new ApiResponse<String>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.serviceImplementation.createUserPMIS(user, headers.get("authorization")));

		return ResponseEntity.ok(apiResponse);

	}

	@PostMapping(value = "/user-role")
	public ResponseEntity<ApiResponse<Users>> portuser(@RequestBody Users user,
			@RequestHeader Map<String, String> headers) {

		ApiResponse<Users> apiResponse = new ApiResponse<Users>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
				this.serviceImplementation.userUpdate(user));

		return ResponseEntity.ok(apiResponse);

	}

	@PostMapping(value = "/port-user")
	public PaginationResponse userByPortId(@NotNull @RequestParam Integer portid, @RequestBody Pagination<Filter> page,
			@RequestHeader Map<String, String> headers) {

		return this.serviceImplementation.pmisgetuserbyportcode(portid, page);

	}

	@PutMapping(value = "/activate-deactivate")
	public ResponseEntity<ResponseEnvelope<String>> activationDeactivation(@RequestBody Users adminPMIS,
			@RequestHeader Map<String, String> headers) {

		ResponseEnvelope<String> apiResponse =  this.serviceImplementation.activateDeactivation(adminPMIS,headers.get("authorization"));

		return new ResponseEntity<ResponseEnvelope<String>>(apiResponse,HttpStatus.valueOf(apiResponse.getResponseCode()));

	}

	@GetMapping(value = "/user-acess")
	public ResponseEntity<ApiResponse<UserAccessResponse>> userAccessDetails(@NotNull @RequestParam String uuid,
			@RequestHeader Map<String, String> headers) {

		ApiResponse<UserAccessResponse> apiResponse = new ApiResponse<UserAccessResponse>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.serviceImplementation.getByuuid(uuid));

		return ResponseEntity.ok(apiResponse);

	}

	@GetMapping(value = "/unassigned-ports")
	public ResponseEntity<ApiResponse<List<PortMaster>>> ports(@RequestHeader Map<String, String> headers) {

		ApiResponse<List<PortMaster>> apiResponse = new ApiResponse<List<PortMaster>>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.serviceImplementation.getPorts());

		return ResponseEntity.ok(apiResponse);

	}

	@PostMapping(value = "/branchwise-users")
	public ResponseEntity<ApiResponse<List<UserListResponse>>> branchUsers(@RequestBody UserListRequest user,
			@RequestHeader Map<String, String> headers) {

		List<UserListResponse> allUser = serviceImplementation.getUserList(user);
		ApiResponse<List<UserListResponse>> apiResponse = new ApiResponse<List<UserListResponse>>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), allUser);

		return ResponseEntity.ok(apiResponse);

	}
	
	@PostMapping(value = "/port-contractor")
	public ResponseEntity<ResponseEnvelope<List<UserPcListResponse>>> pcUsers(@RequestBody UserPcRequset user,
			@RequestHeader Map<String, String> headers) {

		List<UserPcListResponse> allUser = serviceImplementation.getPcUserList(user);
		ResponseEnvelope<List<UserPcListResponse>> apiResponse = new ResponseEnvelope<List<UserPcListResponse>>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), allUser);

		return ResponseEntity.ok(apiResponse);

	}


	@PostMapping(value = "/validate-user")
	public ResponseEntity<ApiResponse<String>> validateUser(@RequestBody UserValidateRequest validateuser,
			@RequestHeader Map<String, String> headers) {

		return ResponseEntity.ok(this.serviceImplementation.getUserExist(validateuser));

	}

	@PostMapping(value = "/users-ids")
	public ResponseEntity<ApiResponse<List<UsersListResponse>>> userList(@RequestBody UserByIdsRequest userIds) {

		ApiResponse<List<UsersListResponse>> apiResponse = new ApiResponse<List<UsersListResponse>>(
				HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.serviceImplementation.userByIds(userIds));

		return ResponseEntity.ok(apiResponse);

	}

	@PostMapping(value = "/profile")
	public ResponseEntity<ApiResponse<Users>> updateUser(@RequestBody Users user) {
		ApiResponse<Users> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
				serviceImplementation.updateUser(user));
		return ResponseEntity.ok(apiResponse);
	}
	
	@PostMapping(value = "/user-create")
	public ResponseEntity<ResponseEnvelope<String>> fasahUserCreation(@RequestBody UserRequest userRequest) {
		
		ResponseEnvelope<String> apiResponse = this.fasahService.fasahUserCreation(userRequest);
	
		
		
		return new ResponseEntity<ResponseEnvelope<String>>(apiResponse, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/reset-password")
	public ResponseEntity<ResponseEnvelope<String>> resetPassword(@RequestBody ResetPasswordRequest passwordRequest,
			@RequestHeader Map<String, String> headers) {
		
		ResponseEnvelope<String> apiResponse = this.serviceImplementation.resetPassword(passwordRequest, headers.get("authorization"));
	
		
		
		return new ResponseEntity<ResponseEnvelope<String>>(apiResponse, HttpStatus.valueOf(apiResponse.getResponseCode()));
	}
	

	/*
	 * ==============================================User
	 * Organization=============================================================
	 */

	@PostMapping(value = "/add-update-user-organisation-request")
	public ResponseEntity<ApiResponse<UserOrganization>> addUpdateUserOrganization(
			@Valid @RequestBody UserOrganization userOrganizationRequest) {

		ApiResponse<UserOrganization> apiResponse = new ApiResponse<UserOrganization>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(),
				userOrganizationService.addAndUpdateOrganization(userOrganizationRequest));
		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping(value = "/user-organisation-details-by-crn")
	public ResponseEntity<ApiResponse<UserOrganization>> getUserOrganisationDetails(
			@RequestParam(value = "crn") Long crn) {

		ApiResponse<UserOrganization> apiResponse = new ApiResponse<UserOrganization>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), userOrganizationService.getByCrn(crn));
		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping(value = "/approve-reject-user-organisation-request")
	public ResponseEntity<ApiResponse<OrganizationUserCreationResponse>> approveRejectUserOrganisation(
			@RequestBody OrganizationApproveReject userOrganization, @RequestHeader Map<String, String> headers) {

		ApiResponse<OrganizationUserCreationResponse> apiResponse = new ApiResponse<OrganizationUserCreationResponse>(
				HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), userOrganizationService
						.approveRejectUserOrganizationRequest(userOrganization, headers.get("authorization")));
		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping(value = "/user-organisation-requests")
	public PaginationResponse getUserOrganisationRequests(
			@RequestBody Pagination<UserOrganisationListResponse> userOrganisationPagination,
			@RequestHeader Map<String, String> headers) throws JsonProcessingException {
		PaginationResponse lstData = userOrganizationService.getAllUserOrganisationRequest(userOrganisationPagination);
		return lstData;
	}
	
	
	@PostMapping(value = "/org-by-license")
	public ResponseEntity<ResponseEnvelope<List<Map<String, Object>>>> orgByLicense(@RequestBody Map<String, List<String>> license) {

		ResponseEnvelope<List<Map<String, Object>>> apiResponse = new ResponseEnvelope<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), userOrganizationService.orgByAgencyCode(license.get("licenseCode")));

		return ResponseEntity.ok(apiResponse);

	}
	

	/*
	 * ==============================================User
	 * Branch=============================================================
	 */
	@PostMapping(value = "/add-update-user-branch")
	public ResponseEntity<ApiResponse<UserBranchRegistration>> addUpdateUserBranch(
			@Valid @RequestBody UserBranchRegistration userBranch, @RequestHeader Map<String, String> headers) {
		ApiResponse<UserBranchRegistration> apiResponse = new ApiResponse<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(),
				userBranchService.addUserBranch(userBranch, headers.get("authorization")));
		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping(value = "/user-branch-details-by-branchId")
	public ResponseEntity<ApiResponse<UserBranchDetailsResponse>> getUserBranchDetails(
			@RequestParam(value = "userId") String userId) {
		ApiResponse<UserBranchDetailsResponse> apiResponse = new ApiResponse<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), userBranchService.getBranch(userId));
		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping(value = "/branch-users")
	public PaginationResponse getUserBranch(@RequestBody Pagination<UserBranchViewResponse> userBranchPagination) {
		return userBranchService.getBranchUsers(userBranchPagination);
	}

	@PostMapping(value = "/port-contractor-branch")
	public ResponseEntity<ResponseEnvelope<List<UserPcListResponse>>> branchById(
			@RequestBody List<Integer> branchIds) {

		ResponseEnvelope<List<UserPcListResponse>> apiResponse = new ResponseEnvelope<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.userBranchService.branchByIds(branchIds));

		return ResponseEntity.ok(apiResponse);
	}
	
	
	/*
	 * ==============================================Port
	 * Permit=============================================================
	 */

	@GetMapping(value = "/approved-ports")
	public ResponseEntity<ResponseEnvelope<List<PortMaster>>> approvedPorts(@RequestParam("orgRid") Integer orgRID) {

		ResponseEnvelope<List<PortMaster>> apiResponse = new ResponseEnvelope<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.userPortPermitService.approvedPorts(orgRID));

		return ResponseEntity.ok(apiResponse);
	}

	@GetMapping(value = "/port-permit-crn")
	public ResponseEntity<ResponseEnvelope<PortPermitApplication>> portPermitCrn(@RequestParam("crn") Long crn,
			@RequestHeader Map<String, String> headers) {

		ResponseEnvelope<PortPermitApplication> apiResponse = new ResponseEnvelope<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), this.userPortPermitService.portPermitCrn(crn));

		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping(value = "/port-permits")
	public ResponseEntity<PaginationResponse> portPermit(
			@RequestBody Pagination<PortPermitFilter> pagerequest) {
		return ResponseEntity.ok(userPortPermitService.portPermitRequests(pagerequest));
	}


	
	@PostMapping(value = "/port-permit")
	public ResponseEntity<ResponseEnvelope<PortPermitResponseDTO>> portPermit(
	        @RequestBody PortPermitApplication application) {

	    PortPermitResponseDTO savedApplication = this.userPortPermitService.savePortPermit(application);

	    ResponseEnvelope<PortPermitResponseDTO> apiResponse = new ResponseEnvelope<>(HttpStatus.OK.value(),
	            "Port Permit saved successfully", savedApplication);

	    return ResponseEntity.ok(apiResponse);
	}


	@PostMapping(value = "/approve-reject-port-permit")
	public ResponseEntity<ResponseEnvelope<PortPermitApplication>> approveRejectPortPermit(@RequestBody ApproveRejectRequest permitListRequest,
			@RequestHeader Map<String, String> headers) {

		ResponseEnvelope<PortPermitApplication> apiResponse = this.userPortPermitService.portPermitApproveReject(permitListRequest);

		return ResponseEntity.ok(apiResponse);

	}
	
	@GetMapping(value = "/active-permit-ports")
	public ResponseEntity<ResponseEnvelope<List<PortMaster>>> getPorts() {
		ResponseEnvelope<List<PortMaster>> apiResponse =new ResponseEnvelope<>(HttpStatus.OK.value(),
	            HttpStatus.OK.getReasonPhrase(), userPortPermitService.activePorts());
		return ResponseEntity.ok(apiResponse);
	}
		
	
	// --------------------------------License ---------------------------------

	@PostMapping(value = "/apply-license")
	public ResponseEntity<ResponseEnvelope<LicenseApplicationResponse>> addUpdateLicense(
			@RequestBody LicenceRequest licenseRequest) {
		ResponseEnvelope<LicenseApplicationResponse> apiResponse =licenseService.addAndUpdateLicence(licenseRequest);
		return ResponseEntity.ok(apiResponse);
	}

	@PostMapping(value = "/licenses")
	public ResponseEntity<PaginationResponse> licenses(
			@RequestBody Pagination<LicenseFilter> pagerequest) {


		return ResponseEntity.ok(licenseService.licenseRequests(pagerequest));

	}

	@GetMapping(value = "/eun-details")
	public ResponseEntity<ResponseEnvelope<Object>> eunDetails(@RequestParam Long eun) {

		ResponseEnvelope<Object> apiResponse = new ResponseEnvelope<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), licenseService.eunDetails(eun));

		return ResponseEntity.ok(apiResponse);

	}

	@GetMapping(value = "/license-details-crn")
	public ResponseEntity<ResponseEnvelope<LicenseCrnViewResponse>> licensecrn(@RequestParam Long crn) {

		ResponseEnvelope<LicenseCrnViewResponse> apiResponse = new ResponseEnvelope<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), licenseService.licenseCrnDetails(crn));

		return ResponseEntity.ok(apiResponse);

	}
	
	@PostMapping(value = "/license-approval")
	public ResponseEntity<ResponseEnvelope<LicenseApproveRejectResponse>> licenseApproval(
			@RequestBody LicenseApproveRejectRequest objLicenseApproveRejectRequest) {
		
		ResponseEnvelope<LicenseApproveRejectResponse> apiResponse = this.licenseService.licenseApproveReject(objLicenseApproveRejectRequest);
		return ResponseEntity.ok(apiResponse);

	}
	
	@PostMapping(value = "/sadad-invoice-generation")
	public ResponseEntity<ResponseEnvelope<CreateInvoiceReturn>> licenseSadadInvoice(@RequestBody InvoiceCreateRequest objCreateInvoice) {

		ResponseEnvelope<CreateInvoiceReturn> apiResponse = new ResponseEnvelope<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), licenseService.sadadInvoiceGeneration(objCreateInvoice));

		return ResponseEntity.ok(apiResponse);

	}
	
	@GetMapping(value = "/sadad-status")
	public ResponseEntity<ResponseEnvelope<ReturnDetails>> sadadPaymentStatus(@RequestParam String sadadNumber) {

		ResponseEnvelope<ReturnDetails> apiResponse = new ResponseEnvelope<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), licenseService.sadadPaymentStatusCheck(sadadNumber));

		return ResponseEntity.ok(apiResponse);

	}
	
	
	@GetMapping(value = "/license-details-orgid")
	public ResponseEntity<ResponseEnvelope<LicenseOrganization>> licenseDetails(@RequestParam String orgId) {

		ResponseEnvelope<LicenseOrganization> apiResponse = new ResponseEnvelope<>(HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(), licenseService.licenseNumberDetails(orgId));

		return ResponseEntity.ok(apiResponse);

	}
	
	@PostMapping(value = "/get-otp")
	public ResponseEntity<ApiResponse<String>> getOtp(@RequestBody UserValidateRequest getOtp,
			@RequestHeader Map<String, String> headers) {

		return ResponseEntity.ok(this.serviceImplementation.sendOtp(getOtp));

	}
	
	@PostMapping(value = "/verify-otp")
	public ResponseEntity<ApiResponse<String>> verifyOtp(@RequestBody UserValidateRequest verifyOtp,
			@RequestHeader Map<String, String> headers) {

		return ResponseEntity.ok(this.serviceImplementation.verifyOtp(verifyOtp));

	}
	
	



}
