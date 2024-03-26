package sa.tabadul.useraccessmanagement.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sa.tabadul.useraccessmanagement.common.configs.PropertiesConfig;
import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.common.models.request.AccessCreateRequest;
import sa.tabadul.useraccessmanagement.common.models.request.ResourceServerRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserAccessRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserUpdateRequest;
import sa.tabadul.useraccessmanagement.common.models.response.Access;
import sa.tabadul.useraccessmanagement.common.models.response.AccessGetResponseV2;
import sa.tabadul.useraccessmanagement.common.models.response.AccessPages;
import sa.tabadul.useraccessmanagement.common.models.response.AccessResponse;
import sa.tabadul.useraccessmanagement.common.models.response.AccessResponseV2;
import sa.tabadul.useraccessmanagement.common.models.response.ResourcePermissions;
import sa.tabadul.useraccessmanagement.common.models.response.ResourcePermissionsPolicy;
import sa.tabadul.useraccessmanagement.common.models.response.ResourceResponse;
import sa.tabadul.useraccessmanagement.common.service.UserManagementExternalService;
import sa.tabadul.useraccessmanagement.domain.AccessHeader;
import sa.tabadul.useraccessmanagement.domain.AccessModule;
import sa.tabadul.useraccessmanagement.domain.AccessPage;
import sa.tabadul.useraccessmanagement.domain.AccessPageStakeHolder;
import sa.tabadul.useraccessmanagement.domain.ResourceServerPolicy;
import sa.tabadul.useraccessmanagement.domain.ResourceServerResource;
import sa.tabadul.useraccessmanagement.domain.ResourceServerScope;
import sa.tabadul.useraccessmanagement.domain.RoleMapping;
import sa.tabadul.useraccessmanagement.domain.UserRole;
import sa.tabadul.useraccessmanagement.exception.BusinessException;
import sa.tabadul.useraccessmanagement.repository.AccessHeaderRepository;
import sa.tabadul.useraccessmanagement.repository.AccessModuleRepository;
import sa.tabadul.useraccessmanagement.repository.AccessModulesRepository;
import sa.tabadul.useraccessmanagement.repository.AccessPageRepository;
import sa.tabadul.useraccessmanagement.repository.AccessStakeholderRepository;
import sa.tabadul.useraccessmanagement.repository.ResourceServerPolicyRepository;
import sa.tabadul.useraccessmanagement.repository.ResourceServerResourceRepository;
import sa.tabadul.useraccessmanagement.repository.ResourceServerScopeRepository;
import sa.tabadul.useraccessmanagement.repository.RoleMappingRepository;

@Service
public class AccessManagerService  {



	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AccessHeaderRepository accessHeaderRepository;

	@Autowired
	RoleMappingRepository roleMappingRepository;


	@Autowired
	AccessModuleRepository accessModuleRepository;

	@Autowired
	AccessPageRepository accessPageRepository;

	@Autowired
	AccessStakeholderRepository accessStakeholderRepository;
	
	@Autowired
	AccessModulesRepository accessModulesRepository;
	
	@Autowired
	ResourceServerResourceRepository resourceRepository;
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	@Autowired
	ResourceServerPolicyRepository resourceServerPolicyRepository; 
	
	@Autowired
	ResourceServerScopeRepository resourceScopeRepository;
	
	@Autowired
	UserManagementExternalService externalApiService;


	@Transactional
	public List<AccessResponse> ViewUserAccess(UserAccessRequest request) {

		Optional<AccessModule> optional = this.accessModuleRepository.findById(request.getModuleId());

		
		if (optional.get().equals(null)) {
			throw new BusinessException(HttpStatus.NOT_FOUND.value(),"moduleId not found "+request.getModuleId());
		}

		Optional<AccessHeader> tempheader = this.accessHeaderRepository.findById(optional.get().getAccessHeaderID());
		List<AccessResponse> response = new ArrayList<>();
		AccessResponse tempResp = new AccessResponse();
		AccessPageStakeHolder stakeholders = new AccessPageStakeHolder();

		List<AccessPage> lstAccessPage = this.accessPageRepository.findByAccessModuleIDAndAppId(request.getModuleId(), request.getAppId());

		for (int i = 0; i < lstAccessPage.size(); i++) {
			AccessPage accessPage = lstAccessPage.get(i);

//			List<AccessPageStakeHolder> sktcheck = this.accessStakeholderRepository
//					.checkstkholder(request.getStakeholder_type(), accessPage.getId());
			List<AccessPageStakeHolder> sktcheck = this.accessStakeholderRepository
					.findAllByStakeholderCategoryRIDAndAccesspageID(request.getStakeholderType(), accessPage.getId());
//			List<AccessPageStakeHolder> sktsubcheck = this.accessStakeholderRepository
//					.checkstkholderandsubrole(request.getStakeholder_type(), request.getSub_role(), accessPage.getId());
			List<AccessPageStakeHolder> sktsubcheck = this.accessStakeholderRepository
					.findAllByStakeholderCategoryRIDAndStakeholdertypeIDAndAccesspageID(request.getStakeholderType(), request.getSubRole(), accessPage.getId());
			
			if (accessPage.getAccessPageStakeHolder().isEmpty() || sktcheck.isEmpty() && (!accessPage.getPageCode().equals(CommonCodes.ACCESSPAGE))) {

				tempResp.setHeadId(tempheader.get().getId());
				tempResp.setHeader(tempheader.get().getHeader());
				tempResp.setHeaderIconPath(tempheader.get().getHeaderIconPath());
				tempResp.setHeaderDescription(tempheader.get().getHeaderDescription());
				tempResp.setHeadSequenceNo(tempheader.get().getHeadSequenceNo());
				tempResp.setModuleId(optional.get().getId());
				tempResp.setModuleDescription(optional.get().getModuleDescription());
				tempResp.setModuleIconPath(optional.get().getModuleIconPath());
				tempResp.setAccesspageID(accessPage.getId());
				tempResp.setAppId(accessPage.getAppId());
				tempResp.setPageCode(accessPage.getPageCode());
				tempResp.setPage(accessPage.getPage());
				tempResp.setPageDescription(accessPage.getPageDescription());
				tempResp.setPageIconPath(accessPage.getPageIconPath());
				tempResp.setStakeholdertypeID(Integer.valueOf(request.getSubRole()));
				tempResp.setStakeholderCategoryRID(request.getStakeholderType());
				tempResp.setAccessCreate(false);
				tempResp.setAccessEdit(false);
				tempResp.setAccessDelete(false);
				tempResp.setAccessView(false);
				tempResp.setAccessApproveReject(false);
				tempResp.setAccessPrint(false);
				tempResp.setIsActive(false);
				response.add(tempResp);
				tempResp = new AccessResponse();

			} else if (sktsubcheck.isEmpty() && (!accessPage.getPageCode().equals(CommonCodes.ACCESSPAGE))) {
				tempResp.setHeadId(tempheader.get().getId());
				tempResp.setHeader(tempheader.get().getHeader());
				tempResp.setHeaderIconPath(tempheader.get().getHeaderIconPath());
				tempResp.setHeaderDescription(tempheader.get().getHeaderDescription());
				tempResp.setHeadSequenceNo(tempheader.get().getHeadSequenceNo());
				tempResp.setModuleId(optional.get().getId());
				tempResp.setModuleDescription(optional.get().getModuleDescription());
				tempResp.setModuleIconPath(optional.get().getModuleIconPath());
				tempResp.setAccesspageID(accessPage.getId());
				tempResp.setAppId(accessPage.getAppId());
				tempResp.setPageCode(accessPage.getPageCode());
				tempResp.setPage(accessPage.getPage());
				tempResp.setPageDescription(accessPage.getPageDescription());
				tempResp.setPageIconPath(accessPage.getPageIconPath());
				tempResp.setStakeholdertypeID(Integer.valueOf(request.getSubRole()));
				tempResp.setStakeholderCategoryRID(request.getStakeholderType());
				tempResp.setAccessCreate(false);
				tempResp.setAccessEdit(false);
				tempResp.setAccessDelete(false);
				tempResp.setAccessView(false);
				tempResp.setAccessApproveReject(false);
				tempResp.setAccessPrint(false);
				tempResp.setIsActive(false);
				response.add(tempResp);
				tempResp = new AccessResponse();

			} else {
				for (int j = 0; j < accessPage.getAccessPageStakeHolder().size(); j++) {
					if (request.getSubRole() == 0) {
						stakeholders = null;
						if (accessPage.getAccessPageStakeHolder().get(j).getStakeholderCategoryRID().equals(request.getStakeholderType()) 
								&& accessPage.getAccessPageStakeHolder().get(j).getStakeholdertypeID() == 0) {
							stakeholders = accessPage.getAccessPageStakeHolder().get(j);
						}
						if (stakeholders != null) {
							if (stakeholders.getStakeholderCategoryRID().equals(request.getStakeholderType()) && (!accessPage.getPageCode().equals(CommonCodes.ACCESSPAGE))) {

								tempResp.setHeadId(tempheader.get().getId());
								tempResp.setHeader(tempheader.get().getHeader());
								tempResp.setHeaderIconPath(tempheader.get().getHeaderIconPath());
								tempResp.setHeaderDescription(tempheader.get().getHeaderDescription());
								tempResp.setHeadSequenceNo(tempheader.get().getHeadSequenceNo());
								tempResp.setModuleId(optional.get().getId());
								tempResp.setModuleDescription(optional.get().getModuleDescription());
								tempResp.setModuleIconPath(optional.get().getModuleIconPath());
								tempResp.setPageId(accessPage.getId());
								tempResp.setAccesspageID(accessPage.getId());
								tempResp.setAppId(accessPage.getAppId());
								tempResp.setPageCode(accessPage.getPageCode());
								tempResp.setPage(accessPage.getPage());
								tempResp.setPageDescription(accessPage.getPageDescription());
								tempResp.setPageIconPath(accessPage.getPageIconPath());
								tempResp.setSkateHolderID(stakeholders.getId());
								tempResp.setStakeholdertypeID(stakeholders.getStakeholdertypeID());
								tempResp.setStakeholderCategoryRID(stakeholders.getStakeholderCategoryRID());
								tempResp.setAccessCreate(stakeholders.getAccessCreate());
								tempResp.setAccessEdit(stakeholders.getAccessEdit());
								tempResp.setAccessDelete(stakeholders.getAccessDelete());
								tempResp.setAccessView(stakeholders.getAccessView());
								tempResp.setAccessApproveReject(stakeholders.getAccessApproveReject());
								tempResp.setAccessPrint(stakeholders.getAccessPrint());
								tempResp.setIsActive(stakeholders.getIsActive());
								tempResp.setEndpointUrl(optional.get().getModuleRoute() + accessPage.getPageRoute());
								response.add(tempResp);
								tempResp = new AccessResponse();

							} else {

								if (this.accessStakeholderRepository.findAllByStakeholderCategoryRIDAndAccesspageID(
										stakeholders.getStakeholderCategoryRID(),
										stakeholders.getAccesspageID()) == null && (!accessPage.getPageCode().equals(CommonCodes.ACCESSPAGE))) {
									tempResp.setHeadId(tempheader.get().getId());
									tempResp.setHeader(tempheader.get().getHeader());
									tempResp.setHeaderIconPath(tempheader.get().getHeaderIconPath());
									tempResp.setHeaderDescription(tempheader.get().getHeaderDescription());
									tempResp.setHeadSequenceNo(tempheader.get().getHeadSequenceNo());
									tempResp.setModuleId(optional.get().getId());
									tempResp.setModuleDescription(optional.get().getModuleDescription());
									tempResp.setModuleIconPath(optional.get().getModuleIconPath());
									tempResp.setAccesspageID(accessPage.getId());
									tempResp.setAppId(accessPage.getAppId());
									tempResp.setPageCode(accessPage.getPageCode());
									tempResp.setPage(accessPage.getPage());
									tempResp.setPageDescription(accessPage.getPageDescription());
									tempResp.setPageIconPath(accessPage.getPageIconPath());
									tempResp.setStakeholdertypeID(Integer.valueOf(request.getSubRole()));
									tempResp.setStakeholderCategoryRID(request.getStakeholderType());
									tempResp.setAccessCreate(false);
									tempResp.setAccessEdit(false);
									tempResp.setAccessDelete(false);
									tempResp.setAccessView(false);
									tempResp.setAccessApproveReject(false);
									tempResp.setAccessPrint(false);
									tempResp.setIsActive(false);
									response.add(tempResp);
									tempResp = new AccessResponse();
								}
							}

						} else {

						}

					} else {

						stakeholders = null;
						if (accessPage.getAccessPageStakeHolder().get(j).getStakeholderCategoryRID().equals(request
								.getStakeholderType())
								&& accessPage.getAccessPageStakeHolder().get(j).getStakeholdertypeID().equals(request
										.getSubRole())
								&& accessPage.getAccessPageStakeHolder().get(j).getIsActive()) {
							stakeholders = accessPage.getAccessPageStakeHolder().get(j);
						}
						if (stakeholders != null) {
							if (stakeholders.getStakeholderCategoryRID().equals(request.getStakeholderType())
									&& accessPage.getAccessPageStakeHolder().get(j).getStakeholdertypeID().equals(request
											.getSubRole())
									&& stakeholders.getIsActive() && (!accessPage.getPageCode().equals(CommonCodes.ACCESSPAGE))) {

								tempResp.setHeadId(tempheader.get().getId());
								tempResp.setHeader(tempheader.get().getHeader());
								tempResp.setHeaderIconPath(tempheader.get().getHeaderIconPath());
								tempResp.setHeaderDescription(tempheader.get().getHeaderDescription());
								tempResp.setHeadSequenceNo(tempheader.get().getHeadSequenceNo());
								tempResp.setModuleId(optional.get().getId());
								tempResp.setModuleDescription(optional.get().getModuleDescription());
								tempResp.setModuleIconPath(optional.get().getModuleIconPath());
								tempResp.setPageId(accessPage.getId());
								tempResp.setAccesspageID(accessPage.getId());
								tempResp.setAppId(accessPage.getAppId());
								tempResp.setPageCode(accessPage.getPageCode());
								tempResp.setPage(accessPage.getPage());
								tempResp.setPageDescription(accessPage.getPageDescription());
								tempResp.setPageIconPath(accessPage.getPageIconPath());
								tempResp.setSkateHolderID(stakeholders.getId());
								tempResp.setStakeholdertypeID(stakeholders.getStakeholdertypeID());
								tempResp.setStakeholderCategoryRID(stakeholders.getStakeholderCategoryRID());
								tempResp.setAccessCreate(stakeholders.getAccessCreate());
								tempResp.setAccessEdit(stakeholders.getAccessEdit());
								tempResp.setAccessDelete(stakeholders.getAccessDelete());
								tempResp.setAccessView(stakeholders.getAccessView());
								tempResp.setAccessApproveReject(stakeholders.getAccessApproveReject());
								tempResp.setAccessPrint(stakeholders.getAccessPrint());
								tempResp.setIsActive(stakeholders.getIsActive());
								tempResp.setEndpointUrl(optional.get().getModuleRoute() + accessPage.getPageRoute());
								response.add(tempResp);
								tempResp = new AccessResponse();
							} else {

								if (this.accessStakeholderRepository.findAllByStakeholderCategoryRIDAndStakeholdertypeIDAndAccesspageID(
										stakeholders.getStakeholderCategoryRID(),
										stakeholders.getStakeholdertypeID(),
										stakeholders.getAccesspageID()) == null && (!accessPage.getPageCode().equals(CommonCodes.ACCESSPAGE))) {
									tempResp.setHeadId(tempheader.get().getId());
									tempResp.setHeader(tempheader.get().getHeader());
									tempResp.setHeaderIconPath(tempheader.get().getHeaderIconPath());
									tempResp.setHeaderDescription(tempheader.get().getHeaderDescription());
									tempResp.setHeadSequenceNo(tempheader.get().getHeadSequenceNo());
									tempResp.setModuleId(optional.get().getId());
									tempResp.setModuleDescription(optional.get().getModuleDescription());
									tempResp.setModuleIconPath(optional.get().getModuleIconPath());
									tempResp.setAccesspageID(accessPage.getId());
									tempResp.setAppId(accessPage.getAppId());
									tempResp.setPageCode(accessPage.getPageCode());
									tempResp.setPage(accessPage.getPage());
									tempResp.setPageDescription(accessPage.getPageDescription());
									tempResp.setPageIconPath(accessPage.getPageIconPath());
									tempResp.setStakeholdertypeID(Integer.valueOf(request.getSubRole()));
									tempResp.setStakeholderCategoryRID(request.getStakeholderType());
									tempResp.setAccessCreate(false);
									tempResp.setAccessEdit(false);
									tempResp.setAccessDelete(false);
									tempResp.setAccessView(false);
									tempResp.setAccessApproveReject(false);
									tempResp.setAccessPrint(false);
									tempResp.setIsActive(false);
									response.add(tempResp);
									tempResp = new AccessResponse();
								}

							}

						} else {

						}
					}

				}
			}
		}

		return response;
	}

	@Async
	public List<AccessResponse> ViewAccessByshType(int shtype, Integer subrole, int app_id) {
		
		List<AccessModule> optional = this.accessModuleRepository.findByIsActive(true);
		List<AccessResponse> response = new ArrayList<>();
		for (AccessModule accessModule : optional) {
			for (int i = 0; i < accessModule.getAccessPage().size(); i++) {
				List<AccessPage> accessPage = accessModule.getAccessPage();
				for (int j = 0; j < accessPage.get(i).getAccessPageStakeHolder().size(); j++) {

					AccessHeader tempheader = accessModule.getAccessHeader();
					AccessResponse tempResp = new AccessResponse();

					AccessPageStakeHolder holders = accessPage.get(i).getAccessPageStakeHolder().get(j);
					if (subrole == null) {
						if (holders.getStakeholderCategoryRID() == shtype && holders.getStakeholdertypeID() == 0
								&& holders.getIsActive() == true) {
							if (accessPage.get(i).getAppId() == app_id && accessPage.get(i).getIsActive() == true) {
								tempResp.setHeadId(tempheader.getId());
								tempResp.setHeader(tempheader.getHeader());
								tempResp.setHeaderIconPath(tempheader.getHeaderIconPath());
								tempResp.setHeaderDescription(tempheader.getHeaderDescription());
								tempResp.setHeadSequenceNo(tempheader.getHeadSequenceNo());
								tempResp.setModuleId(accessModule.getId());
								tempResp.setModuleDescription(accessModule.getModuleDescription());
								tempResp.setModuleIconPath(accessModule.getModuleIconPath());
								tempResp.setAccesspageID(accessPage.get(i).getId());
								tempResp.setAppId(accessPage.get(i).getAppId());
								tempResp.setPageId(accessPage.get(i).getId());
								tempResp.setPageCode(accessPage.get(i).getPageCode());
								tempResp.setPage(accessPage.get(i).getPage());
								tempResp.setPageDescription(accessPage.get(i).getPageDescription());
								tempResp.setPageIconPath(accessPage.get(i).getPageIconPath());
								tempResp.setSkateHolderID(holders.getId());
								tempResp.setStakeholdertypeID(holders.getStakeholdertypeID());
								tempResp.setStakeholderCategoryRID(holders.getStakeholderCategoryRID());
								tempResp.setAccessCreate(holders.getAccessCreate());
								tempResp.setAccessEdit(holders.getAccessEdit());
								tempResp.setAccessDelete(holders.getAccessDelete());
								tempResp.setAccessView(holders.getAccessView());
								tempResp.setAccessApproveReject(holders.getAccessApproveReject());
								tempResp.setAccessPrint(holders.getAccessPrint());
								tempResp.setIsActive(holders.getIsActive());
								tempResp.setEndpointUrl(
										accessModule.getModuleRoute() + accessPage.get(i).getPageRoute());
								response.add(tempResp);
							}
						}

					} else {
						if (holders.getStakeholderCategoryRID() == shtype
								&& holders.getStakeholdertypeID() == subrole && holders.getIsActive() == true) {
							if (accessPage.get(i).getAppId() == app_id && accessPage.get(i).getIsActive() == true) {
								tempResp.setHeadId(tempheader.getId());
								tempResp.setHeader(tempheader.getHeader());
								tempResp.setHeaderIconPath(tempheader.getHeaderIconPath());
								tempResp.setHeaderDescription(tempheader.getHeaderDescription());
								tempResp.setHeadSequenceNo(tempheader.getHeadSequenceNo());
								tempResp.setModuleId(accessModule.getId());
								tempResp.setModuleDescription(accessModule.getModuleDescription());
								tempResp.setModuleIconPath(accessModule.getModuleIconPath());
								tempResp.setAccesspageID(accessPage.get(i).getId());
								tempResp.setAppId(accessPage.get(i).getAppId());
								tempResp.setPageId(accessPage.get(i).getId());
								tempResp.setPageCode(accessPage.get(i).getPageCode());
								tempResp.setPage(accessPage.get(i).getPage());
								tempResp.setPageDescription(accessPage.get(i).getPageDescription());
								tempResp.setPageIconPath(accessPage.get(i).getPageIconPath());
								tempResp.setSkateHolderID(holders.getId());
								tempResp.setStakeholdertypeID(holders.getStakeholdertypeID());
								tempResp.setStakeholderCategoryRID(holders.getStakeholderCategoryRID());
								tempResp.setAccessCreate(holders.getAccessCreate());
								tempResp.setAccessEdit(holders.getAccessEdit());
								tempResp.setAccessDelete(holders.getAccessDelete());
								tempResp.setAccessView(holders.getAccessView());
								tempResp.setAccessApproveReject(holders.getAccessApproveReject());
								tempResp.setAccessPrint(holders.getAccessPrint());
								tempResp.setIsActive(holders.getIsActive());
								tempResp.setEndpointUrl(
										accessModule.getModuleRoute() + accessPage.get(i).getPageRoute());
								response.add(tempResp);
							}
						}
					}
				}
			}
		}

		return response;

	}

	public String updateUserAccess(UserUpdateRequest enity) {

		for (RoleMapping mapping : enity.getActivityList()) {
			mapping.setId(mapping.getSkateHolderID());
			this.roleMappingRepository.save(mapping);
		}

		return "updated";

	}

	public List<AccessHeader> accessHeader() {
		return this.accessHeaderRepository.findAll();
	}

	public List<AccessPages> accessPages(Integer stakteholderID) {
		Integer appID =2;
		//List<AccessPage> lstaccessPage = this.accessPageRepository.findAllAccessPage(appID, stakteholderID);
		
		List<AccessPage> lstaccessPage = this.accessPageRepository
				.findByAccessPageStakeHolder_StakeholderCategoryRIDAndAppId(stakteholderID, appID);
		List<AccessPages> accessPageDTO =  lstaccessPage.stream().map(accessPage->
		modelMapper.map(accessPage, AccessPages.class))
		.collect(Collectors.toList());
		
		
		return accessPageDTO;
	}
	
	
	@Transactional
	public List<AccessResponse> stakeHoldersAccess(List<UserRole> lstUserRoles, Integer appId)
	{
		List<AccessResponse> response = new ArrayList<>();
		List<AccessModule> lstAccessModule = this.accessModuleRepository.findByIsActive(true);
		lstUserRoles.stream().forEach(roles->{
			
		 lstAccessModule.stream().forEach(module->{
				
			 module.getAccessPage().stream().forEach(page->{
				 if(page.getAppId().equals(appId)) {
					
				   page.getAccessPageStakeHolder().stream().forEach(role->{
					 if(roles.getStakeholderSubroleId().equals(0) && role.getStakeholderCategoryRID().equals(roles.getStakeholderTypeId()) && role.getStakeholdertypeID().equals(0) && role.getAccessView().equals(true)) {
						 AccessResponse tempResp = new AccessResponse();
						 tempResp.setHeadId(module.getAccessHeader().getId());
					tempResp.setHeader(module.getAccessHeader().getHeader());
					tempResp.setHeaderAr(module.getAccessHeader().getHeaderAr());
					tempResp.setHeaderIconPath(module.getAccessHeader().getHeaderIconPath());
					tempResp.setHeaderDescription(module.getAccessHeader().getHeaderDescription());
					tempResp.setHeadSequenceNo(module.getAccessHeader().getHeadSequenceNo());
					tempResp.setModuleId(module.getId());
					tempResp.setModuleDescription(module.getModuleDescription());
					tempResp.setModuleIconPath(module.getModuleIconPath());
					tempResp.setAccesspageID(page.getId());
					tempResp.setAppId(page.getAppId());
					tempResp.setPageId(page.getId());
					tempResp.setPageCode(page.getPageCode());
					tempResp.setPage(page.getPage());
					tempResp.setPageArabic(page.getPageArabic());
					tempResp.setPageDescription(page.getPageDescription());
					tempResp.setPageDescriptionArabic(page.getPageDescriptionArabic());
					tempResp.setPageIconPath(page.getPageIconPath());
					tempResp.setPageSequence(page.getSequence());
					tempResp.setMawaniMenuSetting(page.getMawaniMenuSetting());
					tempResp.setSkateHolderID(role.getId());
					tempResp.setStakeholdertypeID(role.getStakeholdertypeID());
					tempResp.setStakeholderCategoryRID(role.getStakeholderCategoryRID());
					tempResp.setAccessCreate(role.getAccessCreate());
					tempResp.setAccessEdit(role.getAccessEdit());
					tempResp.setAccessDelete(role.getAccessDelete());
					tempResp.setAccessView(role.getAccessView());
					tempResp.setAccessApproveReject(role.getAccessApproveReject());
					tempResp.setAccessPrint(role.getAccessPrint());
					tempResp.setIsActive(role.getIsActive());
					tempResp.setEndpointUrl(
							module.getModuleRoute() + page.getPageRoute());
					response.add(tempResp);
					 }
					 else if(role.getStakeholderCategoryRID().equals(roles.getStakeholderTypeId()) && role.getStakeholdertypeID().equals(roles.getStakeholderSubroleId()) && role.getAccessView().equals(true))
					 {
						 AccessResponse tempResp = new AccessResponse();
						 tempResp.setHeadId(module.getAccessHeader().getId());
							tempResp.setHeader(module.getAccessHeader().getHeader());
							tempResp.setHeaderAr(module.getAccessHeader().getHeaderAr());
							tempResp.setHeaderIconPath(module.getAccessHeader().getHeaderIconPath());
							tempResp.setHeaderDescription(module.getAccessHeader().getHeaderDescription());
							tempResp.setHeadSequenceNo(module.getAccessHeader().getHeadSequenceNo());
							tempResp.setModuleId(module.getId());
							tempResp.setModuleDescription(module.getModuleDescription());
							tempResp.setModuleIconPath(module.getModuleIconPath());
							tempResp.setAccesspageID(page.getId());
							tempResp.setAppId(page.getAppId());
							tempResp.setPageId(page.getId());
							tempResp.setPageCode(page.getPageCode());
							tempResp.setPage(page.getPage());
							tempResp.setPageArabic(page.getPageArabic());	
							tempResp.setPageDescription(page.getPageDescription());
							tempResp.setPageDescriptionArabic(page.getPageDescriptionArabic());
							tempResp.setPageIconPath(page.getPageIconPath());
							tempResp.setPageSequence(page.getSequence());
							tempResp.setMawaniMenuSetting(page.getMawaniMenuSetting());
							tempResp.setSkateHolderID(role.getId());
							tempResp.setStakeholdertypeID(role.getStakeholdertypeID());
							tempResp.setStakeholderCategoryRID(role.getStakeholderCategoryRID());
							tempResp.setAccessCreate(role.getAccessCreate());
							tempResp.setAccessEdit(role.getAccessEdit());
							tempResp.setAccessDelete(role.getAccessDelete());
							tempResp.setAccessView(role.getAccessView());
							tempResp.setAccessApproveReject(role.getAccessApproveReject());
							tempResp.setAccessPrint(role.getAccessPrint());
							tempResp.setIsActive(role.getIsActive());
							tempResp.setEndpointUrl(
									module.getModuleRoute() + page.getPageRoute());
							response.add(tempResp);
					 }
					
					 
						 
					 
				    });
				   
				
				 }
				});
			 
				
			}); 
		
			
		});
		
		List<AccessResponse> sequenceAccess = response.stream().sorted(Comparator.comparing(AccessResponse::getPageSequence, Comparator.nullsLast(Comparator.naturalOrder()))).collect(Collectors.toList());
		
		
		
		return sequenceAccess;
	}
	
	public List<AccessResponse> UserAccess(List<Integer> roleIds, List<Integer> subRoles, Integer appId) {
		
		List<Access> lstAccess = this.accessModulesRepository.userAccess(roleIds,subRoles,appId);
		List<AccessResponse> lstAccessResponses = new ArrayList<>();
		for (Access access : lstAccess) {
			AccessResponse tempResp = new AccessResponse();
			tempResp.setHeadId(access.getHeaderId());
			tempResp.setHeader(access.getHeader());
			tempResp.setHeaderAr(access.getHeaderAr());
			tempResp.setHeaderIconPath(access.getHeaderIconPath());
			tempResp.setHeaderDescription(access.getHeaderDescription());
			tempResp.setHeadSequenceNo(access.getHeadSequenceNo());
			tempResp.setModuleId(access.getModuleId());
			tempResp.setModuleDescription(access.getModuleDescription());
			tempResp.setModuleIconPath(access.getModuleIconPath());
			tempResp.setAccesspageID(access.getPageId());
			tempResp.setAppId(access.getAppId());
			tempResp.setPageId(access.getPageId());
			tempResp.setPageCode(access.getPageCode());
			tempResp.setPage(access.getPage());
			tempResp.setPageArabic(access.getPageArabic());
			tempResp.setPageDescription(access.getPageDescription());
			tempResp.setPageDescriptionArabic(access.getPageDescriptionArabic());
			tempResp.setPageIconPath(access.getPageIconPath());
			tempResp.setPageSequence(access.getSequence());
			tempResp.setMawaniMenuSetting(access.getMawaniMenuSetting());
			tempResp.setSkateHolderID(access.getStakeholderId());
			tempResp.setStakeholdertypeID(access.getStakeholderTypeID());
			tempResp.setStakeholderCategoryRID(access.getStakeholderCategoryRID());
			tempResp.setAccessCreate(access.getAccessCreate());
			tempResp.setAccessEdit(access.getAccessEdit());
			tempResp.setAccessDelete(access.getAccessDelete());
			tempResp.setAccessView(access.getAccessView());
			tempResp.setAccessApproveReject(access.getAccessApproveReject());
			tempResp.setAccessPrint(access.getAccessPrint());
			tempResp.setIsActive(access.getIsActive());
			tempResp.setEndpointUrl(access.getModuleRoute() + access.getPageRoute());
			lstAccessResponses.add(tempResp);
		}
		return lstAccessResponses;

	}
	
//----------------------------------------------------------------------------------------------------------------------------------------
	
	public AccessGetResponseV2 getAllAccess(ResourceServerRequest accessRequest) {
	    String appId = null;
	    if ("2".equals(accessRequest.getAppId())) {
	        appId = propertiesConfig.getResourceServerId();
	    }

	    List<ResourceServerResource> lstModules = resourceRepository.findByServerIdAndModuleType(appId, "page: " + accessRequest.getModuleName());

	    List<ResourceResponse> lstResource = resourceRepository.GetAllResources(appId, accessRequest.getStakeholderType() + "_Policy", "page: " + accessRequest.getModuleName());

	    List<ResourceResponse> lstResource2 = new ArrayList<>();

	    List<AccessResponseV2> lstPages = new ArrayList<>();
	    AccessGetResponseV2 accessGetResponseV2 = new AccessGetResponseV2();
	    String policyId = null ;
	    String policyName = null;
	    AccessResponseV2 newResponse = null;
	    if (lstResource.isEmpty()) {
	        ResourceServerPolicy resourceServerPolicy = resourceServerPolicyRepository.findByTypeAndName("role", accessRequest.getStakeholderType() + "_Policy");
	        policyId = resourceServerPolicy.getId();
	        policyName = resourceServerPolicy.getName();
	    } else {
	        for (ResourceServerResource resourceServerResource : lstModules) {
	            lstResource.stream()
	                .filter(resourceResponse -> resourceServerResource.getId().equals(resourceResponse.getResourceID()))
	                .forEach(lstResource2::add);
	        }
	    }
	    
	    for (ResourceResponse resourceResponse : lstResource2) {
	        boolean responseMerged = false;

	        // Check if the response can be merged with an existing one
	        for (AccessResponseV2 existingResponse : lstPages) {
	            if (existingResponse.getPageId().equals(resourceResponse.getResourceID()) ||
	                existingResponse.getPage().equals(resourceResponse.getResourceName())) {
	                // If response can be merged, update the existing response with new permissions
	                updatePermissions(existingResponse, resourceResponse);
	                responseMerged = true;
	                break;
	            }
	        }

	        // If the response couldn't be merged, create a new response and add it to the list
	        if (!responseMerged) {
	            newResponse = createAccessResponse(resourceResponse, accessRequest);
	            lstPages.add(newResponse);
	        }
	    }

	    // Your existing code...
	    String policyIds = newResponse.getPolicyId();
		String policyNames = newResponse.getPolicyName();
		lstModules.stream()
		    .filter(resource -> lstPages.stream().noneMatch(x -> x.getPageId().equals(resource.getId())))
		    .forEach(resource -> {
		        AccessResponseV2 accessResponse = new AccessResponseV2();
		        accessResponse.setPageId(resource.getId());
		        accessResponse.setPage(resource.getName());
		        accessResponse.setPolicyId(policyIds);
		        accessResponse.setPolicyName(policyNames);
		        accessResponse.setAppId(accessRequest.getAppId());
		        accessResponse.setStakeholdertypeID(accessRequest.getStakeholderType());
		        lstPages.add(accessResponse);
		    });
	    accessGetResponseV2.setPages(lstPages);
	    accessGetResponseV2.setModuleName(accessRequest.getModuleName());
	    accessGetResponseV2.setStakeHolder(accessRequest.getStakeholderType());
	    return accessGetResponseV2;
	}

	// Helper method to update permissions in existing response
	private void updatePermissions(AccessResponseV2 existingResponse, ResourceResponse resourceResponse) {
	    if (resourceResponse.getPermissionName().contains("create")) {
	        existingResponse.setAccessCreate(true);
	    } else if (resourceResponse.getPermissionName().contains("edit")) {
	        existingResponse.setAccessEdit(true);
	    } else if (resourceResponse.getPermissionName().contains("view")) {
	        existingResponse.setAccessView(true);
	    } else if (resourceResponse.getPermissionName().contains("approv")) {
	        existingResponse.setAccessApproveReject(true);
	    } else if (resourceResponse.getPermissionName().contains("print")) {
	        existingResponse.setAccessPrint(true);
	    } else if (resourceResponse.getPermissionName().contains("delete")) {
	        existingResponse.setAccessDelete(true);
	    }
	}

	// Helper method to create a new access response
	private AccessResponseV2 createAccessResponse(ResourceResponse resourceResponse, ResourceServerRequest accessRequest) {
	    AccessResponseV2 accessResponse = new AccessResponseV2();
	    accessResponse.setPageId(resourceResponse.getResourceID());
	    accessResponse.setPage(resourceResponse.getResourceName());
	    accessResponse.setPolicyId(resourceResponse.getPolicyID());
	    accessResponse.setPolicyName(resourceResponse.getPolicyName());
	    accessResponse.setAppId(accessRequest.getAppId());
	    accessResponse.setStakeholdertypeID(accessRequest.getStakeholderType());
	    updatePermissions(accessResponse, resourceResponse);
	    return accessResponse;
	}

	
	public String createAccess(AccessGetResponseV2 request, String token) {
		ResourceServerRequest accessRequest = new ResourceServerRequest(request.getModuleName(),
				request.getStakeHolder(), request.getPages().get(0).getAppId(), null);

		AccessGetResponseV2 lstResponse = getAllAccess(accessRequest);
		List<AccessResponseV2> pages = request.getPages();
		List<AccessResponseV2> pages2 = lstResponse.getPages();
		
		for (AccessResponseV2 page : pages) {
			AccessResponseV2 newPages = pages2.stream().filter(x -> x.getPage().equals(page.getPage())).findFirst()
					.orElse(null);
			List<ResourcePermissionsPolicy> lstPermissions = resourceRepository.getAllPermissionsWithPolicy(page.getPageId(),
					propertiesConfig.getResourceServerId());

			List<ResourcePermissions> lstResourcePermissions = resourceRepository.getAllPermissions(page.getPageId());

			if(Boolean.compare(newPages.getAccessPrint(), page.getAccessPrint())!=0) {

				ResourcePermissionsPolicy objPermission = lstPermissions.stream()
						.filter(permissions -> permissions.getPermissionName().contains("print")
								&& permissions.getPolicyName().contains(request.getStakeHolder() + "_Policy"))
						.findFirst().orElse(null);
				List<String> resourceId = new ArrayList<>();
				resourceId.add(page.getPageId());
				// all scopes
				List<ResourceServerScope> lstScopes = resourceScopeRepository
						.allScopes(propertiesConfig.getResourceServerId());
				
				if (objPermission != null) {
					// remove
					AccessCreateRequest accessCreateRequest = new AccessCreateRequest();
					List<ResourcePermissions> lstPermission = lstResourcePermissions.stream()
							.filter(x -> x.getPermissionName().contains("print")).collect(Collectors.toList());
					String permissionId = lstPermission.get(0).getPermissionId();
					ResourceServerScope objScope = lstScopes.stream().filter(x -> x.getName().contains("print"))
							.findFirst().orElse(null);
					List<String> lstPolicies; 
					lstPolicies = lstPermissions.stream().filter(x->x.getPermissionId().equals(permissionId)).map(ResourcePermissionsPolicy::getPolicyId).collect(Collectors.toList());
					lstPolicies.remove(page.getPolicyId());
					accessCreateRequest.setType(CommonCodes.SCOPE);
					accessCreateRequest.setLogic(CommonCodes.POSITIVE);
					accessCreateRequest.setDecisionStrategy(CommonCodes.AFFIRMATIVE);
					accessCreateRequest.setName(lstPermission.get(0).getPermissionName());
					accessCreateRequest.setResources(resourceId);
					accessCreateRequest.setPolicies(lstPolicies);
					accessCreateRequest.getScopes().add(objScope.getId());
					externalApiService.createUpdateAccess(accessCreateRequest, permissionId,
							token);
				} else {
					//add update
					AccessCreateRequest accessCreateRequest = new AccessCreateRequest();
					List<ResourcePermissions> lstPermission = lstResourcePermissions.stream()
							.filter(x -> x.getPermissionName().contains("print")).collect(Collectors.toList());
					String permissionId = lstPermission.get(0).getPermissionId();
					ResourceServerScope objScope = lstScopes.stream().filter(x -> x.getName().contains("print"))
							.findFirst().orElse(null);
					
					List<String> lstPolicies; 
					
					lstPolicies = lstPermissions.stream().filter(x->x.getPermissionId().equals(permissionId)).map(ResourcePermissionsPolicy::getPolicyId).collect(Collectors.toList());
					lstPolicies.add(page.getPolicyId());
					accessCreateRequest.setType(CommonCodes.SCOPE);
					accessCreateRequest.setLogic(CommonCodes.POSITIVE);
					accessCreateRequest.setDecisionStrategy(CommonCodes.AFFIRMATIVE);
					accessCreateRequest.setName(lstPermission.get(0).getPermissionName());
					accessCreateRequest.setResources(resourceId);
					accessCreateRequest.setPolicies(lstPolicies);
					accessCreateRequest.getScopes().add(objScope.getId());
					externalApiService.createUpdateAccess(accessCreateRequest, permissionId,
							token);
				}
			} else if(Boolean.compare(newPages.getAccessDelete(), page.getAccessDelete())!=0){
				ResourcePermissionsPolicy objPermission = lstPermissions.stream()
						.filter(permissions -> permissions.getPermissionName().contains("delete")
								&& permissions.getPolicyName().contains(request.getStakeHolder() + "_Policy"))
						.findFirst().orElse(null);
				List<String> resourceId = new ArrayList<>();
				resourceId.add(page.getPageId());
				// all scopes
				List<ResourceServerScope> lstScopes = resourceScopeRepository
						.allScopes(propertiesConfig.getResourceServerId());
				
				if (objPermission != null) {
					// remove
					AccessCreateRequest accessCreateRequest = new AccessCreateRequest();
					List<ResourcePermissions> lstPermission = lstResourcePermissions.stream()
							.filter(x -> x.getPermissionName().contains("delete")).collect(Collectors.toList());
					String permissionId = lstPermission.get(0).getPermissionId();
					ResourceServerScope objScope = lstScopes.stream().filter(x -> x.getName().contains("delete"))
							.findFirst().orElse(null);
					List<String> lstPolicies; 
					lstPolicies = lstPermissions.stream().filter(x->x.getPermissionId().equals(permissionId)).map(ResourcePermissionsPolicy::getPolicyId).collect(Collectors.toList());
					lstPolicies.remove(page.getPolicyId());
					accessCreateRequest.setType(CommonCodes.SCOPE);
					accessCreateRequest.setLogic(CommonCodes.POSITIVE);
					accessCreateRequest.setDecisionStrategy(CommonCodes.AFFIRMATIVE);
					accessCreateRequest.setName(lstPermission.get(0).getPermissionName());
					accessCreateRequest.setResources(resourceId);
					accessCreateRequest.setPolicies(lstPolicies);
					accessCreateRequest.getScopes().add(objScope.getId());
					externalApiService.createUpdateAccess(accessCreateRequest, permissionId,
							token);
				} else {
					//add update
					AccessCreateRequest accessCreateRequest = new AccessCreateRequest();
					List<ResourcePermissions> lstPermission = lstResourcePermissions.stream()
							.filter(x -> x.getPermissionName().contains("delete")).collect(Collectors.toList());
					String permissionId = lstPermission.get(0).getPermissionId();
					ResourceServerScope objScope = lstScopes.stream().filter(x -> x.getName().contains("delete"))
							.findFirst().orElse(null);
					
					List<String> lstPolicies; 
					
					lstPolicies = lstPermissions.stream().filter(x->x.getPermissionId().equals(permissionId)).map(ResourcePermissionsPolicy::getPolicyId).collect(Collectors.toList());
					lstPolicies.add(page.getPolicyId());
					accessCreateRequest.setType(CommonCodes.SCOPE);
					accessCreateRequest.setLogic(CommonCodes.POSITIVE);
					accessCreateRequest.setDecisionStrategy(CommonCodes.AFFIRMATIVE);
					accessCreateRequest.setName(lstPermission.get(0).getPermissionName());
					accessCreateRequest.setResources(resourceId);
					accessCreateRequest.setPolicies(lstPolicies);
					accessCreateRequest.getScopes().add(objScope.getId());
					externalApiService.createUpdateAccess(accessCreateRequest, permissionId,
							token);
				}
			}else if(Boolean.compare(newPages.getAccessCreate(), page.getAccessCreate())!=0) {
				ResourcePermissionsPolicy objPermission = lstPermissions.stream()
						.filter(permissions -> permissions.getPermissionName().contains("create")
								&& permissions.getPolicyName().contains(request.getStakeHolder() + "_Policy"))
						.findFirst().orElse(null);
				List<String> resourceId = new ArrayList<>();
				resourceId.add(page.getPageId());
				// all scopes
				List<ResourceServerScope> lstScopes = resourceScopeRepository
						.allScopes(propertiesConfig.getResourceServerId());
	
				if (objPermission != null) {
					// remove
					AccessCreateRequest accessCreateRequest = new AccessCreateRequest();
					List<ResourcePermissions> lstPermission = lstResourcePermissions.stream()
							.filter(x -> x.getPermissionName().contains("create")).collect(Collectors.toList());
					String permissionId = lstPermission.get(0).getPermissionId();
					ResourceServerScope objScope = lstScopes.stream().filter(x -> x.getName().contains("create"))
							.findFirst().orElse(null);
					List<String> lstPolicies; 
					lstPolicies = lstPermissions.stream().filter(x->x.getPermissionId().equals(permissionId)).map(ResourcePermissionsPolicy::getPolicyId).collect(Collectors.toList());
					lstPolicies.remove(page.getPolicyId());
					accessCreateRequest.setType(CommonCodes.SCOPE);
					accessCreateRequest.setLogic(CommonCodes.POSITIVE);
					accessCreateRequest.setDecisionStrategy(CommonCodes.AFFIRMATIVE);
					accessCreateRequest.setName(lstPermission.get(0).getPermissionName());
					accessCreateRequest.setResources(resourceId);
					accessCreateRequest.setPolicies(lstPolicies);
					accessCreateRequest.getScopes().add(objScope.getId());
					externalApiService.createUpdateAccess(accessCreateRequest, permissionId,
							token);
				} else {
					//add update
					AccessCreateRequest accessCreateRequest = new AccessCreateRequest();
					List<ResourcePermissions> lstPermission = lstResourcePermissions.stream()
							.filter(x -> x.getPermissionName().contains("create")).collect(Collectors.toList());
					String permissionId = lstPermission.get(0).getPermissionId();
					ResourceServerScope objScope = lstScopes.stream().filter(x -> x.getName().contains("create"))
							.findFirst().orElse(null);
					
					List<String> lstPolicies; 
					
					lstPolicies = lstPermissions.stream().filter(x->x.getPermissionId().equals(permissionId)).map(ResourcePermissionsPolicy::getPolicyId).collect(Collectors.toList());
					lstPolicies.add(page.getPolicyId());
					accessCreateRequest.setType(CommonCodes.SCOPE);
					accessCreateRequest.setLogic(CommonCodes.POSITIVE);
					accessCreateRequest.setDecisionStrategy(CommonCodes.AFFIRMATIVE);
					accessCreateRequest.setName(lstPermission.get(0).getPermissionName());
					accessCreateRequest.setResources(resourceId);
					accessCreateRequest.setPolicies(lstPolicies);
					accessCreateRequest.getScopes().add(objScope.getId());
					externalApiService.createUpdateAccess(accessCreateRequest, permissionId,
							token);
				}
			}else if(Boolean.compare(newPages.getAccessEdit(), page.getAccessEdit())!=0) {
				ResourcePermissionsPolicy objPermission = lstPermissions.stream()
						.filter(permissions -> permissions.getPermissionName().contains("edit")
								&& permissions.getPolicyName().contains(request.getStakeHolder() + "_Policy"))
						.findFirst().orElse(null);
				List<String> resourceId = new ArrayList<>();
				resourceId.add(page.getPageId());
				// all scopes
				List<ResourceServerScope> lstScopes = resourceScopeRepository
						.allScopes(propertiesConfig.getResourceServerId());
				
				if (objPermission != null) {
					// remove
					AccessCreateRequest accessCreateRequest = new AccessCreateRequest();
					List<ResourcePermissions> lstPermission = lstResourcePermissions.stream()
							.filter(x -> x.getPermissionName().contains("edit")).collect(Collectors.toList());
					String permissionId = lstPermission.get(0).getPermissionId();
					ResourceServerScope objScope = lstScopes.stream().filter(x -> x.getName().contains("edit"))
							.findFirst().orElse(null);
					List<String> lstPolicies; 
					lstPolicies = lstPermissions.stream().filter(x->x.getPermissionId().equals(permissionId)).map(ResourcePermissionsPolicy::getPolicyId).collect(Collectors.toList());
					lstPolicies.remove(page.getPolicyId());
					accessCreateRequest.setType(CommonCodes.SCOPE);
					accessCreateRequest.setLogic(CommonCodes.POSITIVE);
					accessCreateRequest.setDecisionStrategy(CommonCodes.AFFIRMATIVE);
					accessCreateRequest.setName(lstPermission.get(0).getPermissionName());
					accessCreateRequest.setResources(resourceId);
					accessCreateRequest.setPolicies(lstPolicies);
					accessCreateRequest.getScopes().add(objScope.getId());
					externalApiService.createUpdateAccess(accessCreateRequest, permissionId,
							token);
				} else {
					//add update
					AccessCreateRequest accessCreateRequest = new AccessCreateRequest();
					List<ResourcePermissions> lstPermission = lstResourcePermissions.stream()
							.filter(x -> x.getPermissionName().contains("edit")).collect(Collectors.toList());
					String permissionId = lstPermission.get(0).getPermissionId();
					ResourceServerScope objScope = lstScopes.stream().filter(x -> x.getName().contains("edit"))
							.findFirst().orElse(null);
					
					List<String> lstPolicies; 
					
					lstPolicies = lstPermissions.stream().filter(x->x.getPermissionId().equals(permissionId)).map(ResourcePermissionsPolicy::getPolicyId).collect(Collectors.toList());
					lstPolicies.add(page.getPolicyId());
					accessCreateRequest.setType(CommonCodes.SCOPE);
					accessCreateRequest.setLogic(CommonCodes.POSITIVE);
					accessCreateRequest.setDecisionStrategy(CommonCodes.AFFIRMATIVE);
					accessCreateRequest.setName(lstPermission.get(0).getPermissionName());
					accessCreateRequest.setResources(resourceId);
					accessCreateRequest.setPolicies(lstPolicies);
					accessCreateRequest.getScopes().add(objScope.getId());
					externalApiService.createUpdateAccess(accessCreateRequest, permissionId,
							token);
				}
			}else if(Boolean.compare(newPages.getAccessView(), page.getAccessView())!=0) {
				ResourcePermissionsPolicy objPermission = lstPermissions.stream()
						.filter(permissions -> permissions.getPermissionName().contains("view")
								&& permissions.getPolicyName().contains(request.getStakeHolder() + "_Policy"))
						.findFirst().orElse(null);
				List<String> resourceId = new ArrayList<>();
				resourceId.add(page.getPageId());
				// all scopes
				List<ResourceServerScope> lstScopes = resourceScopeRepository
						.allScopes(propertiesConfig.getResourceServerId());
				
				if (objPermission != null) {
					// remove
					AccessCreateRequest accessCreateRequest = new AccessCreateRequest();
					List<ResourcePermissions> lstPermission = lstResourcePermissions.stream()
							.filter(x -> x.getPermissionName().contains("view")).collect(Collectors.toList());
					String permissionId = lstPermission.get(0).getPermissionId();
					ResourceServerScope objScope = lstScopes.stream().filter(x -> x.getName().contains("view"))
							.findFirst().orElse(null);
					List<String> lstPolicies; 
					lstPolicies = lstPermissions.stream().filter(x->x.getPermissionId().equals(permissionId)).map(ResourcePermissionsPolicy::getPolicyId).collect(Collectors.toList());
					lstPolicies.remove(page.getPolicyId());
					accessCreateRequest.setType(CommonCodes.SCOPE);
					accessCreateRequest.setLogic(CommonCodes.POSITIVE);
					accessCreateRequest.setDecisionStrategy(CommonCodes.AFFIRMATIVE);
					accessCreateRequest.setName(lstPermission.get(0).getPermissionName());
					accessCreateRequest.setResources(resourceId);
					accessCreateRequest.setPolicies(lstPolicies);
					accessCreateRequest.getScopes().add(objScope.getId());
					externalApiService.createUpdateAccess(accessCreateRequest, permissionId,
							token);
				} else {
					//add update
					AccessCreateRequest accessCreateRequest = new AccessCreateRequest();
					List<ResourcePermissions> lstPermission = lstResourcePermissions.stream()
							.filter(x -> x.getPermissionName().contains("view")).collect(Collectors.toList());
					String permissionId = lstPermission.get(0).getPermissionId();
					ResourceServerScope objScope = lstScopes.stream().filter(x -> x.getName().contains("view"))
							.findFirst().orElse(null);
					
					List<String> lstPolicies; 
					
					lstPolicies = lstPermissions.stream().filter(x->x.getPermissionId().equals(permissionId)).map(ResourcePermissionsPolicy::getPolicyId).collect(Collectors.toList());
					lstPolicies.add(page.getPolicyId());
					accessCreateRequest.setType(CommonCodes.SCOPE);
					accessCreateRequest.setLogic(CommonCodes.POSITIVE);
					accessCreateRequest.setDecisionStrategy(CommonCodes.AFFIRMATIVE);
					accessCreateRequest.setName(lstPermission.get(0).getPermissionName());
					accessCreateRequest.setResources(resourceId);
					accessCreateRequest.setPolicies(lstPolicies);
					accessCreateRequest.getScopes().add(objScope.getId());
					externalApiService.createUpdateAccess(accessCreateRequest, permissionId,
							token);
				}
			}else if(Boolean.compare(newPages.getAccessApproveReject(), page.getAccessApproveReject())!=0) {
				ResourcePermissionsPolicy objPermission = lstPermissions.stream()
						.filter(permissions -> permissions.getPermissionName().contains("approv")
								&& permissions.getPolicyName().contains(request.getStakeHolder() + "_Policy"))
						.findFirst().orElse(null);
				List<String> resourceId = new ArrayList<>();
				resourceId.add(page.getPageId());
				// all scopes
				List<ResourceServerScope> lstScopes = resourceScopeRepository
						.allScopes(propertiesConfig.getResourceServerId());

				if (objPermission != null) {
					// remove
					AccessCreateRequest accessCreateRequest = new AccessCreateRequest();
					List<ResourcePermissions> lstPermission = lstResourcePermissions.stream()
							.filter(x -> x.getPermissionName().contains("approv")).collect(Collectors.toList());
					String permissionId = lstPermission.get(0).getPermissionId();
					ResourceServerScope objScope = lstScopes.stream().filter(x -> x.getName().contains("approv"))
							.findFirst().orElse(null);
					List<String> lstPolicies; 
					lstPolicies = lstPermissions.stream().filter(x->x.getPermissionId().equals(permissionId)).map(ResourcePermissionsPolicy::getPolicyId).collect(Collectors.toList());
					lstPolicies.remove(page.getPolicyId());
					accessCreateRequest.setType(CommonCodes.SCOPE);
					accessCreateRequest.setLogic(CommonCodes.POSITIVE);
					accessCreateRequest.setDecisionStrategy(CommonCodes.AFFIRMATIVE);
					accessCreateRequest.setName(lstPermission.get(0).getPermissionName());
					accessCreateRequest.setResources(resourceId);
					accessCreateRequest.setPolicies(lstPolicies);
					accessCreateRequest.getScopes().add(objScope.getId());
					externalApiService.createUpdateAccess(accessCreateRequest, permissionId,
							token);
				} else {
					//add update
					AccessCreateRequest accessCreateRequest = new AccessCreateRequest();
					List<ResourcePermissions> lstPermission = lstResourcePermissions.stream()
							.filter(x -> x.getPermissionName().contains("approv")).collect(Collectors.toList());
					String permissionId = lstPermission.get(0).getPermissionId();
					ResourceServerScope objScope = lstScopes.stream().filter(x -> x.getName().contains("approv"))
							.findFirst().orElse(null);
					
					List<String> lstPolicies; 
					
					lstPolicies = lstPermissions.stream().filter(x->x.getPermissionId().equals(permissionId)).map(ResourcePermissionsPolicy::getPolicyId).collect(Collectors.toList());
					lstPolicies.add(page.getPolicyId());
					accessCreateRequest.setType(CommonCodes.SCOPE);
					accessCreateRequest.setLogic(CommonCodes.POSITIVE);
					accessCreateRequest.setDecisionStrategy(CommonCodes.AFFIRMATIVE);
					accessCreateRequest.setName(lstPermission.get(0).getPermissionName());
					accessCreateRequest.setResources(resourceId);
					accessCreateRequest.setPolicies(lstPolicies);
					accessCreateRequest.getScopes().add(objScope.getId());
					externalApiService.createUpdateAccess(accessCreateRequest, permissionId,
							token);
				}
			}
			
		}

		return "Access Updated";
	}
}


