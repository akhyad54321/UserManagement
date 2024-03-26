package sa.tabadul.useraccessmanagement.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.common.models.response.PortPermitList;
import sa.tabadul.useraccessmanagement.domain.PortPermit;
import sa.tabadul.useraccessmanagement.domain.PortPermitApplication;

@Repository
public interface PortPermitApplicationRepository extends JpaRepository<PortPermitApplication, Integer> {

	PortPermitApplication findByCrn(@Param("crn") Long crn);

	List<PortPermitApplication> findByUserOrgIdAndApprovalStatusRidIsIn(Integer userOrgId,
			List<Integer> approvalStatusRID);

	List<PortPermitApplication> findByPortRid(Integer portRid);

	List<PortPermitApplication> findByUserOrgId(Integer userOrgId);

	List<PortPermitApplication> findByApprovalStatusRid(@Param("approvalStatusRid") Integer approvalStatusRid);

	List<PortPermitApplication> findByIsActive(Boolean isActive);

	List<PortPermitApplication> findByUserOrgIdAndRequestStatusRid(Integer userOrgId, Integer requestStatusRid);

//	@Query("SELECT new sa.tabadul.useraccessmanagement.common.models.PortPermitListResponse("
//			+ "p.crn,"
//			+ "FROM PortPermitApplication p "
//			+ "JOIN LicenseApplication li ON li.userOrgId=l.id "
//			+ "JOIN UserOrganization uo ON uo.userOrgId=uo.id " +			
//			"WHERE (LOWER(CONCAT('', p.crn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//		       "       LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//		       "       LOWER(CONCAT(li.licenseType)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//		       "       LOWER(li.licenseExpiryDate) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//		       "       LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//		       "       LOWER(li.LicenceInitiationDate) LIKE LOWER(CONCAT('%', :search, '%'))) " +
//		       "      AND ((:fromDate IS NULL OR e.createdDate >= :fromDate) " +
//		       "      AND (:toDate IS NULL OR e.createdDate <= :toDate) " +
//		       "      AND (:licenseType IS NULL OR uo.licenseType = :licenseType) " +
//		       "      AND (:status IS NULL OR e.approvalStatusRid = :status) AND (e.isActive = 1))")
//	List<PortPermitApplication> portPermissionOfficerList(
//	        @Param("search") String search,
//	        @Param("fromDate") LocalDateTime fromDate,
//	        @Param("toDate") LocalDateTime toDate,
//	        @Param("licenseType") String licenseType,
//	        @Param("status") String status,
//	        @Param("pageRequest") PageRequest pageRequest);

	@Query("SELECT new sa.tabadul.useraccessmanagement.common.models.response.PortPermitList(p.id,p.crn,u.orgName,p.portRid,"
			+ "p.createdDate, u.userId,p.approvalStatusRid,l.licenseNumber,l.createdDate,l.licenseExpiryDate,"
			+ "p.eunn,p.requestTypeRid,p.updatedDate) " + "FROM PortPermitApplication p "
			+ "JOIN UserOrganization u ON p.userOrgId=u.id " + "JOIN LicenseApplication l ON u.id=l.userOrgId WHERE"
			+ " p.portRid = :portRid")
	List<PortPermitList> userPortPermit(@Param("portRid") Integer portRid,
			@Param("pageRequest") PageRequest pageRequest);

//	@Query("SELECT new sa.tabadul.useraccessmanagement.common.models.response.PortPermitList(p.id,p.crn,u.orgName,p.portRid,"
//			+ "p.createdDate, u.userId,p.approvalStatusRid,l.licenseNumber,l.createdDate,l.licenseExpiryDate,"
//			+ "p.eunn,p.requestTypeRid,p.updatedDate) "
//			+ "FROM PortPermitApplication p "
//			+ "JOIN UserOrganization u ON p.userOrgId=u.id "
//			+ "JOIN LicenseApplication l ON u.id=l.userOrgId WHERE"+
//			" p.portRid = :portRid")
//	Long userPortPermit(@Param("portRid") Integer portRid);

	@Query("SELECT COUNT(p)" + "FROM PortPermitApplication p " + "JOIN UserOrganization u ON p.userOrgId=u.id "
			+ "JOIN LicenseApplication l ON u.id=l.userOrgId WHERE" + " p.portRid = :portRid")
	long countUserPortPermit(@Param("portRid") Integer portRid);

	@Query("SELECT new sa.tabadul.useraccessmanagement.common.models.response.PortPermitList(p.id,p.crn,u.orgName,p.portRid,"
			+ "p.createdDate, u.userId,p.approvalStatusRid,l.licenseNumber,l.createdDate,l.licenseExpiryDate,"
			+ "p.eunn,p.requestTypeRid,p.updatedDate) " + "FROM PortPermitApplication p "
			+ "JOIN UserOrganization u ON p.userOrgId=u.id " + "JOIN LicenseApplication l ON u.id=l.userOrgId WHERE "
			+ " p.isActive = 1")
	List<PortPermitList> officerPortPermit(@Param("pageRequest") PageRequest pageRequest);

	@Query("SELECT COUNT(p) " + "FROM PortPermitApplication p " + "JOIN UserOrganization u ON p.userOrgId = u.id "
			+ "JOIN LicenseApplication l ON u.id = l.userOrgId " + "WHERE p.isActive = 1")
	long countOfficerPortPermit();

	@Query("SELECT new sa.tabadul.useraccessmanagement.common.models.response.PortPermitList(p.id,p.crn,u.orgName,p.portRid,"
			+ "p.createdDate, u.userId,p.approvalStatusRid,l.licenseNumber,l.createdDate,l.licenseExpiryDate,"
			+ "p.eunn,p.requestTypeRid,p.updatedDate) " + "FROM PortPermitApplication p "
			+ "JOIN UserOrganization u ON p.userOrgId=u.id " + "JOIN LicenseApplication l ON u.id=l.userOrgId WHERE "
			+ " p.approvalStatusRid IN (:statusRid) AND p.requestTypeRid IN (:requestType) AND  p.isActive = 1")
	List<PortPermitList> managerPortPermit(@Param("requestType") List<Integer> requestType,
			@Param("statusRid") List<Integer> statusRid, @Param("pageRequest") PageRequest pageRequest);
	
	List<PortPermitApplication> findByRequestTypeRid(Integer requestTypeRid);

//	@Query("SELECT COUNT(p) " +
//		       "FROM PortPermitApplication p " +
//		       "JOIN UserOrganization u ON p.userOrgId = u.id " +
//		       "JOIN LicenseApplication l ON u.id = l.userOrgId " +
//		       "WHERE l.requestType IN :requestType AND p.statusRid IN :statusRid")
//		long countManagerPortPermit(@Param("requestType") List<Integer> parsedRequestTypeRids, @Param("statusRid") List<Integer> parsedStatusTypeRids);

//	@Query("SELECT e FROM PortPermitApplication e " +
//		       "JOIN UserOrganization uo ON e.userOrgId = uo.id " +
//		       "JOIN LicenseApplication li ON li.userOrgId = uo.id " +
//		       "WHERE (LOWER(CONCAT('', e.crn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//		       "       LOWER(uo.orgName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//		       "       LOWER(CONCAT('', e.eunn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//		       "       LOWER(li.licenseExpiryDate) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//		       "       LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//		       "       LOWER(li.licenseNumber) LIKE LOWER(CONCAT('%', :search, '%'))) " +
//		       "      AND ((:fromDate IS NULL OR e.createdDate >= :fromDate) " +
//		       "      AND (:toDate IS NULL OR e.createdDate <= :toDate) " +
//		       "      AND (:licenseType IS NULL OR uo.licenseType = :licenseType) " +
//		       "      AND (:status IS NULL OR e.approvalStatusRid = :status) AND (e.isActive = 1))")
//		List<PortPermitApplication> portPermissionOfficerList(
//		        @Param("search") String search,
//		        @Param("fromDate") LocalDateTime fromDate,
//		        @Param("toDate") LocalDateTime toDate,
//		        @Param("licenseType") String licenseType,
//		        @Param("status") String status,
//		        @Param("pageRequest") PageRequest pageRequest);
//
//	
//
//			@Query("SELECT COUNT(e) FROM PortPermitApplication e " +
//			"JOIN UserOrganization uo ON e.userOrgId = uo.id " +
//			"JOIN LicenseApplication li ON li.userOrgId = uo.id " +
//			"WHERE (LOWER(CONCAT('', e.crn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//			 "LOWER(uo.orgName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//			 "LOWER(CONCAT('', e.eunn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +			
//			"LOWER(li.licenseExpiryDate) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//			 "LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//			 "LOWER(li.licenseNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +			
//			"((:fromDate IS NULL OR e.createdDate >= :fromDate) " + 
//			 "AND (:toDate IS NULL OR e.createdDate <= :toDate) " +		
//			 " AND (:licenseType IS NULL OR uo.licenseType = :licenseType) " +
//			 " AND (:status IS NULL OR e.approvalStatusRid = :status) AND (e.isActive = 1))")
//			Long portPermissionOfficerList(
//					@Param("search") String search,
//			        @Param("fromDate") LocalDateTime fromDate,
//			        @Param("toDate") LocalDateTime toDate,
//			        @Param("licenseType") String licenseType,
//			        @Param("status") String status);
//			
//			
//
//			@Query("SELECT e FROM PortPermitApplication e " +
//			"JOIN UserOrganization uo ON e.userOrgId = uo.id " +
//			"JOIN LicenseApplication li ON li.userOrgId = uo.id " +
//			"WHERE (LOWER(CONCAT('', e.crn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//			 "LOWER(uo.orgName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//			 "LOWER(CONCAT('', e.eunn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +			
//			"LOWER(li.licenseExpiryDate) LIKE LOWER(CONCAT('%', :search, '%')) OR "
//			+ "LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR "
//			+ "LOWER(li.licenseNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
//			"((:fromDate IS NULL OR e.createdDate >= :fromDate) " + "AND (:toDate IS NULL OR e.createdDate <= :toDate) "
//			+ "AND (:licenseType IS NULL OR uo.licenseType = :licenseType) " +
//			" AND (:status IS NULL OR e.approvalStatusRid = :status) AND (e.isActive = 1))")	
//			List<PortPermitApplication> portPermitManagerList(
//					 @Param("search") String search,
//				     @Param("fromDate") LocalDateTime fromDate,
//				     @Param("toDate") LocalDateTime toDate,
//				     @Param("licenseType") String licenseType,
//				     @Param("status") String status,
//				     @Param("pageRequest") PageRequest pageRequest);
//			
//
//			@Query("SELECT COUNT(e) FROM PortPermitApplication e " +
//					"JOIN UserOrganization uo ON e.userOrgId = uo.id " +
//					"JOIN LicenseApplication li ON li.userOrgId = uo.id " +
//					"WHERE (LOWER(CONCAT('', e.crn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//			"LOWER(uo.orgName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
//			+ "LOWER(CONCAT('', e.eunn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +		
//			"LOWER(li.licenseExpiryDate) LIKE LOWER(CONCAT('%', :search, '%')) OR "
//			+ "LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR "
//			+ "LOWER(li.licenseNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
//			"((:fromDate IS NULL OR e.createdDate >= :fromDate) " + 
//			 " AND (:toDate IS NULL OR e.createdDate <= :toDate) " +
//			 " AND (:licenseType IS NULL OR uo.licenseType = :licenseType) " +
//			 " AND (:status IS NULL OR e.approvalStatusRid = :status) AND (e.isActive = 1))")
//			Long portPermitManagerList(
//					@Param("search") String search,
//			        @Param("fromDate") LocalDateTime fromDate,
//			        @Param("toDate") LocalDateTime toDate,
//			        @Param("licenseType") String licenseType,
//			        @Param("status") String status);
//
//
//			@Query("SELECT COUNT(e) FROM PortPermitApplication e " +
//					"JOIN UserOrganization uo ON e.userOrgId = uo.id " +
//					"JOIN LicenseApplication li ON li.userOrgId = uo.id " +
//					"WHERE (LOWER(CONCAT('', e.crn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " 
//			+ "LOWER(uo.orgName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
//			+ "LOWER(CONCAT('', e.eunn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
//			"LOWER(li.licenseExpiryDate) LIKE LOWER(CONCAT('%', :search, '%')) OR "
//			+ "LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR "
//			+ "LOWER(li.licenseNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
//			"((:fromDate IS NULL OR e.createdDate >= :fromDate) " + 
//			 " AND (:toDate IS NULL OR e.createdDate <= :toDate) " +
//			 " AND (:licenseType IS NULL OR uo.licenseType = :licenseType) " +
//			 " AND (:status IS NULL OR e.approvalStatusRid = :status) AND (e.portRid =:portRid)")
//			List<PortPermitApplication> userList(
//					@Param("search") String search,
//				     @Param("fromDate") LocalDateTime fromDate,
//				     @Param("toDate") LocalDateTime toDate,
//				     @Param("licenseType") String licenseType,
//				     @Param("status") String status,
//				     @Param("pageRequest") PageRequest pageRequest);
//
//			
//			
//			@Query("SELECT COUNT(e) FROM PortPermitApplication e " +
//					"JOIN UserOrganization uo ON e.userOrgId = uo.id " +
//					"JOIN LicenseApplication li ON li.userOrgId = uo.id " +
//					"WHERE (LOWER(CONCAT('', e.crn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " 
//			+ "LOWER(uo.orgName) LIKE LOWER(CONCAT('%', :search, '%')) OR "
//			+ "LOWER(CONCAT('', e.eunn)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +			
//			"LOWER(li.licenseExpiryDate) LIKE LOWER(CONCAT('%', :search, '%')) OR "
//			+ "LOWER(uo.userId) LIKE LOWER(CONCAT('%', :search, '%')) OR "
//			+ "LOWER(e.licenseNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
//			"((:fromDate IS NULL OR e.createdDate >= :fromDate) " + 
//			 " AND (:toDate IS NULL OR e.createdDate <= :toDate) " +
//			 " AND (:licenseType IS NULL OR uo.licenseType = :licenseType) " +
//			 " AND (:status IS NULL OR e.approvalStatusRid = :status) AND (e.portRid =:portRid)")
//			Long userList(
//					@Param("search") String search,
//			        @Param("fromDate") LocalDateTime fromDate,
//			        @Param("toDate") LocalDateTime toDate,
//			        @Param("licenseType") String licenseType,
//			        @Param("status") String status,
//			        @Param("portRid") String portRid);

}
