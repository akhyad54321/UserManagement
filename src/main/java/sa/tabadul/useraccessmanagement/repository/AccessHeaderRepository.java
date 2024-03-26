package sa.tabadul.useraccessmanagement.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import sa.tabadul.useraccessmanagement.domain.AccessHeader;





public interface AccessHeaderRepository extends JpaRepository<AccessHeader, Integer> {
	

}
