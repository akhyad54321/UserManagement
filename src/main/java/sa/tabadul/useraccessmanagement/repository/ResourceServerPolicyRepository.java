package sa.tabadul.useraccessmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.domain.ResourceServerPolicy;


@Repository
public interface ResourceServerPolicyRepository extends JpaRepository<ResourceServerPolicy, String> {

	ResourceServerPolicy findByTypeAndName(String type, String name);
}
