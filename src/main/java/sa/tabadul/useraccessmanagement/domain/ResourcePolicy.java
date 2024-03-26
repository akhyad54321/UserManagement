package sa.tabadul.useraccessmanagement.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RESOURCE_POLICY", schema = "dbo")
public class ResourcePolicy {
	
	@Id
	@Column(name="RESOURCE_ID")
    private String resourceId;
	
	@Column(name="POLICY_ID")
	private String policyId;
	

}
