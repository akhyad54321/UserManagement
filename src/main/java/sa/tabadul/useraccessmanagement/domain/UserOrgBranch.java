package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="[USER_BRANCH]",schema="[USER]")
public class UserOrgBranch {
	
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "branch_ID")
	private String branchID;
	
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "org_ID")
	private UserOrganization userOrg;
	
	@Column(name = "branch_name")
	private String branchName;
	
	@Column(name = "location_RID")
	private Integer locationRid;
	
	@Column(name = "port_RID")
	private Integer portRid;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "updated_date")
	private LocalDateTime updatedDate;
	
	@NotNull
	@Column(name = "is_active")
	private Boolean isActive;

}
