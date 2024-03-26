package sa.tabadul.useraccessmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.domain.UserBranch;
import java.util.List;




@Repository
public interface UserBranchRepository extends JpaRepository<UserBranch , Integer>{

	
	UserBranch findByBranchID(@Param("branchID") String branchID);
	
	List<UserBranch> findByOrgIdInAndPortRid(List<Integer> orgId,Integer portId);
	
	List<UserBranch> findByOrgId(Integer orgId);
	
}
