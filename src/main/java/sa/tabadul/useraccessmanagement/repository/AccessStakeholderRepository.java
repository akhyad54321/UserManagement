package sa.tabadul.useraccessmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import sa.tabadul.useraccessmanagement.domain.AccessPageStakeHolder;


public interface AccessStakeholderRepository extends JpaRepository<AccessPageStakeHolder, Integer> {

	List<AccessPageStakeHolder> findAllByStakeholderCategoryRIDAndAccesspageID(@Param(value = "stakeholderCategoryRID") Integer stakeholderCategoryRID,@Param(value = "accesspageID") Integer accesspageID);
	
	List<AccessPageStakeHolder> findAllByStakeholderCategoryRIDAndStakeholdertypeIDAndAccesspageID(@Param("stakeholderCategoryRID") Integer stakeholderCategoryRID,@Param("stakeholdertypeID") Integer stakeholdertypeID,@Param("accesspageID") Integer accesspageID);
	
	List<AccessPageStakeHolder> findAllByStakeholdertypeIDInAndStakeholderCategoryRIDIn(@Param("stakeholdertypeID") List<Integer> stakeholdertypeID,@Param("stakeholderCategoryRID") List<Integer> stakeholderCategoryRID);
	
	
	
	
	

	
}
