package sa.tabadul.useraccessmanagement.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.common.enums.ExceptionMessage;
import sa.tabadul.useraccessmanagement.common.helpers.ApprovalStatusCode;
import sa.tabadul.useraccessmanagement.common.models.request.ApproveRejectRequest;
import sa.tabadul.useraccessmanagement.common.models.request.CodeMasterRequest;
import sa.tabadul.useraccessmanagement.common.models.request.LicenseApproveRejectRequest;
import sa.tabadul.useraccessmanagement.common.models.request.Pagination;
import sa.tabadul.useraccessmanagement.common.models.request.PortPermitFilter;
import sa.tabadul.useraccessmanagement.common.models.request.PortPermitKeys;
import sa.tabadul.useraccessmanagement.common.models.request.PortPermitListRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserBranchKeys;
import sa.tabadul.useraccessmanagement.common.models.request.UserPortPermitFilter;
import sa.tabadul.useraccessmanagement.common.models.response.LicenseApplicationResponse;
import sa.tabadul.useraccessmanagement.common.models.response.LocationListResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PaginationResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PortCodeResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PortPermitList;
import sa.tabadul.useraccessmanagement.common.models.response.PortPermitListResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PortPermitResponseDTO;
import sa.tabadul.useraccessmanagement.common.models.response.ResponseEnvelope;
import sa.tabadul.useraccessmanagement.common.service.CommonService;
import sa.tabadul.useraccessmanagement.common.service.UserManagementExternalService;
import sa.tabadul.useraccessmanagement.domain.CodeMaster;
import sa.tabadul.useraccessmanagement.domain.LicenseApplication;
import sa.tabadul.useraccessmanagement.domain.LicenseApplicationApproval;
import sa.tabadul.useraccessmanagement.domain.LicenseOrganization;
import sa.tabadul.useraccessmanagement.domain.LocationMaster;
import sa.tabadul.useraccessmanagement.domain.PortMaster;
import sa.tabadul.useraccessmanagement.domain.PortPermitApplication;
import sa.tabadul.useraccessmanagement.domain.PortPermitApplicationApproval;
import sa.tabadul.useraccessmanagement.domain.PortPermitLicense;
import sa.tabadul.useraccessmanagement.domain.RoleMaster;
import sa.tabadul.useraccessmanagement.domain.UserBranch;
import sa.tabadul.useraccessmanagement.domain.UserOrganization;
import sa.tabadul.useraccessmanagement.domain.Users;
import sa.tabadul.useraccessmanagement.exception.BusinessException;
import sa.tabadul.useraccessmanagement.repository.LicenseApplicationRepository;
import sa.tabadul.useraccessmanagement.repository.LicenseOrganizationRepository;
import sa.tabadul.useraccessmanagement.repository.PortLicenseRepository;
import sa.tabadul.useraccessmanagement.repository.PortPermitApplicationApprovalRepository;
import sa.tabadul.useraccessmanagement.repository.PortPermitApplicationRepository;
import sa.tabadul.useraccessmanagement.repository.PortPermitSpecification;
import sa.tabadul.useraccessmanagement.repository.RoleMasterRepository;
import sa.tabadul.useraccessmanagement.repository.UserBranchRepository;
import sa.tabadul.useraccessmanagement.repository.UserOrganizationRepository;
import sa.tabadul.useraccessmanagement.repository.UserRepository;

@Service
public class UserPortPermitService {

	@Autowired
	PortPermitApplicationRepository permitApplicationRepository;

	@Autowired
	PortPermitApplicationApprovalRepository permitApplicationApprovalRepository;

	@Autowired
	UserOrganizationRepository organizationRepository;

	@Autowired
	RoleMasterRepository roleMasterRepository;

	@Autowired
	LicenseApplicationRepository licenseApplicationRepository;

	@Autowired
	UserManagementExternalService restService;

	@Autowired
	ApprovalStatusCode approvalStatusCode;

	@Autowired
	UserBranchRepository branchRepository;

	@Autowired
	CommonService commonService;

	@Autowired
	UserRepository userRepo;

	@Autowired
	PortLicenseRepository portLicenseRepository;

	@Autowired
	LicenseOrganizationRepository orgLicenseRepository;

	private static final Logger logger = LogManager.getLogger(UserPortPermitService.class);

	ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.registerModule(new JavaTimeModule());

	public PortPermitResponseDTO savePortPermit(PortPermitApplication portPermitApplication) {
		LocalDateTime dateObj = LocalDateTime.now();
		DateTimeFormatter formatObj = DateTimeFormatter.ofPattern(CommonCodes.CRN_FORMAT);

		if (portPermitApplication.getCrn() == null) {
			portPermitApplication.setCrn(Long.parseLong(dateObj.format(formatObj)));
			portPermitApplication.setRequestStatusRid(permitIsActive(portPermitApplication.getApprovalStatusRid(),
					portPermitApplication.getRequestTypeRid()));

			portPermitApplication = this.permitApplicationRepository.save(portPermitApplication);
		} else {
			PortPermitApplication portPermitDetails = permitApplicationRepository
					.findByCrn(portPermitApplication.getCrn());
			if (portPermitDetails == null) {
				throw new BusinessException(HttpStatus.NOT_FOUND.value(),
						ExceptionMessage.CRN_NOT_FOUND.getValue() + portPermitApplication.getCrn());
			}

			if (portPermitApplication.getRequestTypeRid().equals(5)) {
				portPermitDetails.setIsActive(false);
				portPermitDetails.setUpdatedDate(LocalDateTime.now());
				portPermitApplication.setRequestStatusRid(permitIsActive(portPermitApplication.getApprovalStatusRid(),
						portPermitApplication.getRequestTypeRid()));
				permitApplicationRepository.save(portPermitDetails);
				LocalDateTime dateObjs = LocalDateTime.now();
				DateTimeFormatter formatObjs = DateTimeFormatter.ofPattern(CommonCodes.CRN_FORMAT);
				portPermitApplication.setCrn(Long.parseLong(dateObjs.format(formatObjs)));
				portPermitApplication.setId(null);
			}
			portPermitApplication.setRequestStatusRid(permitIsActive(portPermitApplication.getApprovalStatusRid(),
					portPermitApplication.getRequestTypeRid()));
			portPermitApplication.setCreatedBy(portPermitDetails.getCreatedBy());
			portPermitApplication.setCreatedDate(portPermitDetails.getCreatedDate());
			portPermitApplication.setUpdatedDate(LocalDateTime.now());
			portPermitApplication = this.permitApplicationRepository.save(portPermitApplication);
		}

		PortPermitResponseDTO responseDTO = new PortPermitResponseDTO();
		responseDTO.setCrn(portPermitApplication.getCrn());
		responseDTO.setCreatedDate(portPermitApplication.getCreatedDate());
		responseDTO.setCreatedBy(portPermitApplication.getCreatedBy());

		return responseDTO;
	}

	public ResponseEnvelope<PortPermitApplication> portPermitApproveReject(ApproveRejectRequest approveRejectRequest) {
		PortPermitApplication objPermitApplication = this.permitApplicationRepository
				.findByCrn(approveRejectRequest.getCrn());

		CodeMasterRequest objCodeMasterRequest = new CodeMasterRequest();
		objCodeMasterRequest.getKey1().add(CommonCodes.PERMIT_STATUS);
		List<CodeMaster> lstCodeMaster = this.restService.getByKeysCodeMaster(objCodeMasterRequest);

		objPermitApplication.setApprovalStatusRid(approveRejectRequest.getApprovalStatusRid());
		objPermitApplication.setRequestTypeRid(approveRejectRequest.getRequestTypeRid());
		objPermitApplication.setRequestStatusRid(
				permitIsActive(approveRejectRequest.getApprovalStatusRid(), approveRejectRequest.getRequestTypeRid()));
		objPermitApplication.setUpdatedBy(approveRejectRequest.getCreatedBy());
		objPermitApplication.setUpdatedDate(LocalDateTime.now());
		objPermitApplication.setFileName(approveRejectRequest.getFileName());
		objPermitApplication.setFilePath(approveRejectRequest.getFilePath());
		objPermitApplication.setSuspensionTo(approveRejectRequest.getSuspensionTo());

		permitApplicationRepository.save(objPermitApplication);

		PortPermitApplicationApproval objApplicationApproval = new PortPermitApplicationApproval();
		objApplicationApproval.setApprovalStatusRid(objPermitApplication.getApprovalStatusRid());
		objApplicationApproval.setPortPermitApplicationId(objPermitApplication.getId());
		objApplicationApproval.setRemark(approveRejectRequest.getRemark());
		objApplicationApproval.setRequestTypeRid(approveRejectRequest.getRequestTypeRid());
		objApplicationApproval.setUserRid(approveRejectRequest.getCreatedBy());
		objApplicationApproval.setCreatedBy(approveRejectRequest.getCreatedBy());
		objApplicationApproval.setUpdatedBy(approveRejectRequest.getCreatedBy());
		objApplicationApproval.setCreatedDate(LocalDateTime.now());
		PortPermitApplicationApproval objPermitApplicationApproval = this.permitApplicationApprovalRepository
				.save(objApplicationApproval);

		if (objPermitApplicationApproval != null) {
			CodeMaster objCodeMaster = lstCodeMaster.stream()
					.filter(x -> x.getCode().equals(String.valueOf(approveRejectRequest.getApprovalStatusRid())))
					.findFirst().orElse(new CodeMaster());
			ResponseEnvelope<PortPermitApplication> apiResponseEnvelope = new ResponseEnvelope<>(HttpStatus.OK.value(),
					statusMessage(approveRejectRequest.getRequestTypeRid(),
							approveRejectRequest.getApprovalStatusRid()),
					objPermitApplication);
			return apiResponseEnvelope;
		} else {
			throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					ExceptionMessage.SOMETHING_WENT_WRONG.getValue());
		}
	}

	private CodeMaster getCodeMasterByCode(List<CodeMaster> lstCodeMaster, String code) {
		return lstCodeMaster.stream().filter(x -> x.getCode().equals(code)).findFirst().orElse(new CodeMaster());
	}

	public PortPermitApplication portPermitCrn(Long crn) {

		PortPermitApplication objPermitApplication = permitApplicationRepository.findByCrn(crn);
		CodeMasterRequest objCodeMasterRequest = new CodeMasterRequest();
		objCodeMasterRequest.getKey1().add(CommonCodes.PERMIT_STATUS);
		objCodeMasterRequest.getKey1().add(CommonCodes.PERMIT_REQUEST_TYPE);
		List<CodeMaster> lstCodeMaster = this.restService.getByKeysCodeMaster(objCodeMasterRequest);

		List<CodeMaster> lstApprovalStatus = lstCodeMaster.stream()
				.filter(x -> x.getKey1().equals(CommonCodes.PERMIT_STATUS)).collect(Collectors.toList());

		List<CodeMaster> lstRequestType = lstCodeMaster.stream()
				.filter(x -> x.getKey1().equals(CommonCodes.PERMIT_REQUEST_TYPE)).collect(Collectors.toList());

		if (objPermitApplication != null) {
			PortMaster objPort = new PortMaster();
			String strPort = this.restService.getPort(objPermitApplication.getPortRid());
			if (strPort != null) {
				try {
					objPort = objectMapper.readValue(strPort, PortCodeResponse.class).getData();
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				}
				objPermitApplication.setPortCode(objPort.getPortCode());
				objPermitApplication.setPortNameEng(objPort.getPortNameEnglish());
				objPermitApplication.setPortNameArabic(objPort.getPortNameArabic());

			}
			if (!lstRequestType.isEmpty()) {
				CodeMaster objStatus = lstRequestType.stream()
						.filter(x -> x.getCode().equals(String.valueOf(objPermitApplication.getRequestTypeRid())))
						.findAny().orElse(new CodeMaster());

				objPermitApplication.setRequestTypeEnglish(objStatus.getCodeDescriptionEnglish());
				objPermitApplication.setRequestTypeArabic(objStatus.getCodeDescriptionArabic());

			}
			if (!lstApprovalStatus.isEmpty()) {
				objPermitApplication.setRequestStatusRid(objPermitApplication.getApprovalStatusRid());

				CodeMaster objStatus = lstApprovalStatus.stream()
						.filter(x -> x.getCode().equals(String.valueOf(objPermitApplication.getRequestStatusRid())))
						.findAny().orElse(new CodeMaster());

				objPermitApplication.setRequestStatusEnglish(objStatus.getCodeDescriptionEnglish());
				objPermitApplication.setRequestStatusArabic(objStatus.getCodeDescriptionArabic());

			}

			List<LicenseOrganization> objOrgLicense = orgLicenseRepository
					.findByUserOrgIdAndLicenseStatusRid(objPermitApplication.getUserOrgId(), 1);
			if (!objOrgLicense.isEmpty()) {
				objPermitApplication.setOrgCode(objOrgLicense.get(0).getUserOrg().getOrgID());
				objPermitApplication.setOrgName(objOrgLicense.get(0).getUserOrg().getOrgName());
				objPermitApplication.setSadadNo(objOrgLicense.get(0).getSadadNo());
				objPermitApplication.setLicenseExpiryDate(objOrgLicense.get(0).getLicenseExpiryDate());
				objPermitApplication.setLicenseNo(objOrgLicense.get(0).getLicenseNumber());
				objPermitApplication.setLicenseExpiryDate(objOrgLicense.get(0).getLicenseExpiryDate());
			}

			List<PortPermitApplicationApproval> portPermitApprovals = permitApplicationApprovalRepository
					.findByPortPermitApplicationId(objPermitApplication.getId());
			for (PortPermitApplicationApproval approval : portPermitApprovals) {
				if (!lstApprovalStatus.isEmpty()) {
					CodeMaster objStatus = lstApprovalStatus.stream()
							.filter(x -> x.getCode().equals(String.valueOf(approval.getApprovalStatusRid()))).findAny()
							.orElse(new CodeMaster());

					approval.setStatusEnglish(objStatus.getCodeDescriptionEnglish());
					approval.setStatusArabic(objStatus.getCodeDescriptionArabic());
				}
				if (!lstRequestType.isEmpty()) {
					CodeMaster objStatus = lstRequestType.stream()
							.filter(x -> x.getCode().equals(String.valueOf(approval.getRequestTypeRid()))).findAny()
							.orElse(new CodeMaster());

					approval.setRequestTypeEnglish(objStatus.getCodeDescriptionEnglish());
					approval.setRequestTypeArabic(objStatus.getCodeDescriptionArabic());

				}
				if (approval.getUserRid() != null && !approval.getUserRid().isEmpty()) {
					List<Users> lstUser = userRepo.findByIamUserID(approval.getUserRid());
					for (Users users : lstUser) {
						approval.setUserName(users.getEmployeeName());
					}
				}
				objPermitApplication.setApprovals(portPermitApprovals);
			}

			return objPermitApplication;
		} else {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.CRN_NOT_FOUND.getValue());
		}

	}

	public PaginationResponse portPermitRequests(Pagination<PortPermitFilter> pageRequest) {
		Integer page = pageRequest.getPage();
		Integer length = pageRequest.getLength();
		String sort = pageRequest.getSort();
		String sortDir = pageRequest.getSortDir();
		Boolean isUser = false;
		List<PortPermitLicense> lstPortPermits;
		List<Integer> lstRequestTypeRids = new ArrayList<>();
		List<Integer> lstApprovalStatusRids = new ArrayList<>();
		List<Integer> lstPortsRid = new ArrayList<>();
		String search = pageRequest.getSearch();

		CodeMasterRequest objCodeMasterRequest = new CodeMasterRequest();
		objCodeMasterRequest.getKey1().add(CommonCodes.PERMIT_STATUS);
		objCodeMasterRequest.getKey1().add(CommonCodes.PERMIT_REQUEST_TYPE);

		List<CodeMaster> lstCodeMaster = this.restService.getByKeysCodeMaster(objCodeMasterRequest);

		List<CodeMaster> lstApprovalStatus = lstCodeMaster.stream()
				.filter(x -> x.getKey1().equals(CommonCodes.PERMIT_STATUS)).collect(Collectors.toList());
		List<CodeMaster> lstRequestType = lstCodeMaster.stream()
				.filter(x -> x.getKey1().equals(CommonCodes.PERMIT_REQUEST_TYPE)).collect(Collectors.toList());

		sort = commonService.sortKeys(sort);
		Long totalRecords = null;
		PageRequest pageable = PageRequest.of(page, length, Sort.by(Sort.Direction.valueOf(sortDir), sort));

		List<PortPermitListResponse> lstPortPermitResponse = new ArrayList<>();

		List<PortMaster> lstPortMaster = this.restService.getByKeysPortMaster();

		if (search != null && !search.isEmpty()) {

			lstApprovalStatusRids = lstApprovalStatus.stream()
					.filter(x -> x.getCodeDescriptionEnglish().toLowerCase().contains(search.toLowerCase()))
					.map(CodeMaster::getCode).map(Integer::parseInt).collect(Collectors.toList());
			lstRequestTypeRids = lstRequestType.stream()
					.filter(x -> x.getCodeDescriptionEnglish().toLowerCase().contains(search.toLowerCase()))
					.map(CodeMaster::getCode).map(Integer::parseInt).collect(Collectors.toList());
			lstPortsRid = lstPortMaster.stream()
					.filter(x -> x.getPortNameEnglish().toLowerCase().contains(search.toLowerCase()))
					.map(PortMaster::getId).collect(Collectors.toList());
		}

		Page<PortPermitLicense> pgPortPermit = this.portLicenseRepository.findAll(
				new PortPermitSpecification(pageRequest, lstPortsRid, lstRequestTypeRids, lstApprovalStatusRids),
				pageable);
		lstPortPermits = pgPortPermit.getContent();
		totalRecords = pgPortPermit.getTotalElements();

		if (!pageRequest.getKeys().getStakeHolderType().equals(CommonCodes.PORTOFF)
				&& !pageRequest.getKeys().getStakeHolderType().equals(CommonCodes.PORTMAN)) {
			isUser = true;
		}

		for (PortPermitLicense objPermit : lstPortPermits) {
			PortPermitListResponse objPortPermit = new PortPermitListResponse();

			processStatus(isUser, lstApprovalStatus, lstRequestType, objPermit, objPortPermit);

			if (!lstPortMaster.isEmpty()) {
				PortMaster objPortMaster = lstPortMaster.stream().filter(x -> x.getId().equals(objPermit.getPortRid()))
						.findAny().orElse(new PortMaster());

				objPortPermit.setPortOfSubmission(objPortMaster.getPortNameEnglish());

			}
			if (objPermit.getSuspensionTo() != null) {
				Long suspendDays = ChronoUnit.DAYS.between(LocalDateTime.now(), objPermit.getSuspensionTo());
				if (suspendDays < 0) {
					ApproveRejectRequest objApproveRejectRequest = new ApproveRejectRequest();
					objApproveRejectRequest.setCrn(objPermit.getCrn());
					if (objPermit.getApprovalStatusRid().equals(CommonCodes.APPROVED_LM)) {
						objApproveRejectRequest.setApprovalStatusRid(CommonCodes.APPROVED_LM);
						objApproveRejectRequest.setRequestTypeRid(CommonCodes.REACTIVATE_LICENSE);
					} else {
						PortPermitApplicationApproval objOldStatus = getPreviousStatus(objPermit);
						objApproveRejectRequest.setApprovalStatusRid(objOldStatus.getApprovalStatusRid());
						objApproveRejectRequest.setRequestTypeRid(objOldStatus.getRequestTypeRid());
					}

					objApproveRejectRequest.setCreatedBy(objPermit.getUpdatedBy());
					portPermitApproveReject(objApproveRejectRequest);
				}
			}

			objPortPermit.setRequestNo(objPermit.getCrn());
			objPortPermit.setOrgName(objPermit.getUserOrg().getOrgName());
			objPortPermit.setRequestDate(objPermit.getCreatedDate());
			objPortPermit.setUserId(objPermit.getUserOrg().getUserId());
			objPortPermit.setUpdatedDate(objPermit.getUpdatedDate());
			objPortPermit.setIsActive(objPermit.getIsActive());

			LicenseApplication objLicenseApplication = objPermit.getUserOrg().getUserLicense().stream()
					.filter(x -> x.getIsActive()).findFirst().orElse(new LicenseApplication());

			objPortPermit.setLicenceNo(objLicenseApplication.getLicenseNumber());
			objPortPermit.setLicenceInitiationDate(objLicenseApplication.getCreatedDate() != null
					? objLicenseApplication.getCreatedDate().toLocalDate()
					: null);
			objPortPermit.setLicenceExpiryDate(objLicenseApplication.getLicenseExpiryDate());

			lstPortPermitResponse.add(objPortPermit);

		}

		return new PaginationResponse(totalRecords, totalRecords, lstPortPermitResponse);

	}

	public PortPermitApplicationApproval getPreviousStatus(PortPermitLicense objPortPermitApplication) {
		List<Integer> requestTypes = new ArrayList<>();
		requestTypes.add(1);
		requestTypes.add(2);
		requestTypes.add(4);

		List<PortPermitApplicationApproval> lstApplicationApprovals = this.permitApplicationApprovalRepository
				.findByPortPermitApplicationIdAndRequestTypeRidIn(objPortPermitApplication.getId(), requestTypes);
		return lstApplicationApprovals.get(lstApplicationApprovals.size() - 1);

	}

	public void processStatus(Boolean isUser, List<CodeMaster> lstApprovalStatus, List<CodeMaster> lstRequestType,
			PortPermitLicense objPermit, PortPermitListResponse objPortPermit) {
		if (Boolean.FALSE.equals(isUser)) {
			if (!lstApprovalStatus.isEmpty()) {
				CodeMaster objStatus = filterCodeMaster(lstApprovalStatus,
						String.valueOf(objPermit.getApprovalStatusRid()));

				objPortPermit.setApprovalStatusRid(objPermit.getApprovalStatusRid());
				objPortPermit.setStatus(objStatus.getCodeDescriptionEnglish());
			}

			if (!lstRequestType.isEmpty()) {
				CodeMaster objLicenseStatus = filterCodeMaster(lstRequestType,
						String.valueOf(objPermit.getRequestTypeRid()));

				objPortPermit.setRequestTypeRid(objPermit.getRequestTypeRid());
				objPortPermit.setRequestTypeEnglish(objLicenseStatus.getCodeDescriptionEnglish());

			}

		} else {

			if ((!objPermit.getRequestTypeRid().equals(CommonCodes.NEW_LICENSE)
					|| !objPermit.getRequestTypeRid().equals(CommonCodes.CANCEL_LICENSE))
					&& !objPermit.getApprovalStatusRid().equals(CommonCodes.APPROVED_LM)) {

				processUserStatus(objPortPermit, objPermit, lstApprovalStatus, lstRequestType);

			} else {

				processOfficerStatus(lstApprovalStatus, lstRequestType, objPermit, objPortPermit);
			}
		}
	}

	public CodeMaster filterCodeMaster(List<CodeMaster> lstCodeMaster, String id) {
		return lstCodeMaster.stream().filter(x -> x.getCode().equals(id)).findAny().orElse(new CodeMaster());
	}

	public void processUserStatus(PortPermitListResponse objPortPermit, PortPermitLicense objPermit,
			List<CodeMaster> lstApprovalStatus, List<CodeMaster> lstRequestType) {
		List<Integer> requestTypes = new ArrayList<>();
		requestTypes.add(CommonCodes.NEW_LICENSE);
		requestTypes.add(CommonCodes.CANCEL_LICENSE);

		List<PortPermitApplicationApproval> lstApplicationApprovals = this.permitApplicationApprovalRepository
				.findByPortPermitApplicationIdAndRequestTypeRidIn(objPermit.getId(), requestTypes);

		if (!lstApplicationApprovals.isEmpty()) {
			PortPermitApplicationApproval objLicenseApplicationApproval = lstApplicationApprovals
					.get(lstApplicationApprovals.size() - 1);
			if (!lstApprovalStatus.isEmpty()) {
				CodeMaster objStatus = filterCodeMaster(lstApprovalStatus,
						String.valueOf(objLicenseApplicationApproval.getApprovalStatusRid()));

				objPortPermit.setApprovalStatusRid(objLicenseApplicationApproval.getApprovalStatusRid());
				objPortPermit.setStatus(objStatus.getCodeDescriptionEnglish());
			}

			if (!lstRequestType.isEmpty()) {
				CodeMaster objLicenseStatus = filterCodeMaster(lstRequestType,
						String.valueOf(objLicenseApplicationApproval.getRequestTypeRid()));

				objPortPermit.setRequestTypeRid(objLicenseApplicationApproval.getRequestTypeRid());
				objPortPermit.setRequestTypeEnglish(objLicenseStatus.getCodeDescriptionEnglish());

			}
		} else {
			if (!lstApprovalStatus.isEmpty()) {
				CodeMaster objStatus = filterCodeMaster(lstApprovalStatus,
						String.valueOf(objPermit.getApprovalStatusRid()));

				objPortPermit.setApprovalStatusRid(objPermit.getApprovalStatusRid());
				objPortPermit.setStatus(objStatus.getCodeDescriptionEnglish());
			}

			if (!lstRequestType.isEmpty()) {
				CodeMaster objLicenseStatus = filterCodeMaster(lstRequestType,
						String.valueOf(objPermit.getRequestTypeRid()));

				objPortPermit.setRequestTypeRid(objPermit.getRequestTypeRid());
				objPortPermit.setRequestTypeEnglish(objLicenseStatus.getCodeDescriptionEnglish());

			}
		}

	}

	public void processOfficerStatus(List<CodeMaster> lstApprovalStatus, List<CodeMaster> lstRequestType,
			PortPermitLicense objPermit, PortPermitListResponse objPortPermit) {
		if (!lstApprovalStatus.isEmpty()) {
			CodeMaster objStatus = filterCodeMaster(lstApprovalStatus,
					String.valueOf(objPermit.getApprovalStatusRid()));

			objPortPermit.setApprovalStatusRid(objPermit.getApprovalStatusRid());
			objPortPermit.setStatus(objStatus.getCodeDescriptionEnglish());
		}

		if (!lstRequestType.isEmpty()) {
			CodeMaster objLicenseStatus = filterCodeMaster(lstRequestType,
					String.valueOf(objPermit.getRequestTypeRid()));

			objPortPermit.setRequestTypeRid(objPermit.getRequestTypeRid());
			objPortPermit.setRequestTypeEnglish(objLicenseStatus.getCodeDescriptionEnglish());

		}
	}

	public List<PortMaster> approvedPorts(Integer orgRID) {
		List<Integer> apporvalStatus = new ArrayList<>();

		UserOrganization objOrganization = this.organizationRepository.findById(orgRID).get();
		List<UserBranch> lstBranchs = this.branchRepository.findByOrgId(objOrganization.getId());
		RoleMaster objRoleMaster = this.roleMasterRepository.findById(objOrganization.getStakeholdertypeRid()).get();
		List<PortMaster> lstAllPorts = this.restService.getByKeysPortMaster();
		CodeMasterRequest objCodeMasterRequest = new CodeMasterRequest();
		objCodeMasterRequest.getKey1().add(CommonCodes.PORT_TYPES);

		List<CodeMaster> lstCodeMaster = this.restService.getByKeysCodeMaster(objCodeMasterRequest);
		String strLocationMaster = this.restService.getAllLocation(CommonCodes.CUSTOMS_COUNTRY);
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

		List<PortMaster> lstSeaPorts = lstAllPorts.stream()
				.filter(y -> y.getCountryId().equals(objLocationMaster.getId())
						&& y.getPortTypeId().equals(objCodeMaster.getId()))
				.collect(Collectors.toList());

		if (objRoleMaster.getIsLicenceRequired()) {

			apporvalStatus.add(CommonCodes.APPROVED_LO);

			List<PortMaster> lstPort = new ArrayList<>();

			List<PortPermitApplication> lstPorts = this.permitApplicationRepository
					.findByUserOrgIdAndRequestStatusRid(orgRID, 1);

			for (UserBranch objBranch : lstBranchs) {

				PortPermitApplication assignPermit = lstPorts.stream()
						.filter(x -> x.getPortRid().equals(objBranch.getPortRid())).findFirst()
						.orElse(new PortPermitApplication());
				lstPorts.remove(assignPermit);
			}

			lstPorts.stream().forEach(port -> {

				PortMaster objMaster = new PortMaster();

				objMaster = lstSeaPorts.stream().filter(x -> x.getId().equals(port.getPortRid())).findAny().get();
				lstPort.add(objMaster);
			});
			return lstPort;
		} else {
			List<PortMaster> lstPort = lstAllPorts;

			for (UserBranch objBranch : lstBranchs) {
				PortMaster objPortMaster;

				objPortMaster = lstSeaPorts.stream().filter(x -> x.getId().equals(objBranch.getPortRid())).findAny()
						.orElse(new PortMaster());
				lstPort.remove(objPortMaster);
			}

			return lstPort;
		}

	}

	public Integer permitIsActive(Integer approvalStaus, Integer requestType) {

		if (requestType.equals(CommonCodes.NEW_LICENSE) && !approvalStaus.equals(CommonCodes.APPROVED_LO)) {
			return 0;
		} else if (requestType.equals(CommonCodes.CANCEL_LICENSE) && approvalStaus.equals(CommonCodes.APPROVED_LM)) {
			return 0;
		} else if (requestType.equals(CommonCodes.CANCEL_LICENSE_LO) && approvalStaus.equals(CommonCodes.APPROVED_LM)) {
			return 0;
		} else if (requestType.equals(CommonCodes.SUSPEND_LICENSE) && !approvalStaus.equals(CommonCodes.APPROVED_LM)) {
			return 0;
		} else if (requestType.equals(CommonCodes.REACTIVATE_LICENSE)
				&& !approvalStaus.equals(CommonCodes.APPROVED_LM)) {
			return 0;
		} else {
			return 1;
		}
	}

	public String statusMessage(Integer requestType, Integer approvalStatus) {
		if (requestType.equals(1) && approvalStatus.equals(11)) {
			return CommonCodes.PORT_PERMIT_REQ_RAISED;
		}
		if (requestType.equals(1) && approvalStatus.equals(15)) {
			return CommonCodes.PORT_PERMIT_REQ_APR;
		} else if (requestType.equals(1) && approvalStatus.equals(16)) {
			return CommonCodes.PORT_PERMIT_REQ_REJ;
		} else if (requestType.equals(2) && approvalStatus.equals(11)) {
			return CommonCodes.PORT_PERMIT_CANCELLED;
		} else if (requestType.equals(2) && approvalStatus.equals(12)) {
			return CommonCodes.PORT_PERMIT_CANCELLED_REQ_APPROVED;
		} else if (requestType.equals(2) && approvalStatus.equals(17)) {
			return CommonCodes.PORT_PERMIT_CANCELLED_REQ_APPROVED;
		} else if (requestType.equals(2) && approvalStatus.equals(16)) {
			return CommonCodes.PORT_PERMIT_CANCELLED_REJECT;
		} else if (requestType.equals(2) && approvalStatus.equals(18)) {
			return CommonCodes.PORT_PERMIT_CANCELLED_REJECT;
		} else if (requestType.equals(3) && approvalStatus.equals(12)) {
			return CommonCodes.PORT_PERMIT_CANCELLED;
		} else if (requestType.equals(3) && approvalStatus.equals(17)) {
			return CommonCodes.PORT_PERMIT_CANCELLED_REQ_APPROVED;
		} else if (requestType.equals(3) && approvalStatus.equals(18)) {
			return CommonCodes.PORT_PERMIT_CANCELLED_REJECT;
		} else if (requestType.equals(5) && approvalStatus.equals(12)) {
			return CommonCodes.PORT_PERMIT_SUSPEND;
		} else if (requestType.equals(5) && approvalStatus.equals(17)) {
			return CommonCodes.PORT_PERMIT_SUSPEND_REQ_APPROVED;
		} else if (requestType.equals(5) && approvalStatus.equals(18)) {
			return CommonCodes.PORT_PERMIT_SUSPEND_REJECT;
		} else if (requestType.equals(6) && approvalStatus.equals(12)) {
			return CommonCodes.PORT_PERMIT_REACT;
		} else if (requestType.equals(6) && approvalStatus.equals(17)) {
			return CommonCodes.PORT_PERMIT_REACT_REQ_APPROVED;
		} else if (requestType.equals(6) && approvalStatus.equals(18)) {
			return CommonCodes.PORT_PERMIT_REACT_REJECT;
		} else if (requestType.equals(4) && approvalStatus.equals(11)) {
			return CommonCodes.PORT_PERMIT_RENEW;
		} else if (requestType.equals(4) && approvalStatus.equals(15)) {
			return CommonCodes.PORT_PERMIT_RENEW_REQ_APPROVED;
		} else if (requestType.equals(4) && approvalStatus.equals(16)) {
			return CommonCodes.PORT_PERMIT_RENEW_REJECT;
		} else {
			return CommonCodes.PORT_PERMIT_MESSAGE;
		}

	}

	public List<PortMaster> activePorts() {
		List<PortPermitApplication> lstPortPermits = permitApplicationRepository.findByRequestTypeRid(1);
		List<Integer> portPermitIds = lstPortPermits.stream().map(PortPermitApplication::getPortRid)
				.collect(Collectors.toList());
		List<PortMaster> lstPorts = restService.getByKeysPortMaster();
		CodeMasterRequest objCodeMasterRequest = new CodeMasterRequest();
		objCodeMasterRequest.getKey1().add(CommonCodes.PORT_TYPES);

		List<CodeMaster> lstCodeMaster = restService.getByKeysCodeMaster(objCodeMasterRequest);
		CodeMaster objCodeMaster = lstCodeMaster.stream().filter(x -> x.getCode().equals(CommonCodes.SE)).findAny()
				.orElse(new CodeMaster());

		lstPorts = lstPorts.stream().filter(
				port -> !portPermitIds.contains(port.getId()) && port.getPortTypeId().equals(objCodeMaster.getId()))
				.collect(Collectors.toList());

		return lstPorts;
	}

}
