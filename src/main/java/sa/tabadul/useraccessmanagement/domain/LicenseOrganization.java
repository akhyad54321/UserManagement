package sa.tabadul.useraccessmanagement.domain;



import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "LICENSE_APPLICATION", schema = "LICENSE")
public class LicenseOrganization {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_org_ID")
	private UserOrganization userOrg;

	@Column(name = "user_org_ID",insertable = false, updatable = false)
	private Integer userOrgId;
	
	@Transient
	@Column(name = "org_ID")
	private String orgID;

	@Transient
	@Column(name = "org_name")
	private String orgName;
	
	@Transient
	@Column(name = "stakeholdertype_RID")
	private Integer stakeholdertypeRid;

	@Column(name = "CRN")
	private Long crn;

	@Column(name = "license_type_RID")
	private Integer licenseTypeRid;

	@Column(name = "investor_type_RID")
	private Integer investorTypeRid;

	@Column(name = "EUNN")
	private Long eunn;

	@Column(name = "establishment_name")
	private String establishmentName;

	@Column(name = "establishment_type")
	private String establishmentType;

	@Column(name = "establishment_activity")
	private String establishmentActivity;

	@Column(name = "establishment_status")
	private String establishmentStatus;

	@Column(name = "establishment_issue_date")
	private LocalDate establishmentIssueDate;

	@Column(name = "establishment_expiry_date")
	private LocalDate establishmentExpiryDate;

	@Column(name = "establishment_city")
	private String establishmentCity;

	@Column(name = "record_type")
	private String recordType;

	@Column(name = "address")
	private String address;

	@Column(name = "mailbox")
	private String mailbox;

	@Column(name = "manager_name")
	private String managerName;

	@Column(name = "manager_nationality")
	private String managerNationality;

	@Column(name = "list_of_partners")
	private String listOfPartners;

	@Column(name = "SADAD_no")
	private String sadadNo;

	@Column(name = "license_number")
	private String licenseNumber;

	@Column(name = "license_expiry_date")
	private LocalDate licenseExpiryDate;

	@Column(name = "license_status_RID")
	private Integer licenseStatusRid;

	@Column(name = "request_type_RID")
	private Integer requestTypeRid;

	@Column(name = "approval_status_RID")
	private Integer approvalStatusRid;

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
	
	@Column(name = "filename")
	private String fileName;
	
	@Column(name = "filepath")
	private String filePath;
	
	@Column(name = "suspension_to")
	private LocalDateTime suspensionTo;

	@PrePersist
	public void prePersist() {
		createdDate = LocalDateTime.now();
		isActive = true;
	}

}