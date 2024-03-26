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
@Table(name="PORT_PERMIT_APPLICATION_APPROVAL",schema="LICENSE")
public class PortPermitApplicationApproval {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@Column(name="port_permit_application_ID")
	private Integer portPermitApplicationId;
	
	@NotNull
	@Column(name="user_RID")
	private String userRid;
	
	@Column(name="filepath")
	private String filepath;
	
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
	private LocalDateTime updatedDate= LocalDateTime.now();
	
	@NotNull
	@Column(name="is_active")
	private Boolean isActive;
	
	@NotNull
	@Column(name="request_type_RID")
	private Integer requestTypeRid;
	
	@Transient
	private String requestTypeArabic;
	
	@Transient
	private String requestTypeEnglish;
	
	
	@PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
        isActive = true;
    }

	@Transient
	private String statusEnglish;
	
	@Transient
	private String statusArabic;
	
	@Transient
	private String userName;
}
