package sa.tabadul.useraccessmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import sa.tabadul.useraccessmanagement.domain.AccessModule;

public interface AccessModuleRepository extends JpaRepository<AccessModule, Integer> {
	
	

	List<AccessModule> findByIsActive(@Param("isActive") Boolean isActive);
	

	
}
