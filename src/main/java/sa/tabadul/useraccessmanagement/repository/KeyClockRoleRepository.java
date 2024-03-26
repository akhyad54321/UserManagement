package sa.tabadul.useraccessmanagement.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.domain.KeyClockRole;

@Repository
public interface KeyClockRoleRepository extends JpaRepository<KeyClockRole, String> {

	@Query("SELECT kr FROM KeyClockRole kr JOIN kr.roleAttributes ra WHERE ra.name = 'appId' AND ra.value = :client "
			+ "AND kr.id IN (SELECT kr.id FROM KeyClockRole kr JOIN kr.roleAttributes ra WHERE ra.name = 'portRid' AND ra.value = :portId)")
	List<KeyClockRole> getAllRoleData(@Param("client") String client,@Param("portId") String portId);
	
	@Query("SELECT kr FROM KeyClockRole kr JOIN kr.roleAttributes ra WHERE ra.name = 'appId' AND ra.value = :client AND kr.id = :id")
	KeyClockRole findByIdAndroleAttributesByValue(@Param("id") String id, @Param("client") String client);
	
	@Query("SELECT kr FROM KeyClockRole kr JOIN kr.roleAttributes ra WHERE ra.name = 'id' AND ra.value = :id "
			+ "AND kr.id IN (SELECT kr.id FROM KeyClockRole kr JOIN kr.roleAttributes ra WHERE ra.name = 'appId' AND ra.value = :appId)")
	KeyClockRole finById(@Param("id") String id, @Param("appId") String appId);
}
