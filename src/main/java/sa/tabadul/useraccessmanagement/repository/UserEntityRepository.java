package sa.tabadul.useraccessmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sa.tabadul.useraccessmanagement.domain.UserEntity;



public interface UserEntityRepository extends JpaRepository<UserEntity, String>{
	
	UserEntity findByUsername(String username);

}
