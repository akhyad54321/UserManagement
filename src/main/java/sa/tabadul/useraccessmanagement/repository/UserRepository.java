package sa.tabadul.useraccessmanagement.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.domain.Users;


@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {


	
	List<Users> findByIamUserIDIn(List<String> iamUserID);

	
	List<Users> findByIamUserID(@Param("iamUserID") String iamUserID);
	
	List<Users> findAllByIdInAndOrgID(@Param("id") List<Integer> id,@Param("orgID") String orgID);
	
	List<Users> findAllByOrgIDAndBranchID(@Param("orgID") String orgID,@Param("branchID") String branchID);
	
	List<Users> findAllByIdInAndOrgIDAndBranchID(@Param("id") List<Integer> id,@Param("orgID") String orgID,@Param("branchID") String branchID);

	List<Users> findByOrgID(@Param("orgID") String orgID);
	
	List<Users> findByBranchID(@Param("branchID") String branchID);
	
	Boolean existsByIamUserID(@Param("iamUserID") String iamUserID);
	
	List<Users> findByPortRIDAndAppId(@Param("portRID") Integer portRID,@Param("appId") Integer appId);

	@Query("SELECT DISTINCT u FROM Users u " + "JOIN UserRole ur ON ur.userRegistrationId = u.id "
			+ "WHERE "
			+ "((u.email IS NULL OR u.email ='' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (u.employeeName IS NULL OR u.employeeName ='' OR LOWER(u.employeeName) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (u.userId IS NULL OR u.userId ='' OR LOWER(u.userId) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (ur.stakeholderTypeId IN (:roles) OR ur.stakeholderSubroleId IN (:roles)) "
			+ "OR (u.isActive IS NULL OR u.isActive ='' OR LOWER(u.isActive) LIKE LOWER(CONCAT('%', :search, '%')))) "
			+ "AND u.portRID = :portRID " + "AND u.appId = :appId AND ur.stakeholderTypeId not in (:lstadmins)")
	List<Users> findBySearchAndPortRIDAndAppIdAndUserIds(@Param("search") String search,
			@Param("portRID") Integer portRID, @Param("appId") Integer appId,@Param("lstadmins") List<Integer> lstadmins,@Param("roles") List<Integer> roles, @Param("pageRequest") PageRequest pageRequest);

	
	@Query("SELECT COUNT(DISTINCT u.id) FROM Users u " + "JOIN UserRole ur ON ur.userRegistrationId = u.id "
			+ "WHERE"
			+ " ((u.email IS NULL OR u.email ='' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (u.employeeName IS NULL OR u.employeeName ='' OR LOWER(u.employeeName) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (u.userId IS NULL OR u.userId ='' OR LOWER(u.userId) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (ur.stakeholderTypeId IN (:roles) OR ur.stakeholderSubroleId IN (:roles)) "
			+ "OR (u.isActive IS NULL OR u.isActive ='' OR LOWER(u.isActive) LIKE LOWER(CONCAT('%', :search, '%')))) "
			+ "AND u.portRID = :portRID " + "AND u.appId = :appId AND ur.stakeholderTypeId not in (:lstadmins)")
	long filteredRecordsPortUser(@Param("search") String search,
			@Param("portRID") Integer portRID, @Param("appId") Integer appId,@Param("lstadmins") List<Integer> lstadmins,@Param("roles") List<Integer> roles);

	
	@Query("SELECT COUNT(DISTINCT u.id) FROM Users u " + "JOIN UserRole ur ON ur.userRegistrationId = u.id "
			+ "WHERE "
			+ "u.portRID = :portRID " + "AND u.appId = :appId AND ur.stakeholderTypeId not in (:lstadmins)")
	long totalRecordsPortUser(@Param("portRID") Integer portRID, @Param("appId") Integer appId,@Param("lstadmins") List<Integer> lstadmins);

	
	
	Users findByUserId(@Param("userId") String userId);

	Users findByEmail(@Param("email") String email);
	
	Users findByPhoneNo(@Param("phoneNo") Long phoneNo);
		
	List<Users> findAllByOrgIDAndBranchIDAndIamUserIDNot(@Param("orgID") String orgID,@Param("branchID") String branchID,@Param("userId") String iamUserID);

	List<Users> findByOrgIDAndIamUserIDNot(@Param("orgID") String orgID,@Param("userId") String iamUserID);

	List<Users> findByPortRID(Integer portRID);


	@Query("SELECT DISTINCT u FROM Users u " + "JOIN UserRole ur ON u.id = ur.userRegistrationId "
			+ "WHERE ur.stakeholderTypeId IN :roles "
			+ "AND ((u.email IS NULL OR u.email ='' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (u.portRID IN (:ports)) "
			+ "OR (u.employeeName IS NULL OR u.employeeName ='' OR LOWER(u.employeeName) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (u.userId IS NULL OR u.userId ='' OR LOWER(u.userId) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (u.isActive IS NULL OR u.isActive ='' OR LOWER(u.isActive) LIKE LOWER(CONCAT('%', :search, '%'))))")
	List<Users> findBySearchAndUserIds(@Param("search") String search, @Param("roles") List<Integer> roles, @Param("ports") List<Integer> ports,@Param("pageRequest") PageRequest pageRequest);

	
	@Query("SELECT u FROM Users u " + "JOIN UserRole ur ON u.id = ur.userRegistrationId "
			+ "JOIN RoleMaster rm ON ur.stakeholderTypeId = rm.id " + "WHERE u.id IN :userIds "
			+ "AND ((u.email IS NULL OR u.email ='' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (u.employeeName IS NULL OR u.employeeName ='' OR LOWER(u.employeeName) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (u.userId IS NULL OR u.userId ='' OR LOWER(u.userId) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (u.isActive IS NULL OR u.isActive ='' OR LOWER(u.isActive) LIKE LOWER(CONCAT('%', :search, '%')))"
			+ "OR (rm.roleNameEnglish IS NULL OR rm.roleNameEnglish ='' OR LOWER(rm.roleNameEnglish) LIKE LOWER(CONCAT('%', :search, '%')))"
			+ "OR (rm.roleNameArabic IS NULL OR rm.roleNameArabic ='' OR LOWER(rm.roleNameArabic) LIKE LOWER(CONCAT('%', :search, '%'))))")
	List<Users> findBySearchAndUserIds(@Param("search") String search, @Param("userIds") List<Integer> userIds);

	
	
	List<Users> findByPortRIDAndAppIdAndIdIn(@Param("portRID") Integer portRID,@Param("appId") Integer appId,@Param("id") List<Integer> id);
	
	List<Users> findByPortRIDAndIdIn(Integer portRID,List<Integer> ids);
	
	@Query("SELECT DISTINCT usr FROM Users usr " +
		       "INNER JOIN UserRole r ON usr.id = r.userRegistrationId " +
		       "INNER JOIN UserOrganization org ON org.orgID = usr.orgID " +
		       "INNER JOIN UserBranch br ON usr.branchID = br.branchID " +
		       "WHERE r.stakeholderTypeId = :roleId AND br.portRid = :portId AND (:branchId IS NULL OR :branchId ='' OR br.branchID = :branchId)")
	List<Users> findByUsers(@Param("roleId") Integer roleId,@Param("portId") Integer portId,@Param("branchId") String branchId);
	
	
	
	
	
	@Query("SELECT COUNT(DISTINCT u.id) FROM Users u " + "JOIN UserRole ur ON u.id = ur.userRegistrationId "
			+ "WHERE ur.stakeholderTypeId IN :roles")
	long totalAdmins(@Param("roles") List<Integer> roles);
	
	
	@Query("SELECT COUNT(DISTINCT u.id) FROM Users u " + "JOIN UserRole ur ON u.id = ur.userRegistrationId "
			+ "WHERE ur.stakeholderTypeId IN :roles "
			+ "AND ((u.email IS NULL OR u.email ='' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (u.portRID IN (:ports)) "
			+ "OR (u.employeeName IS NULL OR u.employeeName ='' OR LOWER(u.employeeName) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (u.userId IS NULL OR u.userId ='' OR LOWER(u.userId) LIKE LOWER(CONCAT('%', :search, '%'))) "
			+ "OR (u.isActive IS NULL OR u.isActive ='' OR LOWER(u.isActive) LIKE LOWER(CONCAT('%', :search, '%'))))")
	long filteredadmins(@Param("search") String search, @Param("roles") List<Integer> roles, @Param("ports") List<Integer> ports);


	
	
	
}
