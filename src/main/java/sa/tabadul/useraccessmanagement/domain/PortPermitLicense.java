package sa.tabadul.useraccessmanagement.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PORT_PERMIT_APPLICATION", schema = "LICENSE")
public class PortPermitLicense extends PortPermit {
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_org_ID")
	private OrganizationLicense userOrg;
	@Column(name = "user_org_ID", insertable = false, updatable = false)
	private Integer userOrgId;


}
