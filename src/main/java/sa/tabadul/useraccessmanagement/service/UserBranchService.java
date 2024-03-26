package sa.tabadul.useraccessmanagement.service;

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

import sa.tabadul.useraccessmanagement.common.configs.PropertiesConfig;
import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.common.enums.ExceptionMessage;
import sa.tabadul.useraccessmanagement.common.models.request.CodeMasterRequest;
import sa.tabadul.useraccessmanagement.common.models.request.Pagination;
import sa.tabadul.useraccessmanagement.common.models.request.UserBranchKeys;
import sa.tabadul.useraccessmanagement.common.models.request.UserCreationKeyCloak;
import sa.tabadul.useraccessmanagement.common.models.request.UserCredential;
import sa.tabadul.useraccessmanagement.common.models.response.KeyCloakUserDetails;
import sa.tabadul.useraccessmanagement.common.models.response.LocationListResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PaginationResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PortCodeResponse;
import sa.tabadul.useraccessmanagement.common.models.response.UserBranchDetailsResponse;
import sa.tabadul.useraccessmanagement.common.models.response.UserBranchViewResponse;
import sa.tabadul.useraccessmanagement.common.models.response.UserPcListResponse;
import sa.tabadul.useraccessmanagement.common.service.UserManagementExternalService;
import sa.tabadul.useraccessmanagement.common.service.UserStatusService;
import sa.tabadul.useraccessmanagement.domain.CodeMaster;
import sa.tabadul.useraccessmanagement.domain.LocationMaster;
import sa.tabadul.useraccessmanagement.domain.PortMaster;
import sa.tabadul.useraccessmanagement.domain.UserBranch;
import sa.tabadul.useraccessmanagement.domain.UserBranchRegistration;
import sa.tabadul.useraccessmanagement.domain.UserOrgBranch;
import sa.tabadul.useraccessmanagement.domain.UserOrganization;
import sa.tabadul.useraccessmanagement.domain.Users;
import sa.tabadul.useraccessmanagement.exception.BusinessException;
import sa.tabadul.useraccessmanagement.repository.UserBranchRepository;
import sa.tabadul.useraccessmanagement.repository.UserOrgBranchRepository;
import sa.tabadul.useraccessmanagement.repository.UserOrganizationRepository;
import sa.tabadul.useraccessmanagement.repository.UserRepository;


@Service
public class UserBranchService {

	@Autowired
	private UserBranchRepository userBranchRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserManagementService userManagementService;

	@Autowired
	private UserManagementExternalService restService;

	@Autowired
	private UserStatusService userStatusService;
	
	@Autowired
	private UserOrgBranchRepository orgBranchRepository;

	@Autowired
	private UserOrganizationRepository userOrganizationRepository;
	
	@Autowired
	private PropertiesConfig propertiesConfig;

	private static final Logger logger = LogManager.getLogger(UserBranchService.class);

	@Value("${defaultSecret}")
	private String UserSecret;

	private ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());

	public UserBranchRegistration addUserBranch(UserBranchRegistration userBranch, String token) {

		if (userBranch.getUserDetails() != null && userBranch.getBranchDetails() != null) {
			String randomBranchId = generateBranchId();
			UserBranch userBranchDetails = userBranch.getBranchDetails();
			userBranchDetails.setBranchID(randomBranchId);

			Users userDetails = userBranch.getUserDetails();
			userDetails.setBranchID(userBranchDetails.getBranchID());
			UserCreationKeyCloak objUserCreationKeyCloak = new UserCreationKeyCloak();
			objUserCreationKeyCloak.setUsername(userDetails.getUserId());
			objUserCreationKeyCloak.setEmail(userDetails.getEmail());
			objUserCreationKeyCloak.setFirstName(userDetails.getEmployeeName());
			objUserCreationKeyCloak.setLastName(userDetails.getEmployeeName());
			objUserCreationKeyCloak.setEmailVerified(false);
			objUserCreationKeyCloak.setEnabled(true);
			UserCredential objUserCredential = new UserCredential();
			objUserCreationKeyCloak.getGroups().add(CommonCodes.USRCRE);
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
				throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
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
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				}
				KeyCloakUserDetails objUserDetail = objUserDetails[0];
				
				KeyCloakUserDetails getUserPcs = restService.userGetPcs(objUserDetail.getId(), token);
				objectMapper = new ObjectMapper();

				Map<String, Object> attributes = new HashMap<>();
				List<String> mobileNumbers = new ArrayList<>();
				mobileNumbers.add(String.valueOf(userDetails.getPhoneNo())); 
				attributes.put("mobile_number", mobileNumbers);

				getUserPcs.setAttributes(objectMapper.convertValue(attributes, JsonNode.class));
				
				restService.userAddPcs(getUserPcs, objUserDetail.getId(), token);
				
				userDetails.setIamUserID(objUserDetail.getId());
				userDetails.setBranchID(randomBranchId);
				userDetails.setEmployeeCode(objUserDetail.getCreatedTimestamp().toString());
				userDetails.setDefaultGroupTypeRID(0);
				userDetails.setDepartmentRID(0);
				userDetails.setDesignationRID(0);
				userDetails.setPortRID(0);
				

				String objNewUser = userManagementService.createUser(userDetails);

				if (objNewUser != null) {
					userBranchDetails = userBranchRepository.save(userBranchDetails);
					userBranch.setUserDetails(userDetails);
					userBranch.setBranchDetails(userBranchDetails);
				}
				return userBranch;

			} else {
				throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase().toString());
			}
		} else {
			Users userDetails = userBranch.getUserDetails();
			UserCreationKeyCloak objUserCreationKeyCloak = new UserCreationKeyCloak();
			objUserCreationKeyCloak.setUsername(userDetails.getUserId());
			objUserCreationKeyCloak.setEmail(userDetails.getEmail());
			objUserCreationKeyCloak.setFirstName(userDetails.getEmployeeName());
			objUserCreationKeyCloak.setLastName(userDetails.getEmployeeName());
			objUserCreationKeyCloak.setEmailVerified(false);
			objUserCreationKeyCloak.setEnabled(true);
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
				throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
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
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				}
				KeyCloakUserDetails objUserDetail = objUserDetails[0];
				
				KeyCloakUserDetails getUserPcs = restService.userGetPcs(objUserDetail.getId(), token);
				objectMapper = new ObjectMapper();

				Map<String, Object> attributes = new HashMap<>();
				List<String> mobileNumbers = new ArrayList<>();
				mobileNumbers.add(String.valueOf(userDetails.getPhoneNo())); 
				attributes.put("mobile_number", mobileNumbers);

				getUserPcs.setAttributes(objectMapper.convertValue(attributes, JsonNode.class));
				
				restService.userAddPcs(getUserPcs, objUserDetail.getId(), token);
				
				userDetails.setIamUserID(objUserDetail.getId());
				userDetails.setEmployeeCode(objUserDetail.getCreatedTimestamp().toString());
				userDetails.setDefaultGroupTypeRID(0);
				userDetails.setDepartmentRID(0);
				userDetails.setDesignationRID(0);
				userDetails.setPortRID(0);
				String objNewUser = userManagementService.createUser(userDetails);
				if (objNewUser != null) {
					userBranch.setUserDetails(userDetails);
				}
				return userBranch;
			} else {
				throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase().toString());
			}

		}

	}

	public static String generateBranchId() {
        String uuid = UUID.randomUUID().toString().replace("-", ""); 
        return uuid.substring(0, 16);
    }

	public UserBranch getByBranchId(String branchId) {
		UserBranch userBranchDetails = userBranchRepository.findByBranchID(branchId);
		if (userBranchDetails == null) {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(),
					ExceptionMessage.BRANCH_DETAILS_NOT_FOUND.getValue() + branchId);
		}
		
		String locationMasterDetails = restService.getAllLocation("CUSTOMS_CITY");
		String portMasterDetails = restService.getPort(userBranchDetails.getPortRid());
		ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		LocationListResponse locationMasterResponse = new LocationListResponse();
		PortCodeResponse portMasterResponse = new PortCodeResponse();
		try {
			if (locationMasterDetails != null) {
				locationMasterResponse = objectMapper.readValue(locationMasterDetails, LocationListResponse.class);
			}
			if (portMasterDetails != null) {
				portMasterResponse = objectMapper.readValue(portMasterDetails, PortCodeResponse.class);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		List<LocationMaster> responseLocationMaster = locationMasterResponse.getData();
		LocationMaster objLocationMaster = new LocationMaster();
		PortMaster responsePortMaster = portMasterResponse.getData();
		if (responseLocationMaster.size() > 0) {
			objLocationMaster = responseLocationMaster.stream()
					.filter(x -> Objects.equals(x.getId(), userBranchDetails.getLocationRid())).findAny()
					.orElse(new LocationMaster());
		}
		if (objLocationMaster != null) {
			userBranchDetails.setLocationNameEnglish(objLocationMaster.getLocationDescriptionEnglish());
			userBranchDetails.setLocationNameArabic(objLocationMaster.getLocationDescriptionArabic());
		}
		if (responsePortMaster != null) {
			userBranchDetails.setPortNameEnglish(responsePortMaster.getPortNameEnglish());
			userBranchDetails.setPortNameArabic(responsePortMaster.getPortNameArabic());
		}
		return userBranchDetails;
	}

	public PaginationResponse getBranchUsers(Pagination<UserBranchViewResponse> pagination) {
		List<Users> userDetails = new ArrayList<>();
		if (pagination.getKeys().getUserId() != null && !pagination.getKeys().getUserId().isEmpty()) {
			if ((pagination.getKeys().getOrgId() != null) && ((pagination.getKeys().getBranchId() != null)
					&& !pagination.getKeys().getBranchId().isEmpty())) {
				userDetails = userRepository.findAllByOrgIDAndBranchIDAndIamUserIDNot(pagination.getKeys().getOrgId(),
						pagination.getKeys().getBranchId(), pagination.getKeys().getUserId());
			} else {
				userDetails = userRepository.findByOrgIDAndIamUserIDNot(pagination.getKeys().getOrgId(),
						pagination.getKeys().getUserId());
			}
		}
		List<UserBranchViewResponse> lstUserBranch = new ArrayList<>();

		for (Users user : userDetails) {
			UserBranchViewResponse userBranchViewResponse = new UserBranchViewResponse();
			if (user.getBranchID() != null) {
				UserBranch userBranch = getByBranchId(user.getBranchID());
				userBranchViewResponse.setBranchId(userBranch.getBranchID());
				userBranchViewResponse.setBranchName(userBranch.getBranchName());
				userBranchViewResponse.setBranchLocation(userBranch.getLocationNameArabic());
				
			}
			userBranchViewResponse.setId(user.getId());
			userBranchViewResponse.setPhoneNumber(user.getPhoneNo());
			userBranchViewResponse.setEmail(user.getEmail());
			userBranchViewResponse.setUserName(user.getEmployeeName());
			userBranchViewResponse.setCreatedDate(user.getCreatedDate());
			userBranchViewResponse.setUpdatedDate(user.getUpdatedDate());
			userBranchViewResponse.setStatus(userStatusService.getStatusName(user.getIsActive()));
			userBranchViewResponse.setUserType(user.getUserType());
			userBranchViewResponse.setUserId(user.getIamUserID());
			lstUserBranch.add(userBranchViewResponse);
		}

		String sort = pagination.getSort();
		if (pagination.getFilter() != null) {
			UserBranchViewResponse filter = pagination.getFilter();
			if (!"-".equals(filter.getBranchName())) {
				lstUserBranch = lstUserBranch.stream().filter(obj -> obj.getBranchName().equals(filter.getBranchName()))
						.collect(Collectors.toList());
			}
			if (filter.getPhoneNumber() != null) {
				lstUserBranch = lstUserBranch.stream()
						.filter(obj -> obj.getPhoneNumber().equals(filter.getPhoneNumber()))
						.collect(Collectors.toList());
			}
			if (!"-".equals(filter.getUserName())) {
				lstUserBranch = lstUserBranch.stream().filter(obj -> obj.getUserName().equals(filter.getUserName()))
						.collect(Collectors.toList());
			}
			if (!"-".equals(filter.getEmail())) {
				lstUserBranch = lstUserBranch.stream().filter(obj -> obj.getEmail().equals(filter.getEmail()))
						.collect(Collectors.toList());
			}
			if (!"-".equals(filter.getBranchId())) {
				lstUserBranch = lstUserBranch.stream().filter(obj -> obj.getBranchId().equals(filter.getBranchId()))
						.collect(Collectors.toList());
			}
			if (!"-".equals(filter.getBranchLocation())) {
				lstUserBranch = lstUserBranch.stream()
						.filter(obj -> obj.getBranchLocation().equals(filter.getBranchLocation()))
						.collect(Collectors.toList());
			}
			if (!"-".equals(filter.getUserType())) {
				lstUserBranch = lstUserBranch.stream().filter(obj -> obj.getUserType().equals(filter.getUserType()))
						.collect(Collectors.toList());
			}
			if (!"-".equals(filter.getStatus())) {
				lstUserBranch = lstUserBranch.stream().filter(obj -> obj.getStatus().equals(filter.getStatus()))
						.collect(Collectors.toList());
			}
		}

		if (!"".equals(pagination.getSearch()) && pagination.getSearch() != null) {

			lstUserBranch = lstUserBranch.stream()
					.filter(obj -> (obj.getPhoneNumber() != null
							&& Long.toString(obj.getPhoneNumber()).contains(pagination.getSearch()))
							|| (obj.getBranchId() != null && obj.getBranchId().contains(pagination.getSearch()))
							|| (obj.getBranchName() != null && obj.getBranchName().contains(pagination.getSearch()))
							|| (obj.getBranchLocation() != null
									&& obj.getBranchLocation().contains(pagination.getSearch()))
							|| (obj.getEmail() != null && obj.getEmail().contains(pagination.getSearch()))
							|| (obj.getUserType() != null && obj.getUserType().contains(pagination.getSearch()))
							|| (obj.getStatus() != null && obj.getStatus().contains(pagination.getSearch()))
							|| (obj.getUserId() != null && obj.getUserId().contains(pagination.getSearch()))
							|| (obj.getUserName() != null && obj.getUserName().contains(pagination.getSearch())))
					.collect(Collectors.toList());
		}

		if (pagination.getSortDir().equalsIgnoreCase(CommonCodes.ASCENDING)) {

			if (sort.equals(CommonCodes.PHONE_NO)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparingLong(UserBranchViewResponse::getPhoneNumber))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.USER_NAME)) {
				lstUserBranch = lstUserBranch.stream().sorted(Comparator.comparing(UserBranchViewResponse::getUserName))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.EMAIL)) {
				lstUserBranch = lstUserBranch.stream().sorted(Comparator.comparing(UserBranchViewResponse::getEmail))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.BRANCH_ID)) {
				lstUserBranch = lstUserBranch.stream().sorted(Comparator.comparing(UserBranchViewResponse::getBranchId))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.BRANCH_NAME)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparing(UserBranchViewResponse::getBranchName))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.BRANCH_LOCATION)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparing(UserBranchViewResponse::getBranchLocation))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.STATUS)) {
				lstUserBranch = lstUserBranch.stream().sorted(Comparator.comparing(UserBranchViewResponse::getStatus))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.USER_TYPE)) {
				lstUserBranch = lstUserBranch.stream().sorted(Comparator.comparing(UserBranchViewResponse::getUserType))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.CREATED_DATE)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparing(UserBranchViewResponse::getCreatedDate , Comparator.nullsLast(Comparator.naturalOrder())))
						.collect(Collectors.toList());
			}else if (sort.equalsIgnoreCase(CommonCodes.UPDATED_DATE)) {
			    lstUserBranch = lstUserBranch.stream()
			            .sorted(Comparator.comparing(UserBranchViewResponse::getUpdatedDate, Comparator.nullsLast(Comparator.naturalOrder())))
			            .collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.ID)) {
				lstUserBranch = lstUserBranch.stream().sorted(Comparator.comparing(UserBranchViewResponse::getId))
						.collect(Collectors.toList());
			}

		} else {
			if (sort.equals(CommonCodes.PHONE_NO)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparingLong(UserBranchViewResponse::getPhoneNumber).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.USER_NAME)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparing(UserBranchViewResponse::getUserName).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.EMAIL)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparing(UserBranchViewResponse::getEmail).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.BRANCH_ID)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparing(UserBranchViewResponse::getBranchId).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.BRANCH_NAME)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparing(UserBranchViewResponse::getBranchName).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.BRANCH_LOCATION)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparing(UserBranchViewResponse::getBranchLocation).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.STATUS)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparing(UserBranchViewResponse::getStatus).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.USER_TYPE)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparing(UserBranchViewResponse::getUserType).reversed())
						.collect(Collectors.toList());
			}else if (sort.equalsIgnoreCase(CommonCodes.UPDATED_DATE)) {
			    lstUserBranch = lstUserBranch.stream()
			            .sorted(Comparator.comparing(UserBranchViewResponse::getUpdatedDate, Comparator.nullsLast(Comparator.reverseOrder())))
			            .collect(Collectors.toList());
			}else if (sort.equalsIgnoreCase(CommonCodes.CREATED_DATE)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparing(UserBranchViewResponse::getCreatedDate, Comparator.nullsLast(Comparator.reverseOrder())))
						.collect(Collectors.toList());
			} else if (sort.equalsIgnoreCase(CommonCodes.ID)) {
				lstUserBranch = lstUserBranch.stream()
						.sorted(Comparator.comparing(UserBranchViewResponse::getId).reversed())
						.collect(Collectors.toList());
			}
		}
		PageRequest pageable = PageRequest.of(pagination.getPage(), pagination.getLength());
		Long totalRecord = (long) userDetails.size();
		Long totalRecordFiltered = (long) lstUserBranch.size();

		lstUserBranch = lstUserBranch.stream().skip(pageable.getOffset()).limit(pageable.getPageSize())
				.collect(Collectors.toList());

		return new PaginationResponse(totalRecord, totalRecordFiltered, lstUserBranch);
	}

	public UserBranchDetailsResponse getBranch(String userId) {
		Users userDetails = userManagementService.getbyIAMid(userId);
		CodeMasterRequest objCodeMasterRequest = new CodeMasterRequest();
		objCodeMasterRequest.getKey1().add(CommonCodes.DOCUMENT_TYPE);
		objCodeMasterRequest.getKey1().add(CommonCodes.REGISTRATION_THROUGH);
		objCodeMasterRequest.getKey1().add(CommonCodes.REGISTRATION_TYPE);
		List<CodeMaster> lstCodeMaster = restService.getByKeysCodeMaster(objCodeMasterRequest);
		if (userDetails == null) {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.USER_ID_NOT_FOUND.getValue());
		}
		String locationMasterDetails = restService.getAllLocation("CUSTOMS_CITY");
		LocationListResponse locationMasterResponse = new LocationListResponse();
		try {
			if (locationMasterDetails != null) {
				locationMasterResponse = objectMapper.readValue(locationMasterDetails, LocationListResponse.class);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		List<LocationMaster> lstLocationMaster = locationMasterResponse.getData();
		LocationMaster objLocationMasterCity = new LocationMaster();
		if (lstLocationMaster.size() > 0) {
			if(userDetails.getCity()!=null) {
			objLocationMasterCity = lstLocationMaster.stream()
					.filter(x -> x.getId()==userDetails.getCity()).findAny()
					.orElse(new LocationMaster());
			}
		}
		UserBranchDetailsResponse userBranchDetailsResponse = new UserBranchDetailsResponse();
		userBranchDetailsResponse.setId(userDetails.getId());
		userBranchDetailsResponse.setUserId(userDetails.getUserId());
		userBranchDetailsResponse.setUserName(userDetails.getEmployeeName());
		userBranchDetailsResponse.setEmail(userDetails.getEmail());
		userBranchDetailsResponse.setPhoneNo(userDetails.getPhoneNo());
		userBranchDetailsResponse.setStakeHolderTypeId(userDetails.getStakeHolders().get(0).getStakeholderTypeId());
		userBranchDetailsResponse
				.setStakeHolderNameEnglish(userDetails.getStakeHolders().get(0).getStakeholderNameEng());
		userBranchDetailsResponse
				.setStakeHolderNameArabic(userDetails.getStakeHolders().get(0).getStakeholderNameArabic());
		userBranchDetailsResponse
				.setStakeHolderSubRoleId(userDetails.getStakeHolders().get(0).getStakeholderSubroleId());
		userBranchDetailsResponse.setStakeHolderSubRoleNameEnglish(
				userDetails.getStakeHolders().get(0).getStakeholdertypeSubroleNameEng());
		userBranchDetailsResponse.setStakeHolderSubRoleNameArabic(
				userDetails.getStakeHolders().get(0).getStakeholdertypeSubroleNameArabic());
		userBranchDetailsResponse.setNationalId(userDetails.getNationalId());
		userBranchDetailsResponse.setAddress(userDetails.getAddress());
		userBranchDetailsResponse.setBuildingNumber(userDetails.getBuildingNumber());
		userBranchDetailsResponse.setCity(userDetails.getCity());
		if (objLocationMasterCity != null) {
			userBranchDetailsResponse.setCityDescArabic(objLocationMasterCity.getLocationDescriptionEnglish());
			userBranchDetailsResponse.setCityDescArabic(objLocationMasterCity.getLocationDescriptionArabic());
		}
		userBranchDetailsResponse.setDistrict(userDetails.getDistrict());
		userBranchDetailsResponse.setUnitNumber(userDetails.getUnitNumber());
		userBranchDetailsResponse.setZipCode(userDetails.getZipCode());
		userBranchDetailsResponse.setStreet(userDetails.getStreet());
		userBranchDetailsResponse.setRepNo(userDetails.getRepNo());
		userBranchDetailsResponse.setBranchId(userDetails.getBranchID());
		if (userDetails.getBranchID() != null) {
			UserBranch userBranch = getByBranchId(userDetails.getBranchID());
			userBranchDetailsResponse.setBranchName(userBranch.getBranchName());
			userBranchDetailsResponse.setPortRid(userBranch.getPortRid());
			userBranchDetailsResponse.setPortLocationEnglish(userBranch.getPortNameEnglish());
			userBranchDetailsResponse.setPortLocationArabic(userBranch.getPortNameArabic());
			userBranchDetailsResponse.setBranchLocationRid(userBranch.getLocationRid());
			userBranchDetailsResponse.setBranchLocationNameEnglish(userBranch.getLocationNameEnglish());
			userBranchDetailsResponse.setBranchLocationNameArabic(userBranch.getLocationNameArabic());
		}
		if (userDetails.getOrgID() != null) {
			UserOrganization userOrganization = userOrganizationRepository.findByOrgID(userDetails.getOrgID());
			if (userOrganization == null) {
				throw new BusinessException(HttpStatus.NOT_FOUND.value(),
						ExceptionMessage.ORGANIZATION_NOT_FOUND.getValue());
			}
			userBranchDetailsResponse.setOrgId(userOrganization.getOrgID());
			userBranchDetailsResponse.setOrgName(userOrganization.getOrgName());
			userBranchDetailsResponse.setLicenceNo(userOrganization.getLicenceNo());
			userBranchDetailsResponse.setOrgNationalId(userOrganization.getNationalId());
			if(userOrganization.getDocumentTypeRid()!=null) {
			CodeMaster objCodeMaster = lstCodeMaster.stream().filter(obj->obj.getId()==userOrganization.getDocumentTypeRid()).findAny().orElse(new CodeMaster());
			userBranchDetailsResponse.setDocumentTypeEng(objCodeMaster.getCodeDescriptionEnglish());
			userBranchDetailsResponse.setDocumentTypeArabic(objCodeMaster.getCodeDescriptionArabic());
			}
			if(userOrganization.getRegistrationTypeRid()!=null) {
				CodeMaster objCodeMaster = lstCodeMaster.stream().filter(obj->obj.getId()==userOrganization.getRegistrationTypeRid()).findAny().orElse(new CodeMaster());
			userBranchDetailsResponse.setRegistrationTypeEng(objCodeMaster.getCodeDescriptionEnglish());
			userBranchDetailsResponse.setRegistrationTypeArabic(objCodeMaster.getCodeDescriptionArabic());
			}
			if(userOrganization.getRegistrationThroughRid()!=null) {
				CodeMaster objCodeMaster = lstCodeMaster.stream().filter(obj->obj.getId()==userOrganization.getRegistrationThroughRid()).findAny().orElse(new CodeMaster());
			userBranchDetailsResponse.setRegistrationThroughEng(objCodeMaster.getCodeDescriptionEnglish());
			userBranchDetailsResponse.setRegistrationThroughArabic(objCodeMaster.getCodeDescriptionArabic());
			}
		}
		return userBranchDetailsResponse;
	}
	
	public List<UserPcListResponse> branchByIds(List<Integer> branchIds)
	{
		List<UserOrgBranch> lstOrgBranchs = this.orgBranchRepository.findAllById(branchIds);
		List<UserPcListResponse> lstPcListResponses = new ArrayList<>();
		for(UserOrgBranch orgBranch:lstOrgBranchs)
		{
			UserPcListResponse objOrgBranch = new UserPcListResponse();
			objOrgBranch.setId(orgBranch.getId());
			objOrgBranch.setBranchId(orgBranch.getBranchID());
			objOrgBranch.setBranchName(orgBranch.getBranchName());
			objOrgBranch.setOrgId(orgBranch.getUserOrg().getOrgID());
			objOrgBranch.setOrgName(orgBranch.getUserOrg().getOrgName());
			
			lstPcListResponses.add(objOrgBranch);
			
		}
		
		return lstPcListResponses;
		
		
	}
	
	

}
