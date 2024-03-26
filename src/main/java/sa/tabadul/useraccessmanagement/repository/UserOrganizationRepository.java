package sa.tabadul.useraccessmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.domain.UserOrganization;
import java.util.List;


@Repository
public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Integer> {

	UserOrganization findByOrgID(@Param("orgID") String orgID);

	UserOrganization findByCrn(@Param("crn") Long crn);

	UserOrganization findByAdditionalEmail(String additionalEmail);

	UserOrganization findByEmailId(String emailId);

	UserOrganization findByUserId(String userId);
	
	UserOrganization findByMobileNumber(Long mobileNumber);

	List<UserOrganization> findByUserIdLike(String userId);
	
	List<UserOrganization> findByLicenceNoIn(List<String> licenceNo);
	
	boolean existsByOrgID(String orgID);
	
	@Query("SELECT e FROM UserOrganization e WHERE LOWER(e.orgName) LIKE LOWER(CONCAT('%', :orgName, '%')) OR  LOWER(e.userId) LIKE LOWER(CONCAT('%', :orgName, '%')) ")
	List<UserOrganization> searchOrgName(@Param("orgName") String orgName);
	
	List<UserOrganization> findByStakeholdertypeRid(Integer stakeholdertypeRid);

	
}
