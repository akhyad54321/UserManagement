package sa.tabadul.useraccessmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.domain.PortPermitApplicationApproval;
import java.util.List;


@Repository  
public interface PortPermitApplicationApprovalRepository extends JpaRepository<PortPermitApplicationApproval, Integer>{
	
	List<PortPermitApplicationApproval> findByPortPermitApplicationId(Integer portPermitApplicationId);
	
	List<PortPermitApplicationApproval> findByPortPermitApplicationIdAndRequestTypeRidIn(Integer portPermitApplicationId, List<Integer> requestTypeRid);     

}
