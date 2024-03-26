/**
 * 
 */
package sa.tabadul.useraccessmanagement.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.common.models.request.UserBranchRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserOrganizationRequest;
import sa.tabadul.useraccessmanagement.common.models.request.UserRequest;
import sa.tabadul.useraccessmanagement.common.models.response.ResponseEnvelope;
import sa.tabadul.useraccessmanagement.constant.PortsType;
import sa.tabadul.useraccessmanagement.domain.CodeMaster;
import sa.tabadul.useraccessmanagement.domain.PortCode;
import sa.tabadul.useraccessmanagement.domain.RoleMaster;
import sa.tabadul.useraccessmanagement.domain.UserBranch;
import sa.tabadul.useraccessmanagement.domain.UserEntity;
import sa.tabadul.useraccessmanagement.domain.UserOrganization;
import sa.tabadul.useraccessmanagement.domain.UserRole;
import sa.tabadul.useraccessmanagement.domain.Users;
import sa.tabadul.useraccessmanagement.repository.CodeMasterRepository;
import sa.tabadul.useraccessmanagement.repository.PortCodeRepo;
import sa.tabadul.useraccessmanagement.repository.RoleMasterRepository;
import sa.tabadul.useraccessmanagement.repository.UserBranchRepository;
import sa.tabadul.useraccessmanagement.repository.UserEntityRepository;
import sa.tabadul.useraccessmanagement.repository.UserOrganizationRepository;
import sa.tabadul.useraccessmanagement.repository.UserRepository;
import sa.tabadul.useraccessmanagement.repository.UserRoleRepository;

/**
 * @author amalkawi
 *
 */
@Service
public class FasahService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserEntityRepository userEntityRepository;

	@Autowired
	private UserBranchRepository userBranchRepository;

	@Autowired
	private UserOrganizationRepository userOrganizationRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private RoleMasterRepository roleMasterRepository;

	@Autowired
	private CodeMasterRepository codeMasterRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private PortCodeRepo portCodeRepo;

	@Value("${do.not.migrate.Roles.if.exist}")
	boolean doNotMigrateRolesIfExist;

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
			CodeMaster codeMaster = codeMasterRepository.findByKey1AndCode("DOCUMENT TYPE", "CRNUM");
			CodeMaster group = codeMasterRepository.findByKey1AndCode("DEFAULT_GROUP", "GROUP1");
			CodeMaster department = codeMasterRepository.findByKey1AndCode("DEPARTMENT", "OPERATIONS");
			CodeMaster designation = codeMasterRepository.findByKey1AndCode("DESIGNATION", "PROJECT MANAGER");
			
			objOrganization.setOrgID(organizationRequest.getOrgId());
			objOrganization.setOrgName(organizationRequest.getOrgName());
			objOrganization.setOrgNameArabic(organizationRequest.getArabicOrgName());
			objOrganization.setEmailId(objUserEntity.getEmail());
			if (!doNotMigrateRolesIfExist) {
				objOrganization.setStakeholdertypeRid(lstRoleMaster.get(0).getId());
			} else {
				if (isNewOrg) {
					objOrganization.setStakeholdertypeRid(lstRoleMaster.get(0).getId());
				}
			}
			
			if(objOrganization.getCrn()==null) {
				LocalDateTime myDateObj = LocalDateTime.now();
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(CommonCodes.CRN_FORMAT);
				objOrganization.setCrn(Long.parseLong(myFormatObj.format(myDateObj)));
			}


			objOrganization.setMobileNumber(Long.parseLong(CommonCodes.PHN));
			objOrganization.setRegistrationThroughRid(2573);
			objOrganization.setDocumentTypeRid(codeMaster.getId());
			objOrganization.setRegistrationTypeRid(2576);
			objOrganization.setDocumentNo(organizationRequest.getCrn());
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
			objUsers.setDepartmentRID(department.getId());
			objUsers.setDesignationRID(designation.getId());
			objUsers.setDefaultGroupTypeRID(group.getId());
			objUsers.setIamUserID(objUserEntity.getId());
			objUsers.setEmail(objUserEntity.getEmail());
			objUsers.setJoiningDate(LocalDate.now());
			objUsers.setCreatedBy(CommonCodes.TABADUL);
			objUsers.setPhoneNo(userRequest.getUserMobile());
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
}
