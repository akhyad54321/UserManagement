package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LICENSE_APPLICATION_APPROVAL", schema = "LICENSE")
public class LicenseApplicationApproval {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "license_application_ID")
	private Integer licenseApplicationId;

	@Column(name = "user_RID")
	private String userRid;

	@Column(name = "approval_status_RID")
	private Integer approvalStatusRid;
	
	@Transient
	private String approvalStatus;

	@Column(name = "remark")
	private String remark;

	@Column(name = "filepath")
	private String filepath;
	
	@Column(name = "request_type_RID")
	private Integer requestTypeRid;
	
	@Transient
	private String requestStatus;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate = LocalDateTime.now();

	@Column(name = "is_active")
	private Boolean isActive;

	@PrePersist
	public void prePersist() {
		createdDate = LocalDateTime.now();
		isActive = true;
	}

}
