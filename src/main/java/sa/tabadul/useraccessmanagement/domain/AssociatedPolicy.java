package sa.tabadul.useraccessmanagement.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ASSOCIATED_POLICY", schema = "dbo")
public class AssociatedPolicy {

	
	@Id
	@Column(name="POLICY_ID")
	private String policyId;
	
	@Column(name="ASSOCIATED_POLICY_ID")
	private String associatedPolicyId;
	
}
