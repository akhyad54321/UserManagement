package sa.tabadul.useraccessmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.domain.RoleMapping;



@Repository
public interface RoleMappingRepository extends JpaRepository<RoleMapping, Integer>{


}
