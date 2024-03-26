package sa.tabadul.useraccessmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sa.tabadul.useraccessmanagement.domain.UserOrganizationApproval;


@Repository
public interface UserOrganizationApprovalRepository extends JpaRepository<UserOrganizationApproval, Integer>{

	List<UserOrganizationApproval> findByUserOrganizationId(Integer userOrganizationId);
	
}
