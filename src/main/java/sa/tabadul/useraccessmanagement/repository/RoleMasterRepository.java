package sa.tabadul.useraccessmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.domain.RoleMaster;

@Repository
public interface RoleMasterRepository extends JpaRepository<RoleMaster, Integer> {

	RoleMaster findByRoleCode(String code1);

	RoleMaster findByRoleNameEnglish(String roleName);

	RoleMaster findByRoleNameArabic(String roleArabic);

	RoleMaster findByRoleDescriptionEnglish(String roleDesc);

	RoleMaster findByRoleDescriptionArabic(String roleDescArabic);
	    
	List<RoleMaster> findByAppId(Integer appId);
	
	List<RoleMaster> findByRoleCodeAndIdNot(@Param("roleCode") String roleCode, @Param("id") int id);

	List<RoleMaster> findByRoleNameEnglishAndIdNot(@Param("roleNameEnglish") String roleNameEnglish,
			@Param("id") int id);
	
	List<RoleMaster> findByRoleNameArabicAndIdNot(@Param("roleNameArabic") String roleNameArabic, @Param("id") int id);
	
	List<RoleMaster> findByRoleDescriptionEnglishAndIdNot(@Param("roleDescriptionEnglish") String roleDescriptionEnglish,@Param("id") int id);
	
	List<RoleMaster> findByRoleDescriptionArabicAndIdNot(@Param("roleDescriptionArabic") String roleDescriptionArabic,@Param("id") int id);
	
	List<RoleMaster> getByRoleCode(@Param("roleCode") String roleCode);
	
	List<RoleMaster> findByIdIn(List<Integer> id);
	
	List<RoleMaster> findAllByOrderByCreatedDateDesc();
	
	@Query("SELECT s "
			+ "FROM RoleMaster s " + "WHERE s.appId = :app_id "
			+ "AND (s.rollType IS NULL OR s.rollType = 'NULL' OR s.rollType = '') " + "AND s.rollType != 'Subrole' "
			+ "AND s.isActive = 1 " + "AND s.canBeAssigned = 1")
	List<RoleMaster> getAllStakeholderByAppId(@Param("app_id") Integer appId);

	@Query("SELECT s "
			+ "FROM RoleMaster s " + "WHERE s.appId = :app_id " + "AND s.rollType = :roll_type "
			+ "AND s.isActive = 1 " + "AND s.canBeAssigned = 1")
	List<RoleMaster> getAllStakeholderByAppIdAndRollType(@Param("app_id") Integer appId,
			@Param("roll_type") String rollType);
	
	@Query("SELECT r FROM RoleMaster r WHERE " + "r.isActive = true AND " + "r.appId = :appId AND "
			+ "r.canBeAssigned = true AND " + "(:roleCode IS NULL OR :roleCode = '' OR r.roleCode = :roleCode) "
			+ "AND (:roleNameEnglish IS NULL OR :roleNameEnglish = '' OR r.roleNameEnglish = :roleNameEnglish) "
			+ "AND (:roleNameArabic IS NULL OR :roleNameArabic = '' OR r.roleNameArabic = :roleNameArabic) "
			+ "AND (:roleDescriptionEnglish IS NULL OR :roleDescriptionEnglish = '' OR r.roleDescriptionEnglish = :roleDescriptionEnglish) "
			+ "AND (:roleDescriptionArabic IS NULL OR :roleDescriptionArabic = '' OR r.roleDescriptionArabic = :roleDescriptionArabic)")
	List<RoleMaster> findAllByFilter(@Param("roleCode") String roleCode,
			@Param("roleNameEnglish") String roleNameEnglish, @Param("roleNameArabic") String roleNameArabic,
			@Param("roleDescriptionEnglish") String roleDescriptionEnglish,
			@Param("roleDescriptionArabic") String roleDescriptionArabic, @Param("appId") Integer appId);
	

	@Query("SELECT r FROM RoleMaster r WHERE " +
			"r.appId = :appId AND " +
			"(:search IS NULL OR :search = '' OR r.roleCode LIKE CONCAT('%',:search,'%'))" +
			"OR (:search IS NULL OR :search = '' OR r.roleNameEnglish LIKE CONCAT('%',:search,'%'))" +
			"OR (:search IS NULL OR :search = '' OR r.roleNameArabic LIKE CONCAT('%',:search,'%'))" +
			"OR (:search IS NULL OR :search = '' OR r.roleDescriptionEnglish LIKE CONCAT('%',:search,'%'))" +
			"OR (:search IS NULL OR :search = '' OR r.roleDescriptionArabic LIKE CONCAT('%',:search,'%'))")
	List<RoleMaster> findByPagination(@Param("search") String search, PageRequest pageable,
			@Param("appId") Integer appId);

	@Query("SELECT r FROM RoleMaster r WHERE " +
			"r.appId = :appId")
	List<RoleMaster> findByPaginationWithOutSearch(PageRequest pageable, @Param("appId") Integer appId);

	@Query("SELECT r FROM RoleMaster r WHERE r.id = :id AND r.appId = :appId")
	Optional<RoleMaster> findByIdAndAppId(@Param("id") int id, @Param("appId") Integer appId);
	
	List<RoleMaster> findByRoleCodeIn(@Param("roleCode") List<String> roleCode);
}
