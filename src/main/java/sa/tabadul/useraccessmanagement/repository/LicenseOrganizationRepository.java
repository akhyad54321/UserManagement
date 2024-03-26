package sa.tabadul.useraccessmanagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sa.tabadul.useraccessmanagement.domain.LicenseOrganization;

public interface LicenseOrganizationRepository extends JpaRepository<LicenseOrganization, Integer> {

	LicenseOrganization findByCrn(Long crn);

	List<LicenseOrganization> findByLicenseNumberLike(String data);

	List<LicenseOrganization> findAllByUserOrgIdIn(List<Integer> userOrgId);

	Long countByUserOrgId(Integer userOrgId);

	Long countByIsActive(Boolean isActive);

	List<LicenseOrganization> findByApprovalStatusRidIn(List<Integer> approvalStatusRid);

	List<LicenseOrganization> findByIsActive(Boolean isActive);

	Long countByApprovalStatusRidInAndRequestTypeRidInAndIsActive(List<Integer> approvalStatusRid,
			List<Integer> requestTypeRid, Boolean isActive);

	@Query("SELECT e FROM LicenseOrganization e JOIN UserOrganization uo ON e.userOrgId=uo.id WHERE "
			+ "(LOWER(CONCAT('', e.crn)) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(uo.orgName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(CONCAT('', e.eunn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(uo.stakholderCategory) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.requestType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.requestTypeRid) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.licenseValidForDays) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			"LOWER(e.licenseExpiryDate) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(e.licenseNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
			// "LOWER(e.licenseStatus) LIKE LOWER(CONCAT('%', :search, '%'))) OR "+
			"((:fromDate IS NULL OR e.createdDate >= :fromDate) " + "AND (:toDate IS NULL OR e.createdDate <= :toDate) "
			+ "AND (:stakeHolderTypeRid IS NULL OR uo.stakeholdertypeRid = :stakeHolderTypeRid) "
			+ "AND (:approvalStatusRid IS NULL OR e.approvalStatusRid = :approvalStatusRid) "
			+ "AND (:licenseStatusRid IS NULL OR e.licenseStatusRid = :licenseStatusRid) "
			+ "AND (:requestTypeRid IS NULL OR e.requestTypeRid = :requestTypeRid)) AND (e.userOrgId=:userOrgId)")
	List<LicenseOrganization> userList(@Param("search") String search, @Param("fromDate") LocalDateTime fromDate,
			@Param("toDate") LocalDateTime toDate, @Param("stakeHolderTypeRid") Integer stakeHolderTypeRid,
			@Param("approvalStatusRid") Integer approvalStatusRid, @Param("licenseStatusRid") Integer licenseStatusRid,
			@Param("requestTypeRid") Integer requestTypeRid, @Param("userOrgId") Integer userOrgId,
			@Param("pageRequest") PageRequest pageRequest);

	@Query("SELECT COUNT(e) FROM LicenseOrganization e JOIN UserOrganization uo ON e.userOrgId=uo.id WHERE "
			+ "(LOWER(CONCAT('', e.crn)) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(uo.orgName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(CONCAT('', e.eunn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(uo.stakholderCategory) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.requestType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.requestTypeRid) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.licenseValidForDays) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			"LOWER(e.licenseExpiryDate) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(e.licenseNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
			// "LOWER(e.licenseStatus) LIKE LOWER(CONCAT('%', :search, '%'))) OR "+
			"((:fromDate IS NULL OR e.createdDate >= :fromDate) " + "AND (:toDate IS NULL OR e.createdDate <= :toDate) "
			+ "AND (:stakeHolderTypeRid IS NULL OR uo.stakeholdertypeRid = :stakeHolderTypeRid) "
			+ "AND (:approvalStatusRid IS NULL OR e.approvalStatusRid = :approvalStatusRid) "
			+ "AND (:licenseStatusRid IS NULL OR e.licenseStatusRid = :licenseStatusRid) "
			+ "AND (:requestTypeRid IS NULL OR e.requestTypeRid = :requestTypeRid)) AND (e.userOrgId=:userOrgId)")
	Long userList(@Param("search") String search, @Param("fromDate") LocalDateTime fromDate,
			@Param("toDate") LocalDateTime toDate, @Param("stakeHolderTypeRid") Integer stakeHolderTypeRid,
			@Param("approvalStatusRid") Integer approvalStatusRid, @Param("licenseStatusRid") Integer licenseStatusRid,
			@Param("requestTypeRid") Integer requestTypeRid, @Param("userOrgId") Integer userOrgId);

	@Query("SELECT e FROM LicenseOrganization e JOIN UserOrganization uo ON e.userOrgId=uo.id WHERE "
			+ "(LOWER(CONCAT('', e.crn)) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(uo.orgName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(CONCAT('', e.eunn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(uo.stakholderCategory) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.requestType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.requestTypeRid) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.licenseValidForDays) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			"LOWER(e.licenseExpiryDate) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(e.licenseNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
			// "LOWER(e.licenseStatus) LIKE LOWER(CONCAT('%', :search, '%'))) OR "+
			"((:fromDate IS NULL OR e.createdDate >= :fromDate) " + "AND (:toDate IS NULL OR e.createdDate <= :toDate) "
			+ "AND (:stakeHolderTypeRid IS NULL OR uo.stakeholdertypeRid = :stakeHolderTypeRid) "
			+ "AND (:approvalStatusRid IS NULL OR e.approvalStatusRid = :approvalStatusRid) "
			+ "AND (:licenseStatusRid IS NULL OR e.licenseStatusRid = :licenseStatusRid) "
			+ "AND (:requestTypeRid IS NULL OR e.requestTypeRid = :requestTypeRid) AND (e.isActive=1))")
	List<LicenseOrganization> licenseOfficerList(@Param("search") String search,
			@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate,
			@Param("stakeHolderTypeRid") Integer stakeHolderTypeRid,
			@Param("approvalStatusRid") Integer approvalStatusRid, @Param("licenseStatusRid") Integer licenseStatusRid,
			@Param("requestTypeRid") Integer requestTypeRid, @Param("pageRequest") PageRequest pageRequest);

	@Query("SELECT COUNT(e) FROM LicenseOrganization e JOIN UserOrganization uo ON e.userOrgId=uo.id WHERE "
			+ "(LOWER(CONCAT('', e.crn)) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(uo.orgName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(CONCAT('', e.eunn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(uo.stakholderCategory) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.requestType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.requestTypeRid) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.licenseValidForDays) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			"LOWER(e.licenseExpiryDate) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(e.licenseNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
			// "LOWER(e.licenseStatus) LIKE LOWER(CONCAT('%', :search, '%'))) OR "+
			"((:fromDate IS NULL OR e.createdDate >= :fromDate) " + "AND (:toDate IS NULL OR e.createdDate <= :toDate) "
			+ "AND (:stakeHolderTypeRid IS NULL OR uo.stakeholdertypeRid = :stakeHolderTypeRid) "
			+ "AND (:approvalStatusRid IS NULL OR e.approvalStatusRid = :approvalStatusRid) "
			+ "AND (:licenseStatusRid IS NULL OR e.licenseStatusRid = :licenseStatusRid) "
			+ "AND (:requestTypeRid IS NULL OR e.requestTypeRid = :requestTypeRid) AND (e.isActive=1))")
	Long licenseOfficerList(@Param("search") String search, @Param("fromDate") LocalDateTime fromDate,
			@Param("toDate") LocalDateTime toDate, @Param("stakeHolderTypeRid") Integer stakeHolderTypeRid,
			@Param("approvalStatusRid") Integer approvalStatusRid, @Param("licenseStatusRid") Integer licenseStatusRid,
			@Param("requestTypeRid") Integer requestTypeRid);

	@Query("SELECT e FROM LicenseOrganization e JOIN UserOrganization uo ON e.userOrgId=uo.id WHERE "
			+ "(LOWER(CONCAT('', e.crn)) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(uo.orgName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(CONCAT('', e.eunn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(uo.stakholderCategory) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.requestType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.requestTypeRid) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.licenseValidForDays) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			"LOWER(e.licenseExpiryDate) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(e.licenseNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
			// "LOWER(e.licenseStatus) LIKE LOWER(CONCAT('%', :search, '%'))) OR "+
			"((:fromDate IS NULL OR e.createdDate >= :fromDate) " + "AND (:toDate IS NULL OR e.createdDate <= :toDate) "
			+ "AND (:stakeHolderTypeRid IS NULL OR uo.stakeholdertypeRid = :stakeHolderTypeRid) "
			+ "AND (:approvalStatusRid IS NULL OR e.approvalStatusRid = :approvalStatusRid) "
			+ "AND (:licenseStatusRid IS NULL OR e.licenseStatusRid = :licenseStatusRid) "
			+ "AND (:requestTypeRid IS NULL OR e.requestTypeRid = :requestTypeRid)) AND (e.approvalStatusRid IN :statuslist AND e.requestTypeRid IN :requestType AND e.isActive = 1)")
	List<LicenseOrganization> licenseManagerList(@Param("search") String search,
			@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate,
			@Param("stakeHolderTypeRid") Integer stakeHolderTypeRid,
			@Param("approvalStatusRid") Integer approvalStatusRid, @Param("licenseStatusRid") Integer licenseStatusRid,
			@Param("requestTypeRid") Integer requestTypeRid, @Param("statuslist") List<Integer> statuslist,
			@Param("requestType") List<Integer> requestType, @Param("pageRequest") PageRequest pageRequest);

	@Query("SELECT COUNT(e) FROM LicenseOrganization e JOIN UserOrganization uo ON e.userOrgId=uo.id WHERE "
			+ "(LOWER(CONCAT('', e.crn)) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(uo.orgName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(CONCAT('', e.eunn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(uo.stakholderCategory) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.requestType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.requestTypeRid) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			// "LOWER(e.licenseValidForDays) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
			"LOWER(e.licenseExpiryDate) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR "
			+ "LOWER(e.licenseNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
			// "LOWER(e.licenseStatus) LIKE LOWER(CONCAT('%', :search, '%'))) OR "+
			"((:fromDate IS NULL OR e.createdDate >= :fromDate) " + "AND (:toDate IS NULL OR e.createdDate <= :toDate) "
			+ "AND (:stakeHolderTypeRid IS NULL OR uo.stakeholdertypeRid = :stakeHolderTypeRid) "
			+ "AND (:approvalStatusRid IS NULL OR e.approvalStatusRid = :approvalStatusRid) "
			+ "AND (:licenseStatusRid IS NULL OR e.licenseStatusRid = :licenseStatusRid) "
			+ "AND (:requestTypeRid IS NULL OR e.requestTypeRid = :requestTypeRid) AND (e.approvalStatusRid IN :statuslist AND e.requestTypeRid IN :requestType AND e.isActive = 1))")
	Long licenseManagerList(@Param("search") String search, @Param("fromDate") LocalDateTime fromDate,
			@Param("toDate") LocalDateTime toDate, @Param("stakeHolderTypeRid") Integer stakeHolderTypeRid,
			@Param("approvalStatusRid") Integer approvalStatusRid, @Param("licenseStatusRid") Integer licenseStatusRid,
			@Param("requestTypeRid") Integer requestTypeRid, @Param("statuslist") List<Integer> statuslist,
			@Param("requestType") List<Integer> requestType);
	
	
	List<LicenseOrganization> findByUserOrgId(Integer userOrgId);
	
	
	List<LicenseOrganization> findByUserOrg_OrgIDAndLicenseStatusRid(@Param("orgID") String orgID,@Param("licenseStatusRid") Integer licenseStatusRid);

	
	List<LicenseOrganization> findByUserOrg_OrgIDAndIsActive(@Param("orgID") String orgID,@Param("isActive") Boolean isActive);
	
	List<LicenseOrganization> findByUserOrgIdAndLicenseStatusRid(Integer userOrgId, Integer licenseStatusRid);

}
