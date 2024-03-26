/**
 * 
 */
package sa.tabadul.useraccessmanagement.repository;

/**
 * @author amalkawi
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sa.tabadul.useraccessmanagement.domain.CodeMaster;

@Repository
public interface CodeMasterRepository extends JpaRepository<CodeMaster, Integer> {
	CodeMaster findByKey1AndCode(String key1, String code);
}
