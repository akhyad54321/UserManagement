package sa.tabadul.useraccessmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.domain.PortCode;

@Repository
public interface PortCodeRepo extends JpaRepository<PortCode, Integer> {

	PortCode findByPortCodeAndPortTypeId(String portcode, int portTypeId);

}
