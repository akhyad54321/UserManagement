package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@Table(name="[USER_ORGANIZATION_APPROVAL]",schema="[USER]")
public class UserOrganizationApproval {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@Column(name="user_organization_ID")
	private Integer userOrganizationId;
	
	@NotNull
	@Column(name="action_by_id")
	private String actionById;
	
	@NotNull
	@Column(name="approval_status_RID")
	private Integer approvalStatusRid;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="created_date")
	private LocalDateTime createdDate;
	
	@Column(name="updated_by")
	private String updatedBy;
	
	@Column(name="updated_date")
	private LocalDateTime updatedDate;
	
	@NotNull
	@Column(name="is_active")
	private Boolean isActive;
	
	@Transient
	private String status;
	
	@Transient
	private String userName;

}
