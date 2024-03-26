package sa.tabadul.useraccessmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sa.tabadul.useraccessmanagement.domain.KeyClockRole;
import sa.tabadul.useraccessmanagement.domain.RoleAttribute;

public interface RoleAttributeRepository extends JpaRepository<RoleAttribute, String> {

	@Query("SELECT ra FROM KeyClockRole kr JOIN kr.roleAttributes ra WHERE ra.name = :name AND ra.value = :value "
			+ "AND kr.id IN (SELECT kr.id FROM KeyClockRole kr JOIN kr.roleAttributes ra WHERE ra.name = 'appId' AND ra.value = :appId)")
	List<RoleAttribute> findByNameAndValue(@Param("name") String name, @Param("value") String value, @Param("appId") String appId);
	
	List<RoleAttribute> findByKeyClockRole(KeyClockRole keyClockRole);
	
	List<RoleAttribute> findByValue(String value);
	
	@Query("SELECT ra FROM RoleAttribute ra WHERE ra.name = 'id' AND ra.value = (SELECT MAX(raInner.value) FROM RoleAttribute raInner WHERE raInner.name = 'id')")
    RoleAttribute findTopByOrderByIdDesc();
	

}
