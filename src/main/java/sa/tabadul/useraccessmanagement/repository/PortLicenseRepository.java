package sa.tabadul.useraccessmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import sa.tabadul.useraccessmanagement.domain.PortPermitLicense;

public interface PortLicenseRepository
		extends JpaRepository<PortPermitLicense, Integer>, JpaSpecificationExecutor<PortPermitLicense> {
}
