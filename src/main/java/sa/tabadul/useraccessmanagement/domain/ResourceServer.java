package sa.tabadul.useraccessmanagement.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "RESOURCE_SERVER", schema = "dbo")
public class ResourceServer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private String id;

    @Column(name = "ALLOW_RS_REMOTE_MGMT")
    private Boolean allowRsRemoteMgmt;

    @Column(name = "POLICY_ENFORCE_MODE")
    private String policyEnforceMode;

    @Column(name = "DECISION_STRATEGY")
    private Integer decisionStrategy;

}
