package sa.tabadul.useraccessmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import sa.tabadul.useraccessmanagement.domain.AccessPage;

public interface AccessPageRepository extends JpaRepository<AccessPage, Integer> {

	
	
	List<AccessPage> findByAccessModuleIDAndAppId(@Param(value = "moduleId") Integer moduleId,@Param(value = "appId") Integer appId);
	

	List<AccessPage> findAllById(Integer id);
	
	List<AccessPage> findByAccessPageStakeHolder_StakeholderCategoryRIDAndAppId(Integer accessPageStakeHolder_StakeholderCategoryRID, Integer appId);


}
