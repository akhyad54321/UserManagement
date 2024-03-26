package sa.tabadul.useraccessmanagement.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.common.enums.ExceptionMessage;
import sa.tabadul.useraccessmanagement.common.helpers.ApprovalStatusCode;
import sa.tabadul.useraccessmanagement.common.models.request.CodeMasterRequest;
import sa.tabadul.useraccessmanagement.common.models.request.OrganizationApproveReject;
import sa.tabadul.useraccessmanagement.common.models.request.Pagination;
import sa.tabadul.useraccessmanagement.common.models.request.UserByIdsRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserCreationKeyCloak;
import sa.tabadul.useraccessmanagement.common.models.request.UserCredential;
import sa.tabadul.useraccessmanagement.common.models.response.KeyCloakUserDetails;
import sa.tabadul.useraccessmanagement.common.models.response.LocationListResponse;
import sa.tabadul.useraccessmanagement.common.models.response.OrganizationUserCreationResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PaginationResponse;
import sa.tabadul.useraccessmanagement.common.models.response.UserOrganisationListResponse;
import sa.tabadul.useraccessmanagement.common.service.GetStatusNameService;
import sa.tabadul.useraccessmanagement.common.service.UserManagementExternalService;
import sa.tabadul.useraccessmanagement.domain.CodeMaster;
import sa.tabadul.useraccessmanagement.domain.LocationMaster;
import sa.tabadul.useraccessmanagement.domain.RoleMaster;
import sa.tabadul.useraccessmanagement.domain.UserOrganization;
import sa.tabadul.useraccessmanagement.domain.UserOrganizationApproval;
import sa.tabadul.useraccessmanagement.domain.UserRole;
import sa.tabadul.useraccessmanagement.domain.Users;
import sa.tabadul.useraccessmanagement.exception.BusinessException;
import sa.tabadul.useraccessmanagement.repository.UserOrganizationApprovalRepository;
import sa.tabadul.useraccessmanagement.repository.UserOrganizationRepository;
import sa.tabadul.useraccessmanagement.repository.UserRepository;


@Service
public class UserOrganizationService {

	@Autowired
	private UserOrganizationRepository userOrganizationRepository;

	@Autowired
	private UserOrganizationApprovalRepository userOrganizationApprovalRepository;

	@Autowired
	private UserManagementService userManagementService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserManagementExternalService restService;

	@Autowired
	private ApprovalStatusCode approvalStatusCode;

	@Autowired
	private RoleMasterService roleMasterService;

	private static final Logger logger = LogManager.getLogger(UserOrganizationService.class);

	private ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());

	@Autowired
	private GetStatusNameService getStatusNameService;

	@Value("${defaultSecret}")
	private String UserSecret;

	public UserOrganization addAndUpdateOrganization(UserOrganization userOrganization) {
		if (userOrganization.getId() == null) {
			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(CommonCodes.CRN_FORMAT);
			String randomOrgId =  generateOrgId();
			userOrganization.setCrn(Long.parseLong(myFormatObj.format(myDateObj)));
			userOrganization.setOrgID(randomOrgId);
		} else {
			UserOrganization userOrganizationDetails = userOrganizationRepository.findByCrn(userOrganization.getCrn());
			if (userOrganizationDetails != null) {
				userOrganization.setCreatedBy(userOrganizationDetails.getCreatedBy());
				userOrganization.setCreatedDate(userOrganizationDetails.getCreatedDate());
				userOrganization.setUpdatedDate(LocalDateTime.now());
			}
		}

		UserOrganization objUserOrganization = this.userOrganizationRepository.save(userOrganization);

		if (objUserOrganization != null) {
			UserOrganizationApproval objUserOrganizationApproval = new UserOrganizationApproval();
			objUserOrganizationApproval.setApprovalStatusRid(userOrganization.getApprovalStatusRid());
			objUserOrganizationApproval.setUserOrganizationId(objUserOrganization.getId());
			objUserOrganizationApproval.setActionById(userOrganization.getActionById());
			objUserOrganizationApproval.setRemark(userOrganization.getRemark());
			objUserOrganizationApproval.setIsActive(userOrganization.getIsActive());
			objUserOrganizationApproval.setCreatedDate(LocalDateTime.now());
			objUserOrganizationApproval.setCreatedBy(userOrganization.getUpdatedBy());
			UserOrganizationApproval userOrganizationApproval = userOrganizationApprovalRepository
					.save(objUserOrganizationApproval);

		} else {
			throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		}

		return objUserOrganization;
	}

	public static String generateOrgId() {
        String uuid = UUID.randomUUID().toString().replace("-", ""); 
        return uuid.substring(0, 16);
    }

	public UserOrganization getByCrn(Long crn) {
		UserOrganization userOrganizationDetails = userOrganizationRepository.findByCrn(crn);
		if (userOrganizationDetails == null) {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.CRN_NOT_FOUND.getValue() + crn);
		}
		String locationMasterDetails = restService.getAllLocation("CUSTOMS_CITY");
		CodeMasterRequest objCodeMasterRequest = new CodeMasterRequest();
		objCodeMasterRequest.getKey1().add(CommonCodes.DOCUMENT_TYPE);
		objCodeMasterRequest.getKey1().add(CommonCodes.REGISTRATION_THROUGH);
		objCodeMasterRequest.getKey1().add(CommonCodes.REGISTRATION_TYPE);
		List<CodeMaster> lstCodeMaster = restService.getByKeysCodeMaster(objCodeMasterRequest);
		CodeMaster regThroughId = new CodeMaster();
		CodeMaster regTypeId = new CodeMaster();
		CodeMaster docTypeId = new CodeMaster();
		ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		LocationListResponse locationMasterResponse = new LocationListResponse();
		try {
			if (locationMasterDetails != null) {
				locationMasterResponse = objectMapper.readValue(locationMasterDetails, LocationListResponse.class);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if (lstCodeMaster != null) {
			regThroughId = lstCodeMaster.stream()
					.filter(x -> Objects.equals(x.getId(), userOrganizationDetails.getRegistrationThroughRid()))
					.findAny().orElse(new CodeMaster());
			regTypeId = lstCodeMaster.stream()
					.filter(x -> Objects.equals(x.getId(), userOrganizationDetails.getRegistrationTypeRid())).findAny()
					.orElse(new CodeMaster());
			docTypeId = lstCodeMaster.stream()
					.filter(x -> Objects.equals(x.getId(), userOrganizationDetails.getDocumentTypeRid())).findAny()
					.orElse(new CodeMaster());

		}
		List<LocationMaster> lstLocationMaster = locationMasterResponse.getData();
		LocationMaster objLocationMasterCity = new LocationMaster();
		if (lstLocationMaster.size() > 0) {
			objLocationMasterCity = lstLocationMaster.stream()
					.filter(x -> Objects.equals(x.getId(), userOrganizationDetails.getCityRid())).findAny()
					.orElse(new LocationMaster());
		}
		RoleMaster roleMaster = roleMasterService.getBy(userOrganizationDetails.getStakeholdertypeRid(), 1);
		userOrganizationDetails.setRoleNameEnglish(roleMaster.getRoleNameEnglish());
		userOrganizationDetails.setRoleNameArabic(roleMaster.getRoleNameArabic());
		userOrganizationDetails.setRoleDescEnglish(roleMaster.getRoleDescriptionEnglish());
		userOrganizationDetails.setRoleDescArabic(roleMaster.getRoleDescriptionArabic());
		userOrganizationDetails.setCityDescEnglish(objLocationMasterCity.getLocationDescriptionEnglish());
		userOrganizationDetails.setCityDescArabic(objLocationMasterCity.getLocationDescriptionArabic());
		userOrganizationDetails.setRegistrationThroughDescEnglish(regThroughId.getCodeDescriptionEnglish());
		userOrganizationDetails.setRegistrationThroughDescArabic(regThroughId.getCodeDescriptionArabic());
		userOrganizationDetails.setRegistrationTypeDescEnglish(regTypeId.getCodeDescriptionEnglish());
		userOrganizationDetails.setRegistrationTypeDescArabic(regTypeId.getCodeDescriptionArabic());
		userOrganizationDetails.setDocumentTypeDescEnglish(docTypeId.getCodeDescriptionEnglish());
		userOrganizationDetails.setDocumentTypeDescArabic(docTypeId.getCodeDescriptionArabic());
		List<UserOrganizationApproval> userOrganizationApprovals = userOrganizationApprovalRepository
				.findByUserOrganizationId(userOrganizationDetails.getId());
		List<String> lstUserIds = userOrganizationApprovals.stream().map(approval -> (approval.getActionById()))
				.collect(Collectors.toList());
		UserByIdsRequest userByIdsRequest = new UserByIdsRequest();
		userByIdsRequest.setIAmUserIds(lstUserIds);
		List<Users> lstUsers = userRepository.findByIamUserIDIn(lstUserIds);
		for (UserOrganizationApproval approval : userOrganizationApprovals) {
			if (!lstUsers.isEmpty()) {
				Users objUsers = lstUsers.stream().filter(x -> x.getIamUserID().equals(approval.getActionById()))
						.findFirst().orElse(new Users());

				if (objUsers != null) {
					approval.setUserName(objUsers.getEmployeeName());
				}
			}
			approval.setStatus(getStatusNameService.getStatusName(approval.getApprovalStatusRid()));
		}
		userOrganizationDetails.setApprovals(userOrganizationApprovals);
		return userOrganizationDetails;
	}

	public OrganizationUserCreationResponse approveRejectUserOrganizationRequest(
			OrganizationApproveReject userOrganization, String token) {
		UserOrganization userOrganizationDetails = userOrganizationRepository.findByCrn(userOrganization.getCrn());
		if (userOrganizationDetails != null) {
			userOrganizationDetails.setUpdatedDate(LocalDateTime.now());
			userOrganizationDetails.setApprovalStatusRid(userOrganization.getApprovalStatusRid());
			userOrganizationDetails.setUpdatedBy(userOrganization.getUpdatedBy());

			if (userOrganization.getApprovalStatusRid().equals(CommonCodes.APPROVED_CODE)) {
				UserCreationKeyCloak objUserCreationKeyCloak = new UserCreationKeyCloak();
				objUserCreationKeyCloak.setUsername(userOrganizationDetails.getUserId());
				objUserCreationKeyCloak.setEmail(userOrganizationDetails.getEmailId());
				objUserCreationKeyCloak.setFirstName(userOrganizationDetails.getOrgName());
				objUserCreationKeyCloak.setLastName(userOrganizationDetails.getOrgName());
				objUserCreationKeyCloak.setEmailVerified(false);
				objUserCreationKeyCloak.setEnabled(true);
				objUserCreationKeyCloak.getGroups().add(CommonCodes.USRCRE);
				UserCredential objUserCredential = new UserCredential();
				objUserCredential.setTemporary(false);
				objUserCredential.setType(CommonCodes.PASSWD);
				objUserCredential.setValue(UserSecret);

				objUserCreationKeyCloak.credentials.add(objUserCredential);

				String requestBody = null;
				try {
					objectMapper.setSerializationInclusion(Include.NON_EMPTY);
					requestBody = objectMapper.writeValueAsString(objUserCreationKeyCloak);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				}

				String strResponse = this.restService.userCreation(requestBody, token);

				if (strResponse.contains(HttpStatus.UNAUTHORIZED.getReasonPhrase())) {
					throw new BusinessException(HttpStatus.UNAUTHORIZED.value(),
							HttpStatus.UNAUTHORIZED.getReasonPhrase());
				} else if (strResponse.contains(CommonCodes.ERR_STR)) {
					Map<String, String> errorMessage = new HashMap<String, String>();
					try {
						errorMessage = objectMapper.readValue(strResponse, new TypeReference<Map<String, String>>() {
						});
					} catch (JsonProcessingException e) {

					}
					throw new BusinessException(HttpStatus.CONFLICT.value(), errorMessage.get(CommonCodes.ERR_STR));
				}

				if (strResponse.equals("OK")) {
					KeyCloakUserDetails[] objUserDetails = new KeyCloakUserDetails[3];
					String strUserDetails = this.restService.validateUser(objUserCreationKeyCloak.getEmail(), token);

					try {
						objUserDetails = objectMapper.readValue(strUserDetails, KeyCloakUserDetails[].class);
					} catch (JsonProcessingException e) {

					}
					KeyCloakUserDetails objUserDetail = objUserDetails[0];
										
					KeyCloakUserDetails getUserPcs = restService.userGetPcs(objUserDetail.getId(), token);
					objectMapper = new ObjectMapper();

					Map<String, Object> attributes = new HashMap<>();
					List<String> mobileNumbers = new ArrayList<>();
					mobileNumbers.add(String.valueOf(userOrganizationDetails.getMobileNumber())); 
					attributes.put("mobile_number", mobileNumbers);

					getUserPcs.setAttributes(objectMapper.convertValue(attributes, JsonNode.class));
					
					restService.userAddPcs(getUserPcs, objUserDetail.getId(), token);

					Users objUser = new Users();
					objUser.setIamUserID(objUserDetail.getId());
					objUser.setUserId(objUserDetail.getUsername());
					objUser.setEmail(objUserDetail.getEmail());
					objUser.setEmployeeName(userOrganizationDetails.getOrgName());
					objUser.setOrgID(userOrganizationDetails.getOrgID());
					objUser.setAppId(CommonCodes.PCS);
					objUser.setBuildingNumber(userOrganizationDetails.getBuildingNo());
					objUser.setAddress(userOrganizationDetails.getAddress());
					//objUser.setCity(userOrganizationDetails.getCityRid());
					objUser.setUnitNumber(userOrganizationDetails.getUnitNo());
					objUser.setZipCode(userOrganizationDetails.getZipCode().intValue());
					objUser.setStreet(userOrganizationDetails.getStreet());
					objUser.setNationalId(userOrganizationDetails.getNationalId());
					objUser.setCustomNo(userOrganizationDetails.getCustomNo());
					objUser.setLicenceNo(userOrganizationDetails.getLicenceNo());
					objUser.setDistrict(userOrganizationDetails.getDistrictRid());
					objUser.setUserType(CommonCodes.ORG_ADMIN);
					objUser.setDefaultGroupTypeRID(0);
					objUser.setDepartmentRID(0);
					objUser.setDesignationRID(0);
					objUser.setPortRID(0);
					objUser.setEmployeeCode(objUserDetail.getCreatedTimestamp().toString());
					objUser.setPhoneNo(Long.parseLong(userOrganizationDetails.getAdditionalMobile()));
					List<UserRole> stakeholders = new ArrayList<>();
					UserRole objRole = new UserRole();
					stakeholders.add(objRole);
					objRole.setStakeholderTypeId(userOrganizationDetails.getStakeholdertypeRid());
					objRole.setUsers(objUser);
					objUser.setStakeHolders(stakeholders);

					objUser.setIsActive(true);

					if (this.userManagementService.createUser(objUser) != null)

					{
						userOrganizationRepository.save(userOrganizationDetails);
						UserOrganizationApproval objUserOrganizationApproval = new UserOrganizationApproval();
						objUserOrganizationApproval.setApprovalStatusRid(userOrganization.getApprovalStatusRid());
						objUserOrganizationApproval.setUserOrganizationId(userOrganizationDetails.getId());
						objUserOrganizationApproval.setActionById(userOrganization.getActionById());
						objUserOrganizationApproval.setRemark(userOrganization.getRemark());
						objUserOrganizationApproval.setIsActive(userOrganization.getIsActive());
						objUserOrganizationApproval.setCreatedDate(LocalDateTime.now());
						objUserOrganizationApproval.setCreatedBy(userOrganization.getUpdatedBy());
						UserOrganizationApproval userOrganizationApproval = userOrganizationApprovalRepository
								.save(objUserOrganizationApproval);
						if (userOrganizationApproval != null) {

							OrganizationUserCreationResponse objOrgUserCreationResponse = new OrganizationUserCreationResponse();

							objOrgUserCreationResponse.setCrn(userOrganizationDetails.getCrn());
							objOrgUserCreationResponse.setIAmUserID(objUserDetail.getId());
							objOrgUserCreationResponse.setStatus(
									approvalStatusCode.getStatusName(userOrganizationApproval.getApprovalStatusRid()));
							objOrgUserCreationResponse.setIsUserCreated(true);
							return objOrgUserCreationResponse;
						}

					}

				} else {
					throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase().toString());

				}

			} else {
				userOrganizationRepository.save(userOrganizationDetails);
				UserOrganizationApproval objUserOrganizationApproval = new UserOrganizationApproval();
				objUserOrganizationApproval.setApprovalStatusRid(userOrganization.getApprovalStatusRid());
				objUserOrganizationApproval.setUserOrganizationId(userOrganizationDetails.getId());
				objUserOrganizationApproval.setActionById(userOrganization.getActionById());
				objUserOrganizationApproval.setRemark(userOrganization.getRemark());
				objUserOrganizationApproval.setIsActive(userOrganization.getIsActive());
				objUserOrganizationApproval.setCreatedDate(LocalDateTime.now());
				objUserOrganizationApproval.setCreatedBy(userOrganization.getUpdatedBy());
				UserOrganizationApproval userOrganizationApproval = userOrganizationApprovalRepository
						.save(objUserOrganizationApproval);
				if (userOrganizationApproval != null) {

					OrganizationUserCreationResponse objOrgUserCreationResponse = new OrganizationUserCreationResponse();

					objOrgUserCreationResponse.setCrn(userOrganizationDetails.getCrn());
					objOrgUserCreationResponse.setIAmUserID(null);
					objOrgUserCreationResponse.setStatus(
							approvalStatusCode.getStatusName(userOrganizationApproval.getApprovalStatusRid()));
					objOrgUserCreationResponse.setIsUserCreated(false);
					return objOrgUserCreationResponse;
				}
			}

		} else {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(),
					ExceptionMessage.CRN_NOT_FOUND.getValue() + userOrganization.getCrn());
		}
		OrganizationUserCreationResponse objOrgUserCreationResponse = new OrganizationUserCreationResponse();

		objOrgUserCreationResponse.setCrn(userOrganizationDetails.getCrn());
		objOrgUserCreationResponse.setIsUserCreated(false);
		objOrgUserCreationResponse.setStatus(approvalStatusCode.getStatusName(0));

		return objOrgUserCreationResponse;
	}

	public PaginationResponse getAllUserOrganisationRequest(Pagination<UserOrganisationListResponse> pagination) {
		List<UserOrganization> lstUserOrganization = userOrganizationRepository.findAll();
		List<RoleMaster> lstRoleMaster = roleMasterService.get(1);
		List<UserOrganisationListResponse> lstResponse = new ArrayList<>();
		for (UserOrganization userOrganization : lstUserOrganization) {
			UserOrganisationListResponse objUserOrganisationListResponse = new UserOrganisationListResponse();
			objUserOrganisationListResponse.setCrn(userOrganization.getCrn());
			objUserOrganisationListResponse.setCreatedDate(userOrganization.getCreatedDate());
			if (userOrganization.getUpdatedDate() != null) {
				objUserOrganisationListResponse.setUpdatedDate(userOrganization.getUpdatedDate());
			}
			objUserOrganisationListResponse.setNationalId(userOrganization.getNationalId());
			objUserOrganisationListResponse.setOrgName(userOrganization.getOrgName());
			objUserOrganisationListResponse.setUserId(userOrganization.getUserId());
			objUserOrganisationListResponse
					.setStatus(getStatusNameService.getStatusName(userOrganization.getApprovalStatusRid()));
			if (!lstRoleMaster.isEmpty()) {
				RoleMaster objRoleMaster = lstRoleMaster.stream()
						.filter(x -> x.getId().equals(userOrganization.getStakeholdertypeRid())).findAny()
						.orElse(new RoleMaster());
				if (objRoleMaster != null) {
					objUserOrganisationListResponse.setRoleNameEnglish(objRoleMaster.getRoleNameEnglish());
					objUserOrganisationListResponse.setRoleNameArabic(objRoleMaster.getRoleNameArabic());
				}
			}
			lstResponse.add(objUserOrganisationListResponse);
		}
		if (pagination.getSearch() != null && !pagination.getSearch().isEmpty()) {
			lstResponse = lstResponse.stream().filter(
					obj -> (obj.getCrn() != null && Long.toString(obj.getCrn()).contains(pagination.getSearch()))
							|| (obj.getNationalId() != null && obj.getNationalId().contains(pagination.getSearch()))
							|| (obj.getOrgName() != null && obj.getOrgName().contains(pagination.getSearch()))
							|| (obj.getUserId() != null && obj.getUserId().contains(pagination.getSearch()))
							|| (obj.getRoleNameEnglish() != null
									&& obj.getRoleNameEnglish().contains(pagination.getSearch()))
							|| (obj.getStatus() != null && obj.getStatus().contains(pagination.getSearch()))
							|| (obj.getRoleNameArabic() != null
									&& obj.getRoleNameArabic().contains(pagination.getSearch())))
					.collect(Collectors.toList());
		}

		if (pagination.getFilter() != null) {
			UserOrganisationListResponse filter = pagination.getFilter();
			if (filter.getCrn() != null) {
				lstResponse = lstResponse.stream().filter(obj -> obj.getCrn().equals(filter.getCrn()))
						.collect(Collectors.toList());
			}
			if (!"-".equals(filter.getOrgName())) {
				lstResponse = lstResponse.stream().filter(obj -> obj.getOrgName().equals(filter.getOrgName()))
						.collect(Collectors.toList());
			}
			if (!"-".equals(filter.getRoleNameEnglish())) {
				lstResponse = lstResponse.stream()
						.filter(obj -> obj.getRoleNameEnglish().equals(filter.getRoleNameEnglish()))
						.collect(Collectors.toList());
			}
			if (!"-".equals(filter.getRoleNameArabic())) {
				lstResponse = lstResponse.stream()
						.filter(obj -> obj.getRoleNameArabic().equals(filter.getRoleNameArabic()))
						.collect(Collectors.toList());
			}
			if (!"-".equals(filter.getNationalId())) {
				lstResponse = lstResponse.stream().filter(obj -> obj.getNationalId().equals(filter.getNationalId()))
						.collect(Collectors.toList());
			}
			if (!"-".equals(filter.getStatus())) {
				lstResponse = lstResponse.stream().filter(obj -> obj.getStatus().equals(filter.getStatus()))
						.collect(Collectors.toList());
			}
			if (!"-".equals(filter.getUserId())) {
				lstResponse = lstResponse.stream().filter(obj -> obj.getUserId().equals(filter.getUserId()))
						.collect(Collectors.toList());
			}
			if (filter.getFromDate() != null) {
				lstResponse = lstResponse.stream().filter(obj -> obj.getCreatedDate().toLocalDate().isAfter(filter.getFromDate())
						|| obj.getCreatedDate().toLocalDate().isEqual(filter.getFromDate())).collect(Collectors.toList());

			}
			if (filter.getToDate() != null) {
				lstResponse = lstResponse.stream().filter(obj -> obj.getCreatedDate().toLocalDate().isBefore(filter.getToDate())
						|| obj.getCreatedDate().toLocalDate().isEqual(filter.getToDate())).collect(Collectors.toList());
			}
		}
		String sort = pagination.getSort();
		if (pagination.getSortDir().equalsIgnoreCase(CommonCodes.ASCENDING)) {

			if (sort.equalsIgnoreCase(CommonCodes.CRN)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparingLong(UserOrganisationListResponse::getCrn))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.ORG_NAME)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getOrgName))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.ROLE_NAME_ENGLISH)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getRoleNameEnglish))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.ROLE_NAME_ARABIC)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getRoleNameArabic))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.NATIONAL_ID)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getNationalId))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.STATUS)) {
				lstResponse = lstResponse.stream().sorted(Comparator.comparing(UserOrganisationListResponse::getStatus))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.USER_ID)) {
				lstResponse = lstResponse.stream().sorted(Comparator.comparing(UserOrganisationListResponse::getUserId))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.UPDATED_DATE)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getUpdatedDate,Comparator.nullsLast(Comparator.naturalOrder())))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.CREATED_DATE)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getCreatedDate,Comparator.nullsLast(Comparator.naturalOrder())))
						.collect(Collectors.toList());
			}

		} else {
			if (sort.equalsIgnoreCase(CommonCodes.CRN)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparingLong(UserOrganisationListResponse::getCrn).reversed())
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.ORG_NAME)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getOrgName).reversed())
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.ROLE_NAME_ENGLISH)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getRoleNameEnglish).reversed())
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.ROLE_NAME_ARABIC)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getRoleNameArabic).reversed())
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.NATIONAL_ID)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getNationalId).reversed())
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.STATUS)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getStatus).reversed())
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.USER_ID)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getUserId).reversed())
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.UPDATED_DATE)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getUpdatedDate ,Comparator.nullsLast(Comparator.reverseOrder())))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.CREATED_DATE)) {
				lstResponse = lstResponse.stream()
						.sorted(Comparator.comparing(UserOrganisationListResponse::getCreatedDate,Comparator.nullsLast(Comparator.reverseOrder())))
						.collect(Collectors.toList());
			}
		}
		PageRequest pageable = PageRequest.of(pagination.getPage(), pagination.getLength());
		Long totalRecord = (long) lstUserOrganization.size();
		Long totalRecordFiltered = (long) lstResponse.size();

		lstResponse = lstResponse.stream().skip(pageable.getOffset()).limit(pageable.getPageSize())
				.collect(Collectors.toList());

		return new PaginationResponse(totalRecord, totalRecordFiltered, lstResponse);
	}
	
	
	
	
	
	
	
	public List<Map<String, Object>> orgByAgencyCode(List<String> licNumber)
	{
		
		List<UserOrganization> lstUserOrganizations = this.userOrganizationRepository.findByLicenceNoIn(licNumber);
		if(!lstUserOrganizations.isEmpty())
		{
			
			List<Map<String, Object>> lstOrg = new ArrayList<>();
			
			for(String strlicNumber : licNumber)
			{
				UserOrganization objOrg = lstUserOrganizations.stream().filter(x->x.getLicenceNo().equals(strlicNumber)).findFirst().orElse(null);
				
				
				
				if(objOrg!=null)
				{
					Map<String, Object> objOrgData = new HashMap<>();
					objOrgData.put("licenseNo", strlicNumber);
					objOrgData.put("orgId", objOrg.getOrgID());
					objOrgData.put("orgNameEng", objOrg.getOrgName());
					objOrgData.put("orgNameArabic", objOrg.getOrgNameArabic());
					objOrgData.put("orgRid", objOrg.getId());
					objOrgData.put("isExists",Boolean.TRUE);
					lstOrg.add(objOrgData);
				}
				else
				{
					Map<String, Object> objOrgData = new HashMap<>();
					objOrgData.put("licenseNo", strlicNumber);
					objOrgData.put("orgId",null);
					objOrgData.put("orgNameEng",null);
					objOrgData.put("orgNameArabic",null);
					objOrgData.put("orgRid",null);
					objOrgData.put("isExists", Boolean.FALSE);
					lstOrg.add(objOrgData);
				}
		
			}
			
			return lstOrg;
		}
		else
		{
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "License number not found");
		}
		
		

	}
	
}
