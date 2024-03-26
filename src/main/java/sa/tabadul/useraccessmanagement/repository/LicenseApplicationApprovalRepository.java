package sa.tabadul.useraccessmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.domain.LicenseApplicationApproval;
import java.util.List;


@Repository
public interface LicenseApplicationApprovalRepository extends JpaRepository<LicenseApplicationApproval, Integer>{

	
	
	List<LicenseApplicationApproval> findByLicenseApplicationId(Integer licenseApplicationId);
	
	List<LicenseApplicationApproval> findByLicenseApplicationIdAndRequestTypeRidIn(Integer licenseApplicationId, List<Integer> requestTypeRid);
}
