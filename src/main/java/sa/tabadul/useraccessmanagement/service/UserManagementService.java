package sa.tabadul.useraccessmanagement.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.time.Duration;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import sa.tabadul.useraccessmanagement.common.configs.PropertiesConfig;
import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.common.enums.ExceptionMessage;
import sa.tabadul.useraccessmanagement.common.filters.Filter;
import sa.tabadul.useraccessmanagement.common.models.request.CodeMasterRequest;
import sa.tabadul.useraccessmanagement.common.models.request.Pagination;
import sa.tabadul.useraccessmanagement.common.models.request.ResetPasswordRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserBranchRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserByIdsRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserCreationKeyCloak;
import sa.tabadul.useraccessmanagement.common.models.request.UserCredential;
import sa.tabadul.useraccessmanagement.common.models.request.UserListRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserOrganizationRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserPcRequset;
import sa.tabadul.useraccessmanagement.common.models.request.UserRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserValidateRequest;
import sa.tabadul.useraccessmanagement.common.models.response.AccessResponse;
import sa.tabadul.useraccessmanagement.common.models.response.ApiResponse;
import sa.tabadul.useraccessmanagement.common.models.response.KeyCloakUserDetails;
import sa.tabadul.useraccessmanagement.common.models.response.LocationListResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PaginationResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PortCodeResponse;
import sa.tabadul.useraccessmanagement.common.models.response.ResponseEnvelope;
import sa.tabadul.useraccessmanagement.common.models.response.UserAccessResponse;
import sa.tabadul.useraccessmanagement.common.models.response.UserListResponse;
import sa.tabadul.useraccessmanagement.common.models.response.UserPcListResponse;
import sa.tabadul.useraccessmanagement.common.models.response.UsersListResponse;
import sa.tabadul.useraccessmanagement.common.service.CommonService;
import sa.tabadul.useraccessmanagement.common.service.UserManagementExternalService;
import sa.tabadul.useraccessmanagement.constant.PortsType;
import sa.tabadul.useraccessmanagement.domain.CodeMaster;
import sa.tabadul.useraccessmanagement.domain.EmailTemplate;
import sa.tabadul.useraccessmanagement.domain.LocationMaster;
import sa.tabadul.useraccessmanagement.domain.PortCode;
import sa.tabadul.useraccessmanagement.domain.PortMaster;
import sa.tabadul.useraccessmanagement.domain.RoleMaster;
import sa.tabadul.useraccessmanagement.domain.SmsTemplate;
import sa.tabadul.useraccessmanagement.domain.UserBranch;
import sa.tabadul.useraccessmanagement.domain.UserEntity;
import sa.tabadul.useraccessmanagement.domain.UserOrganization;
import sa.tabadul.useraccessmanagement.domain.UserRole;
import sa.tabadul.useraccessmanagement.domain.Users;
import sa.tabadul.useraccessmanagement.exception.BusinessException;
import sa.tabadul.useraccessmanagement.repository.LicenseApplicationRepository;
import sa.tabadul.useraccessmanagement.repository.PortCodeRepo;
import sa.tabadul.useraccessmanagement.repository.RoleMasterRepository;
import sa.tabadul.useraccessmanagement.repository.UserBranchRepository;
import sa.tabadul.useraccessmanagement.repository.UserEntityRepository;
import sa.tabadul.useraccessmanagement.repository.UserOrganizationRepository;
import sa.tabadul.useraccessmanagement.repository.UserRepository;
import sa.tabadul.useraccessmanagement.repository.UserRoleRepository;

@Service
public class UserManagementService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleMasterService roleMasterService;

	@Autowired
	private AccessManagerService accessManagerService;

	@Autowired
	private UserEntityRepository userEntityRepository;

	@Autowired
	private UserBranchRepository userBranchRepository;

	@Autowired
	private UserOrganizationRepository userOrganizationRepository;

	@Autowired
	private LicenseApplicationRepository licenseRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private RoleMasterRepository roleMasterRepository;

	@Autowired
	private UserManagementExternalService apiService;

	@Autowired
	private PropertiesConfig propertiesConfig;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private PortCodeRepo portCodeRepo;

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private CommonService commonService;

	@Value("${do.not.migrate.Roles.if.exist}")
	boolean doNotMigrateRolesIfExist;

	private static final Logger logger = LogManager.getLogger(UserManagementService.class);

	ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.registerModule(new JavaTimeModule());

	public PaginationResponse getAllportAdmin(Pagination<Filter> p) {
		List<Users> userData = new ArrayList<>();
		int page = p.getPage();
		int length = p.getLength();
		String sort = p.getSort();
		String sortDir = p.getSortDir();
		String search = p.getSearch();

		PageRequest pageable = PageRequest.of(page, length,
				Sort.by(Sort.Direction.valueOf(sortDir), CommonCodes.CREATED_DATE));

		RoleMaster response = roleMasterService.getByCode(CommonCodes.POADM);
		List<PortMaster> lstPorts = this.apiService.getByKeysPortMaster();

		if (lstPorts == null) {
			lstPorts = new ArrayList<>();
		}

		List<Integer> lstPortIds = new ArrayList<>();
		if (search != null && !search.isEmpty()) {
			lstPortIds = lstPorts.stream()
					.filter(x -> x.getPortNameEnglish().toLowerCase().contains(search.toLowerCase())
							|| x.getPortNameEnglish().toLowerCase().contains(search.toLowerCase()))
					.map(PortMaster::getId).collect(Collectors.toList());
		}

		List<Integer> lstUserIds = new ArrayList<>();
		lstUserIds.add(response.getId());

		List<Users> resultList = userRepository.findBySearchAndUserIds(search.toLowerCase(), lstUserIds, lstPortIds,
				pageable);

		for (Users user : resultList) {

			PortMaster objPortMaster = lstPorts.stream().filter(x -> x.getId().equals(user.getPortRID())).findFirst()
					.orElse(new PortMaster());

			user.setPortCode(objPortMaster.getPortCode());
			user.setPortNameEng(objPortMaster.getPortNameEnglish());
			user.getStakeHolders().forEach(x -> {

				x.setStakeholderNameEng(response.getRoleNameEnglish());
				x.setStakeholderNameArabic(response.getRoleNameArabic());

			});

			userData.add(user);
		}

		long totalRecords = this.userRepository.totalAdmins(lstUserIds);
		long filterRecords = this.userRepository.filteredadmins(search.toLowerCase(), lstUserIds, lstPortIds);

		return new PaginationResponse(totalRecords, filterRecords, userData);

	}

	public Users getbyIAMid(String uuid) {

		List<Users> lstUsers = this.userRepository.findByIamUserID(uuid);

		if (!lstUsers.isEmpty()) {

			Users userData = lstUsers.get(0);
			CodeMasterRequest objCodeMasterRequest = new CodeMasterRequest();
			objCodeMasterRequest.getKey1().add(CommonCodes.DESIGNATION);
			objCodeMasterRequest.getKey1().add(CommonCodes.DEPARTMENT);
			objCodeMasterRequest.getKey1().add(CommonCodes.DEFAULT_GROUP);
			List<CodeMaster> lstCodeMaster = apiService.getByKeysCodeMaster(objCodeMasterRequest);
			List<Integer> roleIds = new ArrayList<>();
			userData.getStakeHolders().stream().forEach(role -> {
				roleIds.add(role.getStakeholderTypeId());
				roleIds.add(role.getStakeholderSubroleId());
			});
			List<RoleMaster> lstRoles = this.roleMasterRepository.findByIdIn(roleIds);
			CodeMaster departmentCode = new CodeMaster();
			CodeMaster designationCode = new CodeMaster();
			CodeMaster groupCode = new CodeMaster();
			String locationMasterDetails = apiService.getAllLocation("CUSTOMS_CITY");
			LocationListResponse locationMasterResponse = new LocationListResponse();
			try {
				if (locationMasterDetails != null) {
					locationMasterResponse = objectMapper.readValue(locationMasterDetails, LocationListResponse.class);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			List<LocationMaster> lstLocationMaster = locationMasterResponse.getData();

			if (userData.getCity() != null) {
				if (lstLocationMaster != null) {
					LocationMaster objLocationMaster = lstLocationMaster.stream()
							.filter(x -> x.getId() == userData.getCity()).findAny().orElse(new LocationMaster());
					userData.setCityName(objLocationMaster.getLocationDescriptionArabic());
				}
			}
			if (lstCodeMaster != null) {
				departmentCode = lstCodeMaster.stream().filter(x -> x.getId() == userData.getDepartmentRID()).findAny()
						.orElse(new CodeMaster());
				designationCode = lstCodeMaster.stream().filter(x -> x.getId() == userData.getDesignationRID())
						.findAny().orElse(new CodeMaster());
				groupCode = lstCodeMaster.stream().filter(x -> x.getId() == userData.getDefaultGroupTypeRID()).findAny()
						.orElse(new CodeMaster());
			}

			if (userData.getOrgID() != null) {
				UserOrganization objOrgUser = this.userOrganizationRepository.findByOrgID(userData.getOrgID());

				if (objOrgUser != null) {
					userData.setOrgnizationRID(objOrgUser.getId());
					userData.setLicenceNo(objOrgUser.getLicenceNo());
					userData.setCustomNo(objOrgUser.getCustomNo());
					userData.setOrgName(objOrgUser.getOrgName());
					userData.setOrgNameArabic(objOrgUser.getOrgNameArabic());
					userData.setDocumentNo(objOrgUser.getDocumentNo());
					userData.setOrgNationalId(objOrgUser.getNationalId());
					if (objOrgUser.getDocumentTypeRid() != null) {
						CodeMaster objCodeMaster = lstCodeMaster.stream()
								.filter(obj -> obj.getId() == objOrgUser.getDocumentTypeRid()).findAny()
								.orElse(new CodeMaster());
						userData.setDocumentTypeEng(objCodeMaster.getCodeDescriptionEnglish());
						userData.setDocumentTypeArabic(objCodeMaster.getCodeDescriptionArabic());
					}
					if (objOrgUser.getRegistrationTypeRid() != null) {
						CodeMaster objCodeMaster = lstCodeMaster.stream()
								.filter(obj -> obj.getId() == objOrgUser.getRegistrationTypeRid()).findAny()
								.orElse(new CodeMaster());
						userData.setRegistrationTypeEng(objCodeMaster.getCodeDescriptionEnglish());
						userData.setRegistrationTypeArabic(objCodeMaster.getCodeDescriptionArabic());
					}
					if (objOrgUser.getRegistrationThroughRid() != null) {
						CodeMaster objCodeMaster = lstCodeMaster.stream()
								.filter(obj -> obj.getId() == objOrgUser.getRegistrationThroughRid()).findAny()
								.orElse(new CodeMaster());
						userData.setRegistrationThroughEng(objCodeMaster.getCodeDescriptionEnglish());
						userData.setRegistrationThroughArabic(objCodeMaster.getCodeDescriptionArabic());
					}
				}
			}

			if (userData.getBranchID() != null) {
				UserBranch objBranchUser = this.userBranchRepository.findByBranchID(userData.getBranchID());
				if (objBranchUser != null) {
					userData.setBranchRID(objBranchUser.getId());
					userData.setBranchName(objBranchUser.getBranchName());
					if (objBranchUser.getLocationRid() != null) {
						userData.setBranchLocationRid(objBranchUser.getLocationRid());
						if (lstLocationMaster != null) {
							LocationMaster objLocationMaster = lstLocationMaster.stream()
									.filter(x -> x.getId() == objBranchUser.getLocationRid().intValue()).findAny()
									.orElse(new LocationMaster());
							userData.setBranchLocationEng(objLocationMaster.getLocationDescriptionEnglish());
							userData.setBranchLocationArb(objLocationMaster.getLocationDescriptionArabic());
						}
					}

					if (userData.getAppId().equals(CommonCodes.PCS)) {
						userData.setPortRID(objBranchUser.getPortRid());
					}
				}

			}

			userData.setDeparmentCode(departmentCode.getCode());
			userData.setDeparmentCodeDescEng(departmentCode.getCodeDescriptionEnglish());
			userData.setDeparmentCodeDescArabic(departmentCode.getCodeDescriptionArabic());

			userData.setDesignationCode(designationCode.getCode());
			userData.setDesignationCodeDescEng(designationCode.getCodeDescriptionEnglish());
			userData.setDesignationCodeDescArabic(designationCode.getCodeDescriptionArabic());

			userData.setDefaultGroupCode(groupCode.getCode());
			userData.setDefaultGroupCodeDescEng(groupCode.getCodeDescriptionEnglish());
			userData.setDefaultGroupCodeDescArabic(groupCode.getCodeDescriptionArabic());

			if (userData.getPortRID() != 0) {
				String portresp = this.apiService.getPort(userData.getPortRID());
				PortCodeResponse portCode;
				try {
					if (portresp != null) {
						portCode = objectMapper.readValue(portresp, PortCodeResponse.class);
						userData.setPortCode(portCode.getData().getPortCode());
						userData.setPortNameEng(portCode.getData().getPortNameEnglish());
					}
				} catch (JsonProcessingException e) {
					logger.error(e.getMessage());
				}

			}

			if (!userData.getStakeHolders().isEmpty()) {

				userData.getStakeHolders().forEach(stakeholder -> {

					RoleMaster objRole = lstRoles.stream()
							.filter(x -> x.getId().equals(stakeholder.getStakeholderTypeId())).findAny()
							.orElse(new RoleMaster());

					stakeholder.setStakeholderCode(objRole.getRoleCode());
					stakeholder.setStakeholderNameEng(objRole.getRoleNameEnglish());
					stakeholder.setStakeholderNameArabic(objRole.getRoleNameArabic());
					stakeholder.setStakeholderDesEng(objRole.getRoleDescriptionEnglish());
					stakeholder.setStakeholderDesArabic(objRole.getRoleDescriptionArabic());
					if (!stakeholder.getStakeholderSubroleId().equals(0)) {
						objRole = lstRoles.stream().filter(x -> x.getId().equals(stakeholder.getStakeholderSubroleId()))
								.findAny().orElse(new RoleMaster());
						userData.setRollType(objRole.getRollType());
						stakeholder.setStakeholdertypeSubroleCode(objRole.getRoleCode());
						stakeholder.setStakeholdertypeSubroleNameEng(objRole.getRoleNameEnglish());
						stakeholder.setStakeholdertypeSubroleNameArabic(objRole.getRoleNameArabic());
						stakeholder.setStakeholdertypeSubroleDesEng(objRole.getRoleDescriptionEnglish());
						stakeholder.setStakeholdertypeSubroleDesArabic(objRole.getRoleDescriptionArabic());
					}

				});
			}

			return userData;
		} else {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(),
					ExceptionMessage.USER_ID_NOT_FOUND.getValue() + uuid);
		}

	}

	@Transactional
	public Users getbyIAMidOpt(Users userData) {

		if (userData != null) {

			List<Integer> roleIds = new ArrayList<>();
			userData.getStakeHolders().stream().forEach(role -> {
				roleIds.add(role.getStakeholderTypeId());
				roleIds.add(role.getStakeholderSubroleId());
			});
			List<RoleMaster> lstRoles = this.roleMasterRepository.findByIdIn(roleIds);

			if (userData.getOrgID() != null) {
				UserOrganization objOrgUser = this.userOrganizationRepository.findByOrgID(userData.getOrgID());

				if (objOrgUser != null) {

					userData.setOrgnizationRID(objOrgUser.getId());

					userData.setLicenceNo(objOrgUser.getLicenceNo());
					userData.setCustomNo(objOrgUser.getCustomNo());
					userData.setOrgName(objOrgUser.getOrgName());
					userData.setOrgNameArabic(objOrgUser.getOrgNameArabic());
					userData.setDocumentNo(objOrgUser.getDocumentNo());
					userData.setOrgNationalId(objOrgUser.getNationalId());

				}
			}

			if (userData.getBranchID() != null) {
				UserBranch objBranchUser = this.userBranchRepository.findByBranchID(userData.getBranchID());
				if (objBranchUser != null) {
					userData.setBranchRID(objBranchUser.getId());
					userData.setBranchName(objBranchUser.getBranchName());
					if (objBranchUser.getLocationRid() != null) {
						userData.setBranchLocationRid(objBranchUser.getLocationRid());

					}

					if (userData.getAppId().equals(CommonCodes.PCS)) {
						userData.setPortRID(objBranchUser.getPortRid());
					}
				}

			}

			if (userData.getPortRID() != 0) {
				String portresp = this.apiService.getPort(userData.getPortRID());
				PortCodeResponse portCode;
				try {
					if (portresp != null) {
						portCode = objectMapper.readValue(portresp, PortCodeResponse.class);
						userData.setPortCode(portCode.getData().getPortCode());
						userData.setPortNameEng(portCode.getData().getPortNameEnglish());
					}
				} catch (JsonProcessingException e) {
					logger.error(e.getMessage());
				}

			}

			if (!userData.getStakeHolders().isEmpty()) {

				userData.getStakeHolders().forEach(stakeholder -> {

					RoleMaster objRole = lstRoles.stream()
							.filter(x -> x.getId().equals(stakeholder.getStakeholderTypeId())).findAny()
							.orElse(new RoleMaster());

					stakeholder.setStakeholderCode(objRole.getRoleCode());
					stakeholder.setStakeholderNameEng(objRole.getRoleNameEnglish());
					stakeholder.setStakeholderNameArabic(objRole.getRoleNameArabic());
					stakeholder.setStakeholderDesEng(objRole.getRoleDescriptionEnglish());
					stakeholder.setStakeholderDesArabic(objRole.getRoleDescriptionArabic());
					if (!stakeholder.getStakeholderSubroleId().equals(0)) {
						objRole = lstRoles.stream().filter(x -> x.getId().equals(stakeholder.getStakeholderSubroleId()))
								.findAny().orElse(new RoleMaster());
						userData.setRollType(objRole.getRollType());
						stakeholder.setStakeholdertypeSubroleCode(objRole.getRoleCode());
						stakeholder.setStakeholdertypeSubroleNameEng(objRole.getRoleNameEnglish());
						stakeholder.setStakeholdertypeSubroleNameArabic(objRole.getRoleNameArabic());
						stakeholder.setStakeholdertypeSubroleDesEng(objRole.getRoleDescriptionEnglish());
						stakeholder.setStakeholdertypeSubroleDesArabic(objRole.getRoleDescriptionArabic());
					}

				});
			}

			return userData;
		} else {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.USER_ID_NOT_FOUND.getValue());
		}

	}

	public String createUser(Users users) {

		Boolean userExists = this.userRepository.existsByIamUserID(users.getIamUserID());
		if (!userExists) {
			if (users.getStakeHolders() != null || users.getStakeHolders().isEmpty()) {

				users.getStakeHolders().forEach(x -> {

					x.setCreatedBy(users.getCreatedBy());
					x.setUpdatedBy(users.getUpdatedBy());

				});
			}

		} else {
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(),
					ExceptionMessage.USR_EXISTS.getValue() + users.getUserId());
		}
		Users objuser = this.userRepository.save(users);

		if (objuser != null) {
			List<UserRole> objUserRole = this.userRoleRepository.saveAll(users.getStakeHolders());
			if (!objUserRole.isEmpty()) {
				return CommonCodes.USER_CREATED;
			} else {
				return ExceptionMessage.SOMETHING_WENT_WRONG.getValue();
			}
		} else {
			return ExceptionMessage.SOMETHING_WENT_WRONG.getValue();
		}

	}

	public String createUserPMIS(Users users, String token) {
		List<Users> user = userRepository.findByIamUserID(users.getIamUserID());
		if (user.isEmpty()) {

			if (users.getStakeHolders() != null || users.getStakeHolders().isEmpty()) {

				UserCreationKeyCloak objUserCreationKeyCloak = new UserCreationKeyCloak();
				objUserCreationKeyCloak.setUsername(users.getUserId());
				objUserCreationKeyCloak.setEmail(users.getEmail());
				objUserCreationKeyCloak.setFirstName(users.getEmployeeName());
				objUserCreationKeyCloak.setLastName(users.getEmployeeName());
				objUserCreationKeyCloak.setEmailVerified(false);
				objUserCreationKeyCloak.setEnabled(true);
				UserCredential objUserCredential = new UserCredential();
				objUserCredential.setTemporary(true);
				if (users.getUserType().equals(CommonCodes.POADM)) {
					objUserCreationKeyCloak.getGroups().add(CommonCodes.POADM);

				}

				objUserCredential.setType(CommonCodes.PASSWD);
				objUserCredential.setValue(users.getPassword());

				objUserCreationKeyCloak.credentials.add(objUserCredential);

				String requestBody = null;
				try {
					objectMapper.setSerializationInclusion(Include.NON_EMPTY);
					requestBody = objectMapper.writeValueAsString(objUserCreationKeyCloak);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				}

				String strUserCreate = this.apiService.userCreationPMIS(requestBody, token);
				if (strUserCreate.equals(HttpStatus.OK.getReasonPhrase())) {

					KeyCloakUserDetails objCloakUserDetails = this.apiService.validateUserPMIS(users.getEmail(), token);

					users.setIamUserID(objCloakUserDetails.getId());
					users.getStakeHolders().forEach(x -> {

						x.setCreatedBy(users.getCreatedBy());
						x.setUpdatedBy(users.getUpdatedBy());

					});

					KeyCloakUserDetails getUserPmis = apiService.userGetPmis(objCloakUserDetails.getId(), token);
					ObjectMapper objectMapper = new ObjectMapper();

					Map<String, Object> attributes = new HashMap<>();
					List<String> mobileNumbers = new ArrayList<>();
					mobileNumbers.add(String.valueOf(users.getPhoneNo()));
					attributes.put("mobile_number", mobileNumbers);

					getUserPmis.setAttributes(objectMapper.convertValue(attributes, JsonNode.class));

					apiService.userAddPmis(getUserPmis, objCloakUserDetails.getId(), token);

					Users objuser = this.userRepository.save(users);
					if (objuser != null) {
						List<UserRole> objUserRole = this.userRoleRepository.saveAll(users.getStakeHolders());
						if (!objUserRole.isEmpty()) {
							return CommonCodes.USER_CREATED;
						}
					}

				} else {
					throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
				}

			}

			return null;

		} else {
			Users existUser = user.get(0);
			existUser.setDepartmentRID(users.getDepartmentRID());
			existUser.setDesignationRID(users.getDesignationRID());
			existUser.setDefaultGroupTypeRID(users.getDefaultGroupTypeRID());
			userRepository.save(existUser);
			return "User Updated Successfully";
		}

	}

	public Users userUpdate(Users users) {

		Boolean userExists = this.userRepository.existsByIamUserID(users.getIamUserID());

		if (userExists) {
			users.getStakeHolders().forEach(x -> {
				x.setCreatedBy(users.getCreatedBy());
				x.setUpdatedBy(users.getUpdatedBy());
			});
			this.userRoleRepository.deleteAll(this.userRoleRepository.findByUsers(users));
			if (this.userRepository.save(users) != null) {
				this.userRoleRepository.saveAll(users.getStakeHolders());
			}

			return this.userRepository.findByIamUserID(users.getIamUserID()).get(0);

		} else {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(),
					ExceptionMessage.USER_ID_NOT_FOUND.getValue() + users.getIamUserID());
		}

	}

	public PaginationResponse pmisgetuserbyportcode(Integer port_code, Pagination<Filter> p) {

		List<Users> userData = new ArrayList<>();
		int page = p.getPage();
		int length = p.getLength();
		String sort = p.getSort();
		String sortDir = p.getSortDir();
		String search = p.getSearch();
		RoleMaster portAdmin = roleMasterService.getByCode(CommonCodes.POADM);
		RoleMaster superUser = roleMasterService.getByCode(CommonCodes.SUADM);
		PageRequest pageable = PageRequest.of(page, length,
				Sort.by(Sort.Direction.valueOf(sortDir), CommonCodes.CREATED_DATE));

		List<Integer> lstPortAdmin = new ArrayList<>();
		lstPortAdmin.add(portAdmin.getId());
		lstPortAdmin.add(superUser.getId());
		List<Integer> lstRoles = new ArrayList<>();
		List<RoleMaster> lstAllRoles = this.roleMasterRepository.findAll();
		if (search != null && !search.isEmpty()) {
			lstRoles = lstAllRoles.stream()
					.filter(z -> (z.getRoleNameEnglish().toLowerCase().contains(search.toLowerCase())
							|| z.getRoleNameArabic().toLowerCase().contains(search.toLowerCase())))
					.map(RoleMaster::getId).collect(Collectors.toList());
		}

		List<Users> lstList = this.userRepository.findBySearchAndPortRIDAndAppIdAndUserIds(search.toLowerCase(),
				port_code, CommonCodes.PMIS, lstPortAdmin, lstRoles, pageable);
		long filteredRecords = this.userRepository.filteredRecordsPortUser(search.toLowerCase(), port_code,
				CommonCodes.PMIS, lstPortAdmin, lstRoles);

		long totalRecords = this.userRepository.totalRecordsPortUser(port_code, CommonCodes.PMIS, lstPortAdmin);

		String portresp = this.apiService.getPort(port_code);
		PortCodeResponse portCode = new PortCodeResponse();
		try {
			if (portresp != null) {
				portCode = objectMapper.readValue(portresp, PortCodeResponse.class);
			}
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		}
		for (Users user : lstList) {

			user.setPortCode(portCode.getData().getPortCode());
			user.setPortNameEng(portCode.getData().getPortNameEnglish());
			user.getStakeHolders().forEach(x -> {
				RoleMaster objRoleMaster = lstAllRoles.stream().filter(y -> y.getId().equals(x.getStakeholderTypeId()))
						.findFirst().orElse(new RoleMaster());
				x.setStakeholderNameEng(objRoleMaster.getRoleNameEnglish());
				x.setStakeholderNameArabic(objRoleMaster.getRoleNameArabic());
				objRoleMaster = lstAllRoles.stream().filter(y -> y.getId().equals(x.getStakeholderSubroleId()))
						.findFirst().orElse(new RoleMaster());
				x.setStakeholdertypeSubroleNameEng(objRoleMaster.getRoleNameEnglish());
				x.setStakeholdertypeSubroleNameArabic(objRoleMaster.getRoleNameArabic());
			});

			userData.add(user);
		}

		return new PaginationResponse(totalRecords, filteredRecords, userData);

	}

	@Transactional
	public ResponseEnvelope<String> activateDeactivation(Users updateEntity, String token) {

		Users baseEntity = this.userRepository.findByIamUserID(updateEntity.getIamUserID()).get(0);
		if (baseEntity != null) {

			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("enabled", updateEntity.getIsActive());
			baseEntity.setIsActive(updateEntity.getIsActive());
			Users user = this.userRepository.save(baseEntity);

			if (user == null) {
				throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			} else {

				String apiUrl;
				if (user.getAppId().equals(CommonCodes.PCS)) {
					apiUrl = propertiesConfig.getKeyCloakBaseUrlPcs() + String.format(
							propertiesConfig.getKeyCloakDeactive(), CommonCodes.STRPCS, updateEntity.getIamUserID());
				} else {
					apiUrl = propertiesConfig.getKeyCloakBaseUrl() + String.format(
							propertiesConfig.getKeyCloakDeactive(), CommonCodes.STRPMIS, updateEntity.getIamUserID());
				}

				String strRequestBody = null;
				try {
					objectMapper.setSerializationInclusion(Include.NON_EMPTY);
					strRequestBody = objectMapper.writeValueAsString(requestBody);
				} catch (JsonProcessingException e) {
				}
				Integer respStatus = this.apiService.restTemplatePut(apiUrl, strRequestBody, token);

				ResponseEnvelope<String> responseEnvelope = new ResponseEnvelope<>();
				if (respStatus.equals(HttpStatus.NO_CONTENT.value())) {

					responseEnvelope.setResponseCode(HttpStatus.OK.value());
					responseEnvelope.setData(" ");
					if (updateEntity.getIsActive()) {
						responseEnvelope.setResponseMessage("User Activated");
					} else {
						responseEnvelope.setResponseMessage("User Deactivated");
					}

				} else {
					responseEnvelope.setData("");
					responseEnvelope.setResponseMessage(HttpStatus.valueOf(respStatus).getReasonPhrase());
					responseEnvelope.setResponseCode(respStatus);
				}
				return responseEnvelope;

			}

		} else {
			throw new BusinessException(HttpStatus.BAD_REQUEST.value(),
					ExceptionMessage.USER_ID_NOT_FOUND.getValue() + updateEntity.getIamUserID());
		}
	}

	public UserAccessResponse getByuuid(String uuid) {

		UserAccessResponse responseModel = new UserAccessResponse();
		List<Users> objUser = this.userRepository.findByIamUserID(uuid);

		if (objUser.isEmpty()) {

			throw new BusinessException(HttpStatus.NOT_FOUND.value(),
					ExceptionMessage.USER_ID_NOT_FOUND.getValue() + uuid);
		}

		Users userEntity = this.getbyIAMidOpt(objUser.get(0));

		List<Integer> roleIds = userEntity.getStakeHolders().stream().map(UserRole::getStakeholderTypeId)
				.collect(Collectors.toList());
		List<Integer> subRoleIds = userEntity.getStakeHolders().stream().map(UserRole::getStakeholderSubroleId)
				.collect(Collectors.toList());

		// List<AccessResponse> lstAccessPageStakeHolders = this.accessManagerService
		// .stakeHoldersAccess(userEntity.getStakeHolders(), userEntity.getAppId());

		List<AccessResponse> lstAccessPageStakeHolders = this.accessManagerService.UserAccess(roleIds, subRoleIds,
				userEntity.getAppId());

		responseModel.setUserdata(userEntity);
		responseModel.setUseraccess(lstAccessPageStakeHolders);

		return responseModel;
	}

	public List<UserListResponse> getUserList(UserListRequest userListRequest) {
		if ((userListRequest.getStakeholdertypeID() == null)
				&& (userListRequest.getBranchID() == null || userListRequest.getBranchID().isEmpty())
				&& (userListRequest.getOrgID() == null || userListRequest.getOrgID().isEmpty())
				&& (userListRequest.getPortId() == null)) {
			throw new RuntimeException("All fields can't be blank");
		}

		List<UserRole> lstUserRole = this.userRoleRepository
				.findByStakeholderTypeId(userListRequest.getStakeholdertypeID());

		if (lstUserRole.isEmpty()) {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.DATA_NOT_FOUND.getValue());
		}

		List<Integer> lstuserIds = new ArrayList<>();

		lstUserRole.forEach(x -> {
			lstuserIds.add(x.getUserRegistrationId());
		});
		List<Users> lstUsers = new ArrayList<>();

		String branch_ID = userListRequest.getBranchID();
		String org_ID = userListRequest.getOrgID();
		Integer portId = userListRequest.getPortId();

		if (portId != null) {
			lstUsers = this.userRepository.findByUsers(userListRequest.getStakeholdertypeID(),
					userListRequest.getPortId(), branch_ID);
		} else {

			if (branch_ID == null || branch_ID == "") {
				if ((branch_ID == null || branch_ID == "") && (org_ID == null || org_ID == "")) {
					lstUsers = this.userRepository.findAllById(lstuserIds);

				} else {
					lstUsers = this.userRepository.findAllByIdInAndOrgID(lstuserIds, org_ID);
				}
			}

			else {
				lstUsers = this.userRepository.findAllByIdInAndOrgIDAndBranchID(lstuserIds, org_ID, branch_ID);
			}
		}
		List<PortMaster> lstPortMaster = apiService.getByKeysPortMaster();
		List<UserListResponse> list = new ArrayList<>();
		for (Users users : lstUsers) {
			UserListResponse response = new UserListResponse();
			response.setId(users.getId());
			response.setUserId(users.getUserId());
			response.setOrgID(users.getOrgID());
			response.setBranchID(users.getBranchID());
			response.setEmail(users.getEmail());
			response.setUserName(users.getEmployeeName());
			response.setUuid(users.getIamUserID());
			if (users.getOrgID() != null) {
				UserOrganization userOrganization = userOrganizationRepository.findByOrgID(users.getOrgID());
				if (userOrganization != null) {
					response.setOrgName(userOrganization.getOrgName());
				}

			}
			if (users.getBranchID() != null) {
				UserBranch userBranch = userBranchRepository.findByBranchID(users.getBranchID());
				if (userBranch != null) {
					Integer portRid = userBranch.getPortRid();

					if (portRid != null) {
						PortMaster portMaster = lstPortMaster.stream().filter(obj -> obj.getId().equals(portRid))
								.findAny().orElse(new PortMaster());

						response.setPortNameEnglish(portMaster.getPortNameEnglish());
						response.setPortNameArabic(portMaster.getPortNameArabic());

					}
					response.setBranchName(userBranch.getBranchName());
				}
			}
			list.add(response);
		}
		return list;
	}

	public ApiResponse<String> getUserExist(UserValidateRequest v) {
		String user_id = v.getUserId();
		String email = v.getEmail();
		ApiResponse<String> apiResponse;

		if ((user_id != null && !user_id.isEmpty()) && (email != null && !email.isEmpty())) {
			Users existEmail = userRepository.findByEmail(email);
			UserOrganization existOrgEmail = userOrganizationRepository.findByEmailId(email);
			Users existUserId = userRepository.findByUserId(user_id);
			UserOrganization existOrgUserId = userOrganizationRepository.findByUserId(user_id);
			if ((existEmail != null || existOrgEmail != null) && (existUserId != null || existOrgUserId != null)) {
				apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Exists",
						"User with same userid and email already exists");
			} else if (existUserId != null || existOrgUserId != null) {
				apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Exists",
						ExceptionMessage.USR_EXISTS.getValue() + user_id);
			} else if (existEmail != null || existOrgEmail != null) {
				apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Exists",
						ExceptionMessage.EMAIL_EXISTS.getValue() + email);
			} else {
				apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Not Exists",
						"User with same userid and email not exists");
			}

			return apiResponse;

		}

		if (user_id != null && !user_id.isEmpty()) {
			Users existUserId = userRepository.findByUserId(user_id);
			UserOrganization existOrgUserId = userOrganizationRepository.findByUserId(user_id);

			if (existUserId != null || existOrgUserId != null) {

				apiResponse = new ApiResponse<String>(HttpStatus.OK.value(), "Exists",
						ExceptionMessage.USR_EXISTS.getValue() + user_id);

				return apiResponse;

			} else {
				apiResponse = new ApiResponse<String>(HttpStatus.OK.value(), "Not Exists",
						ExceptionMessage.USER_ID_NOT_FOUND.getValue() + user_id);

				return apiResponse;

			}
		}
		if (email != null && !email.isEmpty()) {
			Users existEmail = userRepository.findByEmail(email);
			UserOrganization existOrgEmail = userOrganizationRepository.findByEmailId(email);
			if (existEmail != null || existOrgEmail != null) {

				apiResponse = new ApiResponse<String>(HttpStatus.OK.value(), "Exists",
						ExceptionMessage.EMAIL_EXISTS.getValue() + email);

				return apiResponse;
			} else {
				apiResponse = new ApiResponse<String>(HttpStatus.OK.value(), "Not Exists",
						ExceptionMessage.EMAIL_NOT_FOUND.getValue() + email);

				return apiResponse;
			}
		}

		throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "All fields can't be blank");

	}

	public List<PortMaster> getPorts() {
		RoleMaster response = roleMasterService.getByCode(CommonCodes.POADM);

		List<UserRole> lstuserRoles = this.userRoleRepository.findByStakeholderTypeId(response.getId());
		CodeMasterRequest objCodeMasterRequest = new CodeMasterRequest();
		objCodeMasterRequest.getKey1().add(CommonCodes.PORT_TYPES);

		List<CodeMaster> lstCodeMaster = this.apiService.getByKeysCodeMaster(objCodeMasterRequest);
		String strLocationMaster = this.apiService.getAllLocation(CommonCodes.CUSTOMS_COUNTRY);
		LocationListResponse locationMasterResponse = new LocationListResponse();
		try {
			if (strLocationMaster != null) {
				locationMasterResponse = objectMapper.readValue(strLocationMaster, LocationListResponse.class);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		List<LocationMaster> lstLocationMaster = locationMasterResponse.getData();
		LocationMaster objLocationMaster = lstLocationMaster.stream()
				.filter(x -> x.getLocationCode().equals(CommonCodes.SA)).findAny().orElse(new LocationMaster());
		CodeMaster objCodeMaster = lstCodeMaster.stream().filter(x -> x.getCode().equals(CommonCodes.SE)).findAny()
				.orElse(new CodeMaster());

		List<Integer> lstAsssignedPorts = new ArrayList<>();

		lstuserRoles.forEach(x -> {

			lstAsssignedPorts.add(x.getUsers().getPortRID());

		});

		List<PortMaster> lstPorts = this.apiService.getByKeysPortMaster();
		List<PortMaster> lstUnassignedPorts = lstPorts.stream()
				.filter(y -> y.getCountryId().equals(objLocationMaster.getId())
						&& y.getPortTypeId().equals(objCodeMaster.getId()))
				.collect(Collectors.toList());
		lstAsssignedPorts.forEach(ports -> {
			PortMaster objPortMaster = lstUnassignedPorts.stream().filter(x -> x.getId().equals(ports)).findAny()
					.orElse(new PortMaster());
			lstUnassignedPorts.remove(objPortMaster);
		});

		return lstUnassignedPorts;
	}

	public List<UsersListResponse> userByIds(UserByIdsRequest userByIdsRequest) {
		List<Users> lstUsers = this.userRepository.findByIamUserIDIn(userByIdsRequest.getIAmUserIds());
		if (lstUsers.isEmpty()) {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.DATA_NOT_FOUND.getValue());
		}

		List<UsersListResponse> lstUserDetails = new ArrayList<>();

		for (Users objUser : lstUsers) {
			UsersListResponse objListResponse = new UsersListResponse();
			if (objListResponse.getIAmUserId() != objUser.getIamUserID()) {
				objListResponse.setIAmUserId(objUser.getIamUserID());
				objListResponse.setUserName(objUser.getEmployeeName());
				objListResponse.setUserId(objUser.getUserId());

				lstUserDetails.add(objListResponse);
			}

		}

		return lstUserDetails;
	}

	public Users updateUser(Users user) {
		List<Users> userDetails = userRepository.findByIamUserID(user.getIamUserID());
		if (userDetails.isEmpty()) {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(),
					ExceptionMessage.USER_ID_NOT_FOUND.getValue() + user.getIamUserID());
		}
		Users detailsUser = userDetails.get(0);
		user.setId(detailsUser.getId());
		user.setIamUserID(detailsUser.getIamUserID());
		user.setAppId(detailsUser.getAppId());
		user.setEmployeeCode(detailsUser.getEmployeeCode());
		user.setEmployeeName(detailsUser.getEmployeeName());
		user.setJoiningDate(detailsUser.getJoiningDate());
		user.setDepartmentRID(detailsUser.getDepartmentRID());
		user.setDesignationRID(detailsUser.getDesignationRID());
		user.setPortRID(detailsUser.getPortRID());
		user.setPhoneNo(detailsUser.getPhoneNo());
		user.setDefaultGroupTypeRID(detailsUser.getDefaultGroupTypeRID());
		user.setEmail(detailsUser.getEmail());
		user.setOrgID(detailsUser.getOrgID());
		user.setStakeHolders(detailsUser.getStakeHolders());
		user.setBranchID(detailsUser.getBranchID());
		user.setUserId(detailsUser.getUserId());
		user.setUserType(detailsUser.getUserType());
		user.setPortUserNo(detailsUser.getPortUserNo());
		user.setProfileImagePath(detailsUser.getProfileImagePath());
		user.setNationalId(detailsUser.getNationalId());
		user.setCreatedDate(detailsUser.getCreatedDate());
		user.setCreatedBy(detailsUser.getCreatedBy());
		user.setUpdatedDate(LocalDateTime.now());
		user.setIsActive(detailsUser.getIsActive());
		return userRepository.save(user);
	}

	@Transactional
	public ResponseEnvelope<String> fasahUserCreation(UserRequest userRequest) {

		UserEntity objUserEntity = this.userEntityRepository.findByUsername(userRequest.getUserName());
		if (objUserEntity != null) {

			List<RoleMaster> lstRoleMaster = this.roleMasterRepository.findByRoleCodeIn(userRequest.getRoles());
			PortCode portRid = this.portCodeRepo.findByPortCodeAndPortTypeId(userRequest.getPortRid(),
					PortsType.getByPortsType(userRequest.getPortType()).getPortsType());
			int portRId = (portRid != null) ? portRid.getId().intValue() : 0;

			UserOrganizationRequest organizationRequest = userRequest.getOrganization();
			UserOrganization objOrganization = this.userOrganizationRepository
					.findByOrgID(organizationRequest.getOrgId());
			boolean isNewOrg = false;
			if (objOrganization == null) {
				objOrganization = new UserOrganization();
				isNewOrg = true;
			}
			objOrganization.setOrgID(organizationRequest.getOrgId());
			objOrganization.setOrgName(organizationRequest.getOrgName());
//			objOrganization.setCrn(organizationRequest.getCrn());
			objOrganization.setEmailId(objUserEntity.getEmail());
			if (!doNotMigrateRolesIfExist) {
				objOrganization.setStakeholdertypeRid(lstRoleMaster.get(0).getId());
			} else {
				if (isNewOrg) {
					objOrganization.setStakeholdertypeRid(lstRoleMaster.get(0).getId());
				}
			}
			objOrganization.setMobileNumber(Long.parseLong(CommonCodes.PHN));
			objOrganization.setRegistrationThroughRid(2573);
			objOrganization.setDocumentTypeRid(2517);
			objOrganization.setRegistrationTypeRid(2576);
			objOrganization.setDocumentNo("0987609809890000000");
			objOrganization.setNationalId("909000000");
			objOrganization.setApprovalStatusRid(CommonCodes.APPROVED_CODE);
			objOrganization.setUserId(objUserEntity.getUsername());
			objOrganization.setUnitNo("010");
			objOrganization.setZipCode(organizationRequest.getZipCode());
			objOrganization.setBuildingNo("B10101");
			objOrganization.setStreet("street-A01");
			objOrganization.setDistrictRid("10");
			objOrganization.setCityRid(275);
			objOrganization.setLicenceNo(organizationRequest.getLicenceNo());
			objOrganization.setAddress(organizationRequest.getAddress());
			objOrganization.setIsActive(true);
			objOrganization.setCreatedBy(CommonCodes.TABADUL);
			objOrganization.setUpdatedBy(CommonCodes.TABADUL);
			objOrganization.setCreatedDate(LocalDateTime.now());
			objOrganization = this.userOrganizationRepository.save(objOrganization);

			UserBranchRequest branchRequest = userRequest.getBranch();
			UserBranch userBranch = userBranchRepository.findByBranchID(branchRequest.getBranchID());
			if (userBranch == null) {
				userBranch = new UserBranch();
			}
			userBranch.setBranchID(branchRequest.getBranchID());
			userBranch.setOrgId(objOrganization.getId());
			userBranch.setBranchName(branchRequest.getBranchName());
			userBranch.setLocationRid(55);
			userBranch.setPortRid(portRId);
			userBranch.setCreatedBy(CommonCodes.TABADUL);
			userBranch.setCreatedDate(LocalDateTime.now());
			userBranch.setUpdatedBy(CommonCodes.TABADUL);
			userBranch.setUpdatedDate(LocalDateTime.now());
			userBranch.setIsActive(true);
			userBranch = userBranchRepository.save(userBranch);

			List<Users> users = userRepository.findByIamUserID(objUserEntity.getId());
			Users objUsers = null;
			if (!users.isEmpty()) {
				objUsers = users.get(0);
			} else {
				objUsers = new Users();
			}
			objUsers.setIamUserID(objUserEntity.getId());
			objUsers.setEmployeeName(objUserEntity.getFirstName() + " " + objUserEntity.getLastName());
			objUsers.setEmployeeCode(objUserEntity.getRealmId() + objUserEntity.getCreatedTimestamp());
			objUsers.setUserId(objUserEntity.getUsername());
			objUsers.setDepartmentRID(0);
			objUsers.setDesignationRID(0);
			objUsers.setDefaultGroupTypeRID(0);
			objUsers.setIamUserID(objUserEntity.getId());
			objUsers.setEmail(objUserEntity.getEmail());
			objUsers.setJoiningDate(LocalDate.now());
			objUsers.setCreatedBy(CommonCodes.TABADUL);
			objUsers.setPhoneNo(Long.parseLong(CommonCodes.PHN));
			if (userRequest.getUserType().equals(CommonCodes.PCS)) {

				objUsers.setUserType(CommonCodes.BRANCH_USER);
				objUsers.setAppId(CommonCodes.PCS);
				objUsers.setOrgID(organizationRequest.getOrgId());
				objUsers.setBranchID(branchRequest.getBranchID());
				objUsers.setPortRID(portRId);
			} else if (userRequest.getUserType().equals(CommonCodes.PMIS)) {

				objUsers.setAppId(CommonCodes.PMIS);
				objUsers.setPortRID(portRId);
			}
			userRepository.save(objUsers);

			List<UserRole> objStakeHolders = new ArrayList<>();
			List<UserRole> userRolls = userRoleRepository.findByUsers(objUsers);
			if (!doNotMigrateRolesIfExist) {
				if (!userRolls.isEmpty()) {
					userRoleRepository.deleteAll(userRolls);
				}
				for (RoleMaster role : lstRoleMaster) {
					UserRole objUserRole = new UserRole();
					objUserRole.setStakeholderTypeId(role.getId());
					objUserRole.setStakeholderSubroleId(0);
					objUserRole.setStakeholderCode(role.getRoleCode());
					objStakeHolders.add(objUserRole);
					objUserRole.setUsers(objUsers);
				}
				userRoleRepository.saveAll(objStakeHolders);
			} else {
				if (userRolls.isEmpty()) {
					for (RoleMaster role : lstRoleMaster) {
						UserRole objUserRole = new UserRole();
						objUserRole.setStakeholderTypeId(role.getId());
						objUserRole.setStakeholderSubroleId(0);
						objUserRole.setStakeholderCode(role.getRoleCode());
						objStakeHolders.add(objUserRole);
						objUserRole.setUsers(objUsers);
					}
					userRoleRepository.saveAll(objStakeHolders);
				}

			}
			objUsers.setStakeHolders(objStakeHolders);

			if (objUsers.getStakeHolders() != null || objUsers.getStakeHolders().isEmpty()) {
				objUsers.getStakeHolders().forEach(x -> {

					x.setCreatedBy(CommonCodes.TABADUL);
					x.setUpdatedBy(CommonCodes.TABADUL);

				});
			}

			Users savedUsser = this.userRepository.save(objUsers);

			if (savedUsser != null) {
				ResponseEnvelope<String> apiResponse = new ResponseEnvelope<>();
				apiResponse.setResponseCode(HttpStatus.CREATED.value());
				apiResponse.setResponseMessage(CommonCodes.USER_CREATED);
				apiResponse.setData("");

				return apiResponse;
			}

		}
		return null;
	}

	public List<UserPcListResponse> getPcUserList(UserPcRequset userListRequest) {
		if (userListRequest.getStakeHolderTypeId() == null && userListRequest.getPortId() == null
				&& userListRequest.getBranchId() == null) {
			throw new RuntimeException("All fields can't be blank");
		}
		RoleMaster objRoleMaster = this.roleMasterRepository.findByRoleCode(userListRequest.getStakeHolderTypeId());
		List<UserOrganization> lstOrg = userOrganizationRepository.findByStakeholdertypeRid(objRoleMaster.getId());
		List<Integer> lstOrgIds = lstOrg.stream().map(UserOrganization::getId).collect(Collectors.toList());

		List<UserBranch> lstBranch = userBranchRepository.findByOrgIdInAndPortRid(lstOrgIds,
				userListRequest.getPortId());

		List<UserPcListResponse> lstResponse = new ArrayList<>();
		for (UserOrganization org : lstOrg) {
			for (UserBranch branch : lstBranch) {
				if (org.getId().equals(branch.getOrgId())) {
					UserPcListResponse response = new UserPcListResponse();
					response.setOrgId(org.getOrgID());
					response.setOrgName(org.getOrgName());
					response.setId(branch.getId());
					response.setBranchId(branch.getBranchID());
					response.setBranchName(branch.getBranchName());
					lstResponse.add(response);
				}
			}
		}
		if (userListRequest.getBranchId() != null) {
			lstResponse = lstResponse.stream().filter(x -> x.getId().equals(userListRequest.getBranchId()))
					.collect(Collectors.toList());
		}
		return lstResponse;
	}

	public ResponseEnvelope<String> resetPassword(ResetPasswordRequest passwordRequest, String token) {
		String apiUrl;
		if (passwordRequest.getAppId().equals(CommonCodes.PCS)) {
			apiUrl = propertiesConfig.getKeyCloakBaseUrlPcs() + String.format(propertiesConfig.getKeyCloakResetPass(),
					CommonCodes.STRPCS, passwordRequest.getIamUserId());
		} else {
			apiUrl = propertiesConfig.getKeyCloakBaseUrl() + String.format(propertiesConfig.getKeyCloakResetPass(),
					CommonCodes.STRPMIS, passwordRequest.getIamUserId());
		}
		UserCredential objCredential = modelMapper.map(passwordRequest, UserCredential.class);
		String requestBody = null;
		try {
			objectMapper.setSerializationInclusion(Include.NON_EMPTY);
			requestBody = objectMapper.writeValueAsString(objCredential);
		} catch (JsonProcessingException e) {
		}

		Integer respStatus = this.apiService.restTemplatePut(apiUrl, requestBody, token);

		ResponseEnvelope<String> responseEnvelope = new ResponseEnvelope<>();
		if (respStatus.equals(HttpStatus.NO_CONTENT.value())) {
			responseEnvelope.setData("");
			responseEnvelope.setResponseMessage("Password reset successfully");
			responseEnvelope.setResponseCode(HttpStatus.OK.value());
		} else {
			responseEnvelope.setData("");
			responseEnvelope.setResponseMessage(HttpStatus.valueOf(respStatus).getReasonPhrase());
			responseEnvelope.setResponseCode(respStatus);
		}
		return responseEnvelope;

	}

	public ApiResponse<String> sendOtp(UserValidateRequest v) {
		String mobileNo = v.getMobileNo();
		String email = v.getEmail();
		ApiResponse<String> apiResponse;

		if ((mobileNo != null && !mobileNo.isEmpty()) && (email != null && !email.isEmpty())) {
			Users existEmail = userRepository.findByEmail(email);
			UserOrganization existOrgEmail = userOrganizationRepository.findByEmailId(email);
			Users existMobileNo = userRepository.findByPhoneNo(Long.parseLong(mobileNo));
			UserOrganization existOrgMobileNo = userOrganizationRepository.findByMobileNumber(Long.parseLong(mobileNo));
			if ((existEmail != null || existOrgEmail != null) && (existMobileNo != null || existOrgMobileNo != null)) {
				apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Exists", CommonCodes.USER_EXIST);
			} else if (existMobileNo != null || existOrgMobileNo != null) {
				apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Exists",
						ExceptionMessage.MOB_EXISTS.getValue() + mobileNo);
			} else if (existEmail != null || existOrgEmail != null) {
				apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Exists",
						ExceptionMessage.EMAIL_EXISTS.getValue() + email);
			} else {
				apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Not Exists", CommonCodes.USER_NOT_EXIST);
			}

			return apiResponse;

		}

		if (mobileNo != null && !mobileNo.isEmpty()) {
			Users existMobileNo = userRepository.findByPhoneNo(Long.parseLong(mobileNo));
			UserOrganization existOrgMobileNo = userOrganizationRepository.findByMobileNumber(Long.parseLong(mobileNo));

			if (existMobileNo != null || existOrgMobileNo != null) {

				apiResponse = new ApiResponse<String>(HttpStatus.OK.value(), "Exists",
						ExceptionMessage.MOB_EXISTS.getValue() + mobileNo);

				return apiResponse;

			} else {

				Integer otp = generateOtp();
				httpSession.setAttribute("otp", otp);
				SmsTemplate smsTemplate = new SmsTemplate();
				smsTemplate.setMobile(mobileNo);
				smsTemplate.setSmsEng(CommonCodes.VERIFY_OTP + otp.toString() + " ");
				smsTemplate.setSmsArb("التحقق من كلمة المرور لمرة واحدة - " + otp.toString() + " ");

				apiResponse = new ApiResponse<String>(HttpStatus.OK.value(), "Not Exists",
						commonService.SendSms(smsTemplate) + mobileNo);

				// Get the current timestamp in milliseconds
				// Get the current date-time
				Date currentDate = new Date();

				// Format the date-time as a string
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentDateTime = dateFormat.format(currentDate);

				// Save the current date-time in the session
				httpSession.setAttribute("currentDateTime", currentDateTime);

				return apiResponse;

			}
		}
		if (email != null && !email.isEmpty()) {
			Users existEmail = userRepository.findByEmail(email);
			UserOrganization existOrgEmail = userOrganizationRepository.findByEmailId(email);
			if (existEmail != null || existOrgEmail != null) {

				apiResponse = new ApiResponse<String>(HttpStatus.OK.value(), "Exists",
						ExceptionMessage.EMAIL_EXISTS.getValue() + email);

				return apiResponse;
			} else {

				Integer otp = generateOtp();
				httpSession.setAttribute("otp", otp);
				EmailTemplate emailTemplate = new EmailTemplate();
				emailTemplate.setEmail(email);
				emailTemplate.setEmailHeaderEng(CommonCodes.VERIFY_EMAIL_OTP);
				emailTemplate.setEmailHeaderArb("OTP للتحقق من البريد الإلكتروني");
				String body = "<!DOCTYPE html>\r\n" + "<html lang=\"en\">\r\n" + "<head>\r\n"
						+ "    <meta charset=\"UTF-8\">\r\n"
						+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n"
						+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
						+ "    <title>OTP Verification</title>\r\n" + "</head>\r\n" + "<body>\r\n"
						+ "    <div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;\">\r\n"
						+ "        <h2 style=\"color: #333;\">OTP Verification</h2>\r\n" + "        <p>Hello,</p>\r\n"
						+ "        <p>Your One-Time Password (OTP) for verification is:</p>\r\n"
						+ "        <div style=\"background-color: #f5f5f5; padding: 10px; border-radius: 5px; margin-bottom: 20px;\">\r\n"
						+ "            <h3 style=\"margin: 0; font-size: 24px; color: #333;\">" + otp + "</h3>\r\n"
						+ "        </div>\r\n" + "        <p>Please use this OTP to verify your email address.</p>\r\n"
						+ "        <p>If you didn't request this verification, please ignore this email.</p>\r\n"
						+ "        <p>Best regards,<br>Tabadul</p>\r\n" + "    </div>\r\n" + "</body>\r\n" + "</html>";
				emailTemplate.setEmailBodyEng(body);

				apiResponse = new ApiResponse<String>(HttpStatus.OK.value(), "Not Exists",
						commonService.sendEmail(emailTemplate) + email);

				LocalDateTime now = LocalDateTime.now();
				httpSession.setAttribute("currentDateTime", now);

				return apiResponse;
			}
		}

		throw new BusinessException(HttpStatus.BAD_REQUEST.value(), CommonCodes.FIELDS_CANT_BLANK);

	}

	public ApiResponse<String> verifyOtp(UserValidateRequest v) {

		ApiResponse<String> apiResponse;
		try {
			Integer otp = v.getOtp();
			Integer otpValue = (Integer) httpSession.getAttribute("otp");
			

			// Get the current date-time
			LocalDateTime currentDate = LocalDateTime.now();
			LocalDateTime sessionDateTime = (LocalDateTime) httpSession.getAttribute("currentDateTime");

			Duration duration = Duration.between(sessionDateTime, currentDate);

			long minutes = duration.toMinutes() % 60;

			if (minutes < 5) {
				if (!otp.equals(otpValue)) {
					apiResponse = new ApiResponse<String>(HttpStatus.OK.value(), "Failure",
							CommonCodes.OTP_NOT_VERIFIED);
				} else {
					apiResponse = new ApiResponse<String>(HttpStatus.NOT_FOUND.value(), "Success",
							CommonCodes.OTP_VERIFIED);
				}
			} else {
				apiResponse = new ApiResponse<String>(HttpStatus.OK.value(), "Failure", CommonCodes.OTP_EXPIRED);
			}
		} catch (Exception e) {
			apiResponse = new ApiResponse<String>(HttpStatus.OK.value(), "Failure", CommonCodes.OTP_EXPIRED);
			logger.error(e.getMessage());
		}

		return apiResponse;
	}

	public Integer generateOtp() {
		Random random = new Random();

		// Generate a random integer between 100000 and 999999 (inclusive)
		int randomSixDigitNumber = 100000 + random.nextInt(900000);

		return randomSixDigitNumber;

	}

}
