package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
@Table(name="PORT_PERMIT_APPLICATION",schema="LICENSE")
public class PortPermitApplication {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@Column(name="user_org_ID")
	private Integer userOrgId;
	
	@NotNull
	@Column(name="CRN")
	private Long crn;
	
	@NotNull
	@Column(name="port_RID")
	private Integer portRid;
	
	@NotNull
	@Column(name="EUNN")
	private Long eunn;
	
	@NotNull
	@Column(name="establishment_name")
	private String establishmentName;
	
	@NotNull
	@Column(name="establishment_type")
	private String establishmentType;
	
	@NotNull
	@Column(name="establishment_activity")
	private String establishmentActivity;
	
	@NotNull
	@Column(name="establishment_status")
	private String establishmentStatus;
	
	@NotNull
	@Column(name="establishment_issue_date")
	private LocalDate establishmentIssueDate;
	
	@NotNull
	@Column(name="establishment_expiry_date")
	private LocalDate establishmentExpiryDate;
	
	@NotNull
	@Column(name="establishment_city")
	private String establishmentCity;
	
	@NotNull
	@Column(name="address")
	private String address;
	
	@NotNull
	@Column(name="mailbox")
	private String mailbox;
	
	@NotNull
	@Column(name="manager_name")
	private String managerName;
	
	@NotNull
	@Column(name="manager_nationality")
	private String managerNationality;
	
	@NotNull
	@Column(name="list_of_partners")
	private String listOfPartners;
	
	@NotNull
	@Column(name="record_type")
	private String recordType;
	
	@NotNull
	@Column(name="bank_guarantee_filepath")
	private String bankGuaranteeFilepath;
	
	@NotNull
	@Column(name="approval_status_RID")
	private Integer approvalStatusRid;
	
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
	
	@NotNull
	@Column(name="request_type_RID")
	private Integer requestTypeRid;
	
	@NotNull
	@Column(name="request_status_RID")
	private Integer requestStatusRid;
	
	@Column(name="filename")
	private String fileName;
	
	@Column(name="filepath")
	private String filePath;
	
	@Column(name="suspension_to")
	private LocalDateTime suspensionTo;
	
	@Transient
	private String orgName;
	
	@Transient
	private String orgCode;
	
	@Transient
	private String portCode;
	
	@Transient
	private String portNameEng;
	
	@Transient
	private String portNameArabic;
	
	@Transient
	private String licenseNo;
	
	@Transient
	private LocalDate licenseExpiryDate;
	
	@Transient
	private String remark;
	
	@Transient
	private String filepath;
	
	@Transient
	private String approvalStatusType;
	
	@Transient
	private String approvalStatusTypeArabic;
	
	@Transient
	private String requestTypeEnglish;
	
	@Transient
	private String requestTypeArabic;
	
	@Transient
	private String requestStatusEnglish;
	
	@Transient
	private String requestStatusArabic;
	
	@Transient
	private String sadadNo;
	
	@PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
    }
	
	@Transient
	private List<PortPermitApplicationApproval> approvals = new ArrayList<>();

}
