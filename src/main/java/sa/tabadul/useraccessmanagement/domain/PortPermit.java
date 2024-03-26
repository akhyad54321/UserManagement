package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@MappedSuperclass
public class PortPermit {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	@NotNull
	@Column(name = "CRN")
	public Long crn;
	@NotNull
	@Column(name = "port_RID")
	public Integer portRid;
	@NotNull
	@Column(name = "EUNN")
	public Long eunn;
	@NotNull
	@Column(name = "establishment_name")
	public String establishmentName;
	@NotNull
	@Column(name = "establishment_type")
	public String establishmentType;
	@NotNull
	@Column(name = "establishment_activity")
	public String establishmentActivity;
	@NotNull
	@Column(name = "establishment_status")
	public String establishmentStatus;
	@NotNull
	@Column(name = "establishment_issue_date")
	public LocalDate establishmentIssueDate;
	@NotNull
	@Column(name = "establishment_expiry_date")
	public LocalDate establishmentExpiryDate;
	@NotNull
	@Column(name = "establishment_city")
	public String establishmentCity;
	@NotNull
	@Column(name = "address")
	public String address;
	@NotNull
	@Column(name = "mailbox")
	public String mailbox;
	@NotNull
	@Column(name = "manager_name")
	public String managerName;
	@NotNull
	@Column(name = "manager_nationality")
	public String managerNationality;
	@NotNull
	@Column(name = "list_of_partners")
	public String listOfPartners;
	@NotNull
	@Column(name = "record_type")
	public String recordType;
	@NotNull
	@Column(name = "bank_guarantee_filepath")
	public String bankGuaranteeFilepath;
	@NotNull
	@Column(name = "approval_status_RID")
	public Integer approvalStatusRid;
	@Column(name = "created_by")
	public String createdBy;
	@Column(name = "created_date")
	public LocalDateTime createdDate;
	@Column(name = "updated_by")
	public String updatedBy;
	@Column(name = "updated_date")
	public LocalDateTime updatedDate;
	@NotNull
	@Column(name = "is_active")
	public Boolean isActive;
	@NotNull
	@Column(name = "request_type_RID")
	public Integer requestTypeRid;
	@NotNull
	@Column(name = "request_status_RID")
	public Integer requestStatusRid;
	@Column(name="filename")
	private String fileName;
	
	@Column(name="filepath")
	private String filePath;
	
	@Column(name="suspension_to")
	private LocalDateTime suspensionTo;

}
