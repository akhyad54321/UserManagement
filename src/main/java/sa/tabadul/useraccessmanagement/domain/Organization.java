package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public class Organization {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "CRN")
	private Long crn;
	@Column(name = "org_ID")
	private String orgID;
	@Column(name = "org_name")
	private String orgName;
	
	@Column(name = "org_name_arabic")
	private String orgNameArabic;
	@NotNull
	@Column(name = "is_active")
	private Boolean isActive;
	@Column(name = "registration_through_RID")
	private Integer registrationThroughRid;
	@Column(name = "registration_type_RID")
	private Integer registrationTypeRid;
	@Column(name = "document_type_RID")
	private Integer documentTypeRid;
	@Column(name = "document_no")
	private String documentNo;
	@Column(name = "national_id")
	private String nationalId;
	@Column(name = "national_id_expiry_date")
	private LocalDate nationalIdExpiryDate;
	@Column(name = "unit_no")
	private String unitNo;
	@Column(name = "zip_code")
	private Long zipCode;
	@Column(name = "building_no")
	private String buildingNo;
	@Column(name = "street")
	private String street;
	@Column(name = "district_RID")
	private String districtRid;
	@Column(name = "address")
	private String address;
	@Column(name = "additional_mobile")
	private String additionalMobile;
	@Email(message = "Enter a valid email")
	@Column(name = "additional_email")
	private String additionalEmail;
	@Column(name = "approval_status_RID")
	private Integer approvalStatusRid;
	@Column(name = "licence_no")
	private String licenceNo;
		
	@Column(name = "custom_no")
	private String customNo;
	@Column(name = "created_by")
	private String createdBy;
	
	@NotNull
	@Column(name = "stakeholdertype_RID")
	private Integer stakeholdertypeRid;
	
	@NotNull
	@Column(name = "city_RID")
	private Integer cityRid;
	@Column(name = "created_date")
	private LocalDateTime createdDate;
	@Column(name = "updated_by")
	private String updatedBy;
	@Column(name = "updated_date")
	private LocalDateTime updatedDate;
	
	
	@Column(name = "user_Id")
	private String userId;
	
	@NotNull
	@Column(name = "mobile_number")
	private Long mobileNumber;
	
	@NotNull
	@Column(name = "email_id")
	private String emailId;


}
