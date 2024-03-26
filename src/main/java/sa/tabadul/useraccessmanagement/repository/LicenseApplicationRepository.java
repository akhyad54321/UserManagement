package sa.tabadul.useraccessmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.domain.LicenseApplication;


@Repository
public interface LicenseApplicationRepository extends JpaRepository<LicenseApplication, Integer> {

	LicenseApplication findByCrn(Long crn);
	
	List<LicenseApplication> findAllByUserOrgIdIn(List<Integer> userOrgId);
	
	List<LicenseApplication> findByUserOrgIdAndIsActive(Integer userOrgId, Boolean isActive);
	
	List<LicenseApplication> findByLicenseNumber(String licenseNumber);

}
