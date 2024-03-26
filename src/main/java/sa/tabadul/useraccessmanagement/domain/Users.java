package sa.tabadul.useraccessmanagement.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "[USER]", name = "[USER_REGISTRATION]")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @JsonProperty("app_id")
    @Column(name = "app_id")
    private Integer appId;

    @JsonProperty("employee_code")
    @Column(name = "employee_code")
    private String employeeCode;

    @JsonProperty("employee_name")
    @Column(name = "employee_name")
    private String employeeName;

    @JsonProperty("joining_date")
    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @JsonProperty("department_RID")
    @Column(name = "department_RID")
    private Integer departmentRID;

    @Transient
    @JsonProperty("deparment_code")
    @Column(name = "deparment_code")
    private String deparmentCode;

    @Transient
    @JsonProperty("deparment_code_desc_eng")
    @Column(name = "deparment_code_desc_eng")
    private String deparmentCodeDescEng;

    @Transient
    @JsonProperty("deparment_code_desc_arabic")
    private String deparmentCodeDescArabic;

    @JsonProperty("designation_RID")
    @Column(name = "designation_RID")
    private Integer designationRID;

    @Transient
    @JsonProperty("designation_code")
    private String designationCode;

    @Transient
    @JsonProperty("designation_code_desc_eng")
    private String designationCodeDescEng;

    @Transient
    @JsonProperty("designation_code_desc_arabic")
    private String designationCodeDescArabic;

    @Column(name = "port_RID")
    @JsonProperty(value = "port_RID")
    private Integer portRID;

    @JsonProperty("default_group_type_RID")
    @Column(name = "default_group_type_RID")
    private Integer defaultGroupTypeRID;

    @Transient
    @JsonProperty("default_group_code")
    private String defaultGroupCode;

    @Transient
    @JsonProperty("default_group_code_desc_eng")
    private String defaultGroupCodeDescEng;

    @Transient
    @JsonProperty("default_group_code_desc_arabic")
    private String defaultGroupCodeDescArabic;

    @Transient
    @JsonProperty("roll_type")
    private String rollType;

    @Transient
    @JsonProperty("first_name")
    private String firstName;

    @Transient
    @JsonProperty("last_name")
    private String lastName;

    @Transient
    @JsonProperty("port_name_eng")
    private String portNameEng;

    @Transient
    @JsonProperty("port_name_arabic")
    private String portNameArabic;

    @Transient
    private Integer roleId;

    @Transient
    @JsonProperty("port_code")
    private String portCode;

    private String email;

    @JsonProperty("phone_no")
    @Column(name = "phone_no")
    private Long phoneNo;

    @Column(name = "IAM_user_ID")
    @JsonProperty(value = "iam_user_ID")
    private String iamUserID;

    @JsonProperty("created_by")
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    @JsonProperty(value = "created_date")
    private LocalDateTime createdDate;

    @Transient
    @JsonProperty("updated_by")
    @Column(name = "updated_by")
    private String updatedBy;

    @JsonProperty("updated_date")
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "is_active")
    @JsonProperty(value = "is_active")
    private Boolean isActive;

    @JsonProperty("org_ID")
    @Column(name = "org_ID")
    private String orgID;

    @JsonProperty("branch_ID")
    @Column(name = "branch_ID")
    private String branchID;

    @JsonProperty("user_type")
    @Column(name = "user_type")
    private String userType;

    @JsonProperty("user_id")
    @Column(name = "user_id")
    private String userId;

    @JsonProperty("profile_image_path")
    @Column(name = "profile_image_path")
    private String profileImagePath;

    @JsonProperty("port_user_no")
    @Column(name = "port_user_no")
    private String portUserNo;

    @Transient
    private String orgName;
    
    @Transient
    private String orgNameArabic;

    @Transient
    private String password;
    
    @Transient
    private String branchName;

    @Transient
    @JsonProperty("custom_no")
    private String customNo;

    @Transient
    @JsonProperty("licence_no")
    private String licenceNo;

    @Transient
    private Integer orgnizationRID;
    
    @Transient
    private String documentNo;

    @Transient
    private Integer branchRID;
    @JsonProperty("national_ID")
    @Column(name = "national_ID")
    private String nationalId;

    @JsonProperty("unit_number")
    @Column(name = "unit_number")
    private String unitNumber;

    @JsonProperty("zip_code")
    @Column(name = "zip_code")
    private Integer zipCode;

    @JsonProperty("building_number")
    @Column(name = "building_number")
    private String buildingNumber;

    @Column(name = "street")
    private String street;

    @Column(name = "district")
    private String district;

    @Column(name = "city")
    private Integer city;
    
    @Transient
    private String cityName;
    

    @Column(name = "address")
    private String address;
    
    @Transient
    private String registrationTypeEng;
    
    @Transient
	private String registrationTypeArabic;
    
    @Transient
	private String registrationThroughEng;
    
    @Transient
	private String registrationThroughArabic;
    
    @Transient
	private String documentTypeEng;
    
    @Transient
	private String documentTypeArabic;
    
    @Transient
	private String orgNationalId;
    
    @Transient
    private Integer branchLocationRid;
    
    @Transient
    private String BranchLocationEng;
    
    @Transient
    private String BranchLocationArb;
    
    @Column(name = "rep_no")
	private String repNo;

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
        isActive = true;
    }

	@OneToMany(mappedBy = "users"/* , cascade = CascadeType.ALL,orphanRemoval = true */)
    @JsonManagedReference
    private List<UserRole> stakeHolders = new ArrayList<>();

}
