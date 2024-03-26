package sa.tabadul.useraccessmanagement.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.common.enums.ExceptionMessage;
import sa.tabadul.useraccessmanagement.common.models.request.CodeMasterRequest;
import sa.tabadul.useraccessmanagement.common.models.request.InvoiceCreateRequest;
import sa.tabadul.useraccessmanagement.common.models.request.LicenceRequest;
import sa.tabadul.useraccessmanagement.common.models.request.LicenseApproveRejectRequest;
import sa.tabadul.useraccessmanagement.common.models.request.LicenseFilter;
import sa.tabadul.useraccessmanagement.common.models.request.Pagination;
import sa.tabadul.useraccessmanagement.common.models.request.SoapEnvelope;
import sa.tabadul.useraccessmanagement.common.models.request.UserBranchKeys;
import sa.tabadul.useraccessmanagement.common.models.response.CreateInvoiceReturn;
import sa.tabadul.useraccessmanagement.common.models.response.EunViewResponse;
import sa.tabadul.useraccessmanagement.common.models.response.LicenseApplicationResponse;
import sa.tabadul.useraccessmanagement.common.models.response.LicenseApproveRejectResponse;
import sa.tabadul.useraccessmanagement.common.models.response.LicenseCrnViewResponse;
import sa.tabadul.useraccessmanagement.common.models.response.LicenseListResponse;
import sa.tabadul.useraccessmanagement.common.models.response.MCIServiceGetByEUNResposne;
import sa.tabadul.useraccessmanagement.common.models.response.OwnersListResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PaginationResponse;
import sa.tabadul.useraccessmanagement.common.models.response.ResponseEnvelope;
import sa.tabadul.useraccessmanagement.common.models.response.ReturnDetails;
import sa.tabadul.useraccessmanagement.common.service.CommonService;
import sa.tabadul.useraccessmanagement.common.service.SoapClientService;
import sa.tabadul.useraccessmanagement.common.service.UserManagementExternalService;
import sa.tabadul.useraccessmanagement.domain.CodeMaster;
import sa.tabadul.useraccessmanagement.domain.LicenseApplication;
import sa.tabadul.useraccessmanagement.domain.LicenseApplicationApproval;
import sa.tabadul.useraccessmanagement.domain.LicenseOrganization;
import sa.tabadul.useraccessmanagement.domain.RoleMaster;
import sa.tabadul.useraccessmanagement.domain.Users;
import sa.tabadul.useraccessmanagement.exception.BusinessException;
import sa.tabadul.useraccessmanagement.exception.GeneralException;
import sa.tabadul.useraccessmanagement.repository.LicenseApplicationApprovalRepository;
import sa.tabadul.useraccessmanagement.repository.LicenseApplicationRepository;
import sa.tabadul.useraccessmanagement.repository.LicenseOrganizationRepository;
import sa.tabadul.useraccessmanagement.repository.RoleMasterRepository;
import sa.tabadul.useraccessmanagement.repository.UserRepository;
import sa.tabadul.useraccessmanagement.soap.CommonErrorStructure;
import sa.tabadul.useraccessmanagement.soap.GetZakatCertificateByCrNumber;
import sa.tabadul.useraccessmanagement.soap.GetZakatCertificateByCrNumberResponse;
import sa.tabadul.useraccessmanagement.soap.ZakatCertificateStructure;

@Service
public class LicenseService {

	@Autowired
	private LicenseApplicationRepository licenceRepository;

	@Autowired
	private LicenseOrganizationRepository licenseOrganizationRepository;

	@Autowired
	private RoleMasterRepository roleMasterRepository;

	@Autowired
	private LicenseApplicationApprovalRepository applicationApprovalRepository;

	@Autowired
	private UserManagementExternalService restService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SoapClientService soapClientService;

	public ResponseEnvelope<LicenseApplicationResponse> addAndUpdateLicence(LicenceRequest licenseRequest) {

		LicenseApplication objLicenseApplication = new LicenseApplication();
		if (licenseRequest.getCrn() == null) {
			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(CommonCodes.CRN_FORMAT);
			licenseRequest.setCrn(Long.parseLong(myFormatObj.format(myDateObj)));

			objLicenseApplication = modelMapper.map(licenseRequest, LicenseApplication.class);
			
			objLicenseApplication.setLicenseStatusRid(
					licenseIsActive(licenseRequest.getApprovalStatusRid(), licenseRequest.getRequestTypeRid()));

			objLicenseApplication = this.licenceRepository.save(objLicenseApplication);
			LicenseApplicationResponse applicationResponse = new LicenseApplicationResponse(
					objLicenseApplication.getCrn(), objLicenseApplication.getCreatedBy(),
					objLicenseApplication.getUpdatedBy(), objLicenseApplication.getCreatedDate());

			approvalData(objLicenseApplication, null);

			ResponseEnvelope<LicenseApplicationResponse> objResponse = new ResponseEnvelope<LicenseApplicationResponse>(
					HttpStatus.OK.value(),
					statusMessage(licenseRequest.getRequestTypeRid(), licenseRequest.getApprovalStatusRid()),
					applicationResponse);

			return objResponse;

		} else {
			LicenseApplication objApplication = this.licenceRepository.findByCrn(licenseRequest.getCrn());
			if (objApplication == null) {
				throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.CRN_NOT_FOUND.getValue());
			}
			LicenseApplication objMappedLicense = objApplication;
			modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
			objApplication = modelMapper.map(licenseRequest, LicenseApplication.class);
			objApplication.setIsActive(true);
			objApplication.setCreatedDate(objMappedLicense.getCreatedDate());
			objLicenseApplication.setLicenseStatusRid(
					licenseIsActive(licenseRequest.getApprovalStatusRid(), licenseRequest.getRequestTypeRid()));

			if (licenseRequest.getRequestTypeRid().equals(CommonCodes.RENEW_LICENSE)) {
				objMappedLicense.setIsActive(false);
				this.licenceRepository.save(objMappedLicense);
				LocalDateTime myDateObj = LocalDateTime.now();
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(CommonCodes.CRN_FORMAT);
				objApplication.setCrn(Long.parseLong(myFormatObj.format(myDateObj)));
				objApplication.setId(null);

			}
			objApplication = this.licenceRepository.save(objApplication);

			LicenseApplicationResponse applicationResponse = new LicenseApplicationResponse(objApplication.getCrn(),
					objApplication.getCreatedBy(), objApplication.getUpdatedBy(), objApplication.getCreatedDate());
			approvalData(objApplication, null);
			ResponseEnvelope<LicenseApplicationResponse> objResponse = new ResponseEnvelope<LicenseApplicationResponse>(
					HttpStatus.OK.value(),
					statusMessage(licenseRequest.getRequestTypeRid(), licenseRequest.getApprovalStatusRid()),
					applicationResponse);

			return objResponse;
		}

	}

	public PaginationResponse licenseRequests(Pagination<LicenseFilter> pageRequest) {
		Integer page = pageRequest.getPage();
		Integer length = pageRequest.getLength();
		String sort = pageRequest.getSort();
		String sortDir = pageRequest.getSortDir();
		LicenseFilter filter = pageRequest.getFilter();
		UserBranchKeys keys = pageRequest.getKeys();
		Boolean isUser = false;

		if (filter == null) {
			filter = new LicenseFilter();
		}

		CodeMasterRequest objCodeMasterRequest = new CodeMasterRequest();
		objCodeMasterRequest.getKey1().add(CommonCodes.LICENSE_STATUS);
		objCodeMasterRequest.getKey1().add(CommonCodes.LICENSE_REQUEST_TYPE);

		List<CodeMaster> lstCodeMaster = this.restService.getByKeysCodeMaster(objCodeMasterRequest);

		List<CodeMaster> lstApprovalStatus = lstCodeMaster.stream()
				.filter(x -> x.getKey1().equals(CommonCodes.LICENSE_STATUS)).collect(Collectors.toList());
		List<CodeMaster> lstRequestType = lstCodeMaster.stream()
				.filter(x -> x.getKey1().equals(CommonCodes.LICENSE_REQUEST_TYPE)).collect(Collectors.toList());

		sort = commonService.sortKeys(sort);
		Long totalRecords = null;
		PageRequest pageable = PageRequest.of(page, length, Sort.by(Sort.Direction.valueOf(sortDir), sort.toString()));

		List<LicenseListResponse> lstLicenseListResponse = new ArrayList<>();

		List<LicenseOrganization> lstLicenseApplication = null;

		if (keys.getStakeHolderType().toLowerCase().equals(CommonCodes.LICOFF.toLowerCase())) {

			lstLicenseApplication = licenseOrganizationRepository.licenseOfficerList(pageRequest.getSearch(),
					filter.getFromDate(), filter.getToDate(), filter.getStakeHolderTypeRid(),
					filter.getApprovalStatusRid(), filter.getLicenseStatusRid(), filter.getRequestTypeRid(), pageable);
			totalRecords = licenseOrganizationRepository.licenseOfficerList(pageRequest.getSearch(),
					filter.getFromDate(), filter.getToDate(), filter.getStakeHolderTypeRid(),
					filter.getApprovalStatusRid(), filter.getLicenseStatusRid(), filter.getRequestTypeRid());

		} else if (keys.getStakeHolderType().toLowerCase().equals(CommonCodes.LICMAN.toLowerCase())) {

			keys.getRequestTypeRid().add(2);
			keys.getRequestTypeRid().add(3);
			keys.getRequestTypeRid().add(4);
			keys.getRequestTypeRid().add(5);
			keys.getRequestTypeRid().add(6);

			keys.getStatusTypeRid().add(11);
			keys.getStatusTypeRid().add(12);
			keys.getStatusTypeRid().add(15);
			keys.getStatusTypeRid().add(16);
			keys.getStatusTypeRid().add(17);
			keys.getStatusTypeRid().add(18);

			lstLicenseApplication = licenseOrganizationRepository.licenseManagerList(pageRequest.getSearch(),
					filter.getFromDate(), filter.getToDate(), filter.getStakeHolderTypeRid(),
					filter.getApprovalStatusRid(), filter.getLicenseStatusRid(), filter.getRequestTypeRid(),
					keys.getStatusTypeRid(), keys.getRequestTypeRid(), pageable);

			totalRecords = licenseOrganizationRepository.licenseManagerList(pageRequest.getSearch(),
					filter.getFromDate(), filter.getToDate(), filter.getStakeHolderTypeRid(),
					filter.getApprovalStatusRid(), filter.getLicenseStatusRid(), filter.getRequestTypeRid(),
					keys.getStatusTypeRid(), keys.getRequestTypeRid());

		} else {

			lstLicenseApplication = licenseOrganizationRepository.userList(pageRequest.getSearch(),
					filter.getFromDate(), filter.getToDate(), filter.getStakeHolderTypeRid(),
					filter.getApprovalStatusRid(), filter.getLicenseStatusRid(), filter.getRequestTypeRid(),
					Integer.parseInt(keys.getOrgId()), pageable);
			totalRecords = licenseOrganizationRepository.userList(pageRequest.getSearch(), filter.getFromDate(),
					filter.getToDate(), filter.getStakeHolderTypeRid(), filter.getApprovalStatusRid(),
					filter.getLicenseStatusRid(), filter.getRequestTypeRid(), Integer.parseInt(keys.getOrgId()));
			isUser = true;
		}

		List<RoleMaster> lstRoleMasters = this.roleMasterRepository.findAll();

		for (LicenseOrganization objLicenseApplication : lstLicenseApplication) {
			LicenseListResponse objLicenseListResponse = new LicenseListResponse();
			RoleMaster objRoleMaster = lstRoleMasters.stream()
					.filter(x -> x.getId().equals(objLicenseApplication.getUserOrg().getStakeholdertypeRid())).findAny()
					.orElse(new RoleMaster());
			objLicenseListResponse.setIsActive(objLicenseApplication.getIsActive());
			objLicenseListResponse.setExpiryDate(objLicenseApplication.getLicenseExpiryDate());
			if (objLicenseApplication.getLicenseExpiryDate() != null) {
				objLicenseListResponse.setLicenseValidForDays(
						ChronoUnit.DAYS.between(LocalDate.now(), objLicenseApplication.getLicenseExpiryDate()));
				if (!objLicenseApplication.getApprovalStatusRid().equals(19)
						&& objLicenseListResponse.getLicenseValidForDays() < 0
						&& objLicenseListResponse.getLicenseValidForDays() > -60) {
					LicenseApproveRejectRequest objApproveRejectRequest = new LicenseApproveRejectRequest();
					objApproveRejectRequest.setCrn(objLicenseApplication.getCrn());
					objApproveRejectRequest.setApprovalStatusRid(19);
					objApproveRejectRequest.setRequestTypeRid(objLicenseApplication.getRequestTypeRid());
					objApproveRejectRequest.setCreatedBy(objLicenseApplication.getCreatedBy());
					licenseApproveReject(objApproveRejectRequest);
				}
				if (objLicenseListResponse.getLicenseValidForDays() < -60) {
					if (!objLicenseApplication.getApprovalStatusRid().equals(20)) {
						LicenseApproveRejectRequest objApproveRejectRequest = new LicenseApproveRejectRequest();
						objApproveRejectRequest.setCrn(objLicenseApplication.getCrn());
						objApproveRejectRequest.setApprovalStatusRid(20);
						objApproveRejectRequest.setRequestTypeRid(objLicenseApplication.getRequestTypeRid());
						objApproveRejectRequest.setCreatedBy(objLicenseApplication.getCreatedBy());
						licenseApproveReject(objApproveRejectRequest);
					}
					objLicenseListResponse.setIsActive(false);
				}
			}
			if (objLicenseApplication.getSuspensionTo() != null) {
				Integer suspendDays = (int) ChronoUnit.DAYS.between(LocalDateTime.now(),
						objLicenseApplication.getSuspensionTo());
				if (suspendDays < 0) {
					LicenseApproveRejectRequest objApproveRejectRequest = new LicenseApproveRejectRequest();
					objApproveRejectRequest.setCrn(objLicenseApplication.getCrn());
					if (objLicenseApplication.getApprovalStatusRid().equals(CommonCodes.APPROVED_LM)) {
						objApproveRejectRequest.setApprovalStatusRid(CommonCodes.APPROVED_LM);
						objApproveRejectRequest.setRequestTypeRid(CommonCodes.REACTIVATE_LICENSE);
					} else {
						LicenseApplicationApproval objOldStatus = getPreviousStatus(objLicenseApplication);
						objApproveRejectRequest.setApprovalStatusRid(objOldStatus.getApprovalStatusRid());
						objApproveRejectRequest.setRequestTypeRid(objOldStatus.getRequestTypeRid());
					}

					objApproveRejectRequest.setCreatedBy(objLicenseApplication.getCreatedBy());
					licenseApproveReject(objApproveRejectRequest);
				}
			}

			if (!isUser) {
				if (!lstApprovalStatus.isEmpty()) {
					CodeMaster objStatus = lstApprovalStatus.stream().filter(
							x -> x.getCode().equals(String.valueOf(objLicenseApplication.getApprovalStatusRid())))
							.findAny().orElse(new CodeMaster());

					objLicenseListResponse.setApprovalStatusRid(objLicenseApplication.getApprovalStatusRid());
					objLicenseListResponse.setStatus(objStatus.getCodeDescriptionEnglish());
				}

				if (!lstRequestType.isEmpty()) {
					CodeMaster objLicenseStatus = lstRequestType.stream()
							.filter(x -> x.getCode().equals(String.valueOf(objLicenseApplication.getRequestTypeRid())))
							.findAny().orElse(new CodeMaster());

					objLicenseListResponse.setRequestTypeRid(objLicenseApplication.getRequestTypeRid());
					objLicenseListResponse.setRequestType(objLicenseStatus.getCodeDescriptionEnglish());

				}

			} else {

				if (((!objLicenseApplication.getRequestTypeRid().equals(1)
						|| !objLicenseApplication.getRequestTypeRid().equals(4)
						|| !objLicenseApplication.getRequestTypeRid().equals(2)))
						&& !objLicenseApplication.getApprovalStatusRid().equals(17)
						&& !objLicenseApplication.getApprovalStatusRid().equals(19)
						&& !objLicenseApplication.getApprovalStatusRid().equals(20)) {

					List<Integer> requestTypes = new ArrayList<>();
					requestTypes.add(1);
					requestTypes.add(2);
					requestTypes.add(4);

					List<LicenseApplicationApproval> lstApplicationApprovals = this.applicationApprovalRepository
							.findByLicenseApplicationIdAndRequestTypeRidIn(objLicenseApplication.getId(), requestTypes);
					LicenseApplicationApproval objLicenseApplicationApproval = lstApplicationApprovals
							.get(lstApplicationApprovals.size() - 1);

					if (!lstApprovalStatus.isEmpty()) {
						CodeMaster objStatus = lstApprovalStatus.stream()
								.filter(x -> x.getCode()
										.equals(String.valueOf(objLicenseApplicationApproval.getApprovalStatusRid())))
								.findAny().orElse(new CodeMaster());

						objLicenseListResponse
								.setApprovalStatusRid(objLicenseApplicationApproval.getApprovalStatusRid());
						objLicenseListResponse.setStatus(objStatus.getCodeDescriptionEnglish());
					}

					if (!lstRequestType.isEmpty()) {
						CodeMaster objLicenseStatus = lstRequestType.stream()
								.filter(x -> x.getCode()
										.equals(String.valueOf(objLicenseApplicationApproval.getRequestTypeRid())))
								.findAny().orElse(new CodeMaster());

						objLicenseListResponse.setRequestTypeRid(objLicenseApplicationApproval.getRequestTypeRid());
						objLicenseListResponse.setRequestType(objLicenseStatus.getCodeDescriptionEnglish());

					}

				} else {

					if (!lstApprovalStatus.isEmpty()) {
						CodeMaster objStatus = lstApprovalStatus.stream().filter(
								x -> x.getCode().equals(String.valueOf(objLicenseApplication.getApprovalStatusRid())))
								.findAny().orElse(new CodeMaster());

						objLicenseListResponse.setApprovalStatusRid(objLicenseApplication.getApprovalStatusRid());
						objLicenseListResponse.setStatus(objStatus.getCodeDescriptionEnglish());
					}

					if (!lstRequestType.isEmpty()) {
						CodeMaster objLicenseStatus = lstRequestType.stream().filter(
								x -> x.getCode().equals(String.valueOf(objLicenseApplication.getRequestTypeRid())))
								.findAny().orElse(new CodeMaster());

						objLicenseListResponse.setRequestTypeRid(objLicenseApplication.getRequestTypeRid());
						objLicenseListResponse.setRequestType(objLicenseStatus.getCodeDescriptionEnglish());

					}

				}

			}

			objLicenseListResponse.setEstablishmentNumber(objLicenseApplication.getEunn());
			objLicenseListResponse.setOrgName(objLicenseApplication.getUserOrg().getOrgName());
			objLicenseListResponse.setLicenseNo(objLicenseApplication.getLicenseNumber());
			objLicenseListResponse.setStakholderCategory(objRoleMaster.getRoleNameEnglish());
			objLicenseListResponse.setRequestNo(objLicenseApplication.getCrn());
			objLicenseListResponse.setUpdatedDate(objLicenseApplication.getUpdatedDate());
			objLicenseListResponse.setUserId(objLicenseApplication.getUserOrg().getUserId());
			objLicenseListResponse.setIsActive(objLicenseApplication.getIsActive());
			lstLicenseListResponse.add(objLicenseListResponse);

		}

		PaginationResponse objPaginationResponse = new PaginationResponse(totalRecords, totalRecords,
				lstLicenseListResponse);

		return objPaginationResponse;

	}

	public LicenseApplicationApproval getPreviousStatus(LicenseOrganization objLicenseApplication) {
		List<Integer> requestTypes = new ArrayList<>();
		requestTypes.add(1);
		requestTypes.add(2);
		requestTypes.add(4);

		List<LicenseApplicationApproval> lstApplicationApprovals = this.applicationApprovalRepository
				.findByLicenseApplicationIdAndRequestTypeRidIn(objLicenseApplication.getId(), requestTypes);
		LicenseApplicationApproval objLicenseApplicationApproval = lstApplicationApprovals
				.get(lstApplicationApprovals.size() - 1);
		return objLicenseApplicationApproval;

	}

	public Object eunDetails(Long eun) {

		Object objValidateCrn = zakatCertificateValidate(eun);
		ZakatCertificateStructure objZakatCertificate = new ZakatCertificateStructure();
		Boolean isValid = objValidateCrn.getClass().toString().contains(objZakatCertificate.getClass().toString());
		if (isValid) {
			MCIServiceGetByEUNResposne objByEUNResposne = this.restService.eunDetails(eun);
			OwnersListResponse objOwnerDetails = this.restService.ownerDetails(eun);
			EunViewResponse objEunViewResponse = new EunViewResponse();
			if (objByEUNResposne != null && objOwnerDetails != null) {
				objEunViewResponse.setEun(eun);
				objEunViewResponse.setEstablishmentCity(objByEUNResposne.getCrCityEn());
				objEunViewResponse.setEstablishmentExpiryDate(convertHijri(objByEUNResposne.getCrExpiryHijriDate()));
				objEunViewResponse.setEstablishmentIssueDate(convertHijri(objByEUNResposne.getCrIssueHijriDate()));
				objEunViewResponse.setAddress(objByEUNResposne.getCrAddress());
				objEunViewResponse.setEstablishmentName(objByEUNResposne.getCrNameAr());
				objEunViewResponse.setEstablishmentType(objByEUNResposne.getCrType());
				objEunViewResponse.setMailBox(objByEUNResposne.getCrFaxNo());
				objEunViewResponse.setRecordType(objByEUNResposne.getCrType());
				objEunViewResponse.setEstablishmentStatus(objByEUNResposne.getCrStatus());
				objOwnerDetails.getCrOwnersList().forEach(x -> {
					if (x.getRelationTypeId().equals(CommonCodes.MANAGER_CODE)) {
						objEunViewResponse.setManagerName(x.getOwnerName());
						objEunViewResponse.setManagerNationality(x.getOwnerNationalityEn());
					}
					objEunViewResponse.getOwnerDetails().add(x.getOwnerName());
				});

			} else {
				throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			}
			return objEunViewResponse;
		} else {

			CommonErrorStructure objError = modelMapper.map(objValidateCrn, CommonErrorStructure.class);

			throw new GeneralException(HttpStatus.BAD_REQUEST.value(), objError.getErrorText().toString());
		}

	}

	public LicenseCrnViewResponse licenseCrnDetails(Long crn) {

		LicenseOrganization objLicenseApplication = this.licenseOrganizationRepository.findByCrn(crn);

		CodeMasterRequest objCodeMasterRequest = new CodeMasterRequest();
		objCodeMasterRequest.getKey1().add(CommonCodes.LICENSE_STATUS);
		objCodeMasterRequest.getKey1().add(CommonCodes.LICENSE_REQUEST_TYPE);
		objCodeMasterRequest.getKey1().add(CommonCodes.INVESTOR_TYPE);
		List<CodeMaster> lstCodeMaster = this.restService.getByKeysCodeMaster(objCodeMasterRequest);

		List<CodeMaster> lstApprovalStatus = lstCodeMaster.stream()
				.filter(x -> x.getKey1().equals(CommonCodes.LICENSE_STATUS)).collect(Collectors.toList());
		List<CodeMaster> lstRequestType = lstCodeMaster.stream()
				.filter(x -> x.getKey1().equals(CommonCodes.LICENSE_REQUEST_TYPE)).collect(Collectors.toList());

		if (objLicenseApplication != null) {

			Optional<RoleMaster> optRoleMaster = this.roleMasterRepository
					.findById(objLicenseApplication.getLicenseTypeRid());
			LicenseCrnViewResponse objCrnViewResponse = new LicenseCrnViewResponse();
			objCrnViewResponse.setId(objLicenseApplication.getId());
			objCrnViewResponse.setRequestNumber(objLicenseApplication.getCrn());
			objCrnViewResponse.setRequestDate(objLicenseApplication.getCreatedDate());
			objCrnViewResponse.setLicenseUpdate(objLicenseApplication.getUpdatedDate());
			objCrnViewResponse.setSuspensionTo(objLicenseApplication.getSuspensionTo());
			objCrnViewResponse.setFileName(objLicenseApplication.getFileName());
			objCrnViewResponse.setFilePath(objLicenseApplication.getFilePath());

			if (!lstApprovalStatus.isEmpty()) {
				CodeMaster objStatus = lstApprovalStatus.stream()
						.filter(x -> x.getCode().equals(String.valueOf(objLicenseApplication.getApprovalStatusRid())))
						.findAny().orElse(new CodeMaster());

				objCrnViewResponse.setRequestStatusRid(objLicenseApplication.getApprovalStatusRid());
				objCrnViewResponse.setRequestStatus(objStatus.getCodeDescriptionEnglish());

				objStatus = lstApprovalStatus.stream()
						.filter(x -> x.getCode().equals(String.valueOf(objLicenseApplication.getLicenseStatusRid())))
						.findAny().orElse(new CodeMaster());

				objCrnViewResponse.setLicenseStatusRid(objLicenseApplication.getLicenseStatusRid());
				objCrnViewResponse.setLicenseStatus(objStatus.getCodeDescriptionEnglish());

			}

			if (!lstRequestType.isEmpty()) {
				CodeMaster objLicenseStatus = lstRequestType.stream()
						.filter(x -> x.getCode().equals(String.valueOf(objLicenseApplication.getRequestTypeRid())))
						.findAny().orElse(new CodeMaster());

				objCrnViewResponse.setRequestTypeRid(objLicenseApplication.getRequestTypeRid());
				objCrnViewResponse.setRequestType(objLicenseStatus.getCodeDescriptionEnglish());

			}
			objCrnViewResponse.setLicenseNo(objLicenseApplication.getLicenseNumber());
			objCrnViewResponse.setSadadNo(objLicenseApplication.getSadadNo());
			objCrnViewResponse.setExpiryDate(objLicenseApplication.getLicenseExpiryDate());
			if (!lstCodeMaster.isEmpty()) {
				CodeMaster objCodeMaster = lstCodeMaster.stream()
						.filter(x -> x.getId() == objLicenseApplication.getInvestorTypeRid().intValue()).findAny()
						.orElse(new CodeMaster());

				objCrnViewResponse.setInvenstorTyperRid(objLicenseApplication.getInvestorTypeRid());
				objCrnViewResponse.setInvestorType(objCodeMaster.getCodeDescriptionEnglish());

			}
			if (optRoleMaster.isPresent()) {
				objCrnViewResponse.setLicenseTypeRid(objLicenseApplication.getLicenseTypeRid());
				objCrnViewResponse.setLicenseType(optRoleMaster.get().getRoleDescriptionEnglish());
			}

			objCrnViewResponse.setIqmaNo(objLicenseApplication.getUserOrg().getDocumentNo());

			objCrnViewResponse.setEunn(objLicenseApplication.getEunn());
			objCrnViewResponse.setEstablishmentName(objLicenseApplication.getEstablishmentName());
			objCrnViewResponse.setEstablishmentType(objLicenseApplication.getEstablishmentType());
			objCrnViewResponse.setEstablishmentActivity(objLicenseApplication.getEstablishmentActivity());
			objCrnViewResponse.setEstablishmentStatus(objLicenseApplication.getEstablishmentStatus());
			objCrnViewResponse.setEstablishmentIssueDate(objLicenseApplication.getEstablishmentIssueDate());
			objCrnViewResponse.setEstablishmentExpiryDate(objLicenseApplication.getEstablishmentExpiryDate());
			objCrnViewResponse.setEstablishmentCity(objLicenseApplication.getEstablishmentCity());
			objCrnViewResponse.setRecordType(objLicenseApplication.getRecordType());
			objCrnViewResponse.setAddress(objLicenseApplication.getAddress());
			objCrnViewResponse.setMailBox(objLicenseApplication.getMailbox());
			objCrnViewResponse.setManagerName(objLicenseApplication.getManagerName());
			objCrnViewResponse.setManagerNationality(objLicenseApplication.getManagerNationality());
			objCrnViewResponse.setOrgName(objLicenseApplication.getUserOrg().getOrgName());
			objCrnViewResponse.setListOfPartners(objLicenseApplication.getListOfPartners());

			if (objLicenseApplication.getLicenseExpiryDate() != null) {
				objCrnViewResponse.setExpiryDate(objLicenseApplication.getLicenseExpiryDate());
				objCrnViewResponse.setValidateForDays(
						ChronoUnit.DAYS.between(LocalDate.now(), objLicenseApplication.getLicenseExpiryDate()));

			}
			objCrnViewResponse.getOwnerDetails().add(objLicenseApplication.getListOfPartners());

			List<LicenseApplicationApproval> lstLicenseApplicationApprovals = this.applicationApprovalRepository
					.findByLicenseApplicationId(objCrnViewResponse.getId());
			List<String> userIds = lstLicenseApplicationApprovals.stream().map(LicenseApplicationApproval::getUserRid)
					.collect(Collectors.toList());

			List<Users> lstUser = this.userRepository.findByIamUserIDIn(userIds);
			for (LicenseApplicationApproval objApplicationApproval : lstLicenseApplicationApprovals) {
				if (!lstUser.isEmpty()) {
					Users objUser = lstUser.stream()
							.filter(x -> x.getIamUserID().equals(objApplicationApproval.getUserRid())).findAny()
							.orElse(new Users());
					objApplicationApproval.setUserRid(objUser.getEmployeeName());
				}
				if (!lstApprovalStatus.isEmpty()) {
					CodeMaster objStatus = lstApprovalStatus.stream().filter(
							x -> x.getCode().equals(String.valueOf(objApplicationApproval.getApprovalStatusRid())))
							.findAny().orElse(new CodeMaster());
					objApplicationApproval.setApprovalStatus(objStatus.getCodeDescriptionEnglish());

				}
				if (!lstRequestType.isEmpty()) {

					CodeMaster objStatus = lstRequestType.stream()
							.filter(x -> x.getCode().equals(String.valueOf(objApplicationApproval.getRequestTypeRid())))
							.findAny().orElse(new CodeMaster());
					objApplicationApproval.setRequestStatus(objStatus.getCodeDescriptionEnglish());

				}
			}

			objCrnViewResponse.setApproval(lstLicenseApplicationApprovals);

			return objCrnViewResponse;
		} else {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.CRN_NOT_FOUND.getValue());
		}
	}

	public Object zakatCertificateValidate(Long eun) {
		GetZakatCertificateByCrNumber validateCrn = new GetZakatCertificateByCrNumber();

		validateCrn.setCRNumber(eun);

		GetZakatCertificateByCrNumberResponse objZakatCerticate = soapClientService.callSoapService(validateCrn);

		if (objZakatCerticate.getGetZakatCertificateByCommercialRegistrationNumberForMainBranchOrSubBranchesResult()
				.getServiceError() != null) {
			return objZakatCerticate
					.getGetZakatCertificateByCommercialRegistrationNumberForMainBranchOrSubBranchesResult()
					.getServiceError();
		}

		return objZakatCerticate.getGetZakatCertificateByCommercialRegistrationNumberForMainBranchOrSubBranchesResult()
				.getZakatCertificate();
	}

	public LocalDate convertHijri(String hijariDateString) {
		DateTimeFormatter hijriDateFormatter = DateTimeFormatter.ofPattern(CommonCodes.DDMMYYYY)
				.withChronology(HijrahChronology.INSTANCE).withLocale(Locale.ENGLISH);

		HijrahDate hijrahDate = HijrahDate.from(hijriDateFormatter.parse(hijariDateString));

		return LocalDate.from(hijrahDate);

	}

	public ResponseEnvelope<LicenseApproveRejectResponse> licenseApproveReject(
			LicenseApproveRejectRequest approveRejectRequest) {

		LicenseApplication objApplication = this.licenceRepository.findByCrn(approveRejectRequest.getCrn());
		if (objApplication == null) {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.CRN_NOT_FOUND.getValue());

		}

		CodeMasterRequest objCodeMasterRequest = new CodeMasterRequest();
		objCodeMasterRequest.getKey1().add(CommonCodes.LICENSE_STATUS);
		List<CodeMaster> lstCodeMaster = this.restService.getByKeysCodeMaster(objCodeMasterRequest);

		objApplication.setLicenseStatusRid(
				licenseIsActive(approveRejectRequest.getApprovalStatusRid(), approveRejectRequest.getRequestTypeRid()));

		if (approveRejectRequest.getApprovalStatusRid().equals(CommonCodes.LCMAN_APPROVAL_CODE)) {
			LicenseApproveRejectResponse objResponse = new LicenseApproveRejectResponse();
			InvoiceCreateRequest objCreateRequest = new InvoiceCreateRequest();
			objCreateRequest.setCrn(String.valueOf(objApplication.getEunn()));
			objCreateRequest.setAgentType(2);
			objCreateRequest.setLicType(0);
			objCreateRequest.setOrderDate(LocalDate.now().toString());
			objCreateRequest.setInvoiceAmount(10000);
			objCreateRequest.setOrderNumber(generateOrderNumber());

			SoapEnvelope objEnvelope = this.restService.sadadPayment(objCreateRequest);
			if (objEnvelope.getBody().getCreateInvoiceResponse() != null) {
				String sadadNumber = objEnvelope.getBody().getCreateInvoiceResponse().getReturnDetails().getSadadNo();
				if (!sadadNumber.isEmpty()) {
					LicenseApproveRejectResponse objApproveRejectResponse = new LicenseApproveRejectResponse();
					objApproveRejectResponse.setCrn(objApplication.getCrn());
					objApproveRejectResponse.setSadadNumber(sadadNumber);
					objApplication.setApprovalStatusRid(approveRejectRequest.getApprovalStatusRid());
					objApplication.setRequestTypeRid(approveRejectRequest.getRequestTypeRid());
					objApplication.setSuspensionTo(approveRejectRequest.getSuspensionTo());
					objApplication.setFileName(approveRejectRequest.getFileName());
					objApplication.setFilePath(approveRejectRequest.getFilePath());
					objApplication.setUpdatedDate(LocalDateTime.now());

					objApplication.setSadadNo(sadadNumber);

					objResponse.setApprovalStatusRid(approveRejectRequest.getApprovalStatusRid());
					objResponse.setSadadNumber(sadadNumber);
					objResponse.setCrn(approveRejectRequest.getCrn());

					objApplication = this.licenceRepository.save(objApplication);

					LicenseApplicationApproval objLicenseApplicationApproval = approvalData(objApplication,
							approveRejectRequest);
					if (objLicenseApplicationApproval != null) {
						CodeMaster objCodeMaster = lstCodeMaster.stream().filter(
								x -> x.getCode().equals(String.valueOf(approveRejectRequest.getApprovalStatusRid())))
								.findFirst().orElse(new CodeMaster());

						ResponseEnvelope<LicenseApproveRejectResponse> apiResponse = new ResponseEnvelope<>();
						apiResponse.setData(objResponse);
						apiResponse.setResponseCode(HttpStatus.OK.value());
						apiResponse.setResponseMessage(
								String.format(CommonCodes.LICENSE_APPROVAL, objCodeMaster.getCodeDescriptionEnglish()));

						return apiResponse;
					} else {
						throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
								HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
					}
				} else {
					ResponseEnvelope<LicenseApproveRejectResponse> apiResponse = new ResponseEnvelope<>();
					apiResponse.setData(objResponse);
					apiResponse.setResponseCode(HttpStatus.OK.value());
					apiResponse.setResponseMessage(
							objEnvelope.getBody().getCreateInvoiceResponse().getReturnDetails().getStatusDetail());
					return apiResponse;
				}
			} else {
				throw new BusinessException(HttpStatus.SERVICE_UNAVAILABLE.value(),
						HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());
			}

		} else {

			objApplication.setApprovalStatusRid(approveRejectRequest.getApprovalStatusRid());
			objApplication.setRequestTypeRid(approveRejectRequest.getRequestTypeRid());

			objApplication.setSuspensionTo(approveRejectRequest.getSuspensionTo());
			objApplication.setFileName(approveRejectRequest.getFileName());
			objApplication.setFilePath(approveRejectRequest.getFilePath());
			objApplication.setUpdatedDate(LocalDateTime.now());
			LicenseApproveRejectResponse objResponse = new LicenseApproveRejectResponse();
			objApplication = this.licenceRepository.save(objApplication);
			objResponse.setApprovalStatusRid(approveRejectRequest.getApprovalStatusRid());
			objResponse.setSadadNumber(null);
			objResponse.setCrn(approveRejectRequest.getCrn());
			LicenseApplicationApproval objLicenseApplicationApproval = approvalData(objApplication,
					approveRejectRequest);
			if (objLicenseApplicationApproval != null) {
				ResponseEnvelope<LicenseApproveRejectResponse> apiResponse = new ResponseEnvelope<>();

				apiResponse.setData(objResponse);
				apiResponse.setResponseCode(HttpStatus.OK.value());
				apiResponse.setResponseMessage(statusMessage(objLicenseApplicationApproval.getRequestTypeRid(),
						objLicenseApplicationApproval.getApprovalStatusRid()));

				return apiResponse;
			} else {
				throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
						HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			}
		}

	}

	public LicenseApplicationApproval approvalData(LicenseApplication licenseApplication,
			LicenseApproveRejectRequest approveRejectRequest) {
		LicenseApplicationApproval objApplicationApproval = new LicenseApplicationApproval();

		objApplicationApproval.setLicenseApplicationId(licenseApplication.getId());
		objApplicationApproval.setUserRid(licenseApplication.getUpdatedBy());
		objApplicationApproval.setApprovalStatusRid(licenseApplication.getApprovalStatusRid());
		objApplicationApproval.setRequestTypeRid(licenseApplication.getRequestTypeRid());
		objApplicationApproval.setCreatedBy(licenseApplication.getCreatedBy());
		objApplicationApproval.setUpdatedBy(licenseApplication.getUpdatedBy());
		if (approveRejectRequest != null) {
			objApplicationApproval.setApprovalStatusRid(approveRejectRequest.getApprovalStatusRid());
			objApplicationApproval.setRequestTypeRid(approveRejectRequest.getRequestTypeRid());
			objApplicationApproval.setUserRid(approveRejectRequest.getCreatedBy());
			objApplicationApproval.setCreatedBy(approveRejectRequest.getCreatedBy());
			objApplicationApproval.setUpdatedBy(approveRejectRequest.getCreatedBy());
			objApplicationApproval.setRemark(approveRejectRequest.getRemark());
		}

		objApplicationApproval.setIsActive(true);

		return this.applicationApprovalRepository.save(objApplicationApproval);
	}

	public CreateInvoiceReturn sadadInvoiceGeneration(InvoiceCreateRequest createInvoiceRequest) {

		SoapEnvelope objSoapEnvelope = restService.sadadPayment(createInvoiceRequest);
		if (objSoapEnvelope != null) {
			return objSoapEnvelope.getBody().getCreateInvoiceResponse().getReturnDetails();
		} else {
			throw new BusinessException(HttpStatus.SERVICE_UNAVAILABLE.value(),
					HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());
		}

	}

	public ReturnDetails sadadPaymentStatusCheck(String sadadNo) {
		SoapEnvelope objSoapEnvelope = restService.sadadPaymentCheckStatus(sadadNo);

		if (objSoapEnvelope != null) {
			return objSoapEnvelope.getBody().getGetStatusResponse().getReturnDetails();
		} else {
			throw new BusinessException(HttpStatus.SERVICE_UNAVAILABLE.value(),
					HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());
		}

	}

	public LicenseOrganization licenseNumberDetails(String orgId) {

		List<LicenseOrganization> lstLicenseApplication = this.licenseOrganizationRepository
				.findByUserOrg_OrgIDAndLicenseStatusRid(orgId, 1);

		if (!lstLicenseApplication.isEmpty()) {
			return lstLicenseApplication.get(0);
		} else {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(), ExceptionMessage.LIC_NOT_FOUND.getValue());
		}
	}

	public Integer licenseIsActive(Integer approvalStaus, Integer requestType) {

		if (requestType.equals(Integer.valueOf(CommonCodes.NEW_LICENSE))
				&& !approvalStaus.equals(Integer.valueOf(CommonCodes.PAYMENT_SUCCESS))) {
			return 0;
		} else if (requestType.equals(Integer.valueOf(CommonCodes.CANCEL_LICENSE))
				&& approvalStaus.equals(Integer.valueOf(CommonCodes.APPROVED_LM))) {
			return 0;
		} else if (requestType.equals(Integer.valueOf(CommonCodes.CANCEL_LICENSE_LO))
				&& approvalStaus.equals(Integer.valueOf(CommonCodes.APPROVED_LM))) {
			return 0;
		} else if (requestType.equals(Integer.valueOf(CommonCodes.SUSPEND_LICENSE))
				&& approvalStaus.equals(Integer.valueOf(CommonCodes.APPROVED_LM))) {
			return 0;
		} else if (requestType.equals(Integer.valueOf(CommonCodes.RENEW_LICENSE))
				&& approvalStaus.equals(Integer.valueOf(CommonCodes.APPROVED_LO))) {
			return 0;
		} else if (requestType.equals(Integer.valueOf(CommonCodes.REACTIVATE_LICENSE))
				&& !approvalStaus.equals(Integer.valueOf(CommonCodes.APPROVED_LM))) {
			return 0;
		} else {
			return 1;
		}
	}

	private static String generateOrderNumber() {
		Random random = new Random();

		int orderNumberInt = 100000000 + random.nextInt(900000000);
		return String.valueOf(orderNumberInt);
	}

	public String statusMessage(Integer requestType, Integer approvalStatus) {
		if (requestType.equals(1) && approvalStatus.equals(11)) {
			return CommonCodes.LICENSE_REQ_RAISED;
		}
		if (requestType.equals(1) && approvalStatus.equals(14)) {
			return CommonCodes.LICENSE_REQ_APR;
		} else if (requestType.equals(1) && approvalStatus.equals(16)) {
			return CommonCodes.LICENSE_REQ_REJ;
		} else if (requestType.equals(2) && approvalStatus.equals(11)) {
			return CommonCodes.LICENSE_CANCELLED;
		} else if (requestType.equals(2) && approvalStatus.equals(12)) {
			return CommonCodes.LICENSE_CANCELLED_REQ_APPROVED;
		} else if (requestType.equals(2) && approvalStatus.equals(17)) {
			return CommonCodes.LICENSE_CANCELLED_REQ_APPROVED;
		} else if (requestType.equals(2) && approvalStatus.equals(16)) {
			return CommonCodes.LICENSE_CANCELLED_REJECT;
		} else if (requestType.equals(2) && approvalStatus.equals(18)) {
			return CommonCodes.LICENSE_CANCELLED_REJECT;
		} else if (requestType.equals(2) && approvalStatus.equals(18)) {
			return CommonCodes.LICENSE_CANCELLED_REJECT;
		} else if (requestType.equals(3) && approvalStatus.equals(12)) {
			return CommonCodes.LICENSE_CANCELLED;
		} else if (requestType.equals(3) && approvalStatus.equals(17)) {
			return CommonCodes.LICENSE_CANCELLED_REQ_APPROVED;
		} else if (requestType.equals(3) && approvalStatus.equals(18)) {
			return CommonCodes.LICENSE_CANCELLED_REJECT;
		} else if (requestType.equals(5) && approvalStatus.equals(12)) {
			return CommonCodes.LICENSE_SUSPEND;
		} else if (requestType.equals(5) && approvalStatus.equals(17)) {
			return CommonCodes.LICENSE_SUSPEND_REQ_APPROVED;
		} else if (requestType.equals(5) && approvalStatus.equals(18)) {
			return CommonCodes.LICENSE_SUSPEND_REJECT;
		} else if (requestType.equals(6) && approvalStatus.equals(12)) {
			return CommonCodes.LICENSE_REACT;
		} else if (requestType.equals(6) && approvalStatus.equals(17)) {
			return CommonCodes.LICENSE_REACT_REQ_APPROVED;
		} else if (requestType.equals(6) && approvalStatus.equals(18)) {
			return CommonCodes.LICENSE_REACT_REJECT;
		} else if (requestType.equals(4) && approvalStatus.equals(11)) {
			return CommonCodes.LICENSE_RENEW;
		} else if (requestType.equals(4) && approvalStatus.equals(14)) {
			return CommonCodes.LICENSE_RENEW_REQ_APPROVED;
		} else if (requestType.equals(4) && approvalStatus.equals(16)) {
			return CommonCodes.LICENSE_RENEW_REJECT;
		} else {
			return CommonCodes.LICENSE_MESSAGE;
		}

	}

}
