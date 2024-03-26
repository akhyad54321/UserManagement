package sa.tabadul.useraccessmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.common.models.response.ResourcePermissions;
import sa.tabadul.useraccessmanagement.common.models.response.ResourcePermissionsPolicy;
import sa.tabadul.useraccessmanagement.common.models.response.ResourceResponse;
import sa.tabadul.useraccessmanagement.domain.ResourceServerResource;
import java.util.List;

@Repository
public interface ResourceServerResourceRepository extends JpaRepository<ResourceServerResource, String> {

	@Query("SELECT s FROM ResourceServerResource s WHERE s.resourceServerId = :serverId AND s.type LIKE %:moduleType%")
	List<ResourceServerResource> findByServerIdAndModuleType(@Param("serverId") String serverId,
			@Param("moduleType") String moduleType);

	
	@Query("SELECT new sa.tabadul.useraccessmanagement.common.models.response.ResourceResponse(a.id , a.name ,c.id , c.name , e.id , e.name)"
			+ " FROM ResourceServerResource a"
			+ " JOIN ResourcePolicy b ON a.id = b.resourceId"
			+ " JOIN ResourceServerPolicy c ON b.policyId = c.id"
			+ " JOIN AssociatedPolicy d ON c.id = d.policyId"
			+ " JOIN ResourceServerPolicy e ON d.associatedPolicyId = e.id"
			+ " WHERE a.resourceServerId = :resourceServerId"
			+ "      AND e.name = :role"
			+ "      AND a.type LIKE CONCAT('%', :page, '%')")
	List<ResourceResponse> GetAllResources(@Param("resourceServerId")String resourceServerId,@Param("role")String role,@Param("page")String page);
	
	@Query("Select new sa.tabadul.useraccessmanagement.common.models.response.ResourcePermissionsPolicy(a.id,a.name,c.id,c.name,e.id,e.name)"
			+ " from ResourceServerResource a JOIN ResourcePolicy b"
			+ " on a.id = b.resourceId JOIN ResourceServerPolicy c"
			+ "  on b.policyId = c.id  inner join AssociatedPolicy d"
			+ " on c.id = d.policyId inner join ResourceServerPolicy e"
			+ " on d.associatedPolicyId = e.id"
			+ " where a.id  = :pageId"
			+ " and  a.resourceServerId = :resourceServerId")
	List<ResourcePermissionsPolicy> getAllPermissionsWithPolicy(@Param("pageId")String pageId,@Param("resourceServerId")String resourceServerId);
	
	@Query("Select new sa.tabadul.useraccessmanagement.common.models.response.ResourcePermissions(a.id, a.name , c.id, c.name )"
			+ " from ResourceServerResource a JOIN ResourcePolicy b"
			+ " on a.id = b.resourceId JOIN ResourceServerPolicy c"
			+ " on b.policyId = c.id where a.id  = :pageId")
	List<ResourcePermissions> getAllPermissions(@Param("pageId")String pageId);
	
}
