package sa.tabadul.useraccessmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import sa.tabadul.useraccessmanagement.domain.UserRole;
import sa.tabadul.useraccessmanagement.domain.Users;


public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
	
	List<UserRole> findByStakeholderTypeId(@Param("stakeholderTypeId") Integer stakeholderTypeId);

	List<UserRole> findByUsers(Users users);
	
	List<UserRole> findByStakeholderTypeIdNotIn(@Param("stakeholderTypeId") List<Integer> stakeholderTypeId);
	


}
