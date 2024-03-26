package sa.tabadul.useraccessmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.domain.ResourceServerScope;

@Repository
public interface ResourceServerScopeRepository extends JpaRepository<ResourceServerScope, String>{
	
	@Query("select s from ResourceServerScope s where s.resourceServerId = :resourceServerId")
	List<ResourceServerScope> allScopes(@Param("resourceServerId")String resourceServerId);

}
